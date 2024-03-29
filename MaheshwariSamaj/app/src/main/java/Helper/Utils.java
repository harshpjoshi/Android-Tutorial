package Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class Utils {
    /***
     * Create json request
     * @param keys
     * @param values
     * @return String jsonObject
     */
    public static String createJsonRequest(String keys[], String values[]){
        JSONObject jsonObject = new JSONObject();
        try {
            for (int i = 0; i < keys.length; i++) {
                jsonObject.put(keys[i],values[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }


    public static String getFile(int path) {
        Bitmap bm = null;
        ByteArrayOutputStream baos = null;
        byte[] b = null;
        String encodeString = null;
        try {
            bm = BitmapFactory.decodeFile(String.valueOf(path));
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            b = baos.toByteArray();
            encodeString = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;
    }

}