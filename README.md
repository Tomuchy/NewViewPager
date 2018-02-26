# NewViewPager
Splash库，简单设置显示图片和文字，就可以实现Splash页面，支持多种切换页面模式



eg：
实例代码，在首个Activity里添加如下代码


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //firstState == false 为app未执行过splash  点击splash里的按钮后  firstState=true
        if (!NewSplash.firstState) {
            initSplash();
        }
        setContentView(R.layout.activity_main);
    }

    private ArrayList<Bitmap> bmps = new ArrayList<>();

    private void initSplash() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.indicator1);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.indicator2);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.indicator3);
        bmps.add(bitmap);
        bmps.add(bitmap1);
        bmps.add(bitmap2);
        //必须设置  否则在splash界面 点击返回键时，会出现异常
        NewSplash.setRootActivity(MainActivity.this);
        //设置图片
        NewSplash.setShowData(bmps);

        Drawable normal = getResources().getDrawable(R.drawable.shape_btn_normal);
        Drawable pressed = getResources().getDrawable(R.drawable.shape_btn_pressed);
        //设置最后按钮样式和文字
        /**
         * 设置按钮样式
         *
         * @param normal  正常状态样式
         * @param pressed 点击状态样式
         * @param text1   显示文字
         */
        NewSplash.setBtnBackground(normal, pressed, "tuichu");

        //设置动画模式  默认值 0 为默认动画
        NewSplash.setAnimalStype(3);
        Intent intent = new Intent();
        intent.setClass(this, NewSplash.class);
        startActivity(intent);
    }

}
