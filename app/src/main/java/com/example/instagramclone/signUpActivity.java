package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class signUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveBoxer,getAllData;
    private EditText edtName,edtKickSpeed,edtKickPower;
    private TextView getDataServer;

    private String allBoxers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtName=findViewById(R.id.edtName);
        edtKickPower=findViewById(R.id.edtKickPower);
        edtKickSpeed=findViewById(R.id.edtKickSpeed);
        saveBoxer=findViewById(R.id.btnSubmit);
        getDataServer=findViewById(R.id.txtGetData);
        getAllData=findViewById(R.id.btnGetAllData);

        saveBoxer.setOnClickListener(this);

        allBoxers=" ";



        getDataServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery("Boxer");
                parseQuery.getInBackground("1NX8G54V2S", new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        if(object!=null&& e==null){
                            getDataServer.setText(object.get("name")+"");
                        }
                    }
                });
            }
        });


        getAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery <ParseObject> getAll=ParseQuery.getQuery("Boxer");
                getAll.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e==null){
                            if(objects.size()>0){
                                for (ParseObject boxer:objects){
                                    allBoxers+=boxer.get("name") +"\n";
                                }
                                FancyToast.makeText(signUpActivity.this, allBoxers,FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

                            }
                            else{
                                FancyToast.makeText(signUpActivity.this,"error !",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                            }
                        }
                    }
                });



            }

        });






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