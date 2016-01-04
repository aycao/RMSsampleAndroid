package me.alfredcao.android.foodorderguest;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyssn on 2015-12-09.
 */
public class POSTUtils {

    private static final String POST_URL = "http://cyssndy.azurewebsites.net";

    public static abstract class FetchMenuUsingPOSTTask extends AsyncTask<Void,Void,List<FoodItem>> {

        private List<FoodItem> mResultMenu = new ArrayList<>();
        private String mErrMsg;
        private boolean mHasErr;

        @Override
        protected List<FoodItem> doInBackground(Void... params) {
            List<FoodItem> menu = new DataFetcher().fetchMenuUsingPOST(POST_URL);
            return menu;
        }

        @Override
        protected void onPostExecute(List<FoodItem> foodItems) {
            super.onPostExecute(foodItems);
            mResultMenu = foodItems;
            if (mResultMenu.size() == 0){
                mHasErr = true;
                mErrMsg = "No menu found on our server... :(";
            }
            updateUI();
        }

        public List<FoodItem> getResultMenu() {
            return mResultMenu;
        }
        public String getErrMsg() {
            return mErrMsg;
        }
        public boolean isHasErr() {
            return mHasErr;
        }

        public abstract void updateUI();
    }

    public static abstract class FetchOrdersUsingPOSTTask extends AsyncTask<Void,Void,List<FoodOrder>> {

        private List<FoodOrder> mResultFoodOrders = new ArrayList<>();
        private String mErrMsg;
        private boolean mHasErr;
        @Override
        protected List<FoodOrder> doInBackground(Void... params) {
            List<FoodOrder> foodOrders = new DataFetcher().fetchOrdersUsingPOST(POST_URL);
            return foodOrders;
        }

        @Override
        protected void onPostExecute(List<FoodOrder> foodOrders) {
            super.onPostExecute(foodOrders);
            mResultFoodOrders = foodOrders;
            if(mResultFoodOrders.size() == 0){
                mHasErr = true;
                mErrMsg = "No order is found on the server... :(";
            }
            updateUI();
        }

        public List<FoodOrder> getResultFoodOrders() {
            return mResultFoodOrders;
        }
        public String getErrMsg() {
            return mErrMsg;
        }
        public boolean isHasErr() {
            return mHasErr;
        }

        public abstract void updateUI();

    }


    public static abstract class SubmitOrderPOSTTask extends AsyncTask<Void,Void,String> {

        private String mResultMsg;
        private String mErrMsg;
        private boolean mHasErr;
        private FoodOrder mFoodOrder;

        @Override
        protected String doInBackground(Void... params) {
            return new DataFetcher().submitOrderUsingPOST(POST_URL, mFoodOrder);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mResultMsg = s;
            updateUI();
        }

        public void setFoodOrder(FoodOrder foodOrder) {
            mFoodOrder = foodOrder;
        }

        public String getErrMsg() {
            return mErrMsg;
        }
        public boolean isHasErr() {
            return mHasErr;
        }
        public String getResultMsg() {
            return mResultMsg;
        }
        public abstract void updateUI();
    }

    public static abstract class UpdateOrderPOSTTask extends AsyncTask<Void,Void,String>{

        private String mResultMsg;
        private String mErrMsg;
        private boolean mHasErr;
        private FoodOrder mFoodOrder;

        @Override
        protected String doInBackground(Void... params) {
            return new DataFetcher().updateOrderUsingPOST(POST_URL, mFoodOrder);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mResultMsg = s;
            updateUI();
        }

        public void setFoodOrder(FoodOrder foodOrder) {
            mFoodOrder = foodOrder;
        }

        public String getErrMsg() {
            return mErrMsg;
        }
        public boolean isHasErr() {
            return mHasErr;
        }
        public String getResultMsg() {
            return mResultMsg;
        }
        public abstract void updateUI();
    }

}
