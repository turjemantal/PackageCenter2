package com.example.packagecenter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.provider.CalendarContract.CalendarCache.URI;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Back_up extends Fragment {


    private ArrayList<String> list;
    private String longtext;
    private String currentformattedDate;


    //Gets the collections


    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference ref2;

    public static ArrayList<String> listDoctorsFromFireBase;
    Doctor doctorCurrent;


    public Fragment_Back_up() {
        // Required empty public constructor
    }
    TextView text_backed_up;
    EditText eTextMail;
    Button btn_backup;
    String currentEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_fragment__back_up, container, false);


        Bundle bundle = this.getArguments();
        int day=bundle.getInt("day", 0);
        int month=bundle.getInt("month", 0);
        int year=bundle.getInt("year", 0);
        //Formating the corrent date from the picker
        String str=day+"-"+month+"-"+year;

        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(str);
            String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
            Log.i("this","is time"+formattedDate);
            setCurrentformattedDate(formattedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        btn_backup=v.findViewById(R.id.back_Up_Btn);
        eTextMail=v.findViewById(R.id.editText_mail);
        text_backed_up=v.findViewById(R.id.text_backed_up);


        doctorCurrent=new Doctor();
        database=FirebaseDatabase.getInstance();
        ref=database.getReference("Collections").child("Doctor").child(currentformattedDate) ;
        //write(getContext());
        //list =  read(getContext());
        listDoctorsFromFireBase=new ArrayList<>();


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
                for(int i=0;i<listDoctorsFromFireBase.size();i++) {
                    longtext+="\n"+listDoctorsFromFireBase.get(i)+"\n";
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ptt", "Failed to read value.", error.toException());
            }
        });


        btn_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentEmail=eTextMail.getText().toString();
                sendEmail();
                text_backed_up.setText("Email sent successfully!");
                ref2=database.getReference("Collections").child("Doctor").child(currentformattedDate) ;
                ref2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(),"Record delete successfully....!",Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                        else{
                            Toast.makeText(getActivity(),"Record not deleted",Toast.LENGTH_SHORT).show();

                        }
                    }
                });




            }
        });


        return v;
    }


//Example of write method array to file
    public static void write(Context context) {
        File directory = new File(context.getFilesDir().getAbsolutePath()
                + File.separator + "serlization");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = "file_of_array.txt";
        ObjectOutput out = null;
        ArrayList<String> doctors = new ArrayList<String>();
        doctors.add(new Doctor(1111,"yahel").toString());
        doctors.add(new Doctor(2222,"lavi").toString());
        doctors.add(new Doctor(3333,"mlehe").toString());
        doctors.add(new Doctor(4444,"yosi").toString());


        ////



        try {
            out = new ObjectOutputStream(new FileOutputStream(directory
                    + File.separator + filename));

            out.writeObject(doctors);

            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    public static ArrayList<String>  read(Context context) {

        ObjectInputStream input = null;
        ArrayList<String>  ReturnClass = null;
        String filename = "file_of_array.txt";
        File directory = new File(context.getFilesDir().getAbsolutePath()
                + File.separator + "serlization");
        try {

            input = new ObjectInputStream(new FileInputStream(directory
                    + File.separator + filename));
            ReturnClass = (ArrayList<String> ) input.readObject();
            input.close();
            Log.i("the first","location");


        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ReturnClass;
    }



    public void sendEmail()
    {
        try
        {
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { currentEmail });
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,currentformattedDate+"    Back_up");
            if (URI != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
            }
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, longtext);

            ///
            String filename = "file_of_array.txt";

            File directory = new File(getContext().getFilesDir().getAbsolutePath()
                    + File.separator + "serlization");

            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(directory + File.separator + filename));
            this.startActivity(Intent.createChooser(emailIntent,"Sending email..."));
        }
        catch (Throwable t)
        {
            Toast.makeText(getContext(), "Request failed try again: " + t.toString(),Toast.LENGTH_LONG).show();
        }
    }


    public void setCurrentformattedDate(String currentformattedDate) {
        this.currentformattedDate = currentformattedDate;
    }

}
