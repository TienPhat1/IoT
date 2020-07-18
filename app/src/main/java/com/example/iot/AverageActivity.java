package com.example.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class AverageActivity extends AppCompatActivity {
    private TextView username, date, month, year;
    private String admindata = "admin";
    private ImageView btn_search,btn_logo_average;
    private EditText timeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average);

        btn_search = (ImageView) findViewById(R.id.i_btn_search_average);
        btn_logo_average = (ImageView) findViewById(R.id.i_logo_average);
        date = (TextView) findViewById(R.id.tv_date_average);
        year = (TextView) findViewById(R.id.tv_year_average);
        month = (TextView) findViewById(R.id.tv_month_average);



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
                        .setActivatedYear(today.get(Calendar.DATE))
                        .setMaxYear(31)
                        .setTitle("Select Day")
                        .showYearOnly()
                        .build().show();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0 ; i < dateTime.length; i++) {
                    Log.d("year-month-day", dateTime[i]);
                }
                queryDataByDay(dateTime,dataForDate);

//                try {
//                    wait(2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                for(int i = 0 ; i < dataForDate.size(); i++)
                    Log.d("Data12345",dataForDate.get(i).getArea());
                Log.d("DATASIZE", String.valueOf(dataForDate.size()));
            }
        });

//            Log.d("day", String.valueOf(dayT[0]));
//            Log.d("day", String.valueOf(monthT[0]));


        //////////////////////////////////


//        final FirebaseDatabase root = FirebaseDatabase.getInstance();
//        final ArrayList<Light> dataHistory = new ArrayList<>();
//        final TableLayout table = (TableLayout) findViewById(R.id.table_main_average);
//        Date date1 = new Date();
//
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
//        String date = formatter1.format(date1);
//        String [] dateParterns = date.split("/");
//        Log.d("Date", String.valueOf(dateParterns[0]));
//
//        Query query = FirebaseDatabase.getInstance().getReference().child("History").orderByChild("Area");
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dataHistory.clear();
//                if(dataSnapshot.exists()){
//                    //for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
//                            //Light dataLight = snapshot.getValue(Light.class);
//                            Map<String, Object> data = (Map<String, Object>) snap.getValue();
//                            Log.d("data", String.valueOf(data));
//                            //dataHistory.add(dataLight);
//                        }
//                    //}
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        Log.d("Lenght", String.valueOf(dataHistory.size()));


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
                    Log.d("sig", String.valueOf(1));
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
                    Log.d("sig", String.valueOf(1));
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
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//                    Light lightData = snapshot.getValue(Light.class);
//                    dataForDate.add(lightData);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void init(TableLayout table, ArrayList<String[]> dataHis) {
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
        tv_time.setText("TIME");
        tv_time.setTextColor(Color.RED);
        tv_time.setPadding(0,0,0,0);
        tb_row.addView(tv_time);
        table.addView(tb_row);
        for (int i = 0 ; i < dataHis.size() ; i++)
        {
            TableRow tbrow = new TableRow(this);
            TextView tv_area_ind = new TextView(this);
            tv_area_ind.setText(String.valueOf(dataHis.get(i)[0]));
            tv_area_ind.setTextColor(Color.BLACK);
            tv_area_ind.setPadding(50, 0, 200, 0);
            tbrow.addView(tv_area_ind);
            TextView tv_value_ind = new TextView(this);
            tv_value_ind.setText(String.valueOf(dataHis.get(i)[1]));
            tv_value_ind.setTextColor(Color.BLACK);
            tv_value_ind.setPadding(0, 0, 100, 0);
            tbrow.addView(tv_value_ind);
            TextView tv_time_ind = new TextView(this);
            tv_time_ind.setText(String.valueOf(dataHis.get(i)[2]));
            tv_time_ind.setTextColor(Color.BLACK);
            tv_time_ind.setPadding(0, 0, 0, 0);
            tbrow.addView(tv_time_ind);
            table.addView(tbrow);
        }
    }
}