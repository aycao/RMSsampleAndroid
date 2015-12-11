package me.alfredcao.android.foodorderguest;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by cyssn on 2015-12-04.
 */
public class FoodMaster {
    private Context mContext;
    private static FoodMaster sFoodMaster;

    private List<FoodItem> mFoodItems;

    public static FoodMaster get (Context context){
        if(sFoodMaster == null){
            FoodMaster fm = new FoodMaster(context);
            /*for(int i = 0; i < 15; i++){
                FoodItem fi = new FoodItem("dish" + String.valueOf(i),"$123.45");
                fm.addItem(fi);
            }*/
            Log.d("FoodMaster", "new FoodMaster Created!!!");
            return fm;
        }else{
            return sFoodMaster;
        }
    }

    private FoodMaster(Context context){
        mContext = context;
        mFoodItems = new ArrayList<>();
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        mFoodItems = foodItems;
    }

    public  List<FoodItem> getFoodItems() {
        return mFoodItems;
    }

    public FoodItem getMenuItem(UUID id){
        if (mFoodItems.size() == 0){
            return null;
        }else{
            for (FoodItem mi: mFoodItems){
                if(mi.getDishLocalID().equals(id)) {
                    return mi;
                }
            }
            return null;
        }
    }

    public void addItem(FoodItem foodItem){
        mFoodItems.add(foodItem);
    }

}
