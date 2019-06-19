package teqvirtual.deep.healthcare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import teqvirtual.deep.healthcare.Model.CityModel;
import teqvirtual.deep.healthcare.R;


public class CityAdapter extends BaseAdapter {

    Context context;
    ArrayList<CityModel> arrayList;
    LayoutInflater layoutInflater;

    public CityAdapter(Context subscriberRegistration, ArrayList<CityModel> arrayList) {

        this.context=subscriberRegistration;
        this.arrayList=arrayList;
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
        convertView=layoutInflater.inflate(R.layout.city_item,null);

        TextView city_text=(TextView)convertView.findViewById(R.id.city_text);
        city_text.setText(arrayList.get(position).getName());

        return convertView;
    }
}
