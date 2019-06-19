package com.dhanvi.fashionfusion1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class myadpt extends BaseAdapter {
    Context con;
    int res;
    String[] findservices;
    LayoutInflater ly;

    public myadpt(nav_main navMain, int raw, String[] findservices) {
        this.con=navMain;
        this.res=raw;
        this.findservices=findservices;
    }

    @Override
    public int getCount() {
        return findservices.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=ly.from(con).inflate(res,null);
        TextView txt=(TextView) view.findViewById(R.id.text_raw);
        txt.setText(findservices[i]);
        return view;
    }
}
