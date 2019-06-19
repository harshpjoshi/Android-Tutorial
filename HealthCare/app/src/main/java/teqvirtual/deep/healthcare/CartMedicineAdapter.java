package teqvirtual.deep.healthcare;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;
import Model.CartMedicineModel;
import okhttp3.internal.Util;

public class CartMedicineAdapter extends RecyclerView.Adapter<CartMedicineAdapter.MyViewHolder> {

    Context context;
    ArrayList<CartMedicineModel> arrayList;
    LayoutInflater layoutInflater;

    public CartMedicineAdapter(Bag bag, ArrayList<CartMedicineModel> arrayList) {
        this.context=bag;
        this.arrayList=arrayList;
        layoutInflater=layoutInflater.from(context);
    }

    @NonNull
    @Override
    public CartMedicineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.bag_layout_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartMedicineAdapter.MyViewHolder holder, int position) {
        Picasso.get().load(arrayList.get(position).getImage()).into(holder.imageView);
        holder.bag_item_name.setText(arrayList.get(position).getName());
        holder.qty_bag.setText(arrayList.get(position).getQuantity());
        holder.bag_prize.setText("Rs. "+arrayList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView,delete_bag_iem;
        TextView bag_item_name,qty_bag,bag_prize;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.bag_item_image);
            bag_item_name=(TextView)itemView.findViewById(R.id.bag_item_name);
            qty_bag=(TextView)itemView.findViewById(R.id.qty_bag);
            bag_prize=(TextView)itemView.findViewById(R.id.bag_prize);

            qty_bag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog=new Dialog(context);
                    dialog.setContentView(R.layout.qty_picker);

                    final NumberPicker numberPicker=(NumberPicker)dialog.findViewById(R.id.number_picker);
                    String[] data=new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
                    numberPicker.setMinValue(0);
                    numberPicker.setMaxValue(data.length-1);
                    numberPicker.setDisplayedValues(data);

                    Button button=(Button)dialog.findViewById(R.id.update_qty);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(context, ""+arrayList.get(getAdapterPosition()).getCart_id(), Toast.LENGTH_SHORT).show();

                            String[] key=new String[]{"action","id","quantity"};
                            String[] val=new String[]{"like_update",arrayList.get(getAdapterPosition()).getCart_id(), String.valueOf(numberPicker.getValue()+1)};

                            String jsonReq=Utils.createJsonRequest(key,val);

                            String url="http://teqvirtual.com/healthcare/index.php";

                            new WebserviceCall(context, url, jsonReq, "Loading...", false, new AsyncResponse() {
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

                    dialog.show();

                }
            });

            delete_bag_iem=(ImageView)itemView.findViewById(R.id.delete_bag_item);

            delete_bag_iem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final AlertDialog.Builder aBuilder=new AlertDialog.Builder(context);
                    aBuilder.setMessage("Are You Sure You Want To Delete");

                    aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String key[]=new String[]{"action","id"};
                            String val[]=new String[]{"like_delete",arrayList.get(getAdapterPosition()).getCart_id()};

                            String jsonReq= Utils.createJsonRequest(key,val);

                            String url="http://teqvirtual.com/healthcare/index.php";

                            new WebserviceCall(context, url, jsonReq, "Loading...", true, new AsyncResponse() {
                                @Override
                                public void onCallback(String response) {

                                    try {
                                        JSONObject jsonObject=new JSONObject(response);
                                        String message=jsonObject.getString("message");
                                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
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

                    aBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog=aBuilder.create();
                    alertDialog.show();

                }
            });

        }
    }
}
