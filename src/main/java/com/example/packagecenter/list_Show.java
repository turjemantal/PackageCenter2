package com.example.packagecenter;


import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class list_Show extends Fragment {



    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    public static ArrayList<String> listDoctorsFromFireBase;
    ArrayAdapter<String> adapter;
    Doctor doctorCurrent;
    SearchView mySearchView;

    public list_Show() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_list__show, container, false);

        Bundle bundle = this.getArguments();
        String fulldate=bundle.getString("fulldate", null);
        String user_name=bundle.getString("user_to_show", null);

        Log.i("show ","details"+fulldate +" "+user_name);


        doctorCurrent=new Doctor();
        listView=(ListView)v.findViewById(R.id.list_view_show);
        database=FirebaseDatabase.getInstance();
        ref=database.getReference("Schudule").child("Doctors").child(fulldate).child(user_name);



        listDoctorsFromFireBase=new ArrayList<>();
        adapter=new ArrayAdapter<String>(getContext(),R.layout.doctor_info,R.id.doctorinfo,listDoctorsFromFireBase);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    doctorCurrent=ds.getValue(Doctor.class);
                    //Check if the current Doctor is existent in the listOfDoctors
                    if(!listDoctorsFromFireBase.contains(doctorCurrent)) {

                        String StringDoc = "<b>Doctor/Clinic: </b> ";
                        String StringTime = "<b>Time: </b> " ;
                        String StringBy = "<b>By: </b> ";

                        String collect="";
                        if(doctorCurrent.isCollected()==true){
                            collect="Collected";
                        }

                        else{
                            collect="Not collect yet.";
                        }

                        listDoctorsFromFireBase.add(Html.fromHtml(StringDoc)+doctorCurrent.getName()+"\n"+Html.fromHtml(StringTime)+doctorCurrent.getTime()+"\n"+Html.fromHtml(StringBy)+doctorCurrent.getNameOfSender()+"\n"+"Package: "+collect+"\n\n");
                    }
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ptt", "Failed to read value.", error.toException());
            }
        });

        //Search section
        mySearchView=v.findViewById(R.id.search_view_show);

        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });



        return v;
    }

}
