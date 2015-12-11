package me.alfredcao.android.foodorderguest;

import java.util.UUID;

/**
 * Created by cyssn on 2015-12-09.
 */
public class Chief {

    private String mFirstName;
    private String mLastName;
    private String mDishType;
    private UUID mChiefLocalId;

    public Chief(String firstName, String lastName, String dishType) {
        mChiefLocalId = UUID.randomUUID();
        mFirstName = firstName;
        mLastName = lastName;
        mDishType = dishType;
    }
    public Chief(UUID id){
        mChiefLocalId = id;
    }
    public Chief(){
        this(UUID.randomUUID());
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

    public UUID getChiefLocalId() {
        return mChiefLocalId;
    }
}
