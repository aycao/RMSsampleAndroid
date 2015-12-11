package me.alfredcao.android.foodorderguest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by cyssn on 2015-12-09.
 */
public class FoodOrder {
    private int mTableNumber;
    private String mComment;
    private ArrayList<DishQuantPair> mDishQuantPairs = new ArrayList<DishQuantPair>();
    private UUID mFoodOrderLocalId;
    private boolean mProcessed;

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
        mDishQuantPairs.add(new DishQuantPair(foodItem.getDishName(),quantity));
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
}
