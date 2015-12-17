package me.alfredcao.android.foodorderguest;

import android.content.Intent;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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
import java.util.UUID;

/**
 * Created by cyssn on 2015-12-07.
 */
public class DataFetcher {

    private static final String TAG = "DataFetcher.java ";
    private static final int FETCH_MENU = 0;
    private static final int FETCH_ORDERS = 1;
    private static final int FETCH_CHIEFS = 2;
    private static final int SUBMIT_ORDER = 3;

    public List<FoodItem> fetchMenuDirect(String urlStr){
        List<FoodItem> foodItems = new ArrayList<>();
        try {
            foodItems = parseItems(FETCH_MENU, getUrlString(urlStr));
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
            String result = makePostRequest(urlStr,FETCH_MENU);
            Log.d (TAG,result);
            foodItems = parseItems(FETCH_MENU, result);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }catch (JSONException je){
            je.printStackTrace();
        }
        return foodItems;
    }

    public List<FoodOrder> fetchOrdersUsingPOST (String urlStr){
        List<FoodOrder> foodOrders = new ArrayList<>();
        try {
            foodOrders = parseItems(FETCH_ORDERS, makePostRequest(urlStr, FETCH_ORDERS));
        }catch(IOException ioe){
            ioe.printStackTrace();
        }catch (JSONException je){
            je.printStackTrace();
        }
        return foodOrders;
    }

    public String submitOrderUsingPOST (String urlStr,FoodOrder foodOrder){
        String resultString = null;
        try{
            String postResult = makePostRequest(urlStr,SUBMIT_ORDER,foodOrder);
            resultString = parseItemsToString(SUBMIT_ORDER,postResult);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }catch(JSONException je){
            je.printStackTrace();
        }

        return resultString;
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

    private String makePostRequest(String urlStr, int requestTypeCode){
        return makePostRequest(urlStr,requestTypeCode,null);
    }

    private String makePostRequest(String urlStr, int requestTypeCode,FoodOrder foodOrder){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(urlStr);

        //make POST data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        JSONObject orderJsonObj = new JSONObject();
        JSONObject orderHeaderObj = new JSONObject();
        JSONArray orderDishQuantPairsArray = new JSONArray();

        boolean isSubmitOrder = false;

        switch (requestTypeCode) {
            case FETCH_MENU: {
                nameValuePairs.add(new BasicNameValuePair("request", "fetch-menu"));
                break;
            }case FETCH_CHIEFS: {
                nameValuePairs.add(new BasicNameValuePair("request", "fetch-chief"));
                break;
            }case FETCH_ORDERS: {
                nameValuePairs.add(new BasicNameValuePair("request", "fetch-orders"));
                break;
            }case SUBMIT_ORDER: {
                if (foodOrder == null){
                    return "nothing";
                }
                /*
                nameValuePairs.add(new BasicNameValuePair("request", "submit-order"));
                nameValuePairs.add(new BasicNameValuePair("table-number",
                        String.valueOf(foodOrder.getTableNumber())));
                nameValuePairs.add(new BasicNameValuePair("comment",foodOrder.getComment()));
                nameValuePairs.add(new BasicNameValuePair("orderid",
                        foodOrder.getFoodOrderLocalId().toString()));*/
                isSubmitOrder = true;
                try {
                    orderHeaderObj.put("request", "submit-order");
                    orderHeaderObj.put("table-number", foodOrder.getTableNumber());
                    orderHeaderObj.put("comment", foodOrder.getComment() + "");
                    orderHeaderObj.put("orderid",foodOrder.getFoodOrderLocalId().toString());
                    orderHeaderObj.put("processed", String.valueOf(foodOrder.isProcessed()));
                    orderHeaderObj.put("cleared", String.valueOf(foodOrder.isCleared()));
                    orderHeaderObj.put("chief", foodOrder.getChief());
                    orderJsonObj.put("header", orderHeaderObj);
                    for(DishQuantPair dqp: foodOrder.getDishQuantPairs()){
                        JSONObject orderDishQuantPairObj = new JSONObject();
                        orderDishQuantPairObj.put("dish-name",dqp.getDishName());
                        orderDishQuantPairObj.put("quantity",dqp.getQuantity());
                        orderDishQuantPairsArray.put(orderDishQuantPairObj);
                    }
                    orderJsonObj.put("dish-quant-pairs", orderDishQuantPairsArray);
                }catch(JSONException je){
                    Log.e(TAG,"error packing JSON Object for submitting order");
                    je.printStackTrace();
                }
                break;
            }
        }

        //Endoding POST data
        Log.d(TAG, "TRYING TO ENCODING");
        try{
            if(isSubmitOrder){
                StringEntity se = new StringEntity(orderJsonObj.toString());
                Log.d(TAG,orderJsonObj.toString());
                httpPost.setEntity(se);
            }else {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        //making POST req
        Log.d(TAG, "TRYING TO MAKE POST REQ");
        try{
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            Log.d(TAG, "RECEIVING");
            Log.d(TAG, "RECEIVING: " + responseBody);
            return responseBody;
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        httpClient.getConnectionManager().shutdown();
        return "nothing";
    }

    private String parseItemsToString(int requestTypeCode,String jsonStr) throws
            IOException,JSONException{
        String returningString = null;
        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody = new JSONObject(jsonStr);
        }catch (JSONException je){
            Log.e(TAG,"Fail to parse json Str");
            je.printStackTrace();
        }
        switch (requestTypeCode){
            case SUBMIT_ORDER:{
                JSONObject resultObject = jsonBody.getJSONObject("result");
                boolean successed = Boolean.parseBoolean(resultObject.getString("success")) ;
                returningString = resultObject.getString("result_string");
            }
        }

        return returningString;
    }

    private ArrayList parseItems(int requestTypeCode, String jsonStr) throws
            IOException, JSONException{

        ArrayList returningItems = new ArrayList();
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody = new JSONObject(jsonStr);
        }catch(JSONException je){
            Log.e(TAG,"Fail to parse json Str" );
            je.printStackTrace();
        }

        switch(requestTypeCode) {
            case FETCH_MENU: {
                JSONArray foodJsonArray = jsonBody.getJSONArray("fooditems");

                for (int i = 0; i < foodJsonArray.length(); i++) {
                    JSONObject foodJsonObject = foodJsonArray.getJSONObject(i);
                    FoodItem food = FoodItem.parseFromJSONObject(foodJsonObject);
                    returningItems.add(food);
                }
                break;
            }case FETCH_CHIEFS: {
                JSONArray chiefJsonArray = jsonBody.getJSONArray("fooditems");

                for (int i = 0; i < chiefJsonArray.length(); i++) {
                    JSONObject chiefJsonObject = chiefJsonArray.getJSONObject(i);
                    Chief chief = Chief.parseFromJSONObject(chiefJsonObject);
                    returningItems.add(chief);
                }
                break;
            }case FETCH_ORDERS: {
                returningItems = new ArrayList<FoodOrder>();
                int orderCount = Integer.parseInt( jsonBody.getString("ordercount"));
                if(orderCount == 0){
                    return returningItems;
                }

                JSONArray orderJsonArray = jsonBody.getJSONArray("theorders");
                for (int i = 0; i < orderJsonArray.length(); i++) {
                    JSONObject orderJsonObject = orderJsonArray.getJSONObject(i);
                    FoodOrder foodOrder = FoodOrder.parseFromJSONObject(orderJsonObject);
                    returningItems.add(foodOrder);
                }
                break;
            }
        }
        return returningItems;
    }
}
