package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class signUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsername, edtEmail, edtPassword;
    private Button btnLogin, btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setTitle("SignUp");

        edtEmail = findViewById(R.id.edtEnterEmail);
        edtUsername = findViewById(R.id.edtEnterUsername);
        edtPassword = findViewById(R.id.edtEnterPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignup);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
           // ParseUser.getCurrentUser().logOut();
            transitionToSocialMedia();
        }

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnSignUp);
                }

                return false;
            }
        });


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSignup:
                if (edtEmail.getText().toString().equals("")
                        || edtUsername.getText().toString().equals("")
                        || edtPassword.getText().toString().equals("")) {
                    FancyToast.makeText(signUpActivity.this, "username,email,password required", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();

                } else {
                    final ParseUser AppUser = new ParseUser();
                    AppUser.setEmail(edtEmail.getText().toString());
                    AppUser.setUsername(edtUsername.getText().toString());
                    AppUser.setPassword(edtPassword.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage(edtUsername.getText().toString() + "is signing up");
                    progressDialog.show();

                    AppUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(signUpActivity.this, AppUser.get("username") + " is signed up", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                                transitionToSocialMedia();
                            } else {
                                FancyToast.makeText(signUpActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }

                            progressDialog.dismiss();

                        }
                    });
                }
                break;


            case R.id.btnLogin:

                Intent intent = new Intent(signUpActivity.this, login_Activity.class);
                startActivity(intent);
                break;

        }

    }

    public void rootLayoutIsTapped(View view) {

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transitionToSocialMedia(){

     Intent intent=new Intent(signUpActivity.this,SocialMedia_activity.class);
     startActivity(intent);

    }
}