
package com.dhanvi.fashionfusion1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhanvi.fashionfusion1.model.MyData;
import com.dhanvi.fashionfusion1.model.Product;

import java.util.List;

/**
 * Created by Dhanvi on 10/16/2018.
 */

public class ListAdapter extends BaseAdapter {
    private Context mContext;
    LayoutInflater ly;
    List<Product> list;

    // Constructor
    public ListAdapter(Context c, List<Product> list) {
        mContext = c;
        this.list=list;
    }

    public int getCount() {

        return list.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        final Product product = list.get(position);

        convertView = View.inflate(mContext,R.layout.singalraw, null);

        View layout=convertView.findViewById(R.id.layout);
        TextView txt=convertView.findViewById(R.id.grid_text1);
//
        TextView txt1=(TextView)convertView.findViewById(R.id.grid_text);
//
        ImageView img =(ImageView)convertView.findViewById(R.id.grid_image);

        txt.setText(product.getPrice());
        txt1.setText(product.getSize());
        img.setImageResource(product.getImage());

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyData.setSelectedProduct(product);
                Intent intent = new Intent(mContext,Sub_screen.class);
                mContext.startActivity(intent);
            }
        });


        return convertView;
    }


    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.login, R.drawable.joy,
            R.drawable.time, R.drawable.shopping,
            R.drawable.dhanvi, R.drawable.fav,
            R.drawable.splash, R.drawable.fashion,
            R.drawable.dp, R.drawable.dhanvipatel,
            R.drawable.mango, R.drawable.red,
            R.drawable.het, R.drawable.hetu,
            R.drawable.mm, R.drawable.mom,
            R.drawable.het, R.drawable.time,
            R.drawable.hh, R.drawable.ghatha
    };
}

