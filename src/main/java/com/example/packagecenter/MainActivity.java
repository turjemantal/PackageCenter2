package com.example.packagecenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> listDoctorsFromFireBase;
    ArrayAdapter<String> adapter;
    Doctor doctorCurrent;
    private String currentformattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get from the intent of the callender
        Intent intent = getIntent();
        int dayChosed = intent.getIntExtra("day",0); //if it's a string you stored.
        int monthChosed = intent.getIntExtra("month",0); //if it's a string you stored.
        int yearChosed = intent.getIntExtra("year",0); //if it's a string you stored.
        Log.i("this ","is the day"+dayChosed);
        Log.i("this ","is the month"+monthChosed);
        Log.i("this ","is the year"+yearChosed);

        String str=dayChosed+"-"+monthChosed+"-"+yearChosed;


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
        listView=(ListView)findViewById(R.id.list_view);
        database=FirebaseDatabase.getInstance();
        ref=database.getReference("Collections").child("Doctor").child(currentformattedDate) ;




        listDoctorsFromFireBase=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,R.layout.doctor_info,R.id.doctorinfo,listDoctorsFromFireBase);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                doctorCurrent=ds.getValue(Doctor.class);
                //Check if the current Doctor is existent in the listOfDoctors
                    if(!listDoctorsFromFireBase.contains(doctorCurrent)) {
                        listDoctorsFromFireBase.add(doctorCurrent.getName()+" "+doctorCurrent.gettingTheCurrentTime());
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






    }

    public void setCurrentformattedDate(String currentformattedDate) {
        this.currentformattedDate = currentformattedDate;
    }
}
