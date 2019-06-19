package teqvirtual.deep.healthcare.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;
import teqvirtual.deep.healthcare.MedicalstoreDetail;
import teqvirtual.deep.healthcare.Model.MedicalModel;
import teqvirtual.deep.healthcare.Model.SujjestedMedicalModel;
import teqvirtual.deep.healthcare.R;
import teqvirtual.deep.healthcare.SuggestedStore;

public class YourSujjMedical extends RecyclerView.Adapter<YourSujjMedical.MyViewHolder>{

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<SujjestedMedicalModel> arrayList;
    String url="https://teqvirtual.com/healthcare/index.php";


    public YourSujjMedical(Context applicationContext, ArrayList<SujjestedMedicalModel> arrayList) {

        this.context=applicationContext;
        this.arrayList=arrayList;
        layoutInflater=layoutInflater.from(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=layoutInflater.inflate(R.layout.sujmedical_list_item,null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get().load(arrayList.get(position).getImage()).into(holder.imageView);
        holder.name_hos.setText(arrayList.get(position).getName());
        holder.city_hos.setText(arrayList.get(position).getCity());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView,removemed;

        TextView name_hos,city_hos;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=(ImageView) itemView.findViewById(R.id.img_medical_suj);
            name_hos=(TextView)itemView.findViewById(R.id.name_medical_suj);
            city_hos=(TextView)itemView.findViewById(R.id.city_medical_suj);
            removemed=(ImageView) itemView.findViewById(R.id.removemed);

            removemed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Delete");
                    alertDialog.setMessage("Are you Sure Want to Delete?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String id=arrayList.get(getAdapterPosition()).getId();

                            String key[]=new String[]{"action","medical_id"};
                            String val[]=new String[]{"smedical_id_delete",id};

                            String jsonreq=Utils.createJsonRequest(key,val);

                            new WebserviceCall(context, url, jsonreq, "Loading...", true, new AsyncResponse() {
                                @Override
                                public void onCallback(String response) {
                                    try {
                                        JSONObject jsonObject=new JSONObject(response);

                                        Toast.makeText(context, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                        ((Activity)context).finish();
                                        ((Activity) context).overridePendingTransition(0,0);
                                        context.startActivity(((Activity) context).getIntent());
                                        ((Activity) context).overridePendingTransition(0,0);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).execute();

                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = alertDialog.create();
                    dialog.show();

                }
            });

        }
    }
}
