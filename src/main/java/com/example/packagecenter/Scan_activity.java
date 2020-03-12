package com.example.packagecenter;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scan_activity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private int idOfDoctorFromScan;
    ZXingScannerView scannerView;



    DatabaseReference ref2;
   // TextView title_scan_screen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(R.layout.activity_scan_activity);
        setContentView(scannerView);

     //   nameOfSender = getIntent().getExtras().getString("nameOfUser");
       // title_scan_screen=findViewById(R.id.title_scan_screen);

       // title_scan_screen.setText("Welcome "+nameOfSender+"!");


    }

    @Override
    public void handleResult(Result rawResult) {

        /* Set's the id that we get from scan into variable*/
        setIdOfDoctorFromScan(Integer.parseInt(rawResult.getText()));

        for (int i = 0; i< sender_Activity.DoctorList.size(); i++) {
           // try {
                final Doctor doctor = sender_Activity.DoctorList.get(i);
                int id = doctor.getId();
                String name = doctor.getName();


                /* Cehcks who is the doctor that get's from the Scan */
                if (idOfDoctorFromScan == id) {
                    /*    Brings the current doctor from scan to the main by Json and by Object  */

                    sender_Activity.currentDoctorObject=new Doctor(id,name);
                    sender_Activity.textResult.setText("Package collected"+"\n\n"+"from: " + name + "\n\n");
                    Log.i("the doc","tal want"+name);
                    /* 3) Take the current doctor after scan and upload to the FireBase under the current date */

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference myRef = database.getReference("Collections").child("Doctor");

                    final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    //myRef.child(currentDate).child(""+ sender_Activity.currentDoctorObject.getName()).setValue(sender_Activity.currentDoctorObject);

                    //Calculate the current hour
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                    String theCurrentHour = df.format(c.getTime());

                    //Fragment_Scan.currentDoctorObject.setTime(theCurrentHour);
                    myRef.child(currentDate).child(""+sender_Activity.currentDoctorObject.getName()).setValue(sender_Activity.currentDoctorObject);
                    myRef.child(currentDate).child(""+sender_Activity.currentDoctorObject.getName()).child("nameOfSender").setValue(sender_Activity.nameOfSender);
                    myRef.child(currentDate).child(""+sender_Activity.currentDoctorObject.getName()).child("time").setValue(theCurrentHour);

                    ref2=database.getReference("Schudule").child("Doctors").child(currentDate);

                   ref2.child(sender_Activity.nameOfSender).child(sender_Activity.currentDoctorObject.getName()).child("collected").setValue(true);


                }

        }

        if(sender_Activity.textResult.getText()==null){
            sender_Activity.textResult.setText("Sorry, we didnt recognize"+"\n\n"+" the Doctor");

        }
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        scannerView.stopCamera();
    }



    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    public void setIdOfDoctorFromScan(int idOfDoctorFromScan) {
        this.idOfDoctorFromScan = idOfDoctorFromScan;
    }

}
