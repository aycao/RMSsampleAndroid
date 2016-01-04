package me.alfredcao.android.foodorderguest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cyssn on 2015-12-09.
 */
public class OrderListFragment extends Fragment {

    private static final String TAG = "OrderListFragment Class";
    private static final String FETCH_MENU_POST_URL =
            "http://cyssndy.azurewebsites.net";
    private RecyclerView mRecyclerView;
    private TextView mInfoTextView;
    private List<FoodOrder> mOrders;
    private OrderAdapter mOrderAdapter;
    private boolean mFetchOrdersErr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        new FetchOrdersTask().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mOrderAdapter == null){
            mOrderAdapter = new OrderAdapter(OrderMaster.get(getActivity()).getFoodOrders());
            mRecyclerView.setAdapter(mOrderAdapter);
        }else{
            mOrderAdapter.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_order_list,container,false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_order_list_fragment);
        mInfoTextView = (TextView) v.findViewById(R.id.text_view_order_list_fragment_top);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_order_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_switch_to_guest:{
                Intent i = MenuFragment.newIntent(getActivity());
                startActivity(i);
                return true;
            }case R.id.menu_item_refresh_orders:{
                refreshOrderList();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static OrderListFragment newInstance() {

        Bundle args = new Bundle();

        OrderListFragment fragment = new OrderListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static Intent newIntent(Context packageContext){
        Intent i = new Intent(packageContext,OrderListActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    private void updateUI(){
        OrderMaster orderMaster = OrderMaster.get(getActivity());
        if(mOrderAdapter == null){
            mOrderAdapter = new OrderAdapter(orderMaster.getFoodOrders());
        }
        mRecyclerView.setAdapter(mOrderAdapter);
    }
    private void updateAdapter(){
        OrderMaster orderMaster = OrderMaster.get(getActivity());
        if(isAdded()){
            orderMaster.setFoodOrders(mOrders);
        }
        mOrderAdapter = new OrderAdapter(mOrders);
        mRecyclerView.setAdapter(mOrderAdapter);

        if(!mFetchOrdersErr) {
            MyAnimateUtils.fadeOutView(mInfoTextView);
            MyAnimateUtils.fadeInView(mRecyclerView);
        }
    }

    private void refreshOrderList(){
        mFetchOrdersErr = false;
        MyAnimateUtils.showView(mInfoTextView);
        MyAnimateUtils.fadeOutView(mRecyclerView);
        mInfoTextView.setText(R.string.info_refreshing);
        new FetchOrdersTask().execute();
    }

    private class FetchOrdersTask extends POSTUtils.FetchOrdersUsingPOSTTask{
        @Override
        public void updateUI() {
            mOrders = getResultFoodOrders();
            mFetchOrdersErr = isHasErr();
            if (mFetchOrdersErr) {
                mInfoTextView.setText(getErrMsg());
            }
            updateAdapter();
        }
    }

    private class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private FoodOrder mFoodOrder;
        private TextView mTableNumberTv;
        private TextView mOrderIdTv;
        private LinearLayout mDishQuantPairsLinearLayout;
        private CheckBox mProcessed;
        private CheckBox mCleared;
        private TextView mAssignChiefTv;

        public OrderHolder(View itemView){
            super(itemView);
            mTableNumberTv = (TextView) itemView.findViewById(R.id.text_view_table_number_for_order);
            mOrderIdTv = (TextView) itemView.findViewById(R.id.text_view_order_id);
            mDishQuantPairsLinearLayout =
                    (LinearLayout) itemView.findViewById(R.id.linear_layout_order_detail);
            mProcessed = (CheckBox) itemView.findViewById(R.id.checkbox_order_processed);
            mCleared = (CheckBox) itemView.findViewById(R.id.checkbox_order_cleared);
            mProcessed.setClickable(false);
            mCleared.setClickable(false);
            mAssignChiefTv = (TextView) itemView.findViewById(R.id.text_view_assign_chief);

            itemView.setOnClickListener(this);
        }

        private void bindOrderHolder(FoodOrder foodOrder){
            mFoodOrder = foodOrder;
            //Log.d(TAG, String.valueOf(mFoodOrder.getTableNumber()));
            mTableNumberTv.setText(String.valueOf(mFoodOrder.getTableNumber()));
            mOrderIdTv.setText(mFoodOrder.getFoodOrderLocalId().toString());
            mProcessed.setChecked(mFoodOrder.isProcessed());
            mCleared.setChecked(mFoodOrder.isCleared());

            mDishQuantPairsLinearLayout.removeAllViews();
            for(DishQuantPair dqp: mFoodOrder.getDishQuantPairs()){
                View v = LayoutInflater.from(getActivity())
                        .inflate(R.layout.dish_quant_pair,null,false);
                RelativeLayout dishQuantRelativeLayout;
                dishQuantRelativeLayout = (RelativeLayout) v
                        .findViewById(R.id.relative_layout_dish_quant_pair);
                TextView dishTv;
                TextView quantTv;
                dishTv = (TextView) v.findViewById(R.id.text_view_dish_quant_pair_dish);
                quantTv = (TextView) v.findViewById(R.id.text_view_dish_quant_pair_quant);
                dishTv.setText(dqp.getDishName());
                quantTv.setText(String.valueOf(dqp.getQuantity()));

                mDishQuantPairsLinearLayout.addView(dishQuantRelativeLayout);
            }
        }

        @Override
        public void onClick(View v) {
            Intent i = OrderPagerActivity.newIntent(getActivity(),mFoodOrder.getFoodOrderLocalId());
            startActivity(i);
        }
    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderHolder>{

        private List<FoodOrder> mFoodOrders;

        public OrderAdapter(List<FoodOrder> foodOrders) {
            mFoodOrders = foodOrders;
        }

        @Override
        public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.card_view_food_order,parent,false);
            return new OrderHolder(v);
        }

        @Override
        public void onBindViewHolder(OrderHolder holder, int position) {
            holder.bindOrderHolder(mFoodOrders.get(position));
        }

        @Override
        public int getItemCount() {
            return mFoodOrders.size();
        }
    }
}
