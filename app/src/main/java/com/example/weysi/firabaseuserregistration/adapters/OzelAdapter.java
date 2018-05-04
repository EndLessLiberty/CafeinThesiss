package com.example.weysi.firabaseuserregistration.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weysi.firabaseuserregistration.informations.Data;
import com.example.weysi.firabaseuserregistration.R;

import java.util.List;

/**
 * Created by lenovo on 1.11.2017.
 */

public class OzelAdapter extends ArrayAdapter<Data> {

    public OzelAdapter(@NonNull Context context, @LayoutRes int resource, List<Data> items) {
        super(context, resource,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.satir_layout, null);

        }

        Data data = getItem(position);



        ImageView personImage = (ImageView) v.findViewById(R.id.simge);
        TextView personName = (TextView) v.findViewById(R.id.isimsoyisim);
        personName.setText(data.getName());
        personImage.setImageBitmap(data.getBitmap());



        return v;

    }
}
