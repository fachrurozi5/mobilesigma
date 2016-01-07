package com.fachru.sigmamobile.utils;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.Model;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.Warehouse;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by fachru on 15/10/15.
 */
public class CommonUtil {

    /*
    * number format
    * */
    public static String priceFormat2Decimal(double v) {
        return new DecimalFormat("Rp #,##0.00").format(v);
    }

    public static String priceFormat(double v) {
        return new DecimalFormat("#,##0").format(v);
    }

    public static String percentFormat(double v) {
        return new DecimalFormat("##0").format(v);
    }

    /*
    * string to date format
    * */
    public static String stringToDateHelper(String s, int i) {          // dd-MM-yyy, SHORT/MEDIUM/LONG_UK
        try {
            switch (i) {
                case Constanta.SHORT_UK:
                    return dateToStringShort(stringToDateMedium(s));
                case Constanta.MEDIUM_UK:
                    return dateToStringMedium(stringToDateMedium(s));
                case Constanta.LONG_UK:
                    return dateToStringLong(stringToDateMedium(s));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static Date stringToDateLong(String s) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US);
        sf.setLenient(true);
        return sf.parse(s);
    }

    public static Date stringToDateMedium(String s) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        sf.setLenient(true);
        return sf.parse(s);
    }

    public static Date stringToDateShort(String s) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yy", Locale.US);
        sf.setLenient(true);
        return sf.parse(s);
    }

    /*
    * date to string format
    * */
    public static String dateHelper(Date date, int i) {             // Date, SHORT_UK/MEDIUM_UK/LONG_UK
        switch (i) {
            case Constanta.SHORT_UK:
                return dateToStringShort(date);
            case Constanta.MEDIUM_UK:
                return dateToStringMedium(date);
            case Constanta.LONG_UK:
                return dateToStringLong(date);
            case Constanta.TIME :
                return dateToStringTime(date);
            case Constanta.ID:
                return dateToStringID(date);
            case Constanta.ID_LONG:
                return dateToStringIDLong(date);
            default:
                return "";
        }
    }

    public static String dateHelper(int i) {                // SHORT_UK/MEDIUM_UK/LONG_UK/TIME/ID
        return dateHelper(new Date(), i);
    }

    public static String dateToStringShort(Date date) {
        return new SimpleDateFormat("dd MM yyyy", Locale.US).format(date);
    }

    public static String dateToStringMedium(Date date) {
        return new SimpleDateFormat("dd MMMM yyyy", Locale.US).format(date);
    }

    public static String dateToStringLong(Date date) {

        return new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US).format(date);

    }

    public static String dateToStringTime(Date date) {
        return new SimpleDateFormat("HH:mm:ss", Locale.US).format(date);
    }

    public static String dateToStringTime(Calendar date) {
        return new SimpleDateFormat("HH:mm:ss", Locale.US).format(date.getTime());
    }

    public static String dateToStringID(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String day_of_week = getDay(calendar.get(Calendar.DAY_OF_WEEK));
        String month = getMonth(calendar.get(Calendar.MONTH));
        int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        return day_of_week + day_of_month + month + year;
    }

    public static String dateToStringIDLong(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String day_of_week = getDay(calendar.get(Calendar.DAY_OF_WEEK));
        String month = getMonth(calendar.get(Calendar.MONTH));
        int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        return day_of_week + day_of_month + month + year + " " + dateToStringTime(calendar);
    }


    /*
    * set color
    * */
    public static void setTextViewColor(TextView textView, String color) { // instance textview, #ffffff
        textView.setBackgroundColor(Color.parseColor(color));
    }

    public static boolean isLocationOn(Context context){
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private static String getDay(int day_of_week) {

        switch (day_of_week) {
            case Calendar.SUNDAY :
                return  "Minggu, ";
            case Calendar.MONDAY :
                return  "Senin, ";
            case Calendar.TUESDAY :
                return  "Selasa, ";
            case  Calendar.WEDNESDAY :
                return "Rabu, ";
            case  Calendar.THURSDAY :
                return "Kamis, ";
            case Calendar.FRIDAY :
                return "Jum'at, ";
            case Calendar.SATURDAY :
                return "Sabtu, ";
            default:
                return "Minggu, ";
        }
    }

    private static String getMonth(int month) {
        switch (month) {
            case Calendar.JANUARY :
                return " Januari ";
            case Calendar.FEBRUARY :
                return " February ";
            case Calendar.MARCH :
                return " Maret ";
            case Calendar.APRIL :
                return " April ";
            case Calendar.MAY :
                return " Mei ";
            case Calendar.JUNE :
                return " Juni ";
            case Calendar.JULY:
                return " Juli ";
            case Calendar.AUGUST:
                return " Agustus ";
            case Calendar.SEPTEMBER:
                return " Septermber ";
            case Calendar.OCTOBER:
                return " Oktober ";
            case Calendar.NOVEMBER:
                return " November ";
            case Calendar.DECEMBER :
                return " Desember ";
            default:
                return " Januari ";
        }
    }

    public static void showToast(Context context, String message) {
        Toast mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /** Create a file Uri for saving an image or video */
    public static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type, ""));
    }

    public static File getOutputMediaFile(String title) {
        return getOutputMediaFile(Constanta.MEDIA_TYPE_DOCUMENT_TITLE, title);
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type, String title) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        String dirname = "";

        switch (type) {
            case Constanta.MEDIA_TYPE_IMAGE :
                dirname = "Pictures";
                break;
            case Constanta.MEDIA_TYPE_DOCUMENT :
            case Constanta.MEDIA_TYPE_DOCUMENT_TITLE :
                dirname = "Nota";
                break;
            default:
                break;
        }

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "SIGMA" + File.separator + dirname);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(Constanta.TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == Constanta.MEDIA_TYPE_IMAGE) {
            Log.d(Constanta.TAG, mediaStorageDir.getPath());
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == Constanta.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else if (type == Constanta.MEDIA_TYPE_DOCUMENT) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "NOTA_" + timeStamp + ".pdf");
        } else if (type == Constanta.MEDIA_TYPE_DOCUMENT_TITLE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "NOTA_" + title + ".pdf");
        } else {
            return null;
        }

        return mediaFile;
    }


    /*
    * GPS Location Adress
    * */

    public static String getAddress(Address address) {
        return address.getThoroughfare() + ", " + address.getLocality() + ", " + address.getAdminArea() + " " + address.getPostalCode();
    }

    public static String getAddress(Context context, Location location) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

        if (addresses != null && addresses.size() != 0)
            return getAddress(addresses.get(0));

        return "";
    }

    public static String getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Log.d(Constanta.TAG, "address " + addresses.toString());
        } catch (IOException e) {
            Log.e(Constanta.TAG, "Invalid latitude : " + latitude + " longitude : " + longitude, e);
        }

        if (addresses != null && addresses.size() != 0)
            return getAddress(addresses.get(0));

        return "";
    }

    public static int getPx(Context context, int dimensionDp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    }

    public static <T extends Model> Class<T> getInstance(Object o) {
        if (o instanceof Customer)
            return (Class<T>) o;
        else if (o instanceof Warehouse)
            return (Class<T>) o;
        else
            return null;
    }

}
