package me.alfredcao.android.foodorderguest;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.font.TextAttribute;
import java.util.UUID;

/**
 * Created by cyssn on 2015-12-04.
 */
public class FoodItem {

    private static final String TAG = "FoodItem class";
    private String mDishName;
    private String mDishPrice;
    private String mImageUrl;
    private String mDishType;
    private UUID mDishLocalID;

    public FoodItem(String dishName, String dishPrice) {
        mDishName = dishName;
        mDishPrice = dishPrice;
        mDishLocalID = UUID.randomUUID();
    }
    public FoodItem(UUID id){
        mDishLocalID = id;
    }
    public FoodItem(){
        mDishLocalID = UUID.randomUUID();
    }

    public static FoodItem parseFromJSONObject(JSONObject foodItemJsonObj){
        try {
            FoodItem food = new FoodItem();
            food.setDishName(foodItemJsonObj.getString("dish"));
            food.setDishPrice("$ " + foodItemJsonObj.getString("price"));
            String dishType = foodItemJsonObj.getString("dishtype");
            food.setDishType(dishType);
            String imageUrl = foodItemJsonObj.getString("imageurl");
            food.setImageUrl(imageUrl);

            return food;
        }catch(JSONException je){
            Log.e(TAG,"Fail to parse JSON to Food Item");
            je.printStackTrace();
        }
        return null;
    }

    public String getDishName() {
        return mDishName;
    }

    public void setDishName(String dishName) {
        mDishName = dishName;
    }

    public String getDishPrice() {
        return mDishPrice;
    }

    public void setDishPrice(String dishPrice) {
        mDishPrice = dishPrice;
    }

    public UUID getDishLocalID() {
        return mDishLocalID;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getDishType() {
        return mDishType;
    }

    public void setDishType(String dishType) {
        mDishType = dishType;
    }
}
