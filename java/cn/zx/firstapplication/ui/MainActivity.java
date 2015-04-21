package cn.zx.firstapplication.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.zx.firstapplication.R;
import cn.zx.firstapplication.widget.ImageTextButton;

public class MainActivity extends FragmentActivity {
    private List<Fragment> fragmentList = new ArrayList<>();
    private ViewPager viewPager;
    private ImageTextButton[] itbs = new ImageTextButton[4];
    private int[] imageIds = new int[4];
    private int[] imageSelectedIds = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentList.add(new FragmentChat());
        fragmentList.add(new FragmentContacts());
        fragmentList.add(new FragmentExplore());
        fragmentList.add(new FragmentMe());

        imageIds[0] = R.drawable.footer_chats;
        imageIds[1] = R.drawable.footer_contacts;
        imageIds[2] = R.drawable.footer_explore;
        imageIds[3] = R.drawable.footer_me;

        imageSelectedIds[0] = R.drawable.footer_chats_selected;
        imageSelectedIds[1] = R.drawable.footer_contacts_selected;
        imageSelectedIds[2] = R.drawable.footer_explore_selected;
        imageSelectedIds[3] = R.drawable.footer_me_selected;

        viewPager = (ViewPager) findViewById(R.id.vp);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                System.out.println("onPageScrolled --> pos=" + position + " offset=" + positionOffset + " pixels=" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
//                System.out.println("onPageSelected --> pos=" + position);
                for (int i = 0; i < 4; i++) {
                    itbs[i].setImgResource(imageIds[i]);
                    itbs[i].setTextColor(getResources().getColor(R.color.shadow));
                }
                itbs[position].setImgResource(imageSelectedIds[position]);
                itbs[position].setTextColor(getResources().getColor(R.color.green));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                System.out.println("onPageScrollStateChanged --> state=" + state);
            }
        });

        itbs[0] = (ImageTextButton) findViewById(R.id.itb1);
        itbs[1] = (ImageTextButton) findViewById(R.id.itb2);
        itbs[2] = (ImageTextButton) findViewById(R.id.itb3);
        itbs[3] = (ImageTextButton) findViewById(R.id.itb4);

        for (int i = 0; i < 4; i++) {
            itbs[i].setOnClickListener(new MyOnClickListener(i));
        }

        new MyScroller(this).initViewPagerScroll(viewPager);
    }

    public class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View view) {
            viewPager.setCurrentItem(index);
        }
    }

    // 控制滑动速度
    public class MyScroller extends Scroller {
        private int mDuration = 500;

        public MyScroller(Context context) {
            super(context);
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public MyScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void initViewPagerScroll(ViewPager viewPager) {
            try {
                Field mScroller = ViewPager.class.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                mScroller.set(viewPager, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}