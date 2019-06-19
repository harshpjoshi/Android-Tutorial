package teqvirtual.deep.boranasamaj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.AdminMemberListAdapter;
import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;
import Model.MemberModel;

public class AdminViewMember extends AppCompatActivity {


    AdminMemberListAdapter adapter;

    String url="https://teqvirtual.com/BMS/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_member);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_member_admin);
        toolbar.setTitleTextColor(getResources().getColor(R.color.font));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RecyclerView recyclerView=(RecyclerView)findViewById(R.id.member_list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        String[] keys=new String[]{"action"};
        String[] values=new String[]{"member_order"};

        String jsonRequest= Utils.createJsonRequest(keys,values);


        new WebserviceCall(AdminViewMember.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    ArrayList<MemberModel> arrayList=new ArrayList<>();
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String member_id=jsonObject1.getString("id");
                        String member_name=jsonObject1.getString("member_name");
                        String member_dob=jsonObject1.getString("member_dob");
                        String member_city=jsonObject1.getString("member_city");
                        String member_address=jsonObject1.getString("member_address");
                        String member_mobile=jsonObject1.getString("member_mobile");
                        String member_occupation=jsonObject1.getString("member_occupation");
                        String member_image=jsonObject1.getString("member_image");

                        MemberModel memberModel=new MemberModel();
                        memberModel.setId(member_id);
                        memberModel.setName(member_name);
                        memberModel.setDob(member_dob);
                        memberModel.setCity(member_city);
                        memberModel.setAddress(member_address);
                        memberModel.setMobile(member_mobile);
                        memberModel.setOccupation(member_occupation);
                        memberModel.setImage(member_image);


                        arrayList.add(memberModel);
                    }
                     adapter=new AdminMemberListAdapter(AdminViewMember.this,arrayList);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu,menu);

        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView)searchItem.getActionView();
        EditText searchtext=(EditText)searchView.findViewById(R.id.search_src_text);
        searchtext.setTextColor(getResources().getColor(R.color.font));
        searchtext.setHintTextColor(getResources().getColor(R.color.font));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if (id==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
