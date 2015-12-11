package me.alfredcao.android.foodorderguest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyssn on 2015-12-04.
 */
public class MenuFragment extends Fragment {

    private static final String FETCH_MENU_URL = //"http://cyssndy.koding.io/foodorder/FetchMenu.php";
            "http://cyssndy.azurewebsites.net/FetchMenu.php";
    private static final String FETCH_MENU_POST_URL =
            "http://cyssndy.azurewebsites.net";
    private static final String TAG = "MenuFragment";
    private static final String DIALOG_TABLE_NUMBER = "number picker dialog";
    private static final int REQUEST_TABLE_NUMBER = 1;

    private RecyclerView mRecyclerView;
    private FoodItemAdapter mFoodItemAdapter;
    private TextView mInfoTextView;
    private List<FoodItem> mMenu = new ArrayList<FoodItem>();
    private Button mButtonTable;
    private Button mButtonSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //new FetchMenuTask().execute();
        new FetchMenuUsingPOSTTask().execute();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_menu,container,false);

        mButtonTable = (Button) v.findViewById(R.id.button_table_number);
        mButtonSubmit = (Button) v.findViewById(R.id.button_submit_order);
        mInfoTextView = (TextView) v.findViewById(R.id.text_view_menu_fragment_top);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_menu_fragment);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mButtonTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                NumberPickerFragment nbrDialog = NumberPickerFragment.newInstance(null);//default table number, null for now
                nbrDialog.setTargetFragment(MenuFragment.this,REQUEST_TABLE_NUMBER);
                nbrDialog.show(fm,DIALOG_TABLE_NUMBER);
            }
        });

        //TODO mButtonSubmit Listener

        updateUI();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case (REQUEST_TABLE_NUMBER):{
                String resultTableNumber = String.valueOf(NumberPickerFragment.getTableNumberFrom(data));
                mButtonTable.setText(resultTableNumber);
            }
            default:break;
        }
    }

    public static MenuFragment newInstance() {
        Bundle args = new Bundle();

        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void updateUI(){
        FoodMaster foodMaster = FoodMaster.get(getActivity());

        if(mFoodItemAdapter == null) {
            mFoodItemAdapter = new FoodItemAdapter(foodMaster.getFoodItems());
        }
        mRecyclerView.setAdapter(mFoodItemAdapter);
    }

    private void updateAdapter(){
        FoodMaster foodMaster = FoodMaster.get(getActivity());
        if(isAdded()){
            foodMaster.setFoodItems(mMenu);
        }
        mFoodItemAdapter = new FoodItemAdapter(foodMaster.getFoodItems());
        mRecyclerView.setAdapter(mFoodItemAdapter);

        MyAnimateUtils.fadeOutView(mInfoTextView);
        MyAnimateUtils.fadeInView(mRecyclerView);
    }


    private class FoodItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private FoodItem mFoodItem;
        private TextView mDishNameTextView;
        private TextView mDishPriceTextView;
        private ImageButton mButtonMinus;
        private ImageButton mButtonPlus;
        private ImageView mDishImage;
        private EditText mAmountEditText;
        private int mAmount = 0;

        public FoodItemHolder(View itemView) {
            super(itemView);
            mDishNameTextView = (TextView) itemView.findViewById(R.id.text_view_dish);
            mDishPriceTextView = (TextView) itemView.findViewById(R.id.text_view_price);
            mButtonMinus = (ImageButton) itemView.findViewById(R.id.button_minus);
            mButtonPlus = (ImageButton) itemView.findViewById(R.id.button_plus);
            mDishImage = (ImageView) itemView.findViewById(R.id.image_view_dish_image);
            mAmountEditText = (EditText) itemView.findViewById(R.id.edit_text_amount);
            mAmount = Integer.parseInt(mAmountEditText.getText().toString());

            mButtonMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAmount > 0){
                        mAmount--;
                        mAmountEditText.setText(String.valueOf(mAmount));
                    }
                }
            });
            mButtonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAmount++;
                    mAmountEditText.setText(String.valueOf(mAmount));
                }
            });
        }

        public void bindMenuItem(FoodItem foodItem){
            mFoodItem = foodItem;
            mDishNameTextView.setText(mFoodItem.getDishName());
            mDishPriceTextView.setText(mFoodItem.getDishPrice());
            //TODO add image
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class FoodItemAdapter extends RecyclerView.Adapter<FoodItemHolder>{

        private List<FoodItem> mFoodItems;

        public FoodItemAdapter(List<FoodItem> foodItems) {
            mFoodItems = foodItems;
        }

        @Override
        public FoodItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.card_view_food_item,parent,false);
            return new FoodItemHolder(v);
        }

        @Override
        public void onBindViewHolder(FoodItemHolder holder, int position) {
            holder.bindMenuItem(mFoodItems.get(position));
        }

        @Override
        public int getItemCount() {
            if (mFoodItems == null){
                return 0;
            }else {
                return mFoodItems.size();
            }
        }
    }

    private class FetchMenuTask extends AsyncTask<Void,Void,List<FoodItem>>{
        @Override
        protected List<FoodItem> doInBackground(Void... params) {
            List<FoodItem> menu = new DataFetcher().fetchMenuDirect(FETCH_MENU_URL);
            return menu;
        }

        @Override
        protected void onPostExecute(List<FoodItem> foodItems) {
            super.onPostExecute(foodItems);
            mMenu = foodItems;
            updateAdapter();
        }
    }

    private class FetchMenuUsingPOSTTask extends AsyncTask<Void,Void,List<FoodItem>>{
        @Override
        protected List<FoodItem> doInBackground(Void... params) {
            List<FoodItem> menu = new DataFetcher().fetchMenuUsingPOST(FETCH_MENU_POST_URL);;
            return menu;
        }

        @Override
        protected void onPostExecute(List<FoodItem> foodItems) {
            super.onPostExecute(foodItems);
            mMenu = foodItems;
            updateAdapter();
        }
    }
}
