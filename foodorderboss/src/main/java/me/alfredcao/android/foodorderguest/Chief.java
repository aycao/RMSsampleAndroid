package me.alfredcao.android.foodorderguest;

/**
 * Created by cyssn on 2015-12-09.
 */
public class Chief {

    private String mFirstName;
    private String mLastName;
    private String mDishType;

    public Chief(String firstName, String lastName, String dishType) {
        mFirstName = firstName;
        mLastName = lastName;
        mDishType = dishType;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getDishType() {
        return mDishType;
    }

    public void setDishType(String dishType) {
        mDishType = dishType;
    }
}
