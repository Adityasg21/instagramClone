package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.net.URI;

public class SocialMedia_activity extends AppCompatActivity {

    private Toolbar toolBar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media_activity);
        setTitle("Social Media App");

        toolBar=findViewById(R.id.my_toolbar);
        setSupportActionBar(toolBar);

        viewPager=findViewById(R.id.viewpager);
        tabAdapter=new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        tabLayout=findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager,false); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.postImageItem){

            if(Build.VERSION.SDK_INT>=23 &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},3000);
            }
            else {
                captureImage();
            }

        }else if(item.getItemId()==R.id.logoutUserItem){
            ParseUser.getCurrentUser().logOut();
            finish();
            Intent intent =new Intent(SocialMedia_activity.this,signUpActivity.class);
            startActivity(intent);
        }else if( item.getItemId()==R.id.send){
        Intent intent=new Intent(SocialMedia_activity.this,sendTweetAcitvity.class);
        startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==3000){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                captureImage();
            }
        }
    }

    private void captureImage() {

        Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,4000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==4000&& resultCode==RESULT_OK && data!=null){

            try{
                Uri capturedImage=data.getData();
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),capturedImage);
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                 byte[]  bytes= byteArrayOutputStream.toByteArray();

                ParseFile parseFile=new ParseFile("img.png",bytes);
                ParseObject parseObject=new ParseObject("photo");
                parseObject.put("picture",parseFile);

                parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                final ProgressDialog dialog=new ProgressDialog(this);
                dialog.setMessage("Loading");
                dialog.show();
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            FancyToast.makeText(SocialMedia_activity.this, "done", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                        }else {
                            FancyToast.makeText(SocialMedia_activity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                        }
                        dialog.dismiss();
                    }
                });



            }catch (Exception e){

            }

        }
    }
}