package Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

import Model.MemberModel;
import teqvirtual.deep.boranasamaj.Member;
import teqvirtual.deep.boranasamaj.R;
import teqvirtual.deep.boranasamaj.ViewAddMain;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.INTERNET;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<MemberModel> memberModels;
    ArrayList<MemberModel> memberModelsFull;
    LayoutInflater layoutInflater;

    public MemberListAdapter(Member member, ArrayList<MemberModel> arrayList) {
        this.context = member;
        this.memberModels = arrayList;
        this.memberModelsFull = new ArrayList<>(memberModels);
        layoutInflater = layoutInflater.from(context);
    }

    @NonNull
    @Override
    public MemberListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.member_list_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberListAdapter.MyViewHolder holder, int position) {
        holder.member_name.setText(memberModels.get(position).getName());
        holder.member_occupation.setText(memberModels.get(position).getOccupation());
        holder.member_address.setText(memberModels.get(position).getAddress());
        holder.member_city.setText(memberModels.get(position).getCity());
        holder.member_mobile.setText(memberModels.get(position).getMobile());
        holder.member_dob.setText(memberModels.get(position).getDob());
        Picasso.get().load(memberModels.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return memberModels.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<MemberModel> filteredlist = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredlist.addAll(memberModelsFull);
            } else {
                String filterpattern = constraint.toString().toLowerCase().trim();

                for (MemberModel item : memberModelsFull) {
                    if (item.getName().toLowerCase().contains(filterpattern)) {
                        filteredlist.add(item);
                    } else if (item.getCity().toLowerCase().contains(filterpattern)) {
                        filteredlist.add(item);
                    } else if (item.getDob().toLowerCase().contains(filterpattern)) {
                        filteredlist.add(item);
                    } else if (item.getOccupation().toLowerCase().contains(filterpattern)) {
                        filteredlist.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredlist;


            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            memberModels.clear();
            memberModels.addAll((Collection<? extends MemberModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView member_name, member_occupation, member_address, member_city, member_mobile, member_dob;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.member_item_image);
            member_name = (TextView) itemView.findViewById(R.id.member_item_name);
            member_occupation = (TextView) itemView.findViewById(R.id.member_item_occupation);
            member_address = (TextView) itemView.findViewById(R.id.member_item_address);
            member_city = (TextView) itemView.findViewById(R.id.member_item_city);
            member_mobile = (TextView) itemView.findViewById(R.id.member_item_contactno);
            member_dob = (TextView) itemView.findViewById(R.id.member_item_dob);

            member_mobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(checkPermission())
                    {

                    }
                    else
                    {
                        requestPermission();
                    }

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + memberModels.get(getAdapterPosition()).getMobile()));
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

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Dialog dialog=new Dialog(context);
//                    dialog.setContentView(R.layout.member_profile_imagge);
//                    ImageView profile_pic=(ImageView)dialog.findViewById(R.id.member_pro_pic_dialog);
//                    Picasso.get().load(memberModels.get(getAdapterPosition()).getImage()).into(profile_pic);
//                    dialog.show();

                    Intent intent=new Intent(context, ViewAddMain.class);
                    intent.putExtra("advertise_image",memberModels.get(getAdapterPosition()).getImage());
                    ActivityOptions activityOptions= null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) context,imageView,"profile");
                        context.startActivity(intent,activityOptions.toBundle());
                    }else
                    {
                        Toast.makeText(context, "Version Not Supported", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions((Activity) context, new String[]
                {
                        INTERNET,
                        CALL_PHONE
                }, 200);

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 200:

                if (grantResults.length > 0) {

                    boolean internetpermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean callphone = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                }

                break;
        }
    }



    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(context, INTERNET);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(context, CALL_PHONE);


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;

    }
}
