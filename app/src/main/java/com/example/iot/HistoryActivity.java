package com.example.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.iot.Model.Light;
import com.example.iot.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {
    private TextView username;
    private String admindata = "admin";
    private ImageView btn_search,btn_logo_history;
    private EditText timeData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        btn_search = (ImageView) findViewById(R.id.i_btn_search);
        timeData = (EditText) findViewById(R.id.et_history);
        btn_logo_history = (ImageView) findViewById(R.id.i_logo_history);

        btn_logo_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        username = (TextView) findViewById(R.id.tv_admin_history);
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
////////////////////////////////////
//        MQTT startMQTT = new MQTT(getApplicationContext());
//        Context context = startMQTT.getAppContex();
//        startMQTT.startMQTT(context);
///////////////////////////////////

        final ArrayList<Light> dataHistory = new ArrayList<>();
        final TableLayout table = (TableLayout) findViewById(R.id.table_main);

        timeData.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH
                        || event!=null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    if(event == null || !event.isShiftPressed()){
                        final String date_input = timeData.getText().toString().replace("-","").replace("/","");
                        dataHistory.clear();

                        Query queryHis = FirebaseDatabase.getInstance().getReference().child("History").orderByChild("Day").equalTo(date_input);
                        queryHis.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                    Light light_data = snapshot.getValue(Light.class);
                                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                                    Log.d("data", String.valueOf(data));
                                    //Log.d("Value",.getValue();
                                    dataHistory.add(light_data);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                return false;
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                table.removeAllViewsInLayout();
                Log.d("DataHisLenght", String.valueOf(dataHistory.get(0).getArea()));
                init(table,dataHistory);

            }


//            private void collectDataHis(Map<String, Object> value, ArrayList<String[]> dataHistory) {
//                for(Map.Entry<String,Object> entry: value.entrySet())
//                {
//                    Map singleTime = (Map) entry.getValue();
//                    String[] temp = {(String) singleTime.get("Area"),(String) singleTime.get("Value"),(String) singleTime.get("Time")};
//                    dataHistory.add(temp);
//                }
//                for(int i = 0; i < dataHistory.size();i++)
//                    Log.d("value is" , String.valueOf(dataHistory.get(i)[0]));
//            }
        });

    }


    private void init(TableLayout table, ArrayList<Light> dataHis) {
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
            tv_area_ind.setText(String.valueOf(dataHis.get(i).getArea()));
            tv_area_ind.setTextColor(Color.BLACK);
            tv_area_ind.setPadding(50, 0, 200, 0);
            tbrow.addView(tv_area_ind);
            TextView tv_value_ind = new TextView(this);
            tv_value_ind.setText(String.valueOf(dataHis.get(i).getValue()));
            tv_value_ind.setTextColor(Color.BLACK);
            tv_value_ind.setPadding(0, 0, 100, 0);
            tbrow.addView(tv_value_ind);
            TextView tv_time_ind = new TextView(this);
            tv_time_ind.setText(String.valueOf(dataHis.get(i).getTime()));
            tv_time_ind.setTextColor(Color.BLACK);
            tv_time_ind.setPadding(0, 0, 0, 0);
            tbrow.addView(tv_time_ind);
            table.addView(tbrow);
        }
    }


//    private void collectDataHistory(Map<String, Object> value, ArrayList<String[]> dataHistory) {
//          for(Map.Entry<String,Object> entry: value.entrySet())
//                {
//                    Map singleTime = (Map) entry.getValue();
//                    String[] temp = {(String) singleTime.get("Area"),(String) singleTime.get("Value"),(String) singleTime.get("Time")};
//                    dataHistory.add(temp);
//                }
//
////            for(int i = 0; i < dataHistory.size();i++)
////                Log.d("value is" , String.valueOf(dataHistory.get(i)[1]));
//    }
    //Query data history and insert into list
}