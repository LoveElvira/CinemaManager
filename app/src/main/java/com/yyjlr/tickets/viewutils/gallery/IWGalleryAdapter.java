/*
 * Copyright (c) 2016 wuzhen. All rights reserved.
 */

package com.yyjlr.tickets.viewutils.gallery;

/**
 * 当需要改变 ITEM 中单个控件的透明度时 Adapter 的实现。
 *
 * @author wuzhen
 * @version Version 1.0, 2016-09-05
 */
public interface IWGalleryAdapter {

    /**
     * 获取 ITEM 中需要改变透明度的控件的 ID。
     *
     * @return 需要改变透明度的控件的 ID
     */
    int getChangeAlphaViewId();
}
