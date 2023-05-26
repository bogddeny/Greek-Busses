package gr.greekbusses.misc;

import java.util.ArrayList;

public class Line
{
    Object category;
    Object line;
    Object description;
    Object time;
    Object timeSat;
    Object timeSun;

    public Line(Object category, Object line, Object description, Object time, Object timeSat, Object timeSun)
    {
        this.category = category;
        this.line = line;
        this.description = description;
        this.time = time;
        this.timeSat = timeSat;
        this.timeSun = timeSun;
    }
    public Object getCategory()
    {
        return category;
    }
    public Object getLine()
    {
        return line;
    }
    public Object getDescription()
    {
        return description;
    }
    public Object getTime()
    {
        return time;
    }
    public Object getTimeSat()
    {
        return timeSat;
    }
    public Object getTimeSun()
    {
        return timeSun;
    }
}
