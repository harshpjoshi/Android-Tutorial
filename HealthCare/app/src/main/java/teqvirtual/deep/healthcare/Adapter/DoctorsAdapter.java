package teqvirtual.deep.healthcare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.DoctorMessagingActivity;
import teqvirtual.deep.healthcare.DoctorSubProfile;
import teqvirtual.deep.healthcare.MessagingActivity;
import teqvirtual.deep.healthcare.Model.DoctorModel;
import teqvirtual.deep.healthcare.R;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.MyViewHolder> {

    ArrayList<DoctorModel> mUsers;
    Context context;
    LayoutInflater layoutInflater;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mReference;
    String type;

    public DoctorsAdapter(Context context, ArrayList<DoctorModel> mUsers) {

        this.context=context;
        this.mUsers=mUsers;
        layoutInflater=layoutInflater.from(context);
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        mReference=firebaseDatabase.getReference();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.doctors_list_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textView.setText(mUsers.get(position).getFirstname());
        Picasso.get().load(mUsers.get(position).getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.friend_doctor_img);
            textView=(TextView)itemView.findViewById(R.id.friend_doctor_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mReference.child("Friend List").child(mAuth.getCurrentUser().getUid()).child(mUsers.get(getAdapterPosition()).getF_doctor_id())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    type= (String) dataSnapshot.child("request_type").getValue();

                                    if (type!=null)
                                    {
                                        Intent intent=new Intent(context, DoctorMessagingActivity.class);
                                        intent.putExtra("userId",mUsers.get(getAdapterPosition()).getF_doctor_id());
                                        context.startActivity(intent);
                                    }
                                    else
                                    {
                                        Intent intent=new Intent(context, DoctorSubProfile.class);
                                        intent.putExtra("f_doctor_id",mUsers.get(getAdapterPosition()).getF_doctor_id());
                                        context.startActivity(intent);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                }
            });
        }
    }

}
