package com.example.packagecenter;


import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Adds extends Fragment {


    public Fragment_Adds() {
        // Required empty public constructor
    }
    private String doctorname;
    private int idOfDoctor;
    private EditText editTextId;
    private EditText editTextname;
    private TextView editTextShowDoctor;
    private Button buttonOfAdd;
    private boolean theIdOfDoctorisExist=false;

    //Get the list of doctors to check the id

    //Getting the list from the FireBase
    DatabaseReference ref;
    public static List<Doctor> DoctorList;
    Doctor doctorCurrent;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fragment__adds, container, false);

        editTextId=v.findViewById(R.id.id_editTxt);
        editTextname=v.findViewById(R.id.name_editTxt);
        editTextShowDoctor=v.findViewById(R.id.textShowDoc);
        buttonOfAdd=v.findViewById(R.id.button_of_add);

        //Belong to get the list of doctors
        doctorCurrent=new Doctor();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
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


        buttonOfAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibra();
                doctorname=editTextname.getText().toString();
                idOfDoctor=Integer.parseInt(editTextId.getText().toString());

                for(int i=0;i<DoctorList.size();i++){
                    /* Check if we find id exist in the list of doctors */
                    if(idOfDoctor==DoctorList.get(i).getId()){
                        //here we find the doctor
                        editTextShowDoctor.setText("Sorry ,"+"\n\n"+"try other id.");
                        theIdOfDoctorisExist=true;

                    }
                }
                //If the doctor id isn't exist in the doctor list from firebase so add him into.

                if(theIdOfDoctorisExist==false){

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Adding the Doctor
                            Doctor doctor=new Doctor(idOfDoctor,doctorname);
                            ref.child(doctorname).setValue(doctor);
                            editTextShowDoctor.setText("Doctor/medical clinic,"+"\n\n"+doctorname+" Add to the data.");

                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("ptt", "Failed to read value.", error.toException());
                        }
                    });


                }



                theIdOfDoctorisExist=false;
            }

            public void vibra(){
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);
            }

        });




        return v;
    }

}
