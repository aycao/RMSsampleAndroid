package me.alfredcao.android.foodorderguest;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by cyssn on 2015-12-09.
 */
public class Chief {

    private static final String TAG = "Chief class";
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

    public static Chief parseFromJSONObject(JSONObject chiefJsonObject){
        try {
            Chief chief = new Chief();
            chief.setDishType(chiefJsonObject.getString("dishtype"));
            chief.setFirstName(chiefJsonObject.getString("firstname"));
            chief.setLastName(chiefJsonObject.getString("lastname"));
            return chief;
        }catch(JSONException je){
            Log.e(TAG, "Fail to parse JSON to Chief");
            je.printStackTrace();
        }
        return null;
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
