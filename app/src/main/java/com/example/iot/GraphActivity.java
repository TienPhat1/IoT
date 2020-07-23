package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Orientation;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.scales.Linear;
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
        Button btn_comback = findViewById(R.id.btn_comback_aver);
        Linear scalesLinear = Linear.instantiate();
        scalesLinear.minimum(0d);
        scalesLinear.maximum(10d);
        scalesLinear.ticks("{ interval: 1 }");

        HashMap<String,String[]> dataAverage = (HashMap<String, String[]>) getIntent().getSerializableExtra("DataAverage");
        String[] dateTemp = getIntent().getStringArrayExtra("Date");
        String date = "";
        assert dateTemp != null;
        for(int i = dateTemp.length -1; i >= 0; i --){
                date += (i > 0 && !dateTemp[i].equals("")) ? dateTemp[i] + "/" : dateTemp[i];
        }


        List<DataEntry> data = new ArrayList<>();
        assert dataAverage != null;
        for(String key : dataAverage.keySet()){
            String check = key.substring(key.length()-4);
            String keyT = key.substring(0,key.length()-4);
//            if(check.equals("room"))
//                data.add(new ValueDataEntry(keyT,Float.parseFloat(Objects.requireNonNull(Objects.requireNonNull(dataAverage.get(key))[0]))));
//            else
//                data.add(new ValueDataEntry(key,Float.parseFloat(Objects.requireNonNull(Objects.requireNonNull(dataAverage.get(key))[0]))));
            if(check.equals("room"))
                data.add(new CustomDataEntry(keyT,Float.parseFloat(Objects.requireNonNull(Objects.requireNonNull(dataAverage.get(key))[0])),
                        Float.parseFloat(Objects.requireNonNull(Objects.requireNonNull(dataAverage.get(key))[1]))));
            else
                data.add(new CustomDataEntry(key,Float.parseFloat(Objects.requireNonNull(Objects.requireNonNull(dataAverage.get(key))[0])),
                        Float.parseFloat(Objects.requireNonNull(Objects.requireNonNull(dataAverage.get(key))[1]))));
        }
//        data.add(new ValueDataEntry("Rouge", 80540));
//        data.add(new ValueDataEntry("Foundation", 94190));
//        data.add(new ValueDataEntry("Mascara", 102610));
//        data.add(new ValueDataEntry("Lip gloss", 110430));
//        data.add(new ValueDataEntry("Lipstick", 128000));
        ////////////////////////

        Cartesian cartesian = AnyChart.column();
        com.anychart.core.axes.Linear extraYAxis =cartesian.yAxis(1d);
        extraYAxis.orientation(Orientation.RIGHT).scale(scalesLinear);
        extraYAxis.labels().padding(0d,0d,0d,5d).format("{%Value}");
        extraYAxis.title("Power");
        Set set = Set.instantiate();
        set.data(data);
        Mapping lineData = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping colData = set.mapAs("{ x: 'x', value: 'value' }");

        Column col = cartesian.column(colData);
        // col.fill("funtion()");
        col.tooltip()
                .titleFormat("Area: {%X},Value: {%Value}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .fontSize(2d)
                .offsetX(5d)
                .offsetY(7d);
        col.labels().enabled(true).format("{%value}");
        cartesian.animation(true);
        cartesian.title("Average value of "+date);
        cartesian.yScale().minimum(0d);
        cartesian.xScale();
        //cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.xAxis(0).title("Area");
        cartesian.yAxis(0).title("Value");
        Line line = cartesian.line(lineData);
        line.yScale(scalesLinear).color("#ff0000");
        line.tooltip().titleFormat("Power: {%value2}");

        anyChartView.setChart(cartesian);
        btn_comback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphActivity.this,AverageActivity.class);
                startActivity(intent);
            }
        });

    }
    private  class CustomDataEntry extends ValueDataEntry{

        public CustomDataEntry(String x, Number value,Number value2) {
            super(x, value);
            setValue("value2", value2);

        }
    }
}