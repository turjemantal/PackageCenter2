package com.example.packagecenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirstScreen extends AppCompatActivity {


    BottomNavigationView bottomNavigation;
    FrameLayout frameMain;
    private Fragment_Collections fragment_collections;
    private Fragment_Adds fragment_adds;
    private Fragment_Scan fragment_scan;
    private Fragment_Shcudules fragment_shcudules;
    private TextView textWelcome;
    private String nameOfUser;
    private String idOfImage;
    private CircleImageView ImageWelcome;
    private Bundle bundle;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);


        bottomNavigation = findViewById(R.id.bottom_navigation);
        frameMain = findViewById(R.id.frameMain);
        fragment_collections =new Fragment_Collections();
        fragment_adds =new Fragment_Adds();
        fragment_scan=new Fragment_Scan();
        fragment_shcudules=new Fragment_Shcudules();
        textWelcome=findViewById(R.id.TextWelcome);
        ImageWelcome=findViewById(R.id.ImageWelcome);

        nameOfUser = getIntent().getExtras().getString("nameOfUser",null);
        if(nameOfUser.equals(null)){
            nameOfUser="";
        }

        idOfImage=getIntent().getExtras().getString("idOfImage",null);
        Log.i("id",""+idOfImage);
        if(idOfImage!=null){
            String image_url="http://graph.facebook.com/"+ idOfImage + "/picture?type=normal";
            RequestOptions requestOptions=new RequestOptions();
            requestOptions.dontAnimate();
            Glide.with(FirstScreen.this).load(image_url).into(ImageWelcome);

        }

        else{
            ImageWelcome.setImageResource(R.drawable.box_colored);

        }


        bundle = new Bundle();
        bundle.putString("nameOf", nameOfUser);
        fragment_scan.setArguments(bundle);
        fragment_collections.setArguments(bundle);
        fragment_adds.setArguments(bundle);





        textWelcome.setText("Welcome "+nameOfUser+"!");
        bottomNavigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {

            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_collections:
                        vibra();
                        setFragment(fragment_collections);
                        textWelcome.setText("");
                        ImageWelcome.setImageResource(0);


                        break;


                    case R.id.navigation_adds:
                        vibra();
                        setFragment(fragment_adds);
                        textWelcome.setText("");
                        ImageWelcome.setImageResource(0);
                        break;


                    case R.id.navigation_scan:
                        vibra();
                        setFragment(fragment_scan);
                        textWelcome.setText("");
                        ImageWelcome.setImageResource(0);

                        break;

                    case R.id.navigation_Shcudule:
                        vibra();
                        setFragment(fragment_shcudules);
                        textWelcome.setText("");
                        ImageWelcome.setImageResource(0);
                        break;

                }
            }
            public void setFragment(Fragment fragment) {
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameMain,fragment);
                fragmentTransaction.commit();
            }

            public void vibra(){
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);
            }

        });

    }

    @Override public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameMain);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

}
