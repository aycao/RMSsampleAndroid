package me.alfredcao.android.foodorderguest;

import java.util.UUID;

/**
 * Created by cyssn on 2015-12-04.
 */
public class FoodItem {

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
