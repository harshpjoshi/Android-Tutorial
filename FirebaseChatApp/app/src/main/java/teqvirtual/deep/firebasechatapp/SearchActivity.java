package teqvirtual.deep.firebasechatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.firebasechatapp.Holder.SearchHolder;
import teqvirtual.deep.firebasechatapp.Model.SearchUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.libizo.CustomEditText;

public class SearchActivity extends AppCompatActivity {

    String TAG="searchact";
    RecyclerView recyclerView;

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    ImageView mImageView;
    CustomEditText meditText;
    String mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mDatabase=FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference();

        mImageView=(ImageView)findViewById(R.id.searchbtn);
        meditText=(CustomEditText)findViewById(R.id.user_search);

        recyclerView=(RecyclerView)findViewById(R.id.searchlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchFriend();

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearch=meditText.getText().toString();

                searchFriend();
            }
        });


    }

    private void searchFriend() {


        Query searchQuery=mReference.child("Users")
                .orderByChild("name")
                .startAt(mSearch)
                .endAt(mSearch+"\uf8ff");

        FirebaseRecyclerAdapter<SearchUser, SearchHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<SearchUser, SearchHolder>(
                SearchUser.class,
                R.layout.show_search_friend,
                SearchHolder.class,
                searchQuery
        ) {
            @Override
            protected void populateViewHolder(SearchHolder searchHolder, SearchUser searchUser, int i) {

                final String userKey=getRef(i).getKey();



                searchHolder.set_Name(searchUser.getName());
                searchHolder.set_Status(searchUser.getStatus());
                searchHolder.set_Profile(SearchActivity.this,searchUser.getImage());

                searchHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d(TAG,"Friend id"+userKey);

                        Intent intent=new Intent(SearchActivity.this,FriendProfileActivity.class);
                        intent.putExtra("key",userKey);
                        startActivity(intent);

                    }
                });

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}
