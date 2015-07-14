package com.eclubprague.iot.android.weissmydeweiss.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;

import java.util.List;

/**
 * Created by paulos on 14. 7. 2015.
 */
public class SensorListViewAdapter extends ArrayAdapter<Sensor> {

    public SensorListViewAdapter(Context context, int resource) {
        super(context, resource);
    }

    public SensorListViewAdapter(Context context, int resource, Sensor[] objects) {
        super(context, resource, objects);
    }

    public SensorListViewAdapter(Context context, int resource, List<Sensor> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_img_twolines, parent, false);

        Sensor s = getItem(position);
        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        textView.setText(s.getUuid());
        TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
        textView2.setText(s.getType().getName());

        return rowView;
    }
}
