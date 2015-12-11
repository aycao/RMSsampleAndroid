package me.alfredcao.android.foodorderguest;

import android.support.v4.app.Fragment;

/**
 * Created by cyssn on 2015-12-09.
 */
public class OrderActivity extends OneFragmentActivity {
    @Override
    public Fragment createFragment() {
        return OrderListFragment.newInstance();
    }
}
