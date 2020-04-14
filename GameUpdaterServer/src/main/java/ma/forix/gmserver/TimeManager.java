package ma.forix.gmserver;

import java.util.Date;
import java.util.GregorianCalendar;

public class TimeManager {

    private static GregorianCalendar calendar;
    private static Date time;

    public TimeManager(){
        calendar = new GregorianCalendar();
        time = calendar.getTime();
    }

    public static String getTime(){
        calendar = new GregorianCalendar();
        time = calendar.getTime();
        return time.getHours()+":"+time.getMinutes()+":"+time.getSeconds();
    }

    public static int getHour(){
        calendar = new GregorianCalendar();
        time = calendar.getTime();
        return time.getHours();
    }

    public static int getMinute(){
        calendar = new GregorianCalendar();
        time = calendar.getTime();
        return time.getMinutes();
    }
}
