# NewViewPager
Splash库，简单设置显示图片和文字，就可以实现Splash页面，支持多种切换页面模式

Step 1. Add the JitPack repository to your build file

gradle
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	        compile 'com.github.Tomuchy:NewViewPager:V2.0'
	}

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

    private void initSplash() {
        //用于或许关联moudle的资源文件   必须设置  否则在splash界面 点击返回键时，会出现异常。
        NewSplash.setRootActivity(MainActivity.this);
        //设置动画模式  默认值 0 为默认动画
        NewSplash.setBasicData("mipmap", "drawable", "textName", 1);
        String textName = "ssss";
        ArrayList<String> imgList = new ArrayList<>();
        imgList.add("indicator1");
        imgList.add("indicator2");
        imgList.add("indicator3");
        ArrayList<String> bgList = new ArrayList<>();
        bgList.add("shape_btn_normal");
        bgList.add("shape_btn_pressed");
        Intent intent = new Intent();
        intent.putStringArrayListExtra("mipmap",imgList);
        intent.putStringArrayListExtra("drawable",bgList);
        intent.putExtra("textName", textName);
        intent.setClass(this, NewSplash.class);
        startActivity(intent);
    }

}
