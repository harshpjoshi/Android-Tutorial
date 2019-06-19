package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import teqvirtual.deep.healthcare.R;


public class DocFunctionAdapter extends RecyclerView.Adapter<DocFunctionAdapter.MyViewHolder>
{

    Context context;
    int[] fun_img;
    String[] fun_text;
    LayoutInflater layoutInflater;

    public DocFunctionAdapter(Context profileDoctor, int[] fun_img, String[] fun_txt) {
        this.context=profileDoctor;
        this.fun_img=fun_img;
        this.fun_text=fun_txt;
        this.layoutInflater=layoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.doc_fun_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.imageView.setImageResource(fun_img[position]);
        holder.textView.setText(fun_text[position]);

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

            imageView=(ImageView)itemView.findViewById(R.id.fun_image_doc);
            textView=(TextView)itemView.findViewById(R.id.fun_text_doc);
        }
    }
}
