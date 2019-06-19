package Adapter;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefrenceManager {

    Context context;
    SharedPreferences sharedPreferences;

    public PrefrenceManager(Context context) {
        this.context = context;
        getSharedPrefrence();
    }

    void getSharedPrefrence()
    {
        sharedPreferences=context.getSharedPreferences("hello",Context.MODE_PRIVATE);
    }

    public void writePreference()
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("hello","INIT_OK");
        editor.commit();
    }

    public  boolean chechPrefrence()
    {
        boolean status=false;

        if (sharedPreferences.getString("hello","null").equals("null"))
        {
            status=false;
        }
        else
        {
            status=true;
        }
        return status;
    }

    void clearPrefrence()
    {
        sharedPreferences.edit().clear().commit();
    }
}
