package com.eclubprague.iot.android.weissmydeweiss.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eclubprague.iot.android.weissmydeweiss.R;

import java.util.List;

/**
 * Created by Dat on 13.9.2015.
 */
public class CustomGridViewAdapter extends ArrayAdapter<GridViewItemWrapper> {

    public CustomGridViewAdapter(Context context, int resource, List<GridViewItemWrapper> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sensor_grid_menu_item, parent, false);
        }

        //parent.setBackground(convertView.getResources().getDrawable(R.drawable.shelfcell_bgr2x));

        GridViewItemWrapper item = getItem(position);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_grid_item_label);
        textView.setText(item.getDesc());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_grid_item_img);
        
        imageView.setImageResource(item.getRes());



        // set image based on selected text
            /* TODO
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);
            Set image according to app
            */

        return convertView;
    }
}