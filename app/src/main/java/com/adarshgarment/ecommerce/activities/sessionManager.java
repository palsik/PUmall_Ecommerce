package com.adarshgarment.ecommerce.activities;

import android.content.Context;
import android.content.SharedPreferences;

import com.adarshgarment.ecommerce.fragments.FragmentProfile;

import java.util.HashMap;

public class sessionManager {
       //Variables
    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final  String IS_LOGIN = "IslogedIn";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_MIDDLENAME = "middleName";
    public static final String KEY_EMAIL = "mail";
    public static final String KEY_PHONE = "phone";



    public sessionManager(Context _context){
        context= _context;
        userSession = _context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
    }


    public  void createLoginSession(String userName,String firstName, String middleName, String email, String phone){
        editor.putBoolean((IS_LOGIN), true);
        editor.putString((KEY_USERNAME), userName);
        editor.putString((KEY_FIRSTNAME), firstName);
        editor.putString((KEY_MIDDLENAME), middleName);
        editor.putString((KEY_EMAIL), email);
        editor.putString((KEY_PHONE), phone);

        editor.commit();

    }

    public HashMap<String, String > getUserDetailsFromSession(){
        HashMap<String,String> userdata = new HashMap< String, String>();

        userdata.put(KEY_USERNAME, userSession.getString(KEY_USERNAME, null));
        userdata.put(KEY_FIRSTNAME, userSession.getString(KEY_FIRSTNAME, null));
        userdata.put(KEY_MIDDLENAME, userSession.getString(KEY_MIDDLENAME, null));
        userdata.put(KEY_EMAIL, userSession.getString(KEY_EMAIL, null));
        userdata.put(KEY_PHONE, userSession.getString(KEY_PHONE, null));

        return  userdata;
    }

    public  boolean checkLogin(){
        if(userSession.getBoolean(IS_LOGIN, true)) {
            return true;
        }else
            return false;
    }

    public void logoutUserFromSession(){
        editor.clear();
        editor.clear();
    }



}
