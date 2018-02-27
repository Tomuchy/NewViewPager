package com.newlive.yyviewpager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by y on 2018/2/26.
 */

public class NewSplash extends AppCompatActivity {

    public static boolean firstState = false;
    public static Activity rootActivity;
    private ViewPager mViewpager;
    private PagerAdapter pagerAdapter;
    ArrayList<Integer> btmIdList = new ArrayList<>();
    ArrayList<Integer> bgIdList = new ArrayList<>();
    static int type = 0;
    private final int MAXTOTATION = 20;
    private LinearLayout mPointsContant;
    private ArrayList<ImageView> pointList;
    private int pointDistance;
    private ImageView mLightPoint;
    private Button mBtn;
    public static String text;
    public static String imgSrc;
    public static String bgSrc;
    private String btnName;

    /**
     * 用于获取关联moudle的资源文件  必须设置
     *
     * @param activity
     */
    public static void setRootActivity(Activity activity) {
        rootActivity = activity;
    }

    /**
     * 设置各种资源的文件夹名称、按钮名称、动画类型
     *
     * @param imgRootSrc 显示图片的文件夹名称
     * @param bgRootSrc  按钮点击效果
     * @param textName   按钮文字
     * @param types      动画类型  eg：0,1,2,3
     */
    public static void setBasicData(String imgRootSrc, @Nullable String bgRootSrc, @Nullable String textName, int types) {
        imgSrc = imgRootSrc;
        type = types;
        bgSrc = bgRootSrc;
        text = textName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yyviewpager);
//        获取各种资源的ID集合
        Intent intent = getIntent();
        btnName = intent.getStringExtra(text);
        ArrayList<String> imgList = intent.getStringArrayListExtra(imgSrc);
        ArrayList<String> bgList = intent.getStringArrayListExtra(bgSrc);
        for (int i = 0; i < imgList.size(); i++) {
            btmIdList.add(getResources().getIdentifier(imgList.get(i), imgSrc, rootActivity.getPackageName()));
        }
        for (int i = 0; i < bgList.size(); i++) {
            bgIdList.add(getResources().getIdentifier(bgList.get(i), bgSrc, rootActivity.getPackageName()));
        }
        initData();
        initView();
        initPoint();

    }

    private void initData() {
        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return btmIdList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                ImageView image = new ImageView(NewSplash.this);
                image.setBackgroundResource(btmIdList.get(position));
                container.addView(image);
                return image;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        };
    }

    private void initView() {
        mViewpager = (ViewPager) findViewById(R.id.yyview_viewpager);
        mPointsContant = (LinearLayout) findViewById(R.id.yyview_ll);
        mLightPoint = (ImageView) findViewById(R.id.yyview_iv);
        mBtn = (Button) findViewById(R.id.yyview_btn);
        initBtn();
        mViewpager.setAdapter(pagerAdapter);
        mViewpager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                setAnimal(type, page, position);
            }
        });
        //当 mPointsContant 绘制后  测量每个点的距离
        mPointsContant.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (btmIdList.size() == 0) {
                    pointDistance = 0;
                } else {
                    pointDistance = mPointsContant.getChildAt(1).getLeft() - mPointsContant.getChildAt(0).getLeft();
                }
                mPointsContant.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                float moveDistance = pointDistance * (position + positionOffset);
                RelativeLayout.LayoutParams relaParams = (RelativeLayout.LayoutParams) mLightPoint.getLayoutParams();
                relaParams.leftMargin = (int) moveDistance;
                mLightPoint.setLayoutParams(relaParams);
                if (position == btmIdList.size() - 1) {
                    mBtn.setVisibility(View.VISIBLE);
                } else {
                    mBtn.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //完成splash
                firstState = true;
                finish();
            }
        });

    }

    private void initBtn() {
        StateListDrawable s = new StateListDrawable();
        if (bgIdList.get(0) == null) {
            Drawable drawable = getResources().getDrawable(R.drawable.shape_btn_normal);
            s.addState(new int[]{-android.R.attr.state_pressed}, drawable);
        } else {
            s.addState(new int[]{-android.R.attr.state_pressed}, getResources().getDrawable(bgIdList.get(0)));
        }
        if (bgIdList.get(1) == null) {
            Drawable drawable1 = getResources().getDrawable(R.drawable.shape_btn_pressed);
            s.addState(new int[]{android.R.attr.state_pressed}, drawable1);
        } else {
            s.addState(new int[]{android.R.attr.state_pressed}, getResources().getDrawable(bgIdList.get(1)));
        }
        if (!TextUtils.isEmpty(text)) {
            mBtn.setText(btnName);
        }

        mBtn.setBackgroundDrawable(s);

    }

    private void setAnimal(int type, View page, float position) {
        switch (type) {
            case 1:
                if (position <= 0) {
                    //当前页面
                    page.setAlpha(1 + position);
                } else if (position <= 1) {
                    //下一个页面
                    page.setAlpha(1 - position);
                    page.setTranslationX(page.getWidth() * -position);
                    page.setScaleX(1 - position);
                    page.setScaleY(1 - position);
                }
                break;
            case 2:
                if (position <= 0) {
                    //当前页面
                    page.setAlpha(1 + position);
                } else if (position <= 1) {
                    //下一个页面
                    page.setAlpha(1 - position);
                    page.setTranslationX(page.getWidth() * -position);
                }
                break;
            case 3:
                if (position <= 0) {
                    //当前页面
                    page.setRotation(-Math.abs(position) * MAXTOTATION);
                    page.setPivotX(page.getWidth() / 2);
                    page.setPivotY(page.getHeight());
                } else if (position <= 1) {
                    //下一个页面
                    page.setRotation(Math.abs(position) * MAXTOTATION);
                }
                break;
            default:
                break;
        }

    }

    private void initPoint() {
        pointList = new ArrayList<>();
        LinearLayout.LayoutParams contentLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentLayoutParams.setMargins(0, 0, 40, 0);
        for (int i = 0; i < btmIdList.size(); i++) {
            ImageView iv = new ImageView(NewSplash.this);
            iv.setImageResource(R.drawable.shape_point_gray);
            if (i != btmIdList.size() - 1) {
                mPointsContant.addView(iv, contentLayoutParams);
            } else {
                mPointsContant.addView(iv);
            }
            pointList.add(iv);
        }
    }

    @Override
    public void onBackPressed() {
//        在此页面点击返回按钮，则强制退出app
        exit();
    }

    private long exitTime;

    public void exit() {
        //        moveTaskToBack(true);
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            rootActivity.finish();
            System.exit(0);//全部退出
        }
    }

}
