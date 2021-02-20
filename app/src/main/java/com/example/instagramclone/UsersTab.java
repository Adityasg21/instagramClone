package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


public class UsersTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listview;
    private ArrayList<String> arraylist;

    private ArrayAdapter arrayAdapter;

    public UsersTab() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view =inflater.inflate(R.layout.fragment_users_tab, container, false);

        final TextView loading_users=view.findViewById(R.id.Loading_users);
        listview=view.findViewById(R.id.listview);
        arraylist=new ArrayList();
        arrayAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arraylist);

        listview.setOnItemClickListener(UsersTab.this);
        listview.setOnItemLongClickListener(UsersTab.this);

        ParseQuery<ParseUser> parseQuery= ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        for(ParseUser user:objects){
                            arraylist.add(user.getUsername());
                        }
                        listview.setAdapter(arrayAdapter);
                        loading_users.animate().alpha(0).setDuration(2000);
                        listview.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent= new Intent(getContext(),UsersPost.class);
        intent.putExtra("username",arraylist.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ParseQuery<ParseUser> parseQuery= ParseUser.getQuery();
        parseQuery.whereEqualTo("username",arraylist.get(position));

        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {

                if(e==null && object!=null){

                    final PrettyDialog prettyDialog=new PrettyDialog(getContext());
                    prettyDialog.setTitle(object.get("username")+" Info")
                            .setMessage(object.get("bio")+ "\n" +
                                      object.get("profession")+"\n" )
                            .setIcon(R.drawable.person)
                            .addButton("ok",
                                    R.color.pdlg_color_red,
                                    R.color.pdlg_color_white,
                                    new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            prettyDialog.dismiss();

                                        }
                                    }).show();



                }
            }
        });

        return true;
    }
}