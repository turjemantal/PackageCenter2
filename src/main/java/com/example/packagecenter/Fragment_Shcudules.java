package com.example.packagecenter;


import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Shcudules extends Fragment {


    private Button showbtn;
    private Button addBtn;


    private Fragment_Add_mission fragment_add_mission;
    private Fragment_show_shcudule fragment_show_shcudule;

    public Fragment_Shcudules() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fragment_shcudules, container, false);

        showbtn=v.findViewById(R.id.show_shcudules_btn);
        addBtn=v.findViewById(R.id.Add_shcudule_btn);



        //Jump to show the shcudule
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibra();
                fragment_add_mission=new Fragment_Add_mission();
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameMain,fragment_add_mission);
                fragmentTransaction.commit();

            }

            public void vibra(){
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);
            }
        });



        //Jump to show the addbtn

        showbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibra();
                fragment_show_shcudule=new Fragment_show_shcudule();
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameMain,fragment_show_shcudule);
                fragmentTransaction.commit();

            }

            public void vibra(){
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);
            }
        });




        return v;
    }

}
