package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {
    private ImageView btn_search;
    private EditText timeData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        btn_search = (ImageView) findViewById(R.id.i_btn_search);
        timeData = (EditText) findViewById(R.id.et_history);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void init() {
        TableLayout table = (TableLayout) findViewById(R.id.table_main);
        TableRow tb_row = new TableRow(this);
        TextView tv_area = new TextView(this);
        tv_area.setText("AREA");
        tv_area.setTextColor(Color.BLACK);
        tv_area.setPadding(50,0,380,0);
        tb_row.addView(tv_area);
        TextView tv_value = new TextView(this);
        tv_value.setText("VALUE");
        tv_value.setTextColor(Color.BLACK);
        tv_value.setPadding(0,0,220,0);
        tb_row.addView(tv_value);
        TextView tv_time = new TextView(this);
        tv_time.setText("TIME");
        tv_time.setTextColor(Color.BLACK);
        tv_time.setPadding(0,0,0,0);
        tb_row.addView(tv_time);
        table.addView(tb_row);
        for (int i = 0 ; i < 10 ; i++)
        {
            TableRow tbrow = new TableRow(this);
            TextView tv_area_ind = new TextView(this);
            tv_area_ind.setText("AREA");
            tv_area_ind.setTextColor(Color.BLACK);
            tv_area_ind.setPadding(50,0,380,0);
            tbrow.addView(tv_area_ind);
            TextView tv_value_ind = new TextView(this);
            tv_value_ind.setText("VALUE");
            tv_value_ind.setTextColor(Color.BLACK);
            tv_value_ind.setPadding(0,0,220,0);
            tbrow.addView(tv_value_ind);
            TextView tv_time_ind = new TextView(this);
            tv_time_ind.setText("TIME");
            tv_time_ind.setTextColor(Color.BLACK);
            tv_time_ind.setPadding(0,0,0,0);
            tbrow.addView(tv_time_ind);
            table.addView(tbrow);
        }

    }
}