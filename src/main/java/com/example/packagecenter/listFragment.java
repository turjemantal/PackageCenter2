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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class listFragment extends Fragment {

    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    public static ArrayList<String> listDoctorsFromFireBase;
    ArrayAdapter<String> adapter;
    Doctor doctorCurrent;
    private String currentformattedDate;

    //Search view

    SearchView mySearchView;




    public listFragment() {
        // Required empty public constructor
    }

    public void setCurrentformattedDate(String currentformattedDate) {
        this.currentformattedDate = currentformattedDate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_list, container, false);




        Bundle bundle = this.getArguments();
        int day=bundle.getInt("day", 0);
        int month=bundle.getInt("month", 0);
        int year=bundle.getInt("year", 0);
        Log.i("this ","is the day"+day);
        Log.i("this ","is the month"+month);
        Log.i("this ","is the year"+year);

        String str=day+"-"+month+"-"+year;

        //Formating the corrent date from the picker
        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(str);
            String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
            Log.i("this","is time"+formattedDate);
            setCurrentformattedDate(formattedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        doctorCurrent=new Doctor();
        listView=(ListView)v.findViewById(R.id.list_view1);
        database=FirebaseDatabase.getInstance();
        ref=database.getReference("Collections").child("Doctor").child(currentformattedDate) ;



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

                        String StringDoc = "<b>Doctor/Clinic:</b> ";
                        String StringTime = "<b>Time:</b> " ;
                        String StringBy = "<b>By:</b> ";

                        listDoctorsFromFireBase.add(Html.fromHtml(StringDoc)+doctorCurrent.getName()+"\n"+Html.fromHtml(StringTime)+doctorCurrent.getTime()+"\n"+Html.fromHtml(StringBy)+doctorCurrent.getNameOfSender()+"\n\n");
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
        mySearchView=v.findViewById(R.id.search_view);

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


        ///


        return v;
    }


}
