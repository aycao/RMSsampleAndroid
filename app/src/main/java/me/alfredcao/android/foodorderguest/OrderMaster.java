package me.alfredcao.android.foodorderguest;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by cyssn on 2015-12-04.
 */
public class OrderMaster {
    private Context mContext;
    private static OrderMaster sOrderMaster;

    private List<FoodOrder> mFoodOrders;

    public static OrderMaster get (Context context){
        if(sOrderMaster == null){
            sOrderMaster = new OrderMaster(context);
            Log.d("OrderMaster", "new OrderMaster Created!!!");
            return sOrderMaster;
        }else{
            return sOrderMaster;
        }
    }

    private OrderMaster(Context context){
        mContext = context;
        mFoodOrders = new ArrayList<>();
    }

    public void setFoodOrders(List<FoodOrder> foodOrders) {
        mFoodOrders = foodOrders;
    }

    public  List<FoodOrder> getFoodOrders() {
        return mFoodOrders;
    }

    public FoodOrder getFoodOrder(UUID id){
        if (mFoodOrders.size() == 0){
            return null;
        }else{
            for (FoodOrder fi: mFoodOrders){
                if(fi.getFoodOrderLocalId().equals(id)) {
                    return fi;
                }
            }
            return null;
        }
    }

    public void addItem(FoodOrder foodOrder){
        mFoodOrders.add(foodOrder);
    }

}
