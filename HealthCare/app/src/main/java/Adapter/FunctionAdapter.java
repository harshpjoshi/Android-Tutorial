package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import teqvirtual.deep.healthcare.Profile;
import teqvirtual.deep.healthcare.R;

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.MyViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    int fun_img[];
    String[] fun_txt;

    public FunctionAdapter(Context profile, int[] fun_img, String[] fun_txt) {
        this.context=profile;
        this.fun_img=fun_img;
        this.fun_txt=fun_txt;
        layoutInflater=layoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.function_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageView.setImageResource(fun_img[position]);
        holder.textView.setText(fun_txt[position]);
    }

    @Override
    public int getItemCount() {
        return fun_img.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.fun_image);
            textView=(TextView)itemView.findViewById(R.id.fun_text);
        }
    }
}
