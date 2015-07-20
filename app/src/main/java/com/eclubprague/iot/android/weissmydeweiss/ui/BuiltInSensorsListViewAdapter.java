package com.eclubprague.iot.android.weissmydeweiss.ui;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eclubprague.iot.android.weissmydeweiss.R;

import java.util.List;

/**
 * Created by Dat on 20.7.2015.
 */
public class BuiltInSensorsListViewAdapter extends ArrayAdapter<Sensor> {

    public BuiltInSensorsListViewAdapter(Context context, int resource, List<Sensor> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_img_twolines_pure, parent, false);
        }

        Sensor s = getItem(position);
        TextView textView = (TextView) convertView.findViewById(R.id.firstLine_pure);
        textView.setText(s.getName());
        TextView textView2 = (TextView) convertView.findViewById(R.id.secondLine_pure);
        textView2.setText(Float.toString(s.getMaximumRange()));

        return convertView;
    }
}
