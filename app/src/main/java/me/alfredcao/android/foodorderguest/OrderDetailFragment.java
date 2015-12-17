package me.alfredcao.android.foodorderguest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by cyssn on 2015-12-16.
 */
public class OrderDetailFragment extends Fragment{

    private static final String ARGS_FOOD_ORDER_ID =
            "me.alfredcao.android.foodorderguest.args_food_order_id";

    private FoodOrder mFoodOrder;
    private TextView mTableNumberTV;
    private TextView mOrderIDTV;
    private TextView mChiefTV;
    private TextView mCommentTV;
    private CheckBox mProcessedCBX;
    private CheckBox mClearedCBX;
    private LinearLayout mFoodItemsLL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_order_detail,container,false);

        UUID foodOrderID = (UUID) getArguments().getSerializable(ARGS_FOOD_ORDER_ID);
        mFoodOrder = OrderMaster.get(getActivity()).getFoodOrder(foodOrderID);

        mTableNumberTV = (TextView) v.findViewById(R.id.text_view_order_detail_table_number);
        mOrderIDTV = (TextView) v.findViewById(R.id.text_view_order_detail_order_id);
        mChiefTV=(TextView) v.findViewById(R.id.text_view_order_detail_chief);
        mCommentTV = (TextView) v.findViewById(R.id.text_view_order_detail_comment);
        mProcessedCBX = (CheckBox) v.findViewById(R.id.checkbox_order_processed);
        mClearedCBX = (CheckBox) v.findViewById(R.id.checkbox_order_cleared);
        mFoodItemsLL = (LinearLayout) v.findViewById(R.id.linear_layout_fooditems_detail);

        mTableNumberTV.setText(String.valueOf(mFoodOrder.getTableNumber()));
        mOrderIDTV.setText(mFoodOrder.getFoodOrderLocalId().toString());
        mChiefTV.setText(mFoodOrder.getChief());
        mCommentTV.setText(mFoodOrder.getComment());
        mProcessedCBX.setChecked(mFoodOrder.isProcessed());
        mClearedCBX.setChecked(mFoodOrder.isCleared());

        for (DishQuantPair dqp: mFoodOrder.getDishQuantPairs()) {
            View dishQuantPairView = inflater.inflate(R.layout.dish_quant_pair, null, false);

            TextView dishNameTv =
                    (TextView) dishQuantPairView.findViewById(R.id.text_view_dish_quant_pair_dish);
            dishNameTv.setText(dqp.getDishName());

            TextView quantTv =
                    (TextView) dishQuantPairView.findViewById(R.id.text_view_dish_quant_pair_quant);
            quantTv.setText(String.valueOf(dqp.getQuantity()));

            mFoodItemsLL.addView(dishQuantPairView);
        }

        return v;
    }

    public static OrderDetailFragment newInstance(UUID foodOrderID) {

        Bundle args = new Bundle();
        args.putSerializable(ARGS_FOOD_ORDER_ID,foodOrderID);

        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
