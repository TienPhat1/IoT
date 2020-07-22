package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.iot.Model.Light;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);

        HashMap<String,String> dataAverage = (HashMap<String, String>) getIntent().getSerializableExtra("DataAverage");
        Log.d("Data", String.valueOf(dataAverage));

        List<DataEntry> data = new ArrayList<>();
        assert dataAverage != null;
        for(String key : dataAverage.keySet()){
            data.add(new ValueDataEntry(key,Float.parseFloat(Objects.requireNonNull(dataAverage.get(key)))));
        }
        Cartesian cartesian = AnyChart.column();
        Column col = cartesian.column(data);
        // col.fill("funtion()");
        col.tooltip()
                .titleFormat("{%X")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d);
        cartesian.animation(true);
        cartesian.title("Average Value of .....");
        cartesian.yScale().minimum(0d);
        //cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.xAxis(0).title("Area");
        cartesian.yAxis(0).title("Value");

        anyChartView.setChart(cartesian);
    }
}