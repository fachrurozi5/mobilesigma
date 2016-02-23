package com.fachru.sigmamobile.utils;

/**
 * Created by fachru on 23/10/15.
 */
public final class Constanta {

    public static final String TAG = "com.fachru.sigma";
    //    public static final String BASE_URL = "http://192.168.0.104/sigmamobile/";
    public static final String BASE_URL = "http://192.168.1.10/sigmamobile/";

    /*
    * for date time
    * */
    public static final byte SHORT_UK = 0x0000;
    public static final byte MEDIUM_UK = 0x0001;
    public static final byte LONG_UK = 0x0002;
    public static final byte TIME = 0x0003;
    public static final byte ID = 0x0004;
    public static final byte ID_LONG = 0x0005;

    /*
    * for result activity
    * */
    public static final int REQUEST_CODE = 200;
    public static final int RESULT_FAIL = 100;
    public static final int RESULT_OK = 101;

    /*
    * for directory
    * */
    public static final byte MEDIA_TYPE_IMAGE = 0x0000;
    public static final byte MEDIA_TYPE_VIDEO = 0x0001;
    public static final byte MEDIA_TYPE_DOCUMENT = 0x0002;
    public static final byte MEDIA_TYPE_DOCUMENT_TITLE = 0x0003;

    /*
    * for service and receiver
    * */
    public static final String SERVICE_RECEIVER = "service_receiver";
    public static final String RESULT_DATE = "result_date";
    public static final String RESULT_TIME = "result_time";
    public static final String RESULT_ADDRESS = "result_address";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    /*
    * for RestApi
    * */
    public static final String TAG_STATUS = "Status";
    public static final String TAG_DATA = "Data";
    public static final String TAG_MESSAGE = "Message";

    public static final String KEY_DOC_NO = "doc_no";
    public static final String KEY_SO = "so";
    public static final String SIMPLE_LIST_ITEM_1 = "item_1";
    public static final String SIMPLE_LIST_ITEM_2 = "item_2";
    public static final String SIMPLE_LIST_ITEM_PRICE = "item_sell";
    public static final String SIMPLE_LIST_ITEM_STOCK = "item_stock";
    public static final String KEY_WHID = "whid";
}
