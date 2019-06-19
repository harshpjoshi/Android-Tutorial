package com.example.developer.audioplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CustomAdapter extends BaseAdapter{
    Context context;
    String items[];
    LayoutInflater layoutInflater;
    public CustomAdapter(Context applicationContext, String[] items) {
        this.context=applicationContext;
        this.items=items;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=layoutInflater.from(context).inflate(R.layout.listitem,null);
        TextView textView=(TextView)convertView.findViewById(R.id.text);
        textView.setText(items[position]);
        return convertView;
    }
}
