package com.newlive.yyviewpager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

    private static String packetName;
    public static boolean firstState = false;
    private ViewPager mViewpager;
    private PagerAdapter pagerAdapter;
    private final float MINSCALE = 0.75f;
    static ArrayList<Bitmap> bitmaps = new ArrayList<>();
    static int type = 0;
    private final int MAXTOTATION = 20;
    private LinearLayout mPointsContant;
    private ArrayList<ImageView> pointList;
    private int pointDistance;
    private ImageView mLightPoint;
    private Button mBtn;
    private ArrayList<String> bmps;
    public static Drawable btnNormal;
    public static Drawable btnPressed;
    public static String text;
    public static Activity rootActivity;

    public static void setShowData(ArrayList<Bitmap> bmps) {
        bitmaps = bmps;
    }

    public static void setAnimalStype(int types) {
        type = types;
    }

    /**
     * 设置按钮样式
     *
     * @param normal  正常状态样式
     * @param pressed 点击状态样式
     * @param text1   显示文字
     */
    public static void setBtnBackground(@Nullable Drawable normal, @Nullable Drawable pressed, @Nullable String text1) {
        btnNormal = normal;
        btnPressed = pressed;
        text = text1;
    }

    public static void setRootActivity(Activity activity) {
        rootActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yyviewpager);
//        Intent intent = getIntent();
//        bmps = intent.getStringArrayListExtra("bmps");

        initData();
        initView();
        initPoint();
    }

    private void initData() {
        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return bitmaps.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView image = new ImageView(NewSplash.this);
                Drawable bitmapDrawable = new BitmapDrawable(bitmaps.get(position));
                image.setImageBitmap(bitmaps.get(position));
//                image.setImageResource(Integer.parseInt(packetName + bmps.get(position)));
                image.setBackground(bitmapDrawable);
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
                if (bitmaps.size() == 0) {
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
                if (position == bitmaps.size() - 1) {
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
        if (btnNormal == null) {
            Drawable drawable = getResources().getDrawable(R.drawable.shape_btn_normal);
            s.addState(new int[]{-android.R.attr.state_pressed}, drawable);
        } else {
            s.addState(new int[]{-android.R.attr.state_pressed}, btnNormal);
        }
        if (btnPressed == null) {
            Drawable drawable1 = getResources().getDrawable(R.drawable.shape_btn_pressed);
            s.addState(new int[]{android.R.attr.state_pressed}, drawable1);
        } else {
            s.addState(new int[]{android.R.attr.state_pressed}, btnPressed);
        }
        if (!TextUtils.isEmpty(text)) {
            mBtn.setText(text);
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
        for (int i = 0; i < bitmaps.size(); i++) {
            ImageView iv = new ImageView(NewSplash.this);
            iv.setImageResource(R.drawable.shape_point_gray);
            if (i != bitmaps.size() - 1) {
                mPointsContant.addView(iv, contentLayoutParams);
            } else {
                mPointsContant.addView(iv);
            }
            pointList.add(iv);
        }
    }

    public static void setPacketName(String packetName1) {
        packetName = packetName1;

    }

    @Override
    public void onBackPressed() {
//        在此页面点击返回按钮，则强制退出app
        exit();
//        super.onBackPressed();
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
