package com.lingdian.mylogindemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.util.TypedValue.COMPLEX_UNIT_PX;
import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Created by lingdian on 2017/11/15.
 */

public class MyEditTitleLayout extends LinearLayout {
    private TextView textView;
    private EditText editText;
    private int editHeight;
    private int titleHeight;
    private int totalHeight;
    private boolean isInit = false;
    private float MAX_EDIT_SIZE = 17.0F;
    private static final float SCALE = 0.75F;
    private float MIN_EDIUT_SIZE = MAX_EDIT_SIZE * SCALE;
    private CharSequence beforeContent;

    private int title_hint_color;
    private int title_warn_color;
    private int edit_max_length;
    private  String  title_warn_text;
    private  String title_hint_text;
    public MyEditTitleLayout(Context context) {
        super(context);
        init(null);
    }

    public MyEditTitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyEditTitleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setOrientation(VERTICAL);


        LayoutParams textLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView = new TextView(getContext());
        textView.setVisibility(GONE);
        textView.setText("我是标题");
        textView.setTextSize(COMPLEX_UNIT_SP, 10);
        addView(textView, textLp);

        LayoutParams editLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editLp.topMargin = 0;
        editLp.bottomMargin = 0;
        editText = new EditText(getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            editText.setBackground(null);
        } else {
            editText.setBackgroundDrawable(null);
        }
        editText.setPadding(0, 0, 0, 0);
        editText.setSingleLine();
        editText.setTextColor(getContext().getResources().getColor(android.R.color.black));
        editText.setTextSize(COMPLEX_UNIT_SP, MAX_EDIT_SIZE);


        addView(editText, editLp);

        editText.addTextChangedListener(mTextWatcher);

        if (attrs != null) {
            TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.EditWithTitleLayout);
            title_hint_color = mTypedArray.getColor(R.styleable.EditWithTitleLayout_title_hint_color, Color.BLACK);
            title_warn_color = mTypedArray.getColor(R.styleable.EditWithTitleLayout_title_warn_color, Color.RED);


            title_warn_text = mTypedArray.getString(R.styleable.EditWithTitleLayout_title_warn_text);
            title_hint_text = mTypedArray.getString(R.styleable.EditWithTitleLayout_title_hint_text);

            float title_text_size = mTypedArray.getDimensionPixelSize(R.styleable.EditWithTitleLayout_title_text_size, 15);
            float edit_text_size = mTypedArray.getDimensionPixelSize(R.styleable.EditWithTitleLayout_edit_text_size, 15);
            String edit_hint = mTypedArray.getString(R.styleable.EditWithTitleLayout_edit_hint);
            int edit_color=mTypedArray.getColor(R.styleable.EditWithTitleLayout_edit_color, Color.BLACK);

            edit_max_length = mTypedArray.getInt(R.styleable.EditWithTitleLayout_edit_max_length, -1);

            textView.setTextColor(title_hint_color);
            textView.setTextSize(COMPLEX_UNIT_PX, title_text_size);

            editText.setTextColor(edit_color);
            editText.setTextSize(COMPLEX_UNIT_PX, edit_text_size);
            editText.setHint(edit_hint);

            if (edit_max_length != -1) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(edit_max_length)});
            }
            mTypedArray.recycle();
        }

    }

    private float getTextSize(float textSizeSp) {
        return TypedValue.applyDimension(COMPLEX_UNIT_SP, textSizeSp, getContext().getResources().getDisplayMetrics());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        totalHeight = getMeasuredHeight();
        editHeight = (int) (totalHeight * SCALE);
        titleHeight = totalHeight - editHeight;
        textView.setHeight(titleHeight);
        editText.setHeight(totalHeight);
    }

    public int dip2px(float dipVlue) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        float sDensity = metrics.density;
        return (int) (dipVlue * sDensity + 0.5F);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeContent = editText.getText().toString();
        }

        @Override
        public void onTextChanged(CharSequence after, int start, int before, int count) {
            if (TextUtils.isEmpty(beforeContent) && !TextUtils.isEmpty(after)) {

                startScale2Small();
                editText.setHeight(editHeight);
                editText.setTextSize(COMPLEX_UNIT_SP, MIN_EDIUT_SIZE);
            } else if (TextUtils.isEmpty(after) && !TextUtils.isEmpty(beforeContent)) {

                startScale2Big();
                editText.setHeight(totalHeight);
                editText.setTextSize(COMPLEX_UNIT_SP, MAX_EDIT_SIZE);

            }

            if (editText.getText().toString().length()==edit_max_length){
                textView.setTextColor(title_warn_color);
                if (!TextUtils.isEmpty(title_warn_text)){
                    textView.setText(title_warn_text);
                }
            }else{
                textView.setTextColor(title_hint_color);

                if (!TextUtils.isEmpty(title_warn_text)){
                    textView.setText(title_hint_text);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 缩小
     */
    private void startScale2Small() {

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);

        alphaAnimation.setFillAfter(true);
        alphaAnimation.setFillBefore(false);
        //设置动画持续时长
        alphaAnimation.setDuration(500);
        //设置动画播放次数
        textView.startAnimation(alphaAnimation);

//      AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.in_alpha);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                textView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        textView.startAnimation(alphaAnimation);

    }

    /**
     * 伸缩到放大
     */
    private void startScale2Big() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        //设置动画持续时长
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setFillBefore(false);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        //设置动画播放次数


//        AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.out_alpha);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        textView.startAnimation(alphaAnimation);

    }
}
