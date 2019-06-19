package teqvirtual.deep.healthcare.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.HospitalDetail;
import teqvirtual.deep.healthcare.Model.HospitalModel;
import teqvirtual.deep.healthcare.R;

public class NearByHospitalAdapter extends RecyclerView.Adapter<NearByHospitalAdapter.MyViewHolder> {

    Context context;
    ArrayList<HospitalModel> arrayList;
    LayoutInflater layoutInflater;


    public NearByHospitalAdapter(Context profile, ArrayList<HospitalModel> arrayList) {
        this.context=profile;
        layoutInflater=layoutInflater.from(context);
        this.arrayList=arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.nearhos_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Picasso.get().load(arrayList.get(position).getImage()).into(holder.imageView);
        holder.textView.setText(arrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.nearhos_image);
            textView=(TextView)itemView.findViewById(R.id.nearhos_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, HospitalDetail.class);
                    intent.putExtra("hosid",arrayList.get(getAdapterPosition()).getId());
                    context.startActivity(intent);

                }
            });
        }
    }
}
