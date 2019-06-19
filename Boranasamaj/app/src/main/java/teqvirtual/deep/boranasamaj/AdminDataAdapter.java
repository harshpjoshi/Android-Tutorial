package teqvirtual.deep.boranasamaj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class AdminDataAdapter extends BaseAdapter{
    Context context;
    String[] data;
    int img[];
    LayoutInflater layoutInflater;
    public AdminDataAdapter(AdminProfile adminProfile, String[] data, int[] img) {
        this.context=adminProfile;
        this.data=data;
        this.img=img;
    }

    @Override
    public int getCount() {
        return data.length;
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
        convertView=layoutInflater.from(context).inflate(R.layout.listadminlay,null);
        ImageView imageView=(ImageView)convertView.findViewById(R.id.list_admin_item_img);
        TextView textView=(TextView)convertView.findViewById(R.id.list_admin_item_text);
        imageView.setImageResource(img[position]);
        textView.setText(data[position]);
        return convertView;
    }
}
