package com.example.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


public class SharePicturesTab extends Fragment implements View.OnClickListener {

    private EditText EdtDesc;
    private ImageView imgShare;
    private Button btnSharePic;
    Bitmap receivedImageBitmap;


    public SharePicturesTab() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_share_pictures_tab, container, false);
        EdtDesc=view.findViewById(R.id.edtdesc);
        imgShare=view.findViewById(R.id.imgShare);
        btnSharePic=view.findViewById(R.id.btnsharepic);
        btnSharePic.setOnClickListener(SharePicturesTab.this);
        imgShare.setOnClickListener(SharePicturesTab.this);

    return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.imgShare:
                if(android.os.Build.VERSION.SDK_INT >=23 &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1000);
                }else {
                    getChosenImage();
                }

                break;
            case R.id.btnsharepic:
                if(receivedImageBitmap!=null){
                    if(EdtDesc.getText().toString().equals("")){

                        FancyToast.makeText(getContext(),"enter description", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                    }
                    else{
                        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                        receivedImageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                        byte bytes[]=byteArrayOutputStream.toByteArray();
                        ParseFile parseFile=new ParseFile("img.png",bytes);
                        ParseObject parseObject=new ParseObject("photo");
                        parseObject.put("picture",parseFile);
                        parseObject.put("description",EdtDesc.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        final ProgressDialog dialog=new ProgressDialog(getContext());
                        dialog.setMessage("Loading");
                        dialog.show();
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null){
                                    FancyToast.makeText(getContext(), "done", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                }else {
                                    FancyToast.makeText(getContext(), e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                }
                                dialog.dismiss();
                            }
                        });

                    }

                }else {
                    FancyToast.makeText(getContext(), "upload a image", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                }

                break;

        }

    }

    private void getChosenImage() {
        Intent intent =new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2000);
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1000){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getChosenImage();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000) {

            if(resultCode== Activity.RESULT_OK)
            {
                try{
                    Uri selectedImage=data.getData();
                    String[] filePathColumn={MediaStore.Images.Media.DATA};
                    Cursor cursor= getActivity().getContentResolver().query(selectedImage,filePathColumn
                    ,null,null,null);
                    cursor.moveToFirst();
                    int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath=cursor.getString(columnIndex);
                    cursor.close();
                     receivedImageBitmap= BitmapFactory.decodeFile(picturePath);
                    imgShare.setImageBitmap(receivedImageBitmap);


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}