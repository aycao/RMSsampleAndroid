package me.alfredcao.android.foodorderguest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    private ListView mListView;

    private FoodItemAdapter mFoodItemAdapter;
    private TextView mInfoTextView;
    private Button mButtonTable;
    private Button mButtonSubmit;
    private boolean mFetchMenuErr;
    private String mSubmitResult;
    private FoodOrder mFoodOrder;
//    private Button mButtonViewOrders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_menu,container,false);

        mButtonTable = (Button) v.findViewById(R.id.button_table_number);
        mButtonSubmit = (Button) v.findViewById(R.id.button_submit_order);
//        mButtonViewOrders = (Button) v.findViewById(R.id.button_view_orders);
        mInfoTextView = (TextView) v.findViewById(R.id.text_view_menu_fragment_top);

        mListView = (ListView) v.findViewById(R.id.list_view_dish_type_section);

        mButtonTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNumberPickerDialog();
            }
        });

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFoodOrder.getTableNumber() == 0) {
                    Toast.makeText(getActivity(), R.string.select_table, Toast.LENGTH_LONG);
                    makeNumberPickerDialog();
                    return;
                }
                if (mFoodOrder.getDishQuantPairs().size() == 0) {
                    Dialog d = new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.error_title)
                            .setMessage(R.string.message_no_order)
                            .create();
                    d.show();
                    return;
                }
                new SubmitOrderPOSTTask(mFoodOrder).execute();
            }
        });

