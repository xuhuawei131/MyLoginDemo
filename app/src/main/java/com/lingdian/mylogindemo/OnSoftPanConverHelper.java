package com.lingdian.mylogindemo;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by lingdian on 2017/11/14.
 */

public class OnSoftPanConverHelper implements ViewTreeObserver.OnGlobalLayoutListener {
    private View rootView;
    private View bottomView;
    private int scrollDistance = 0;
    private boolean isInit = false;
    private int SRC_HEIGHT = 0;

    public OnSoftPanConverHelper(View bottomView) {
        this.bottomView = bottomView;
    }
    public void registerRootView(View rootView){
        this.rootView = rootView;
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }
    @Override
    public void onGlobalLayout() {
        Rect rect = new Rect();
        //1 获取main在窗体可视区域
        rootView.getWindowVisibleDisplayFrame(rect);
        Log.v("xhw", "window size height " + rect.height() + " bottom " + rect.bottom);

        if (isInit) {
            SRC_HEIGHT = rect.height();
        } else {
            if (SRC_HEIGHT < rect.height()) {
                SRC_HEIGHT = rect.height();
            }
        }
        //3、不可见区域大于100 说明键盘弹起来了
        if (SRC_HEIGHT > rect.height()) {
            int[] location = new int[2];
            bottomView.getLocationInWindow(location);

            Log.v("xhw", "bottomView size  " + location[0] + ":" + location[1]);
            //4、获取scroll的窗体坐标 算出main需要滚动的 高度
            int scrollHeight = (location[1] + bottomView.getHeight()) - rect.bottom;
            Log.v("xhw", "bottomView scrollHeight  " + scrollHeight);
            scrollDistance = scrollHeight;

            //5、让界面整体上移键盘的高度
            rootView.scrollBy(0, scrollHeight);
        } else {
            //不可见区域 小于100 说明键盘隐藏了 把界面下移 移回原有高度
            rootView.scrollBy(0, -scrollDistance);
            Log.v("xhw", "bottomView scrollHeight  00");
        }
        Log.v("xhw", "*************************");
    }
}
