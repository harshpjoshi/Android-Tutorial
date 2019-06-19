package com.dhanvi.fashionfusion1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Dhanvi on 10/16/2018.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    LayoutInflater ly;

    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {

        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=ly.from(mContext).inflate(R.layout.singalraw,null);
        //TextView txt=(TextView)findViewById(R.id.grid_text1);

        //TextView txt1=(TextView)findViewById(R.id.grid_text);

        //ImageView img =(ImageView)findViewById(R.id.grid_image);

        return convertView;
    }


    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.login, R.drawable.joy,
            R.drawable.time, R.drawable.shopping,
            R.drawable.dhanvi, R.drawable.fav,
            R.drawable.splash, R.drawable.fashion,
            R.drawable.dp,R.drawable.dhanvipatel,
            R.drawable.mango, R.drawable.red,
            R.drawable.het, R.drawable.hetu,
            R.drawable.mm, R.drawable.mom,
            R.drawable.het,R.drawable.time,
            R.drawable.hh, R.drawable.ghatha
    };
}

