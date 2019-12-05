package com.wmy.kotlin.mvvm.theme.entity;

import android.view.View;


import com.wmy.kotlin.mvvm.theme.utils.ListUtils;

import java.util.List;

/**
 * 是否需要换肤
 * @author：wmyas
 * @date：2019/8/19 14:45
 */
public class SkinView {
    //控件本身
    View view;
    //控件属性集
    List<SkinItem> list;

    public SkinView(View view, List<SkinItem> list) {
        this.view = view;
        this.list = list;
    }

    public View getView() {
        return view;
    }

    public List<SkinItem> getList() {
        return list;
    }

    /**
     * 执行换肤操作
     */
    public void apply() {
        if (ListUtils.isEmpty(list)) {
            return;
        }
        for (SkinItem at : list) {
            at.apply(view);
        }

    }

    public void clean() {
        if (ListUtils.isEmpty(list)) {
            return;
        }
        for (SkinItem at : list) {
            at = null;
        }
    }
}