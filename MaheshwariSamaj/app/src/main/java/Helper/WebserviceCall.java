package Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class WebserviceCall extends AsyncTask<Void,Void,String> {
    // interface for response

    AsyncResponse delegate;
    private final MediaType URLENCODE = MediaType.parse("application/json;charset=utf-8");
    ProgressDialog dialog;
    Context context;
    String dialogMessage;
    boolean showDialog = true;
    String URL;
    String jsonBody;

    public WebserviceCall(Context context, String URL, String jsonRequestBody, String dialogMessage, boolean showDialog, AsyncResponse delegate) {
        this.context = context;
        this.URL = URL;
        this.jsonBody = jsonRequestBody;
        this.dialogMessage = dialogMessage;
        this.showDialog = showDialog;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showDialog) {
            dialog = new ProgressDialog(context);
            dialog.setMessage(dialogMessage);
            dialog.show();
        }
    }

    /**
     * Web service call okhttp
     *
     * @param params url, request body in string respectively
     * @return String response
     */
    @Override
    protected String doInBackground(Void... params) {

        // creating okhttp client
        OkHttpClient client = new OkHttpClient();
        // client.setConnectTimeout(10L, TimeUnit.SECONDS);
        // creating request body
        RequestBody body;
        if (jsonBody != null) {
            body = RequestBody.create(URLENCODE, jsonBody);
        } else {
            body = null;
        }
        ;
        // creating request
        Request request = new Request.Builder()
                .post(body)
                .url(URL)
                .build();

        // creating webserivce call and get response

        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d("myapp", res);
            return res;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (showDialog) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        if (s != null) {


            delegate.onCallback(s);
        } else {
            Log.d("myapp", getClass().getSimpleName() + ": response null");
        }
    }
}