/*        mButtonViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        new FetchMenuPOSTTask().execute();
        mFoodOrder = new FoodOrder();
        updateAdapter();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_switch_to_boss:{
                Intent i = OrderListFragment.newIntent(getActivity());
                startActivity(i);
                return true;
            }case R.id.menu_item_refresh_menu:{
                refreshMenu();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case (REQUEST_TABLE_NUMBER):{
                mFoodOrder.setTableNumber(NumberPickerFragment.getTableNumberFrom(data));
                mButtonTable.setText(String.valueOf(mFoodOrder.getTableNumber()));
                break;
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

    public static Intent newIntent(Context packageContext){
        Intent i = new Intent(packageContext,MenuActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    public void updateUI(){
        FoodMaster foodMaster = FoodMaster.get(getActivity());

//        if(mFoodItemAdapter == null) {
//            mFoodItemAdapter = new FoodItemAdapter(foodMaster.getFoodItems());
//        }
//        mRecyclerView.setAdapter(mFoodItemAdapter);
    }

    private void updateAdapter(){
        FoodMaster foodMaster = FoodMaster.get(getActivity());
//        mFoodItemAdapter = new FoodItemAdapter(foodMaster.getFoodItems());
//        mRecyclerView.setAdapter(mFoodItemAdapter);

        if(!mFetchMenuErr) {
            MyAnimateUtils.fadeOutView(mInfoTextView);
//            MyAnimateUtils.fadeInView(mRecyclerView);
            MyAnimateUtils.fadeInView(mListView);
        }
        try{
            mFoodOrder.setTableNumber(Integer.parseInt(mButtonTable.getText().toString()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void refreshMenu(){
        mFetchMenuErr = false;
        mFoodOrder = new FoodOrder();
        MyAnimateUtils.showView(mInfoTextView);
//        MyAnimateUtils.fadeOutView(mRecyclerView);
        MyAnimateUtils.fadeOutView(mListView);
        mInfoTextView.setText(R.string.info_refreshing);
        new FetchMenuPOSTTask().execute();
    }

    private void updateList(){
        List<FoodItem> foodItems = FoodMaster.get(getActivity()).getFoodItems();
        String currentDishType;
        if (foodItems.size() > 0){
            currentDishType = foodItems.get(0).getDishType();
        }
        ArrayList<View> dishTypeViewsList = new ArrayList<View>();

        while(foodItems.size() > 0) {
            List<FoodItem> dishTypeFoodItems = new ArrayList<FoodItem>();
            currentDishType = foodItems.get(0).getDishType();
            View v = LayoutInflater.from(getActivity())
                    .inflate(R.layout.linear_layout_dish_type_section,null,false);
            LinearLayout dishTypeSectionLinearLayout = (LinearLayout)
                    v.findViewById(R.id.linear_layout_dish_type_section);
            TextView dishTypeSectionTitleTv =
                    (TextView) v.findViewById(R.id.text_view_dish_type_section_title);
            RecyclerView dishTypeSectionRecyclerView =
                    (RecyclerView) v.findViewById(R.id.recycler_view_dish_type_section);
            dishTypeSectionTitleTv.setText(currentDishType.toUpperCase());

            for (FoodItem foodItem: foodItems){
                if(foodItem.getDishType().equals(currentDishType)){
                    dishTypeFoodItems.add(foodItem);
                }
            }

//            LinearLayoutManager lym =
//                    new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
            dishTypeSectionRecyclerView.setLayoutManager(
                    new CustomLinearLayoutManager(getActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false));
            FoodItemAdapter adapter = new FoodItemAdapter(dishTypeFoodItems);
            dishTypeSectionRecyclerView.setAdapter(adapter);

            dishTypeViewsList.add(v);

            //remove processed ones
            foodItems.removeAll(dishTypeFoodItems);
//            Log.d(TAG, String.valueOf(dishTypeFoodItems.size()));
//            Log.d(TAG,"Added one dish Type" );
        }

        ArrayAdapter dishTypeListViewAdaper =
                new ArrayListViewAdapter(getActivity(),
                        R.layout.linear_layout_dish_type_section,
                        dishTypeViewsList);

        mListView.setAdapter(dishTypeListViewAdaper);
    }

    private void makeNumberPickerDialog(){
        FragmentManager fm = getFragmentManager();
        NumberPickerFragment nbrDialog = NumberPickerFragment.newInstance(
                mFoodOrder.getTableNumber());//default table number, null for now
        nbrDialog.setTargetFragment(MenuFragment.this, REQUEST_TABLE_NUMBER);
        nbrDialog.show(fm, DIALOG_TABLE_NUMBER);
    }

    private class ArrayListViewAdapter extends ArrayAdapter<View>{

        private Context mContext;
        private int mLayoutID;
        private List<View> mViews;

        public ArrayListViewAdapter(Context context, int resource, List<View> objects) {
            super(context, resource, objects);
            mContext = context;
            mLayoutID = resource;
            mViews = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //super.getView(position, convertView, parent);

            if(convertView == null) {
                convertView = mViews.get(position);
            }

            return convertView;
        }
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
                        mFoodOrder.addDishQuant(mFoodItem,-1);
                        mAmount--;
                        mAmountEditText.setText(String.valueOf(mAmount));
                    }
                }
            });
            mButtonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFoodOrder.addDishQuant(mFoodItem);
                    mAmount++;
                    mAmountEditText.setText(String.valueOf(mAmount));
                }
            });
        }

        public void bindMenuItem(FoodItem foodItem){
            mFoodItem = foodItem;
            mDishNameTextView.setText(mFoodItem.getDishName());
            mDishPriceTextView.setText(mFoodItem.getDishPrice());
            mAmountEditText.setText(String.valueOf(mFoodOrder.getDishQuant(foodItem)));
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
            FoodMaster.get(getActivity()).setFoodItems(foodItems);
            updateAdapter();
        }
    }

    private class FetchMenuPOSTTask extends POSTUtils.FetchMenuUsingPOSTTask{
        @Override
        public void updateUI() {
            mFetchMenuErr = isHasErr();
            if (mFetchMenuErr){
                mInfoTextView.setText(getErrMsg());
            }
            FoodMaster.get(getActivity()).setFoodItems(getResultMenu());
            updateAdapter();
            updateList();
        }

    }

    private class SubmitOrderPOSTTask extends POSTUtils.SubmitOrderPOSTTask{

        public SubmitOrderPOSTTask(FoodOrder foodOrder) {
            super.setFoodOrder(foodOrder);
        }

        @Override
        public void updateUI() {
            mSubmitResult = getResultMsg();
            Toast.makeText(getActivity(),mSubmitResult,Toast.LENGTH_LONG).show();
        }
    }

}
