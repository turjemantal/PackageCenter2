package com.example.packagecenter;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Collections extends Fragment {


    private listFragment listFragment;
    private Fragment_Back_up fragment_back_up;
    private DatePickerDialog picker;
    EditText eText;
    Button btnGet;

    Button back_up_Collections;
    TextView tvw;

    private int dayChoose;
    private int monthChoose;
    private int yearChoose;

    public Fragment_Collections() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fragment__search, container, false);
        listFragment=new listFragment();
        fragment_back_up=new Fragment_Back_up();


        tvw=(TextView)v.findViewById(R.id.textView2);
        eText=(EditText) v.findViewById(R.id.editText2);
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
        btnGet=(Button)v.findViewById(R.id.button2);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibra();
             //  Intent newIntent = new Intent(getContext(), MainActivity.class);
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameMain,listFragment);
                fragmentTransaction.commit();

                Bundle bundle = new Bundle();
                bundle.putInt("day", dayChoose);
                bundle.putInt("month", monthChoose);
                bundle.putInt("year", yearChoose);
                listFragment.setArguments(bundle);
//               newIntent.putExtra("day", dayChoose); //Optional parameters
//                newIntent.putExtra("month", monthChoose);
//                newIntent.putExtra("year", yearChoose);
//               startActivity(newIntent);
                tvw.setText("Selected Date: "+ eText.getText());
            }

            public void vibra(){
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);
            }


        });




        back_up_Collections=v.findViewById(R.id.back_up_Collections);
        back_up_Collections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibra();
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameMain,fragment_back_up);
                fragmentTransaction.commit();

                Bundle bundle = new Bundle();
                bundle.putInt("day", dayChoose);
                bundle.putInt("month", monthChoose);
                bundle.putInt("year", yearChoose);
                fragment_back_up.setArguments(bundle);
                tvw.setText("Selected Date: "+ eText.getText());

            }

            public void vibra(){
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);
            }
        });




        return v;
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
