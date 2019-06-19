package teqvirtual.deep.healthcare.Adapter;

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
import teqvirtual.deep.healthcare.Model.MainCategoryModel;

import teqvirtual.deep.healthcare.R;
import teqvirtual.deep.healthcare.Submedicine;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<MainCategoryModel> arrayList;
    LayoutInflater layoutInflater;
    String userid;

    public CategoryAdapter(Context profile, ArrayList<MainCategoryModel> arrayList) {
        this.context=profile;
        layoutInflater=layoutInflater.from(context);
        this.arrayList=arrayList;
        this.userid=userid;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.category_item,null);
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

            imageView=(ImageView)itemView.findViewById(R.id.category_image);
            textView=(TextView)itemView.findViewById(R.id.category_item_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, Submedicine.class);
                    intent.putExtra("name",arrayList.get(getAdapterPosition()).getName());
                    intent.putExtra("id",arrayList.get(getAdapterPosition()).getId());
                    intent.putExtra("userid",userid);
                    intent.putExtra("image",arrayList.get(getAdapterPosition()).getImage());
                    context.startActivity(intent);

                }
            });

        }
    }
}
