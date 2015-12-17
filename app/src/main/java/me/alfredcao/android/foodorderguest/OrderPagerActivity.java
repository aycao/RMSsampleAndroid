package me.alfredcao.android.foodorderguest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by cyssn on 2015-12-16.
 */
public class OrderPagerActivity extends AppCompatActivity{

    private static final String EXTRA_FOOD_ORDER_ID =
            "me.alfredcao.android.foodorderguest.extra_food_order_id";
    private ViewPager mOrderPager;
    private List<FoodOrder> mFoodOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pager);

        UUID foodOrderID = (UUID) getIntent().getSerializableExtra(EXTRA_FOOD_ORDER_ID);

        mOrderPager = (ViewPager) findViewById(R.id.view_pager_order_pager_activity);
        mFoodOrders = OrderMaster.get(this).getFoodOrders();

        FragmentManager fm = getSupportFragmentManager();
        mOrderPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                FoodOrder foodOrder = mFoodOrders.get(position);
                return OrderDetailFragment.newInstance(foodOrder.getFoodOrderLocalId());
            }

            @Override
            public int getCount() {
                return mFoodOrders.size();
            }
        });

        for(int i = 0 ; i < mFoodOrders.size(); i++){
            if(mFoodOrders.get(i).getFoodOrderLocalId().equals(foodOrderID)){
                mOrderPager.setCurrentItem(i);
                break;
            }
        }
    }

    public static Intent newIntent(Context packageContext, UUID foodOrderID){
        Intent i = new Intent(packageContext,OrderPagerActivity.class);
        i.putExtra(EXTRA_FOOD_ORDER_ID,foodOrderID);
        return i;
    }


}
