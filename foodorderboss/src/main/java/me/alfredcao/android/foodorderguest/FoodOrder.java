package me.alfredcao.android.foodorderguest;

/**
 * Created by cyssn on 2015-12-09.
 */
public class FoodOrder {
    private int mTableNumber;
    private String mDishName;
    private int mQuantity;
    private String mComment;

    public FoodOrder(int tableNumber, String dishName, int quantity, String comment) {
        mTableNumber = tableNumber;
        mDishName = dishName;
        mQuantity = quantity;
        mComment = comment;
    }

    public int getTableNumber() {
        return mTableNumber;
    }

    public void setTableNumber(int tableNumber) {
        mTableNumber = tableNumber;
    }

    public String getDishName() {
        return mDishName;
    }

    public void setDishName(String dishName) {
        mDishName = dishName;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }
}
