package com.example.packagecenter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Scan extends Fragment implements IOnBackPressed{


    public Fragment_Scan() {
        // Required empty public constructor
    }
    private ZXingScannerView scannerView;
    public static TextView textResult;
    private Button scan_btn;
    public static Doctor currentDoctorObject;
    private String nameOfSender;

    //Getting the list from the FireBase
    DatabaseReference ref;
    public static List<Doctor> DoctorList;
    Doctor doctorCurrent;


    //ref for adding to the missions
    DatabaseReference ref2;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fragment__scan, container, false);
        Bundle bundle = this.getArguments();
        nameOfSender=bundle.getString("nameOf", null);
        Log.i("name of","sender"+nameOfSender);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Data").child("Doctors");
//        Doctor newDoc=new Doctor(1111,"klalit-smile ashdod");
//        Doctor newDoc1=new Doctor(2222,"klalit-smile karmiel");
//
//        myRef.child("Doc1").setValue(newDoc);
//        myRef.child("Doc2").setValue(newDoc1);

        /* 1) Start to create the arrayOfDoctors reading from FireBase ErrandsProject*/

        doctorCurrent=new Doctor();
        database=FirebaseDatabase.getInstance();
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

        /////







        //////
        /* 2) Scan Action  */
        scannerView=v.findViewById(R.id.zxscan);

        scan_btn=v.findViewById(R.id.btn_scan);
        textResult=v.findViewById(R.id.text_res);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibra();
                //startActivity(new Intent(getActivity(),Scan_activity.class));
                Log.i("you","click");

                IntentIntegrator.forSupportFragment(Fragment_Scan.this).initiateScan();


            }

            public void vibra(){
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);
            }
        });


        return v;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        int barcode = Integer.parseInt(result.getContents());

        for (int i = 0; i<Fragment_Scan.DoctorList.size(); i++) {
            // try {
            final Doctor doctor = Fragment_Scan.DoctorList.get(i);
            int id = doctor.getId();
            String name = doctor.getName();

            /* Cehcks who is the doctor that get's from the Scan */
            if (barcode == id) {
                /*    Brings the current doctor from scan to the main by Json and by Object  */

                Fragment_Scan.currentDoctorObject=new Doctor(id,name);
                Fragment_Scan.textResult.setText("Package collected"+"\n\n"+"from: " + name + "\n\n"+"by "+nameOfSender);

                /* 3) Take the current doctor after scan and upload to the FireBase under the current date */

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("Collections").child("Doctor");

                final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                //Calculate the current hour
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                String theCurrentHour = df.format(c.getTime());

                //Fragment_Scan.currentDoctorObject.setTime(theCurrentHour);
                myRef.child(currentDate).child(""+Fragment_Scan.currentDoctorObject.getName()).setValue(Fragment_Scan.currentDoctorObject);
                myRef.child(currentDate).child(""+Fragment_Scan.currentDoctorObject.getName()).child("nameOfSender").setValue(nameOfSender);
                myRef.child(currentDate).child(""+Fragment_Scan.currentDoctorObject.getName()).child("time").setValue(theCurrentHour);

                ref2=database.getReference("Schudule").child("Doctors").child(currentDate);

                ref2.child(nameOfSender).child(currentDoctorObject.getName()).child("collected").setValue(true);




            }

        }

    }


    @Override
    public boolean onBackPressed() {
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameMain,Fragment_Scan.this);
        fragmentTransaction.commit();
        return true;
    }
}
