package com.example.packagecenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class LogInActivity extends AppCompatActivity {


    private LoginButton loginButton;
    private CircleImageView circleImageView;
    private TextView textName,textEmail;
    private Button continButton;
    DatabaseReference ref;


    GoogleSignInClient mGoogleSignInClient;

    private CallbackManager callbackManager;
    private String stringId;
    private Button continureButton;
    private Bitmap bitmap;
    SignInButton signInButton;
    Button signin;
    int RC_SIGN_IN=0;
    Spinner jobs_spinner_login_activity;
    String jobChose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //spiner
        jobs_spinner_login_activity=findViewById(R.id.jobs_spinner_login_activity);
        //jobs_spinner_login_activity.setVisibility(View.GONE);

        ArrayAdapter adapter=ArrayAdapter.createFromResource(this,R.array.jobs_array,R.layout.color_spinner_layout);
        adapter.setDropDownViewResource(R.layout.drop_down_spinner);
        jobs_spinner_login_activity.setAdapter(adapter);


        //FACEBOOK BUTTON
        loginButton=findViewById(R.id.login_button);
        //


        textName=findViewById(R.id.profile_name);
        textEmail=findViewById(R.id.profile_email);
        circleImageView=findViewById(R.id.profile_pic);

        //continue BUTTON
        continButton=findViewById(R.id.continButton);
        continButton.setVisibility(View.GONE);

        //Sign in button

        signin=findViewById(R.id.signIn);
        //continButton.setVisibility(View.VISIBLE);




        //GOOGLE//
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setVisibility(View.VISIBLE);



        circleImageView.getLayoutParams().height=350;
        circleImageView.getLayoutParams().width=350;

        Glide.with(LogInActivity.this).load(R.drawable.box_colored).into(circleImageView);
        if(signin.getVisibility()==View.VISIBLE){
           // continButton.setVisibility(View.GONE);


        }

        else{
            continButton.setVisibility(View.VISIBLE);
        }

if(continButton.getVisibility()==View.VISIBLE){
    jobs_spinner_login_activity.setVisibility(View.VISIBLE);
}



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });



        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibra();
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;


                }
            }


            public void vibra(){
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);






        //Facebook callback
        callbackManager=CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        checkLoginStatus();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                jobs_spinner_login_activity.setVisibility(View.VISIBLE);
                continButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibra();
               openActivity();
            }

            public void vibra(){
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);
            }
        });


        continButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibra();
                open2Activity();
            }

            public void vibra(){
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);
            }
        });

    }

    //function of google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }




    public void openActivity(){

        Intent nextintent=new Intent(this,LogInHere.class);

        startActivity(nextintent);

    }
    public void open2Activity(){
        jobChose = jobs_spinner_login_activity.getSelectedItem().toString();
        Intent nextintent;
        if(jobChose.equals("Doctor/Clinic")||jobChose.equals("Shipping Center")) {
            nextintent = new Intent(this, FirstScreen.class);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            ref = database.getReference("Data").child("Users");
            String fullNameFromFaceBook = textName.getText().toString();
            String[] parts = fullNameFromFaceBook.split(" ");
            String firstName = parts[0];
            String lastName = parts[1];
            String EmailFromFaceBook = textEmail.getText().toString();
            String idOfImg = stringId;

            User userFromFaceBookLogIn = new User(firstName, lastName, EmailFromFaceBook, idOfImg);
            ref.child(userFromFaceBookLogIn.getFirstName()).setValue(userFromFaceBookLogIn);
            nextintent.putExtra("nameOfUser", firstName);
            nextintent.putExtra("idOfImage", stringId);
            startActivity(nextintent);
        }

        else if(jobChose.equals("Delivery Person")){
            nextintent = new Intent(this, sender_Activity.class);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            ref = database.getReference("Data").child("Users");
            String fullNameFromFaceBook = textName.getText().toString();
            String[] parts = fullNameFromFaceBook.split(" ");
            String firstName = parts[0];
            String lastName = parts[1];
            String EmailFromFaceBook = textEmail.getText().toString();
            String idOfImg = stringId;

            User userFromFaceBookLogIn = new User(firstName, lastName, EmailFromFaceBook, idOfImg);
            ref.child(userFromFaceBookLogIn.getFirstName()).setValue(userFromFaceBookLogIn);
            nextintent.putExtra("nameOfUser", firstName);
            nextintent.putExtra("idOfImage", stringId);
            startActivity(nextintent);

        }


        //        nextintent.putExtra("first_name",textName.getText().toString());
//        nextintent.putExtra("email",textEmail.getText().toString());
//
//        nextintent.putExtra("id",stringId);
//
    }









    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken==null){
                textName.setText("");
                textEmail.setText("");
                circleImageView.setImageResource(0);
                Toast.makeText(LogInActivity.this,"User logged out",Toast.LENGTH_LONG).show();
                signin.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.VISIBLE);
                continButton.setVisibility(View.INVISIBLE);
                jobs_spinner_login_activity.setVisibility(View.INVISIBLE);

                Glide.with(LogInActivity.this).load(R.drawable.box_colored).into(circleImageView);


            }

            else{
                loaduserProfile(currentAccessToken);
            }
        }
    };


    private void loaduserProfile(AccessToken newAccessToken)
    {
        GraphRequest request=GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {
                try {
                    String first_name=object.getString("first_name");
                    String last_name=object.getString("last_name");
                    String email=object.getString("email");
                    String id=object.getString("id");


                    String image_url="http://graph.facebook.com/"+id+ "/picture?type=normal";

                    textEmail.setText(email);
                    textName.setText(first_name+"  "+last_name);
                    RequestOptions requestOptions=new RequestOptions();
                    requestOptions.dontAnimate();

                    stringId=id;
                    signin.setVisibility(View.GONE);

                    continButton.setVisibility(View.VISIBLE);
                    jobs_spinner_login_activity.setVisibility(View.VISIBLE);
                    signInButton.setVisibility(View.GONE);


                    Glide.with(LogInActivity.this).load(image_url).into(circleImageView);




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters=new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void checkLoginStatus(){
        if(AccessToken.getCurrentAccessToken()!=null){
            loaduserProfile(AccessToken.getCurrentAccessToken());
        }
    }

}
