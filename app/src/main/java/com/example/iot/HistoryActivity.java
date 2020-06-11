package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();
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

    }
}