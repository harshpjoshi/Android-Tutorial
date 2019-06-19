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
import Model.NewsModel;
import teqvirtual.deep.boranasamaj.AdminViewNews;
import teqvirtual.deep.boranasamaj.R;
import teqvirtual.deep.boranasamaj.ViewAddMain;

public class AdminNewsListAdapter extends RecyclerView.Adapter<AdminNewsListAdapter.MyViewHolder> {

    Context context;
    ArrayList<NewsModel> newsModels;
    LayoutInflater layoutInflater;

    public AdminNewsListAdapter(AdminViewNews adminViewNews, ArrayList<NewsModel> newsModels) {
        this.context=adminViewNews;
        this.newsModels=newsModels;
        layoutInflater=layoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view=layoutInflater.inflate(R.layout.news_list_item_admin,null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(newsModels.get(position).getImage()).into(holder.imageView);
        holder.textView.setText(newsModels.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return newsModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.news_image_admin);
            textView=(TextView)itemView.findViewById(R.id.news_description_admin);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, ViewAddMain.class);
                    intent.putExtra("advertise_image",newsModels.get(getAdapterPosition()).getImage());
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Are You Sure You Want To Delete");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String keys[]=new String[]{"action","id"};
                            String values[]=new String[]{"news_delete",newsModels.get(getAdapterPosition()).getId()};

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

                                        context.startActivity(new Intent(context,AdminViewNews.class));

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
