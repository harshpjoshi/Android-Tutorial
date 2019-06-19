package teqvirtual.deep.healthcare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.Model.TipsModel;
import teqvirtual.deep.healthcare.R;
import teqvirtual.deep.healthcare.SuggestedTips;

public class SujjTips extends RecyclerView.Adapter<SujjTips.MyViewHolder> {

    Context context;
    ArrayList<TipsModel> arrayList;
    LayoutInflater layoutInflater;

    public SujjTips(SuggestedTips suggestedTips, ArrayList<TipsModel> arrayList) {

        this.context=suggestedTips;
        this.arrayList=arrayList;
        layoutInflater=layoutInflater.from(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.sujjtips_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get().load(arrayList.get(position).getImage()).into(holder.imageView);
        holder.textView.setText(arrayList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.tip_img);
            textView=(TextView)itemView.findViewById(R.id.tip_text);
        }
    }
}
