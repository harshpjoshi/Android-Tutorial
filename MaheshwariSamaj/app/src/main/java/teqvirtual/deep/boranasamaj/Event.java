package teqvirtual.deep.boranasamaj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.EventListAdapter;
import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;
import Model.EventModel;

public class Event extends AppCompatActivity {

    EventListAdapter adapter;

    String url="https://teqvirtual.com/BMS/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_event);
        toolbar.setTitleTextColor(getResources().getColor(R.color.font));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RecyclerView recyclerView=(RecyclerView)findViewById(R.id.event_list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        String[] keys=new String[]{"action"};
        String[] values=new String[]{"event_view"};

        String jsonRequest= Utils.createJsonRequest(keys,values);


        new WebserviceCall(Event.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    ArrayList<EventModel> eventModels=new ArrayList<>();

                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String id=jsonObject1.getString("id");
                        String event_name=jsonObject1.getString("event_name");
                        String event_location=jsonObject1.getString("event_location");
                        String event_time=jsonObject1.getString("event_time");
                        String event_date=jsonObject1.getString("event_date");
                        String event_image=jsonObject1.getString("event_image");

                        EventModel eventModel=new EventModel();
                        eventModel.setId(id);
                        eventModel.setName(event_name);
                        eventModel.setLocation(event_location);
                        eventModel.setDate(event_date);
                        eventModel.setTime(event_time);
                        eventModel.setImage(event_image);
                        eventModels.add(eventModel);
                    }
                    adapter=new EventListAdapter(Event.this,eventModels);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();
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


