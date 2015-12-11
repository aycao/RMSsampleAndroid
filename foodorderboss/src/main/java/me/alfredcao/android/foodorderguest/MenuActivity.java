package me.alfredcao.android.foodorderguest;

import android.support.v4.app.Fragment;

public class MenuActivity extends OneFragmentActivity {
    @Override
    public Fragment createFragment() {
        return MenuFragment.newInstance();
    }
}
