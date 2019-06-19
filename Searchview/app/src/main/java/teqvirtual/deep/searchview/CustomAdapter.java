package teqvirtual.deep.searchview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>  implements Filterable{

    Context context;
    ArrayList<Data> examplelist;
    ArrayList<Data> examplelistfull;
    LayoutInflater layoutInflater;

    public CustomAdapter(MainActivity mainActivity, ArrayList<Data> string) {
        this.context=mainActivity;
        this.examplelist=string;
        this.examplelistfull=new ArrayList<>(examplelist);
        layoutInflater=layoutInflater.from(context);
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.itemlist,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        holder.textView.setText(examplelist.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return examplelist.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Data> filteredlist=new ArrayList<>();

            if (constraint==null || constraint.length() == 0)
            {
                filteredlist.addAll(examplelistfull);
            }
            else
            {
                String filterpattern=constraint.toString().toLowerCase().trim();

                for (Data item:examplelistfull)
                {
                    if (item.getName().toLowerCase().contains(filterpattern))
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
            examplelist.clear();
            examplelist.addAll((Collection<? extends Data>) results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.name);
        }
    }

}
