package com.eclubprague.iot.android.weissmydeweiss.cloud;

import java.util.List;

/**
 * Created by paulos on 15. 7. 2015.
 */
public class PaginatedCollection<T> {
    int count;
    int offset;

    List<T> items;

    public List<T> getItems() {
        return items;
    }

    public int getOffset() {
        return offset;
    }

    public int getCount() {
        return count;
    }
}
