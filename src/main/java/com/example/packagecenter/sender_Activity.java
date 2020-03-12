package com.example.packagecenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class sender_Activity extends AppCompatActivity {


    public static TextView textResult;


    private Button scan_btn;
    //private RequestQueue mQueue;


    public static Doctor currentDoctorObject;

    //Getting the list from the FireBase
    DatabaseReference ref;
    public static List<Doctor> DoctorList;
    Doctor doctorCurrent;

    public static String nameOfSender;
    TextView title_scan_screen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_scan);


          nameOfSender = getIntent().getExtras().getString("nameOfUser");
        title_scan_screen=findViewById(R.id.title_scan_screen);

        title_scan_screen.setText("Welcome "+nameOfSender+"!");


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Data").child("Doctors");
        Doctor newDoc=new Doctor(1111,"klalit-smile ashdod");
        Doctor newDoc1=new Doctor(2222,"klalit-smile karmiel");

        myRef.child("Doc1").setValue(newDoc);
        myRef.child("Doc2").setValue(newDoc1);

        /* 1) Start to create the arrayOfDoctors reading from FireBase ErrandsProject*/

        doctorCurrent=new Doctor();
        database= FirebaseDatabase.getInstance();
        ref=database.getReference("Data").child("Doctors");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DoctorList = new ArrayList<>();

                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Doctor doctor = d.getValue(Doctor.class);
                    DoctorList.add(doctor);
                    Log.i("the","doc id:"+doctor.getId()+" name"+doctor.getName());
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ptt", "Failed to read value.", error.toException());
            }
        });




        /* 2) Scan Action                               */
        scan_btn=findViewById(R.id.btn_scan_of_sender);
        textResult=findViewById(R.id.text_res_of_sender);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Scan_activity.class));
            }
        });


    }

}
