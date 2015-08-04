package com.eclubprague.iot.android.weissmydeweiss.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;

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
            convertView = inflater.inflate(R.layout.item_img_twolines, parent, false);
        }

        Sensor s = (Sensor)getChild(groupPosition, childPosition);

        TextView txtListChild = (TextView) convertView.findViewById(R.id.firstLine);
        txtListChild.setText(s.getUuid());
        //txtListChild.setText("TEXT");

        TextView textView2 = (TextView) convertView.findViewById(R.id.secondLine);
        textView2.setText(Integer.toString(s.getType()));

        return convertView;
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

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (getGroup(groupPosition)).toString();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sensors_group_items, null);
        }

        TextView groupLabel = (TextView) convertView
                .findViewById(R.id.sensors_group_item_label);
        groupLabel.setTypeface(null, Typeface.BOLD);
        groupLabel.setText(headerTitle);

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