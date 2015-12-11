package me.alfredcao.android.foodorderguest;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by cyssn on 2015-12-09.
 */
public class FoodOrder {

    private static final String TAG = "FoodOrderClass";
    private int mTableNumber;
    private String mComment;
    private ArrayList<DishQuantPair> mDishQuantPairs = new ArrayList<DishQuantPair>();
    private UUID mFoodOrderLocalId;
    private boolean mProcessed;
    private String mSubmitTime;
    private String mUpdateTime;

    public FoodOrder(int tableNumber, ArrayList<DishQuantPair> dishQuantPairs, String comment) {
        mFoodOrderLocalId = UUID.randomUUID();
        mTableNumber = tableNumber;
        mDishQuantPairs = dishQuantPairs;
        mFoodOrderLocalId = UUID.randomUUID();
        mComment = comment;
    }

    public FoodOrder(){
        this(UUID.randomUUID());
    }
    public FoodOrder(UUID id){
        mFoodOrderLocalId = id;
    }

    public void addDishQuant(FoodItem foodItem){
        this.addDishQuant(foodItem, 1);
    }
    public void addDishQuant(FoodItem foodItem,int quantity){
        mDishQuantPairs.add(new DishQuantPair(foodItem.getDishName(), quantity));
    }

    public static FoodOrder parseFromJSONObject(JSONObject orderJsonObject){
        try {
            FoodOrder foodOrder =
                    new FoodOrder(/*UUID.fromString(orderJsonObject.getString("orderid"))*/);
            foodOrder.setTableNumber(
                    Integer.parseInt(orderJsonObject.getString("table_number")));
            foodOrder.setComment(orderJsonObject.getString("comment"));
            JSONArray dishQuantPairsJsonArray = orderJsonObject.getJSONArray("dish_quant_pairs");
            ArrayList<DishQuantPair> dishQuantPairs= new ArrayList<DishQuantPair>();
            for(int i = 0; i < dishQuantPairsJsonArray.length(); i++){
                JSONObject dishQuantPairJsonObj = dishQuantPairsJsonArray.getJSONObject(i);
                DishQuantPair dqp = new DishQuantPair(
                        dishQuantPairJsonObj.getString("dishname"),
                        Integer.parseInt(dishQuantPairJsonObj.getString("quantity")));
                dishQuantPairs.add(dqp);
            }
            foodOrder.setDishQuantPairs(dishQuantPairs);
            return foodOrder;

        }catch (JSONException je){
            Log.e(TAG, "Fail to parse Json to Foodorder");
            je.printStackTrace();
        }
        return null;
    }

    public int getTableNumber() {
        return mTableNumber;
    }

    public void setTableNumber(int tableNumber) {
        mTableNumber = tableNumber;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public UUID getFoodOrderLocalId() {
        return mFoodOrderLocalId;
    }

    public ArrayList<DishQuantPair> getDishQuantPairs() {
        return mDishQuantPairs;
    }

    public void setDishQuantPairs(ArrayList<DishQuantPair> dishQuantPairs) {
        mDishQuantPairs = dishQuantPairs;
    }

    public boolean isProcessed() {
        return mProcessed;
    }

    public void setProcessed(boolean processed) {
        mProcessed = processed;
    }

    public String getSubmitTime() {
        return mSubmitTime;
    }

    public void setSubmitTime(String submitTime) {
        mSubmitTime = submitTime;
    }

    public String getUpdateTime() {
        return mUpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        mUpdateTime = updateTime;
    }
}
