package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import Model.DoctorTypeModel;
import teqvirtual.deep.healthcare.R;

public class DoctorTypeAdapter extends BaseAdapter {

    Context context;
    ArrayList<DoctorTypeModel> arrayList;
    LayoutInflater layoutInflater;

    public DoctorTypeAdapter(Context applicationContext, ArrayList<DoctorTypeModel> arrayList) {
        this.context=applicationContext;
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
        View view=layoutInflater.inflate(R.layout.doctype_item,null);

        TextView doctor_type=(TextView)view.findViewById(R.id.doctortype_text);

        doctor_type.setText(arrayList.get(position).getName());
        return view;
    }
}
