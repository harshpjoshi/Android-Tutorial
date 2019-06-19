package teqvirtual.deep.healthcare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.DoctorSubProfile;
import teqvirtual.deep.healthcare.Model.DoctorModel;
import teqvirtual.deep.healthcare.R;

public class NearByDoctorAdapter extends RecyclerView.Adapter<NearByDoctorAdapter.MyViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<DoctorModel> doctorModelArrayList;

    public NearByDoctorAdapter(Context applicationContext, ArrayList<DoctorModel> doctorModelArrayList) {

        this.context=applicationContext;
        this.doctorModelArrayList=doctorModelArrayList;
        layoutInflater=layoutInflater.from(context);

    }

    @NonNull
    @Override
    public NearByDoctorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=layoutInflater.inflate(R.layout.near_doctor_item,null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearByDoctorAdapter.MyViewHolder holder, int position) {

        Picasso.get().load(doctorModelArrayList.get(position).getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return doctorModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.doc_image);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent=new Intent(context, DoctorSubProfile.class);
                    intent.putExtra("id",doctorModelArrayList.get(getAdapterPosition()).getId());
                    intent.putExtra("f_doctor_id",doctorModelArrayList.get(getAdapterPosition()).getF_doctor_id());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });
        }
    }
}
