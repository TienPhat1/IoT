package com.example.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iot.Model.Light;
import com.example.iot.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Objects;

public class AverageActivity extends AppCompatActivity {
    private TextView username, date, month, year;
    private String admindata = "admin";
    private ImageView btn_search,btn_logo_average;
    private Button btn_ok,btn_viewgraph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average);

        btn_search = (ImageView) findViewById(R.id.i_btn_search_average);
        btn_logo_average = (ImageView) findViewById(R.id.i_logo_average);
        date = (TextView) findViewById(R.id.tv_date_average);
        year = (TextView) findViewById(R.id.tv_year_average);
        month = (TextView) findViewById(R.id.tv_month_average);
        btn_ok = (Button) findViewById(R.id.btn_OK);
        btn_viewgraph = (Button) findViewById(R.id.btn_viewgraph);
        btn_viewgraph.setVisibility(Button.INVISIBLE);
        final TableLayout table = (TableLayout) findViewById(R.id.table_main_average);


        btn_logo_average.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AverageActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        username = (TextView) findViewById(R.id.tv_admin_average);
        FirebaseDatabase dataUser = FirebaseDatabase.getInstance();
        DatabaseReference RootRef = dataUser.getReference();
        DatabaseReference rf1= RootRef;
        DatabaseReference rf2 = RootRef.child("History");
        rf1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users adminData = dataSnapshot.child("Admin").child(admindata).getValue(Users.class);
                username.setText(adminData.getUsername().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /////////////////////////////////////////////////
//        MQTT startMQTT = new MQTT(getApplicationContext());
//        Context context = startMQTT.getAppContex();
//        startMQTT.startMQTT(context);

        ///////////////////////////////////////////////////////////////////////////////////
        final ArrayList<Light> dataForDate = new ArrayList<Light>();
        final String[] dateTime = {"","",""};
        //Log.d("Lenght date", String.valueOf(dateTime.length));
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar today  = Calendar.getInstance();
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(AverageActivity.this,
                        new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        year.setText(String.valueOf(selectedYear));
                        dateTime[0] = (String.valueOf(selectedYear));
                    }
                },today.get(Calendar.YEAR),today.get(Calendar.MONTH));
                builder.setActivatedMonth(Calendar.JULY)
                        .setMinYear(2000)
                        .setActivatedYear(2020)
                        .setMaxYear(2030)
                        .setTitle("Select Year")
                        .showYearOnly()
                        .build().show();
            }
        });

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar today  = Calendar.getInstance();
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(AverageActivity.this,
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {
                                month.setText(String.valueOf(selectedMonth+1));
                                dateTime[1] = (0 +String.valueOf(selectedMonth+1));
                            }
                        },today.get(Calendar.YEAR),today.get(Calendar.MONTH));
                builder.setActivatedMonth(Calendar.JULY)
                        .setMinMonth(0)
                        .setActivatedYear(today.get(Calendar.YEAR))
                        .setMaxMonth(11)
                        .setTitle("Select Month")
                        .showMonthOnly()
                        .build().show();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar today  = Calendar.getInstance();
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(AverageActivity.this,
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {
                                date.setText(String.valueOf(selectedYear));
                                dateTime[2] = (String.valueOf(selectedYear));
                            }
                        },today.get(Calendar.YEAR),today.get(Calendar.MONTH));
                builder.setActivatedMonth(Calendar.JULY)
                        .setMinYear(1)
                        .setActivatedYear(1)
                        .setMaxYear(31)
                        .setTitle("Select Day")
                        .showYearOnly()
                        .build().show();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryDataByDay(dateTime,dataForDate);
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataForDate.size()  == 0) {
                    Toast.makeText(AverageActivity.this,"The selected date has no data",Toast.LENGTH_LONG).show();
                }
                else{
                    HashMap<String, String[]> dataAverage = computeDataAverage(dataForDate);
                    Log.d("daaaaaaaa", String.valueOf(dataAverage));
                    init(table, (HashMap<String, String[]>) dataAverage);
                    btn_viewgraph.setVisibility(Button.VISIBLE);
                }
            }
        });

        btn_viewgraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap <String, String[]> dataAverage = computeDataAverage(dataForDate);
                Intent intent = new Intent(AverageActivity.this,GraphActivity.class);
                intent.putExtra("DataAverage",dataAverage);
                intent.putExtra("Date",dateTime);
                startActivity(intent);
            }
        });



    }

    @SuppressLint("DefaultLocale")
    private HashMap<String,String[]> computeDataAverage(ArrayList<Light> dataForDate) {
        HashMap<String, String[]> value = new HashMap<>();
        HashMap<String, int[]> valueTemp = new HashMap<>();

        for(Light light: dataForDate){
            if(valueTemp.get(light.getArea())== null){
                int [] lightvalue = new int[3];
                lightvalue[0] = Integer.parseInt(light.getValue());
                lightvalue[1] = 1;
                lightvalue[2] = Integer.parseInt(light.getPower());
                valueTemp.put(light.getArea(),lightvalue);
            }
            else{
                int [] lightvalue = new int[3];
                lightvalue[0] = Objects.requireNonNull(valueTemp.get(light.getArea()))[0] + Integer.parseInt(light.getValue());
                lightvalue[1] = Objects.requireNonNull(valueTemp.get(light.getArea()))[1] + 1;
                lightvalue[2] = Objects.requireNonNull(valueTemp.get(light.getArea()))[2] + Integer.parseInt(light.getPower());
                valueTemp.put(light.getArea(),lightvalue);
            }
        }

        for(String key: valueTemp.keySet()){
            Log.d("Key",key);
            String [] valueAver = new String[2];
            valueAver[0] = String.format("%.2f",(float)Objects.requireNonNull(valueTemp.get(key))[0] / (float)Objects.requireNonNull(valueTemp.get(key))[1]);
            valueAver[1] = String.format("%.2f",(float)Objects.requireNonNull(valueTemp.get(key))[2] / (float)Objects.requireNonNull(valueTemp.get(key))[1]);
            value.put(key,valueAver);
        }

        return value;
    }

    private void queryDataByDay(String[] dateTime, final ArrayList<Light> dataForDate) {
        String dateChoose = dateTime[0]+dateTime[1]+dateTime[2];
        Query query;
        Log.d("datechoose", String.valueOf(dateChoose.length()));
        if(dateChoose.length() == 0){
            Toast.makeText(AverageActivity.this,"Please choose date...",Toast.LENGTH_SHORT).show();
        }
        else{
            switch (dateChoose.length()){
                case 4:
                    Log.d("sig", String.valueOf(1));
                    query = FirebaseDatabase.getInstance().getReference().child("History").orderByChild("Year").equalTo(dateChoose);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Light lightData = snapshot.getValue(Light.class);
                                dataForDate.add(lightData);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    break;
                case 6:
                    query = FirebaseDatabase.getInstance().getReference().child("History").orderByChild("Month").equalTo(dateChoose);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Light lightData = snapshot.getValue(Light.class);
                                dataForDate.add(lightData);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    break;
                case 8:
                    query  = FirebaseDatabase.getInstance().getReference().child("History").orderByChild("Day").equalTo(dateChoose);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Light lightData = snapshot.getValue(Light.class);
                                dataForDate.add(lightData);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    break;
            }
        }

    }

    private void init(TableLayout table, HashMap<String, String[]> dataAverage) {
        TableRow tb_row = new TableRow(this);
        TextView tv_area = new TextView(this);
        tv_area.setText("AREA");
        tv_area.setTextColor(Color.RED);
        tv_area.setPadding(50,0,250,0);
        tb_row.addView(tv_area);
        TextView tv_value = new TextView(this);
        tv_value.setText("VALUE");
        tv_value.setTextColor(Color.RED);
        tv_value.setPadding(0,0,150,0);
        tb_row.addView(tv_value);
        TextView tv_time = new TextView(this);
        tv_time.setText("POWER");
        tv_time.setTextColor(Color.RED);
        tv_time.setPadding(0,0,0,0);
        tb_row.addView(tv_time);
        table.addView(tb_row);
        for (String key : dataAverage.keySet())
        {
            TableRow tbrow = new TableRow(this);
            TextView tv_area_ind = new TextView(this);
            tv_area_ind.setText(key);
            tv_area_ind.setTextColor(Color.BLACK);
            tv_area_ind.setPadding(50, 0, 200, 0);
            tbrow.addView(tv_area_ind);
            TextView tv_value_ind = new TextView(this);
            tv_value_ind.setText(Objects.requireNonNull(dataAverage.get(key))[0]);
            tv_value_ind.setTextColor(Color.BLACK);
            tv_value_ind.setPadding(0, 0, 100, 0);
            tbrow.addView(tv_value_ind);
            TextView tv_time_ind = new TextView(this);
            tv_time_ind.setText(Objects.requireNonNull(dataAverage.get(key))[1]);
            tv_time_ind.setTextColor(Color.BLACK);
            tv_time_ind.setPadding(0, 0, 0, 0);
            tbrow.addView(tv_time_ind);
            table.addView(tbrow);
        }
    }
}