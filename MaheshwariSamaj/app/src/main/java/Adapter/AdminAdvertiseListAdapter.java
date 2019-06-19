package Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import Model.AdvertiseModel;
import teqvirtual.deep.boranasamaj.AdminViewAdvertise;
import teqvirtual.deep.boranasamaj.R;
import teqvirtual.deep.boranasamaj.ViewAddMain;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.INTERNET;

public class AdminAdvertiseListAdapter extends RecyclerView.Adapter<AdminAdvertiseListAdapter.MyViewHolder> {

    Context context;
    ArrayList<AdvertiseModel> arrayList;
    LayoutInflater layoutInflater;

    public AdminAdvertiseListAdapter(AdminViewAdvertise adminViewAdvertise, ArrayList<AdvertiseModel> advertiseModels) {

        this.context=adminViewAdvertise;
        this.arrayList=advertiseModels;
        layoutInflater=layoutInflater.from(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.admin_advertise_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(arrayList.get(position).getImage()).into(holder.imageView);
        holder.advertise_mobile.setText(arrayList.get(position).getMobile());
        holder.advertise_description.setText(arrayList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView advertise_mobile,advertise_description;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.advertise_image_item_admin);
            advertise_mobile=(TextView)itemView.findViewById(R.id.advertise_item_contactno_admin);
            advertise_description=(TextView)itemView.findViewById(R.id.advertise_item_description_admin);

            advertise_mobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(checkPermission())
                    {

                    }
                    else
                    {
                        requestPermission();
                    }

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + arrayList.get(getAdapterPosition()).getMobile()));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    context.startActivity(intent);

                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(context);
                    alertdialogbuilder.setMessage("Are You Sure You Want To Delete");
                    alertdialogbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String keys[]=new String[]{"action","id"};
                            String values[]=new String[]{"advertise_delete",arrayList.get(getAdapterPosition()).getId()};

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

                                        context.startActivity(new Intent(context,AdminViewAdvertise.class));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).execute();


                        }
                    });

                    alertdialogbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog=alertdialogbuilder.create();
                    alertDialog.show();


                    return true;
                }
            });
        }
    }


    private void requestPermission() {

        ActivityCompat.requestPermissions((Activity) context, new String[]
                {
                        INTERNET,
                        CALL_PHONE
                }, 200);

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 200:

                if (grantResults.length > 0) {

                    boolean internetpermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean callphone = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                }

                break;
        }
    }



    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(context, INTERNET);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(context, CALL_PHONE);


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;

    }
}
