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
import teqvirtual.deep.healthcare.DoctorSubProfile;
import teqvirtual.deep.healthcare.Model.DoctorModel;
import teqvirtual.deep.healthcare.Model.MedicalModel;
import teqvirtual.deep.healthcare.R;


public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.MyViewHolder> implements Filterable {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<DoctorModel> arrayList;
    ArrayList<DoctorModel> arrayListfull;


    public DoctorListAdapter(Context applicationContext, ArrayList<DoctorModel> arrayList) {

        this.arrayList=arrayList;
        this.context=applicationContext;
        this.arrayListfull=new ArrayList<>(arrayList);
        layoutInflater=layoutInflater.from(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=layoutInflater.inflate(R.layout.doctor_list_view,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get().load(arrayList.get(position).getImage()).into(holder.imageView);
        holder.name.setText(arrayList.get(position).getFirstname());
        holder.mobile.setText(arrayList.get(position).getMobileno());
        holder.city.setText(arrayList.get(position).getCity());
        holder.type.setText(arrayList.get(position).getType());

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
            ArrayList<DoctorModel> filteredlist=new ArrayList<>();

            if (constraint==null || constraint.length() == 0)
            {
                filteredlist.addAll(arrayListfull);
            }
            else
            {
                String filterpattern=constraint.toString().toLowerCase().trim();

                for (DoctorModel item:arrayListfull)
                {
                    if (item.getFirstname().toLowerCase().contains(filterpattern))
                    {
                        filteredlist.add(item);
                    }
                    else if (item.getCity().toLowerCase().contains(filterpattern))
                    {
                        filteredlist.add(item);
                    }
                    else if (item.getType().toLowerCase().contains(filterpattern))
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
            arrayList.addAll((Collection<? extends DoctorModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,mobile,type,city;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.doc_profile_item);
            name=(TextView)itemView.findViewById(R.id.doc_name_item);
            mobile=(TextView)itemView.findViewById(R.id.doc_mobile_item);
            type=(TextView)itemView.findViewById(R.id.doc_type_item);
            city=(TextView)itemView.findViewById(R.id.doc_city_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, DoctorSubProfile.class);
                    intent.putExtra("id",arrayList.get(getAdapterPosition()).getId());
                    intent.putExtra("f_doctor_id",arrayList.get(getAdapterPosition()).getF_doctor_id());
                    context.startActivity(intent);
                }
            });
        }
    }
}
