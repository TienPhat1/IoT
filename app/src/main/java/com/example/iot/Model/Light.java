package com.example.iot.Model;

public class Light {
    private String Month, Area, Year,Value, Time, Day;

    public  Light(){

    }
    public Light(String month, String area, String time, String year, String value, String day) {
        this.Area = area;
        this.Year = year;
        this.Time = time;
        this.Value = value;
        this.Day = day;
        this.Month = month;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public void setArea(String area) {
        this.Area = area;
    }

    public void setValue(String value) {
        this.Value = value;
    }

    public void setDay(String day) {
        this.Day = day;
    }

    public void setMonth(String month) {
        this.Month = month;
    }

    public void setYear(String year) {
        this.Year = year;
    }

    public String getValue() {
        return Value;
    }

    public String getArea() {
        return Area;
    }

    public String getTime() {
        return Time;
    }

    public String getMonth() {
        return Month;
    }

    public String getDay() {
        return Day;
    }

    public String getYear() {
        return Year;
    }
}
