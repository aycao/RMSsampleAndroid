package me.alfredcao.android.foodorderguest;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyssn on 2015-12-07.
 */
public class DataFetcher {

    private static final String TAG = "DataFetcher.java ";

    public List<FoodItem> fetchMenuDirect(String urlStr){
        List<FoodItem> foodItems = new ArrayList<>();
        try {
            parseItems(foodItems, getUrlString(urlStr));
        }catch(IOException ioe){
            ioe.printStackTrace();
        }catch (JSONException je){
            je.printStackTrace();
        }

        return foodItems;
    }

    public List<FoodItem> fetchMenuUsingPOST (String urlStr){
        List<FoodItem> foodItems = new ArrayList<>();
        try {
            parseItems(foodItems,makePostRequest(urlStr));
        }catch(IOException ioe){
            ioe.printStackTrace();
        }catch (JSONException je){
            je.printStackTrace();
        }
        return foodItems;
    }



    private byte[] getUrlBytes(String urlStr) throws IOException{
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = conn.getInputStream();

            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(conn.getResponseMessage() + ": w/ " + urlStr);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0){
                out.write(buffer,0,bytesRead);
            }
            out.close();
            return out.toByteArray();

        }finally{
            conn.disconnect();
        }
    }

    private String getUrlString(String urlStr) throws IOException{
        return new String(getUrlBytes(urlStr));
    }

    private String makePostRequest(String urlStr){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(urlStr);

        //make POST data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("request", "fetch-menu"));

        //Endoding POST data
        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        //making POST req
        try{
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            Log.i(TAG, responseBody);
            return responseBody;
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        httpClient.getConnectionManager().shutdown();
        return "nothing";
    }

    private void parseItems(List<FoodItem> returningItems, String jsonStr) throws
            IOException, JSONException{
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody = new JSONObject(jsonStr);
        }catch(JSONException je){
            Log.e(TAG,"Fail to parse json Str" );
            je.printStackTrace();
        }
        JSONArray foodJsonArray = jsonBody.getJSONArray("fooditems");

        for (int i = 0; i < foodJsonArray.length(); i++){
            JSONObject foodJsonObject = foodJsonArray.getJSONObject(i);
            FoodItem food = new FoodItem();
            food.setDishName(foodJsonObject.getString("dish"));
            food.setDishPrice("$ " + foodJsonObject.getString("price"));
            String dishType = foodJsonObject.getString("dishtype");
            food.setDishType(dishType);
            String imageUrl = foodJsonObject.getString("imageurl");
            food.setImageUrl(imageUrl);

            returningItems.add(food);
        }
    }
}
