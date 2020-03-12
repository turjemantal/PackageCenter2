package com.example.packagecenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Signup_Page extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText user_Name;
    private EditText password;
    private EditText email;
    private TextView text_correct;
    private Button SignUpButton;

    private String userName;
    private String lastNameVal;
    private String firstNameVal;
    private String emailVal;
    private int userPass;
    private int idUserVal;

    private boolean theUserExist=false;
    private boolean needToContinue=false;




    //Getting the list from the FireBase of users
    DatabaseReference ref;
    public static List<User> UserList;
    User userCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__page);

        firstName=findViewById(R.id.firstname_edit);
        lastName=findViewById(R.id.lastname_edit);
        user_Name=findViewById(R.id.username_edit);
        password=findViewById(R.id.password_editTxt);
        email=findViewById(R.id.maileditText);
        SignUpButton=findViewById(R.id.button_of_Signup);
        text_correct=findViewById(R.id.text_correct);



        userCurrent=new User();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        ref=database.getReference("Data").child("Users");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserList = new ArrayList<>();

                for (DataSnapshot d: dataSnapshot.getChildren()){
                    User user = d.getValue(User.class);
                    UserList.add(user);
                    Log.i("the user",""+user.getUserName());
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ptt", "Failed to read value.", error.toException());
            }
        });


        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        theUserExist=false;
                userName=user_Name.getText().toString();
                userPass=Integer.parseInt(password.getText().toString());


                for(int i=0;i<UserList.size();i++){
                    /* Check if we find id exist in the list of doctors */
                    if(userName.equals(UserList.get(i).getUserName())||userPass==UserList.get(i).getUserPassword()){
                        theUserExist=true;
                        text_correct.setText("Try other username and password.");

                    }
                }
                //If the User isn't exist in the database of user can't login

                if(theUserExist==false){
                    lastNameVal=lastName.getText().toString();
                    firstNameVal=firstName.getText().toString();
                    emailVal=email.getText().toString();
//
                    int i;
                    while(true) {
                        boolean theIdSame=false;

                        idUserVal = (int) (Math.random() * ((9999 - 1000) + 1)) + 1000;


                        for ( i = 0; i < UserList.size(); i++) {

                            if (UserList.get(i).getUserId() == idUserVal) {
                                break;
                            }
                        }

                        if ((i==UserList.size()))
                            break;
                        else continue;
                    }

                    User userFromSignup=new User(idUserVal,userName,firstNameVal,lastNameVal,emailVal,userPass);
                    ref.child(firstNameVal).setValue(userFromSignup);
                    Intent newintent=new Intent(Signup_Page.this,FirstScreen.class);
                    newintent.putExtra("nameOfUser",firstNameVal);
                    UserList.clear();
                    startActivity(newintent);

                }


            theUserExist=false;

            }
        });
    }
}
