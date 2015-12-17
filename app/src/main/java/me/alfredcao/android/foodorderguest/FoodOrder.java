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
    private boolean mCleared;
    private String mSubmitTime;
    private String mUpdateTime;
    private String mChief;

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
        mProcessed = false;
        mCleared = false;
        mComment = "";
    }

    public void addDishQuant(FoodItem foodItem){
        this.addDishQuant(foodItem, 1);
    }
    public void addDishQuant(FoodItem foodItem,int quantity){
        for(DishQuantPair dqp: mDishQuantPairs){
            if (dqp.getDishName() == foodItem.getDishName()){
                dqp.setQuantity(dqp.getQuantity() + quantity);
                if(dqp.getQuantity() == 0){
                    this.removeDishQuant(dqp);
                }
                return;
            }
        }
        mDishQuantPairs.add(new DishQuantPair(foodItem.getDishName(), quantity));
    }

    public void removeDishQuant(FoodItem foodItem){
        for(DishQuantPair dqp: mDishQuantPairs){
            if (dqp.getDishName() == foodItem.getDishName()){
                mDishQuantPairs.remove(dqp);
                return;
            }
        }
    }

    public void removeDishQuant(DishQuantPair dishQuantPair){
        for(DishQuantPair dqp: mDishQuantPairs){
            if (dqp.equals(dishQuantPair)){
                mDishQuantPairs.remove(dqp);
                return;
            }
        }
    }

    public int getDishQuant(FoodItem foodItem){
        for(DishQuantPair dqp: mDishQuantPairs){
            if (dqp.getDishName() == foodItem.getDishName()){
                return dqp.getQuantity();
            }
        }
        return 0;
    }

    public static FoodOrder parseFromJSONObject(JSONObject orderJsonObject){
        try {
            FoodOrder foodOrder =
                    new FoodOrder(UUID.fromString(orderJsonObject.getString("orderid")));
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

            foodOrder.setProcessed(orderJsonObject.getInt("processed") == 1);
            foodOrder.setCleared(orderJsonObject.getInt("cleared") == 1);
            foodOrder.setChief(orderJsonObject.getString("chief"));

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

    public String getChief() {
        return mChief;
    }

    public void setChief(String chief) {
        mChief = chief;
    }

    public boolean isCleared() {
        return mCleared;
    }

    public void setCleared(boolean cleared) {
        mCleared = cleared;
    }
}
