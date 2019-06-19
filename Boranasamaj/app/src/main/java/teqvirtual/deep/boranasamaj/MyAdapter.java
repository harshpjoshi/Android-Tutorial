package teqvirtual.deep.boranasamaj;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class MyAdapter extends BaseAdapter {


    Context context;
    ArrayList<Member> arrayList;
    LayoutInflater layoutInflater;

    public MyAdapter(ViewMember viewMember, ArrayList<Member> arrayList) {
        this.context = viewMember;
        this.arrayList = arrayList;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.from(context).inflate(R.layout.list_item, null);

        TextView list_name = (TextView) convertView.findViewById(R.id.list_name);
        TextView list_desig = (TextView) convertView.findViewById(R.id.list_desig);
        TextView list_add = (TextView) convertView.findViewById(R.id.list_add);
        TextView list_mobile = (TextView) convertView.findViewById(R.id.list_mobile);
        TextView list_birth = (TextView) convertView.findViewById(R.id.list_birth);


        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_mem);
        Picasso.get().load(arrayList.get(position).getImage()).into(imageView);


        list_name.setText(arrayList.get(position).getName());
        list_desig.setText(arrayList.get(position).getDesignation());
        list_add.setText(arrayList.get(position).getAddress());
        list_mobile.setText(arrayList.get(position).getMobile());
        list_birth.setText(arrayList.get(position).getDob());

        list_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + arrayList.get(position).getMobile()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
