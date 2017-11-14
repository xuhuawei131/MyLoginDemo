package com.lingdian.mylogindemo;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

public class Main2Activity extends AppCompatActivity {
    private View main;
    private View scroll;
    private int scrollDistance=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        main = findViewById(R.id.layout_main);
        scroll = findViewById(R.id.text_forget);
        OnSoftPanConverHelper imple=new OnSoftPanConverHelper(scroll);
        imple.registerRootView(main);
//        main.getViewTreeObserver().addOnGlobalLayoutListener(mainGlobal);
//        main.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                main.getViewTreeObserver().addOnGlobalLayoutListener(mainGlobal);
//            }
//        },500);

    }


private ViewTreeObserver.OnGlobalLayoutListener mainGlobal=new ViewTreeObserver.OnGlobalLayoutListener(){

    @Override
    public void onGlobalLayout() {
        Rect rect=new Rect();
        //1 获取main在窗体可视区域
        main.getWindowVisibleDisplayFrame(rect);
        Log.v("xhw","window size height "+rect.height()+" bottom "+rect.bottom);

        //2 获取main在窗体不可见区域的高度 在键盘没有谈起时
        // main.getRootView().getHeight 调节度应该和rect.botto高度一样
        int mainInvisibleHeight=main.getRootView().getHeight()-rect.bottom;

        Log.v("xhw","main.getRootView size height "+main.getRootView().getHeight()+" mainInvisibleHeight "+mainInvisibleHeight);
        //3、不可见区域大于100 说明键盘弹起来了
        if (mainInvisibleHeight>200){
            int[] location=new int[2];
            scroll.getLocationInWindow(location);

            Log.v("xhw","scroll size  "+location[0]+":"+location[1]);
            //4、获取scroll的窗体坐标 算出main需要滚动的 高度
            int scrollHeight=(location[1]+scroll.getHeight())-rect.bottom;
            Log.v("xhw","scroll scrollHeight  "+scrollHeight);
            scrollDistance=scrollHeight;

            //5、让界面整体上移键盘的高度
            main.scrollBy(0,scrollHeight);
        }else{
            //不可见区域 小于100 说明键盘隐藏了 把界面下移 移回原有高度
            main.scrollBy(0,-scrollDistance);
            Log.v("xhw","scroll scrollHeight  00");
        }
        Log.v("xhw","*************************");
    }
};
}
