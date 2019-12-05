package com.wmy.kotlin.mvvm.theme.entity;

import android.view.View;

/**
 * view 下的属性集对象
 *
 * @author：wmyas
 * @date：2019/8/19 14:44
 */
public abstract class SkinItem {

    protected static final String RES_TYPE_NAME_COLOR = "color";
    protected static final String RES_TYPE_NAME_DRAWABLE = "drawable";
    //属性名
    String name;
    //属性的资源id
    int resId;
    //属性类型  color、mipmap 等
    String typeName;
    //属性名
    String entryName;

//    public SkinItem(String name, int resId, String typeName, String entryName) {
//        this.name = name;
//        this.resId = resId;
//        this.typeName = typeName;
//        this.entryName = entryName;
//    }


    public abstract void apply(View view);

    public String getName() {
        return name;
    }

    public int getResId() {
        return resId;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getEntryName() {
        return entryName;
    }
}