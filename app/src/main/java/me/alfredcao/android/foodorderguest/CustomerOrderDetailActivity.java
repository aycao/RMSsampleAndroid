package me.alfredcao.android.foodorderguest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by cyssn on 2015-12-29.
 */
public class CustomerOrderDetailActivity extends OneFragmentActivity {

    private UUID mOrderID;
    private final static String EXTRA_ORDER_ID =
            "me.alfredcao.android.foodorderguest.extra_order_id";

    public Fragment createFragment() {
        return new CustomerOrderDetailFragment().newInstance(mOrderID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent() != null) {
            mOrderID = (UUID) getIntent().getSerializableExtra(EXTRA_ORDER_ID);
        }
    }

    public static Intent newIntent(Context packageContext, UUID orderID){
        Intent i = new Intent(packageContext,CustomerOrderDetailActivity.class);
        i.putExtra(EXTRA_ORDER_ID,orderID);
        return i;
    }
}
