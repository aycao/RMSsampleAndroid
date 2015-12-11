package me.alfredcao.android.foodorderguest;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MenuActivity extends OneFragmentActivity {
    @Override
    public Fragment createFragment() {
        return MenuFragment.newInstance();
    }
}
