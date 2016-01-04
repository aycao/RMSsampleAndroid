package me.alfredcao.android.foodorderguest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;

/**
 * Created by cyssn on 2015-12-29.
 */
public class CustomerOrderDetailFragment extends OrderDetailFragment {

    @Override
    protected OrderDetailFragment newInstance(UUID foodOrderID) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_FOOD_ORDER_ID,foodOrderID);
        CustomerOrderDetailFragment fragment = new CustomerOrderDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
