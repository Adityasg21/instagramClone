package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.WrapperListAdapter;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPost extends AppCompatActivity {

    private  LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_post);

        mLinearLayout= findViewById(R.id.LinearLayout);


        Intent receivedObjects=getIntent();
        final String receivedUsername=receivedObjects.getStringExtra("username");

        setTitle(receivedUsername+"'s posts ");

        ParseQuery<ParseObject> parseQuery= new ParseQuery<ParseObject>("photo");
        parseQuery.whereEqualTo("username",receivedUsername);
        parseQuery.orderByDescending("createdAt");

       final ProgressDialog  dialog= new ProgressDialog(this);
        dialog.setMessage("loading...");
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()>0 && e==null) {

                    for (ParseObject post : objects) {
                        final TextView desc = new TextView(UsersPost.this);
                        desc.setText(post.get("description")+"");
                        ParseFile postPicture=(ParseFile) post.getParseFile("picture");
                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                if(data!=null && e==null){

                                    Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView imageView=new ImageView(UsersPost.this);
                                    LinearLayout.LayoutParams imageView_params= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageView_params.setMargins(5,5,5,5);
                                    imageView.setLayoutParams(imageView_params);
                                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    imageView.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams descParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    descParams.setMargins(5,5,5,5);
                                    desc.setLayoutParams(descParams);
                                    desc.setGravity(Gravity.CENTER);
                                    desc.setBackgroundColor(Color.BLUE);
                                    desc.setTextColor(Color.WHITE);
                                    desc.setTextSize(30f);
                                    mLinearLayout.addView(imageView);
                                    mLinearLayout.addView(desc);


                                }
                            }
                        });
                    }
                }else {
                    FancyToast.makeText(UsersPost.this,receivedUsername+ " doesnt have any post", Toast.LENGTH_LONG,FancyToast.INFO,true).show();
                    finish();
                }

                dialog.dismiss();
            }

        });


    }
}