package teqvirtual.deep.fireinsertselect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<User> arrayList;

    public CustomAdapter(Context context, ArrayList<User> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater=layoutInflater.from(context);
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
        convertView=layoutInflater.inflate(R.layout.item_list,null);

        ImageView imageView=(ImageView)convertView.findViewById(R.id.profile_image);
        TextView name=(TextView)convertView.findViewById(R.id.name);
        TextView address=(TextView)convertView.findViewById(R.id.address);

        name.setText(arrayList.get(position).getName());
        address.setText(arrayList.get(position).getAddress());
        Picasso.get().load(arrayList.get(position).getImage()).into(imageView);

        return convertView;
    }
}
