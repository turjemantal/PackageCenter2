package com.example.packagecenter;


import android.app.DatePickerDialog;
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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
public class Fragment_show_shcudule extends Fragment {




    //User
    DatabaseReference ref;
    String UserName="";
    User userCurrent;
    FirebaseDatabase database;
    private Spinner spinner_User;
    public static List<String> UserNameList;

    //Date picker values
    private DatePickerDialog picker;
    EditText eText;

    private int dayChoose;
    private int monthChoose;
    private int yearChoose;
    String fullDate;

    //Button
    Button show_list_shcudule;

    private list_Show list_show;


    public Fragment_show_shcudule() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fragment_show_shcudule, container, false);

        userCurrent=new User();
        database = FirebaseDatabase.getInstance();
        ref=database.getReference("Data").child("Users");


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




        ///Date picker
        eText=(EditText) v.findViewById(R.id.date_picker_txt_show);
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
                        Log.i("date "," date "+dayOfMonth+"-"+monthOfYear+1+"-"+year);

                    }}, year, month, day);
                picker.show();
            }
        });


        //Show
        spinner_User=v.findViewById(R.id.spinner_User_show);

        show_list_shcudule=v.findViewById(R.id.show_list_shcudule);
        list_show=new list_Show();
        show_list_shcudule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibra();
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameMain,list_show);
                fragmentTransaction.commit();


                UserName = spinner_User.getSelectedItem().toString();
                fullDate=createDateString(yearChoose,monthChoose,dayChoose);


                Log.i("date "," date "+fullDate);


                Bundle bundle = new Bundle();
                bundle.putString("fulldate", fullDate);
                bundle.putString("user_to_show", UserName);
                list_show.setArguments(bundle);
            }

            public void vibra(){
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);
            }
        });



        return  v;
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

}
