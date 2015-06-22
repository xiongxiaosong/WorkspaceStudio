package com.bestbrand.hh.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.xxs.sdk.adapter.AdapterPagerBase;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by xiongxs on 2015/6/22.
 */
public class AdapterGadePager extends AdapterPagerBase {
    private ArrayList<View> arrayview;

    /**
     * 构造函数
     *
     * @param arrayview view识图队列
     */
    public AdapterGadePager(ArrayList<View> arrayview) {
        super(arrayview);
        this.arrayview = arrayview;
    }

    @Override
    public int getCount() {
        return arrayview == null ? 0 : arrayview.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
