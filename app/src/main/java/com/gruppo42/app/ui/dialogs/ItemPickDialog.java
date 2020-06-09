package com.gruppo42.app.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gruppo42.app.R;

import java.util.ArrayList;

public class ItemPickDialog extends DialogFragment {
    private Object item;
    private ArrayList<? extends Object> options;
    private ChangeListener listener;
    private int layout;

    public ItemPickDialog(int resource, Object item, ArrayList<? extends Object> options, ChangeListener changeListener) {
        super();
        this.layout = resource;
        this.item = item;
        this.options = options;
        this.listener = changeListener;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(layout, null);
        AutoCompleteTextView mactview = view.findViewById(R.id.mactView);
        Log.d("dd", "dddd");
        ArrayAdapter<? extends Object> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                options);
        mactview.setAdapter(adapter);
        mactview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onChange(parent.getItemAtPosition(position));
                dismiss();
            }
        });
        builder.setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which<0)
                            return;
                        listener.onChange(adapter.getItem(which));
                    }
                });
        Dialog ret = builder.create();
        return ret;
    }
}

