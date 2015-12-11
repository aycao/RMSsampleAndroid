package me.alfredcao.android.foodorderguest;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by cyssn on 2015-12-04.
 */
public class ChiefMaster {
    private Context mContext;
    private static ChiefMaster sChiefMaster;

    private List<Chief> mChiefs;

    public static ChiefMaster get (Context context){
        if(sChiefMaster == null){
            ChiefMaster cm = new ChiefMaster(context);

            Log.d("ChiefMaster", "new ChiefMaster Created!!!");
            return cm;
        }else{
            return sChiefMaster;
        }
    }

    private ChiefMaster(Context context){
        mContext = context;
        mChiefs = new ArrayList<>();
    }

    public void setChiefs(List<Chief> chiefs) {
        mChiefs = chiefs;
    }

    public  List<Chief> getChiefs() {
        return mChiefs;
    }

    public Chief getChief(UUID id){
        if (mChiefs.size() == 0){
            return null;
        }else{
            for (Chief chief: mChiefs){
                if(chief.getChiefLocalId().equals(id)) {
                    return chief;
                }
            }
            return null;
        }
    }

    public void addItem(Chief chief){
        mChiefs.add(chief);
    }

}
