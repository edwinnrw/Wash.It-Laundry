package com.project.edn.washit_laundry;

/**
 * Created by EDN on 12/14/2016.
 */

public class Config {
    public static final String BASE_URL = "http://washittest.azurewebsites.net";
    public static final String API_CONFIRMORDER = BASE_URL+"/API/confirm_order.php";
    public static final String API_LISTORDER = BASE_URL+"/API/get_order_list.php";
    public static final String API_LOGIN = BASE_URL+"/API/login_admin.php";
    public static final String API_LOGOUT = BASE_URL+"/API/logout.php";
    public static final String API_OWNERCHANGE = BASE_URL+"/API/edit_profile.php";
    public static final String API_REPORT = BASE_URL+"/API/report_order.php";
    public static final String API_STORECHANGE = BASE_URL+"/API/edit_store.php";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    public static final String TOKEN_SHARED_PREF = "token";
}
