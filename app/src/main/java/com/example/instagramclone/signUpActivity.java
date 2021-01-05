package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class signUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveBoxer;
    private EditText edtName,edtKickSpeed,edtKickPower;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtName=findViewById(R.id.edtName);
        edtKickPower=findViewById(R.id.edtKickPower);
        edtKickSpeed=findViewById(R.id.edtKickSpeed);
        saveBoxer=findViewById(R.id.btnSubmit);

        saveBoxer.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        try{
        final ParseObject boxer=new ParseObject("Boxer");
        boxer.put("name",edtName.getText().toString());
        boxer.put("speed",edtKickSpeed.getText().toString());
        boxer.put("Power",edtKickPower.getText().toString());

        boxer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e==null){
                    Toast.makeText(signUpActivity.this,boxer.get("name")+" has been saved",Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(signUpActivity.this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
        catch (Exception e)
        {
            Toast.makeText(signUpActivity.this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }
}