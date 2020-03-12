package com.example.packagecenter;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Add_mission extends Fragment {


    public Fragment_Add_mission() {
        // Required empty public constructor
    }

    //Date picker values
    private DatePickerDialog picker;
    EditText eText;
    //Time picker dialog
    TimePickerDialog timePickerDialog;
    EditText chooseTime;
    private int hourCurrent;
    private int minuteCurrent;
    Calendar calendar;
    String amPm;

    private int dayChoose;
    private int monthChoose;
    private int yearChoose;


    //Getting the list from the FireBase
    DatabaseReference ref;
    DatabaseReference ref2;
    DatabaseReference ref3;



    //User
    User userCurrent;
    FirebaseDatabase database;
    private Spinner spinner_User;
    public static List<String> UserNameList;


    //Doctor/Clinic
    Doctor doctorCurrent;
    private Spinner spinner_Doctor;
    public static List<String> DoctorNameList;


    //Button add
    Button Add_shcudule;

    TextView text_after_add;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_fragment__add_mission, container, false);


        userCurrent=new User();
        database = FirebaseDatabase.getInstance();
        ref=database.getReference("Data").child("Users");
        spinner_User=v.findViewById(R.id.spinner_User);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserNameList = new ArrayList<>();

                for (DataSnapshot d: dataSnapshot.getChildren()){
                    User user = d.getValue(User.class);
                    if(user.getUserName()!=null) {
                        UserNameList.add(user.getUserName());
                    }
                    Log.i("user","name:"+user.getUserName());
                }
                Collections.sort(UserNameList, String.CASE_INSENSITIVE_ORDER);

                ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),R.layout.style_of_user_spin,R.id.text_style_user_spin,UserNameList);
                spinner_User.setAdapter(adapter);


            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ptt", "Failed to read value.", error.toException());
            }
        });





        doctorCurrent=new Doctor();
        database = FirebaseDatabase.getInstance();
        ref2=database.getReference("Data").child("Doctors");
        spinner_Doctor=v.findViewById(R.id.spinner_Doctor);


        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DoctorNameList = new ArrayList<>();

                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Doctor doctor = d.getValue(Doctor.class);
                    if(doctor.getName()!=null) {
                        DoctorNameList.add(doctor.getName());
                    }
                    Log.i("doctor","name:"+doctor.getName());
                }

                Collections.sort(DoctorNameList, String.CASE_INSENSITIVE_ORDER);

                ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),R.layout.style_of_user_spin,R.id.text_style_user_spin,DoctorNameList);
                spinner_Doctor.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ptt", "Failed to read value.", error.toException());
            }
        });




        ///Date picker
        eText=(EditText) v.findViewById(R.id.editText_date_picker);
        eText.setInputType(InputType.TYPE_NULL);

        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        setDayChoose(dayOfMonth);
                        setMonthChoose(monthOfYear+1);
                        setYearChoose(year);

                    }}, year, month, day);
                picker.show();
            }
        });

        //Time picker

        chooseTime=(EditText) v.findViewById(R.id.editText_time_picker);
        chooseTime.setInputType(InputType.TYPE_NULL);


        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar=Calendar.getInstance();
                hourCurrent=calendar.get(Calendar.HOUR_OF_DAY);
                minuteCurrent=calendar.get(Calendar.MINUTE);
                timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay>=12){
                            amPm="PM";
                        }
                        else{
                            amPm="AM";
                        }
                        setHourCurrent(hourOfDay);
                        setMinuteCurrent(minute);
                        chooseTime.setText(hourOfDay+":"+minute);
                    }
                },hourCurrent,minuteCurrent,false);
                timePickerDialog.show();
            }
        });

        Add_shcudule=v.findViewById(R.id.Add_shcudule);
        text_after_add=v.findViewById(R.id.text_after_add);
        ref3=database.getReference("Schudule").child("Doctors");


        Add_shcudule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibra();
                String UserName = spinner_User.getSelectedItem().toString();
                String DoctorName = spinner_Doctor.getSelectedItem().toString();
                String time;
              if(minuteCurrent<10){
                  time=hourCurrent+":0"+minuteCurrent+":00";

              }
              else{
                  time=hourCurrent+":"+minuteCurrent+":00";

              }
              String currentDate=createDateString(yearChoose,monthChoose,dayChoose);
                Doctor doctorAdded=new Doctor(DoctorName,time,UserName);
                Log.i("the doc","doctor: "+doctorAdded.getName()+"time: "+doctorAdded.getTime());
                text_after_add.setText("This mission added to "+doctorAdded.getNameOfSender()+"'s list"+"\n"+"at "+doctorAdded.getTime()+","+currentDate+", From "+doctorAdded.getName() );
                ref3.child(currentDate).child(doctorAdded.getNameOfSender()).child(doctorAdded.getName()).setValue(doctorAdded);

            }

            public void vibra(){
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);
            }
        });


        return v;
    }

    public String createDateString(int yearChoose,int monthChoose,int dayChoose){
        String str=dayChoose+"-"+monthChoose+"-"+yearChoose;
        String formattedDate="";
        //Formating the corrent date from the picker
        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(str);
            formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
            Log.i("this","is time"+formattedDate);
            return formattedDate;

        } catch (ParseException e) {
            e.printStackTrace();
            return formattedDate;

        }

    }


    public void setDayChoose(int dayChoose) {
        this.dayChoose = dayChoose;
    }

    public void setMonthChoose(int monthChoose) {
        this.monthChoose = monthChoose;
    }

    public void setYearChoose(int yearChoose) {
        this.yearChoose = yearChoose;
    }

    public void setHourCurrent(int hourCurrent) {
        this.hourCurrent = hourCurrent;
    }

    public void setMinuteCurrent(int minuteCurrent) {
        this.minuteCurrent = minuteCurrent;
    }
}
