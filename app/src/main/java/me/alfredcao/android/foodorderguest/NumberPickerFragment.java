package me.alfredcao.android.foodorderguest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by cyssn on 2015-12-08.
 */
public class NumberPickerFragment extends DialogFragment {

    private static final String ARG_DEFAULT_TABLE = "arg_default_table_number";
    private static final String EXTRA_TABLE_NUMBER = "extra_table_number";
    private NumberPicker mNumberPicker;

    public static NumberPickerFragment newInstance(Integer defaultTable){
        int tableNum = 8;
        if(defaultTable != null){
            tableNum = defaultTable;
        }
        NumberPickerFragment numberPickerFragment = new NumberPickerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DEFAULT_TABLE,tableNum);
        numberPickerFragment.setArguments(args);
        return numberPickerFragment;
    }

    private void sendResult(int resultCode, int tableNumber){
        if(getTargetFragment() == null){
            return;
        }
        Intent i = new Intent();
        i.putExtra(EXTRA_TABLE_NUMBER,tableNumber);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,i);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        int defaultTableNum = getArguments().getInt(ARG_DEFAULT_TABLE);

        View numberPickerView = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_number_picker,null);
        mNumberPicker = (NumberPicker) numberPickerView.findViewById(R.id.number_picker);
        mNumberPicker.setMaxValue(20);
        mNumberPicker.setMinValue(1);
        mNumberPicker.setValue(defaultTableNum);

        Dialog d = new AlertDialog.Builder(getActivity())
                .setView(numberPickerView)
                .setTitle(R.string.number_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK, mNumberPicker.getValue());
                    }
                })
                .create();

        return d;
    }

    public static int getTableNumberFrom(Intent data){
        int resultTableNumber = data.getIntExtra(EXTRA_TABLE_NUMBER,8);
        return resultTableNumber;
    }
}
