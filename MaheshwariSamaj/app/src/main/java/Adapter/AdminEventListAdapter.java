package Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;
import Model.EventModel;
import teqvirtual.deep.boranasamaj.AdminViewEvent;
import teqvirtual.deep.boranasamaj.EventUpdate;
import teqvirtual.deep.boranasamaj.R;
import teqvirtual.deep.boranasamaj.ViewAddMain;

public class AdminEventListAdapter extends RecyclerView.Adapter<AdminEventListAdapter.MyViewHolder> {

    Context context;
    ArrayList<EventModel> eventModels;
    LayoutInflater layoutInflater;

    public AdminEventListAdapter(AdminViewEvent event, ArrayList<EventModel> eventModels) {
        this.context=event;
        this.eventModels=eventModels;
        layoutInflater=layoutInflater.from(context);
    }


    @NonNull
    @Override
    public AdminEventListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.admin_event_item_list,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminEventListAdapter.MyViewHolder holder, int position) {
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

        ImageView imageView,event_update_img;
        TextView event_name,event_time,event_date,event_location;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.admin_event_image_item);
            event_name=(TextView)itemView.findViewById(R.id.admin_event_name_item);
            event_time=(TextView)itemView.findViewById(R.id.admin_event_time_item);
            event_date=(TextView)itemView.findViewById(R.id.admin_event_date_item);
            event_location=(TextView)itemView.findViewById(R.id.admin_event_location_item);
            event_update_img=(ImageView)itemView.findViewById(R.id.event_update_img);

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

            event_update_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent intent=new Intent(context,EventUpdate.class);
                   intent.putExtra("event_image",eventModels.get(getAdapterPosition()).getImage());
                    intent.putExtra("event_name",eventModels.get(getAdapterPosition()).getName());
                    intent.putExtra("event_time",eventModels.get(getAdapterPosition()).getTime());
                    intent.putExtra("event_date",eventModels.get(getAdapterPosition()).getDate());
                    intent.putExtra("event_location",eventModels.get(getAdapterPosition()).getLocation());
                    intent.putExtra("event_id",eventModels.get(getAdapterPosition()).getId());
                   context.startActivity(intent);

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Are You Sure You Want To Delete");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String keys[]=new String[]{"action","id"};
                            String values[]=new String[]{"event_delete",eventModels.get(getAdapterPosition()).getId()};

                            String url="https://teqvirtual.com/BMS/index.php";
                            String jsonRequest= Utils.createJsonRequest(keys,values);
                            new WebserviceCall(context, url, jsonRequest, "Deleting...", true, new AsyncResponse() {
                                @Override
                                public void onCallback(String response) {

                                    JSONObject jsonObject= null;
                                    try {
                                        jsonObject = new JSONObject(response);
                                        String message=jsonObject.getString("message");
                                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();

                                        context.startActivity(new Intent(context,AdminViewEvent.class));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).execute();

                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog=alertDialogBuilder.create();
                    alertDialog.show();

                    return true;
                }
            });

        }
    }
}
