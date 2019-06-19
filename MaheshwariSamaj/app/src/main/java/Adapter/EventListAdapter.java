package Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.EventModel;
import teqvirtual.deep.boranasamaj.Event;
import teqvirtual.deep.boranasamaj.R;
import teqvirtual.deep.boranasamaj.ViewAddMain;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {

    Context context;
    ArrayList<EventModel> eventModels;
    LayoutInflater layoutInflater;

    public EventListAdapter(Event event, ArrayList<EventModel> eventModels) {
        this.context=event;
        this.eventModels=eventModels;
        layoutInflater=layoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.event_item_list,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get().load(eventModels.get(position).getImage()).into(holder.imageView);
        holder.event_name.setText(eventModels.get(position).getName());
        holder.event_date.setText(eventModels.get(position).getDate());
        holder.event_time.setText("Event Start Time: "+eventModels.get(position).getTime());
        holder.event_location.setText(eventModels.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return eventModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView event_name,event_time,event_date,event_location;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.event_image_item);
            event_name=(TextView)itemView.findViewById(R.id.event_name_item);
            event_time=(TextView)itemView.findViewById(R.id.event_time_item);
            event_date=(TextView)itemView.findViewById(R.id.event_date_item);
            event_location=(TextView)itemView.findViewById(R.id.event_location_item);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, ViewAddMain.class);
                    intent.putExtra("advertise_image",eventModels.get(getAdapterPosition()).getImage());
                    ActivityOptions activityOptions= null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) context,imageView,"profile");
                        context.startActivity(intent,activityOptions.toBundle());
                    }else
                    {
                        Toast.makeText(context, "Version Not Supported", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
