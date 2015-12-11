package me.alfredcao.android.foodorderguest;

/**
 * Created by cyssn on 2015-12-09.
 */
public class DishQuantPair {
    private String mDishName;
    private Integer mQuantity;

    public DishQuantPair(String dishName, Integer quantity) {
        mDishName = dishName;
        mQuantity = quantity;
    }

    public String getDishName() {
        return mDishName;
    }

    public void setDishName(String dishName) {
        mDishName = dishName;
    }

    public Integer getQuantity() {
        return mQuantity;
    }

    public void setQuantity(Integer quantity) {
        mQuantity = quantity;
    }
}
