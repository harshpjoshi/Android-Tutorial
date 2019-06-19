package teqvirtual.deep.healthcare.Firebase;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SendNotification {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public void sendNotification(final String regToken, final String body, final String title) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json=new JSONObject();
                    JSONObject dataJson=new JSONObject();
                    dataJson.put("body",body);
                    dataJson.put("title",title);
                    dataJson.put("sound","default");
                    json.put("notification",dataJson);
                    json.put("to",regToken);
                    json.put("priority","high");
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization","key=AIzaSyC4qz4GNL5OI4LyOZwkeXAf8ZTs40SKlqM")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();

                    Log.d("signup","demooo"+finalResponse);

                }catch (Exception e){
                    //Log.d(TAG,e+"");
                }
                return null;
            }
        }.execute();

    }

}
