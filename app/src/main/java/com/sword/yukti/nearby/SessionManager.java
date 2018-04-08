package com.sword.yukti.nearby;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager
{
    public static String TAG = SessionManager.class.getSimpleName();
    Context mContext;
    SharedPreferences pref;

    SharedPreferences.Editor editor;

    public String USERNAME = "username";
    public String PASSWORD = "password";
    private String LOGGED_IN = "login_shared_pref";
    private String SAVE_DETAILS = "savelogin";
    private String USER_ID = "user_id";
    //private String PARENT_ID = "parent_id";
    private String SELECTION = "selection";
    private String CHANGE_PASSWORD = "change_password";
    private static SessionManager sessionManager;

    public static SessionManager getInstance(){
        if(sessionManager== null)
           sessionManager= new SessionManager();
        return sessionManager;
    }

    private SessionManager() {

    }    // Shared preferences file name
    private static final String PREF_NAME = "detail_shared_prep";

    public SessionManager(Context context)
    {
        mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLoginDetails_Rem_meChecked(String username, String password){
        editor = pref.edit();
        editor.putBoolean(SAVE_DETAILS, true);
        editor.putBoolean(LOGGED_IN, true);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.putBoolean(CHANGE_PASSWORD, false);
        editor.commit();
    }

    public void saveLoginDetails_WO_Rem_me(String username, String password){
        editor = pref.edit();
        editor.putBoolean(SAVE_DETAILS, false);
        editor.putBoolean(LOGGED_IN, true);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.putBoolean(CHANGE_PASSWORD, false);
        editor.commit();
    }

    /*public void setUserId_Parent(String user_id, String selection){
        editor = pref.edit();
        editor.putString(USER_ID, user_id);
        editor.putString(SELECTION, selection);
        editor.putString(PARENT_ID, user_id);
        editor.commit();
    }*/
    public void setUser_Id(String user_id, String selection){
        editor = pref.edit();
        editor.putString(USER_ID, user_id);
        editor.putString(SELECTION, selection);
        editor.commit();
    }

    public void logoutUser(){
        editor = pref.edit();
        editor.putBoolean(LOGGED_IN, false);
        editor.commit();
    }

    public String getUser_id(){
        return pref.getString(USER_ID,"");
    }

    public String getUserSelection(){
        return pref.getString(SELECTION,"");
    }

    public boolean getUserLogginIn()
    {
        return pref.getBoolean(LOGGED_IN, false);
    }

    public boolean getLoginDetails()
    {
        return pref.getBoolean(SAVE_DETAILS, false);
    }

    public SessionDetails_GetSet getUserDetails(){
        SessionDetails_GetSet getSet = new SessionDetails_GetSet();

        getSet.setUsername(pref.getString(USERNAME, ""));
        getSet.setPassword(pref.getString(PASSWORD, ""));

        return getSet;
    }

    public void changePassword(){
        editor = pref.edit();
        editor.putBoolean(CHANGE_PASSWORD, true);
        editor.commit();
    }

    public boolean fetch_ChangePassword(){
        return pref.getBoolean(CHANGE_PASSWORD, false);
    }

    public void close_REquestPassword() {

        editor = pref.edit();
        editor.putBoolean(CHANGE_PASSWORD, false);
        editor.commit();
    }

    public void clost_ChnagePassword(String mobile_number, String password){
        editor = pref.edit();
        editor.putBoolean(CHANGE_PASSWORD, false);
        editor.putString(USERNAME, mobile_number);
        editor.putString(PASSWORD, password);
        editor.commit();
    }

}
