package com.example.packagecenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LogInHere extends AppCompatActivity {


    private EditText editTextUserName;
    private EditText editTextPass;
    private TextView textShow;
    private TextView toSignUptext;
    private Button buttonOfLogIn;
    private String userName;
    private int userPass;
    private boolean theUserNameOfUserIsExist=false;
    Spinner spinnerjobs;
    String jobChose;


    //Getting the list from the FireBase of users
    DatabaseReference ref;
    public static List<User> UserList;
    User userCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_here);


        editTextUserName=findViewById(R.id.username_editTxt);
        editTextPass=findViewById(R.id.password_editTxt);
        textShow=findViewById(R.id.textShowSome);
        toSignUptext=findViewById(R.id.toSignUptext);
        buttonOfLogIn=findViewById(R.id.button_of_Login);
        spinnerjobs=findViewById(R.id.jobs_spinner_login_here);


        //Create Users List
        userCurrent=new User();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        ref=database.getReference("Data").child("Users");
        User usertry=new User("yahel",31232);
        ref.child(usertry.getUserName()).setValue(usertry);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserList = new ArrayList<>();

                for (DataSnapshot d: dataSnapshot.getChildren()){
                    User user = d.getValue(User.class);
                    UserList.add(user);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ptt", "Failed to read value.", error.toException());
            }
        });

        ArrayAdapter adapter=ArrayAdapter.createFromResource(this,R.array.jobs_array,R.layout.color_spinner_layout);
        adapter.setDropDownViewResource(R.layout.drop_down_spinner);
            spinnerjobs.setAdapter(adapter);


        buttonOfLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName=editTextUserName.getText().toString();
                userPass=Integer.parseInt(editTextPass.getText().toString());


                for(int i=0;i<UserList.size();i++){
                    /* Check if we find id exist in the list of doctors */
                    if(userName.equals(UserList.get(i).getUserName())&&userPass==UserList.get(i).getUserPassword()){

                        jobChose = spinnerjobs.getSelectedItem().toString();

                        if(jobChose.equals("Doctor/Clinic")||jobChose.equals("Shipping Center")) {
                             theUserNameOfUserIsExist=true;
                            Intent newIntent = new Intent(LogInHere.this, FirstScreen.class);
                            newIntent.putExtra("nameOfUser", userName);
                            UserList.clear();
                            textShow.setText("The user Correct.");

                            startActivity(newIntent);
                            finish();
                        }

                        else if(jobChose.equals("Delivery Person")){
                            Intent newIntent1 = new Intent(LogInHere.this, sender_Activity.class);
                            newIntent1.putExtra("nameOfUser", userName);
                            UserList.clear();
                            textShow.setText("The user Correct.");
                            startActivity(newIntent1);
                            finish();

                       }

                    }
                }
                //If the User isn't exist in the database of user can't login
                    textShow.setText("The user is incorrect.");
            }
        });

        toSignUptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent=new Intent(LogInHere.this,Signup_Page.class);
                startActivity(newIntent);

            }
        });

    }
}
