package com.eclubprague.iot.android.weissmydeweiss.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Dat on 16.7.2015.
 */
public class SensorsExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Hub> groupItems; // header titles
    // child data in format of header title, child title
    private HashMap<Hub, List<Sensor>> childItems;

    public SensorsExpandableListViewAdapter(Context context, List<Hub> groupItems,
                                 HashMap<Hub, List<Sensor>> childItems) {
        this.context = context;
        this.groupItems = groupItems;
        this.childItems = childItems;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childItems.get(this.groupItems.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_img_twolines_2, parent, false);
        }

        Sensor s = (Sensor)getChild(groupPosition, childPosition);


        ImageView ic_sensor = (ImageView) convertView.findViewById(R.id.iv_sensor_icon);
        ic_sensor.setImageResource(getIconImageResource(s.getType()));

        TextView txtListChild = (TextView) convertView.findViewById(R.id.tv_firstLine);
        txtListChild.setText(SensorType.getStringSensorType(s.getType()));
        //txtListChild.setText("TEXT");

        TextView textView2 = (TextView) convertView.findViewById(R.id.tv_secondLine);
        textView2.setText("uuid: " + s.getUuid());

        return convertView;
    }

    private int getIconImageResource(int sensorType) {
        switch (sensorType) {
            case SensorType.THERMOMETER:
                return R.drawable.ic_therm;
            case SensorType.PIR:
                return R.drawable.ic_pir;
        }
        return R.drawable.ic_sensor;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childItems.get(this.groupItems.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.groupItems.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.groupItems.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


//    private int[] colors = {
//            android.R.color.holo_green_light,
//            android.R.color.holo_blue_light,
//            android.R.color.holo_red_light,
//            android.R.color.holo_orange_light,
//            android.R.color.holo_purple,
//            android.R.color.black
//    };

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        //Log.e("GROUPOS", Integer.toString(groupPosition));

        String headerTitle = (getGroup(groupPosition)).toString();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sensors_group_items_2, null);
        }

        //RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.sensors_group_item_2_id);
        //layout.setBackgroundColor(colors[groupPosition]);

        TextView groupLabel = (TextView) convertView
                .findViewById(R.id.sensors_group_item_label_2);
        //Log.e("COLOR", Integer.toString(colors[groupPosition]));
        //groupLabel.setTypeface(null, Typeface.BOLD);
        groupLabel.setText(headerTitle);
        //groupLabel.setBackgroundColor(convertView.getContext().getResources().getColor(colors[groupPosition]));

        ImageView groupIcon = (ImageView) convertView.findViewById(R.id.iv_sensor_group_icon);
        switch (groupPosition) {
            case 0:
                groupIcon.setImageResource(R.drawable.ic_sensor_blue);
                break;
            case 1:
                groupIcon.setImageResource(R.drawable.ic_sensor_green);
                break;
            case 2:
                groupIcon.setImageResource(R.drawable.ic_sensor_yellow);
                break;
        }



        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}