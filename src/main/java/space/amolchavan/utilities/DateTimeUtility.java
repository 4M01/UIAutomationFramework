package space.amolchavan.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtility {

    /**
     * This method accept EPOC date in string format and returns date in mentioned format
    * @param: String
    * @return :
    * */
    public static String epochToDateConverter(String epochTime){
        Long lDate= Long.valueOf(epochTime);
        Date date = new Date(lDate* 1000L);
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(date);
        System.out.println(formatted);
        return formatted;
    }


    public static int secondsConverter(String time) {
        int totaltime = 0,hr , min, sec, hrs , mins;
        String[] abc = time.split(":", 0);
        int arrSize=(abc.length)-1;
        System.out.println("size ="+arrSize);

        if(arrSize==2) {
            hrs = Integer.valueOf(abc[0]);
            hr=hrs*3600;
            System.out.println(hr);

            mins = Integer.valueOf(abc[1]);
            min=mins*60;
            System.out.println(min);

            sec=Integer.valueOf(abc[2]);
            totaltime= hr+min+sec;
            System.out.println("total time = "+totaltime);

        }else if (arrSize==1) {

            mins = Integer.valueOf(abc[0]);
            min=mins*60;
            System.out.println(min);

            sec=Integer.valueOf(abc[1]);
            totaltime= min+sec;
            System.out.println("total time = "+totaltime);

        }else if(arrSize==0) {
            sec=Integer.valueOf(abc[0]);
            totaltime= sec;
            System.out.println("total time = "+totaltime);
        }
        return totaltime;
    }



    public static int covertTimeInSeconds(String time) {
        int totaltime = 0,hr , min, sec, hrs , mins;
        String[] abc = time.split(":", 0);
        int arrSize=(abc.length)-1;
        System.out.println("size ="+arrSize);

        if(arrSize==2) {
            hrs = Integer.valueOf(abc[0]);
            hr=hrs*3600;
            System.out.println(hr);

            mins = Integer.valueOf(abc[1]);
            min=mins*60;
            System.out.println(min);

            sec=Integer.valueOf(abc[2]);
            totaltime= hr+min+sec;
            System.out.println("total time = "+totaltime);

        }else if (arrSize==1) {

            mins = Integer.valueOf(abc[0]);
            min=mins*60;
            System.out.println(min);

            sec=Integer.valueOf(abc[1]);
            totaltime= min+sec;
            System.out.println("total time = "+totaltime);

        }else if(arrSize==0) {
            sec=Integer.valueOf(abc[0]);
            totaltime= sec;
            System.out.println("total time = "+totaltime);
        }
        return totaltime;
    }


    public static String secondsToMinConverter(int timeInSecs){
        int hours = timeInSecs / 3600;
        int minutes = (timeInSecs % 3600) / 60;
        int seconds = timeInSecs % 60;
        String timeString;
        if(timeInSecs<3600){
            timeString = String.format("%02d:%02d", minutes, seconds);
        }else{
            timeString = String.format("%02d:%02d:%02d", minutes, seconds);
        }
        System.out.println(timeString);
        return timeString;
    }


    public static String getTimeStamp(){
        java.util.Date date = new java.util.Date();
        return date.toString();
    }

    public static String getDateTime(){
        long date = new Date().getTime();
        String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(date);
        return formattedDate;
    }

    public static String getDate(){
        long date = new Date().getTime();
        String formattedDate = new SimpleDateFormat("dd MMM yyyy").format(date);
        return formattedDate;
    }

    public static Date convertStringToDate(String stringDate, String pattern){
        DateFormat formatter = new SimpleDateFormat(pattern);
        Date expectedDate = null;
        try {
            expectedDate = (Date)formatter.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return expectedDate;
    }

    public static String convertDateToPattern(Date inputDate, String pattern){
        DateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(inputDate);
    }
}
