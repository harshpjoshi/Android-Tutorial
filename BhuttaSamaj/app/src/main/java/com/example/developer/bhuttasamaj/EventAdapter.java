package com.example.developer.bhuttasamaj;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class EventAdapter  extends BaseAdapter{
    Context context;
    ArrayList<Eventdate> arrayList;
    LayoutInflater layoutInflater;

    public EventAdapter(Event event, ArrayList<Eventdate> arrayList) {
        this.context=event;
        this.arrayList=arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
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
    public View getView(int position, View itemView, ViewGroup parent) {

        itemView=layoutInflater.from(context).inflate(R.layout.eventlist_lay,null);
        TextView event_nam,event_time,event_date,event_location;
        ImageView list__time,location,event_img;

        event_nam=itemView.findViewById(R.id.list_event_name);
        event_time=itemView.findViewById(R.id.event_time);
        event_date=itemView.findViewById(R.id.event_date);
        event_location=itemView.findViewById(R.id.event_location);

        event_img=itemView.findViewById(R.id.event_img);
        list__time=itemView.findViewById(R.id.img_time);
        location=itemView.findViewById(R.id.loc_event);

        event_nam.setText(arrayList.get(position).getName());
        event_time.setText(arrayList.get(position).getTime());
        event_date.setText(arrayList.get(position).getDate());
        event_location.setText(arrayList.get(position).getLocation());
        Picasso.get().load(arrayList.get(position).getImage()).into(event_img);

        return itemView;
    }

}
