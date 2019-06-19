package teqvirtual.deep.boranasamaj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class NewsDataAdapter extends BaseAdapter{
    Context context;
    ArrayList<NewsData> arrayList;
    LayoutInflater layoutInflater;
    public NewsDataAdapter(News news, ArrayList<NewsData> arrayList) {
        this.context=news;
        this.arrayList=arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=layoutInflater.from(context).inflate(R.layout.newslistlay,null);
        ImageView imageView=(ImageView)convertView.findViewById(R.id.news_img);
        TextView textView=(TextView)convertView.findViewById(R.id.news_desc);
        Picasso.get().load(arrayList.get(position).getImage()).into(imageView);
        
        textView.setText(arrayList.get(position).getDescription());
        return convertView;
    }
}
