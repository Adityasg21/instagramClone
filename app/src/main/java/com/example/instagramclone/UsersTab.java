package com.example.instagramclone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class UsersTab extends Fragment {

    private ListView listview;
    private ArrayList arraylist;

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
}