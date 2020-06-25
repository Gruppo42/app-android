package com.gruppo42.app.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String REFRESH = "Refresh";

    public static final String USER_TOKEN = "user";

    public SessionManager(Context context) {
        this.context = context;
        userSession = context.getSharedPreferences("userSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(String userToken) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USER_TOKEN, userToken);
        editor.commit();
    }

    public void needRefresh(boolean needRefresh)
    {
        userSession = context.getSharedPreferences("userSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
        editor.putBoolean(REFRESH, needRefresh);
        editor.commit();
    }

    public String getUserAuthorization() {
        return "Bearer " + userSession.getString(USER_TOKEN, null);
    }

    public boolean isLoggedIn() {
        if (userSession.getBoolean(IS_LOGIN, false))
            return true;
        else
            return false;
    }

    public boolean needToRefresh()
    {
        if(userSession.getBoolean(REFRESH, false))
            return true;
        else
            return false;
    }

    public void logoutFromSession() {
        editor.clear();
        editor.commit();
    }
}

