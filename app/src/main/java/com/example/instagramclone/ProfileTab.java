package com.example.instagramclone;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


public class ProfileTab extends Fragment {

    private EditText EdtProfileName,EdtProfileBio,EdtProfileProfession,EdtProfileHobbies
            ,EdtProfileSport;
    private Button btnUpdateInfo;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters


    public ProfileTab() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_profile_tab, container, false);

        EdtProfileName=view.findViewById(R.id.EdtProfileName);
        EdtProfileBio=view.findViewById(R.id.EdtProfileBio);
        EdtProfileProfession=view.findViewById(R.id.EdtProfileProfession);
        EdtProfileHobbies=view.findViewById(R.id.EdtProfileHobbies);
        EdtProfileSport=view.findViewById(R.id.EdtprofileSport);

        btnUpdateInfo=view.findViewById(R.id.BtnUpdate);
        final ParseUser parseUser=ParseUser.getCurrentUser();

        if(parseUser.get("profileName")==null){
            EdtProfileName.setText("");

        }else {
            EdtProfileName.setText(parseUser.get("profileName")+"");
        }

        if(parseUser.get("bio")==null){
            EdtProfileBio.setText("");

        }else {
            EdtProfileBio.setText(parseUser.get("bio")+"");
        }

        if(parseUser.get("profession")==null){
            EdtProfileProfession.setText("");

        }else {
            EdtProfileProfession.setText(parseUser.get("profession")+"");
        }

        if(parseUser.get("hobbies")==null){
            EdtProfileHobbies.setText("");

        }else {
            EdtProfileHobbies.setText(parseUser.get("hobbies")+"");
        }
        if(parseUser.get("sport")==null){
            EdtProfileSport.setText("");

        }else {
            EdtProfileSport.setText(parseUser.get("sport")+"");
        }





        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                parseUser.put("profileName",EdtProfileName.getText().toString());
                parseUser.put("bio",EdtProfileBio.getText().toString());
                parseUser.put("profession",EdtProfileProfession.getText().toString());
                parseUser.put("hobbies",EdtProfileHobbies.getText().toString());
                parseUser.put("sport",EdtProfileSport.getText().toString());

                final ProgressDialog progressDialog=new ProgressDialog(getContext());
                progressDialog.setMessage("Updating info");
                progressDialog.show();


                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if(e==null){
                            FancyToast.makeText(getContext(),
                                    "info updated",
                                    FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                        }
                        else{
                            FancyToast.makeText(getContext(),
                                    e.getMessage(),
                                    FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                        }
                    }
                });

                progressDialog.dismiss();
            }
        });





        return view;
    }
}