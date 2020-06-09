package com.gruppo42.app.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gruppo42.app.R;


public class YearPickDialog extends DialogFragment {
    private int minVal, maxVal;
    private ChangeListener listener;
    private int layout;

    public YearPickDialog(int resource, int minVal, int maxVal, ChangeListener listener)
    {
        super();
        this.listener = listener;
        this.layout = resource;
        this.maxVal = maxVal;
        this.minVal = minVal;
    }
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(layout, null);
        NumberPicker numberPicker = view.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(this.maxVal);
        numberPicker.setMinValue(this.minVal);
        numberPicker.setValue(Calendar.getInstance().get(Calendar.YEAR));
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //listener.onChange(newVal);
            }
        });
        builder.setView(view)
                .setTitle(getString(R.string.year_select))
                .setPositiveButton("Sumbit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onChange(numberPicker.getValue());
                    }
                });
        Dialog ret = builder.create();
        return ret;
    }
}
