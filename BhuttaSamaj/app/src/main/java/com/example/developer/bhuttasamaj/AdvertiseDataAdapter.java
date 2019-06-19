package com.example.developer.bhuttasamaj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


class AdvertiseDataAdapter extends BaseAdapter{

    Context context;
    ArrayList<AdvertiseData> advertiseData;
    LayoutInflater layoutInflater;
    public AdvertiseDataAdapter(Advertise advertise, ArrayList<AdvertiseData> advertiseData) {
        this.context=advertise;
        this.advertiseData=advertiseData;
    }

    @Override
    public int getCount() {
        return advertiseData.size();
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
        convertView=layoutInflater.from(context).inflate(R.layout.advertiselay,null);
        ImageView imageView=(ImageView)convertView.findViewById(R.id.advert_img);
        TextView textView=(TextView)convertView.findViewById(R.id.advert_mobile);
        TextView desc=(TextView)convertView.findViewById(R.id.advert_desc);

        Picasso.get().load(advertiseData.get(position).getImage()).into(imageView);
        textView.setText(advertiseData.get(position).getMobile());
        desc.setText(advertiseData.get(position).getDescription());
        return convertView;
    }
}
