package me.alfredcao.android.foodorderguest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

/**
 * Created by cyssn on 2015-12-16.
 */
public class OrderDetailFragment extends Fragment{

    protected static final String ARGS_FOOD_ORDER_ID =
            "me.alfredcao.android.foodorderguest.args_food_order_id";

    protected FoodOrder mFoodOrder;
    protected TextView mTableNumberTV;
    protected TextView mOrderIDTV;
    protected TextView mChiefTV;
    protected TextView mCommentTV;
    protected CheckBox mProcessedCBX;
    protected CheckBox mClearedCBX;
    protected LinearLayout mFoodItemsLL;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

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

        mProcessedCBX.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFoodOrder.setProcessed(isChecked);
            }
        });

        mClearedCBX.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFoodOrder.setCleared(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_order_detail,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();
        switch (itemid){
            case R.id.menu_item_update_order:{
                updateOrder();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected OrderDetailFragment newInstance(UUID foodOrderID) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_FOOD_ORDER_ID, foodOrderID);
        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void updateOrder(){
        new UpdateOrderPOSTTask(mFoodOrder).execute();
    }

    private class UpdateOrderPOSTTask extends POSTUtils.UpdateOrderPOSTTask{

        public UpdateOrderPOSTTask(FoodOrder foodOrder) {
            super.setFoodOrder(foodOrder);
        }

        @Override
        public void updateUI() {
            String resultMsg = this.getResultMsg();
            Toast.makeText(getActivity(),resultMsg,Toast.LENGTH_LONG).show();
        }
    }

}
