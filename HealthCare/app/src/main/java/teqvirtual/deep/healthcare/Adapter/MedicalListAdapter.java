package teqvirtual.deep.healthcare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.MedicalstoreDetail;
import teqvirtual.deep.healthcare.Model.HospitalModel;
import teqvirtual.deep.healthcare.Model.MedicalModel;
import teqvirtual.deep.healthcare.R;

public class MedicalListAdapter extends RecyclerView.Adapter<MedicalListAdapter.MyViewHolder> implements Filterable {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<MedicalModel> arrayList;
    ArrayList<MedicalModel> arrayListfull;

    public MedicalListAdapter(Context applicationContext, ArrayList<MedicalModel> arrayList) {

        this.context=applicationContext;
        this.arrayList=arrayList;
        this.arrayListfull=new ArrayList<>(arrayList);
        layoutInflater=layoutInflater.from(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=layoutInflater.inflate(R.layout.medical_list_item,null);

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

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<MedicalModel> filteredlist=new ArrayList<>();

            if (constraint==null || constraint.length() == 0)
            {
                filteredlist.addAll(arrayListfull);
            }
            else
            {
                String filterpattern=constraint.toString().toLowerCase().trim();

                for (MedicalModel item:arrayListfull)
                {
                    if (item.getName().toLowerCase().contains(filterpattern))
                    {
                        filteredlist.add(item);
                    }
                    else if (item.getCity().toLowerCase().contains(filterpattern))
                    {
                        filteredlist.add(item);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredlist;


            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList.clear();
            arrayList.addAll((Collection<? extends MedicalModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name_hos,city_hos,type_hos;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=(ImageView) itemView.findViewById(R.id.img_medical);
            name_hos=(TextView)itemView.findViewById(R.id.name_medical);
            city_hos=(TextView)itemView.findViewById(R.id.city_medical);

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   Intent intent=new Intent(context, MedicalstoreDetail.class);
                   intent.putExtra("medicalid",arrayList.get(getAdapterPosition()).getMedicalid());
                   context.startActivity(intent);

               }
           });

        }
    }
}
