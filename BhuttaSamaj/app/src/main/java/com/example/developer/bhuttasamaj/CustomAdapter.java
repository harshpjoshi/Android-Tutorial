package com.example.developer.bhuttasamaj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class CustomAdapter extends BaseAdapter{
    Context context;
    String data[];
    int img[];
    LayoutInflater layoutInflater;
    public CustomAdapter(Profile profile, String[] data, int[] img) {
        this.context=profile;
        this.data=data;
        this.img=img;
    }

    @Override
    public int getCount() {
        return data.length;
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
        convertView=layoutInflater.from(context).inflate(R.layout.listlay,null);
        ImageView imageView=(ImageView)convertView.findViewById(R.id.list_item_img);
        TextView textView=(TextView)convertView.findViewById(R.id.list_item_text);

        imageView.setImageResource(img[position]);
        textView.setText(data[position]);
        return convertView;
    }
}
