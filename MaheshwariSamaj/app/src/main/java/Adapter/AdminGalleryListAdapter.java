package Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;
import Model.GalleryModel;
import teqvirtual.deep.boranasamaj.AdminViewGallery;
import teqvirtual.deep.boranasamaj.R;
import teqvirtual.deep.boranasamaj.ViewAddMain;

public class AdminGalleryListAdapter extends RecyclerView.Adapter<AdminGalleryListAdapter.MyViewHolder> {

    Context context;
    ArrayList<GalleryModel> arrayList;
    LayoutInflater layoutInflater;

    public AdminGalleryListAdapter(AdminViewGallery adminViewGallery, ArrayList<GalleryModel> arrayList) {

        this.context=adminViewGallery;
        this.arrayList=arrayList;
        layoutInflater=layoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.admin_gallery_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(arrayList.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView,closeview;


        public MyViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.gallery_image_item_admin);
            closeview=(ImageView)itemView.findViewById(R.id.close);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Dialog dialog=new Dialog(context);
//                            dialog.setContentView(R.layout.galery_imagge);
//                            ImageView profile_pic=(ImageView)dialog.findViewById(R.id.gallery_pic_dialog);
//                            Picasso.get().load(arrayList.get(getAdapterPosition()).getImage()).into(profile_pic);
//                            dialog.show();

                            Intent intent=new Intent(context, ViewAddMain.class);
                            intent.putExtra("advertise_image",arrayList.get(getAdapterPosition()).getImage());
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
            });

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    closeview.setVisibility(View.VISIBLE);

                    closeview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder alertDailogbuilder=new AlertDialog.Builder(context);
                            alertDailogbuilder.setMessage("Are You Sure You Want To Delete");

                            alertDailogbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String keys[]=new String[]{"action","id"};
                                    String values[]=new String[]{"gallery_delete",arrayList.get(getAdapterPosition()).getId()};

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

                                                context.startActivity(new Intent(context,AdminViewGallery.class));

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }).execute();


                                }
                            });

                            alertDailogbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog=alertDailogbuilder.create();
                            alertDialog.show();
                        }
                    });

                    return true;
                }
            });
        }
    }

}
