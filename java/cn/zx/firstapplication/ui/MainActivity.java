package cn.zx.firstapplication.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.zx.firstapplication.R;
import cn.zx.firstapplication.widget.ImageTextButton;

public class MainActivity extends FragmentActivity {
    private List<Fragment> fragmentList = new ArrayList<>();
    private ViewPager viewPager;
    private ImageTextButton[] imageTextButtons = new ImageTextButton[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentList.add(new FragmentChat());
        fragmentList.add(new FragmentContacts());
        fragmentList.add(new FragmentExplore());
        fragmentList.add(new FragmentMe());

        viewPager = (ViewPager) findViewById(R.id.vp);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                System.out.println("onPageScrolled --> pos=" + position + " offset=" + positionOffset + " pixels=" + positionOffsetPixels);

                imageTextButtons[position].updateAlpha((int) (255 * (1 - positionOffset)));
                if (position < viewPager.getChildCount()) {
                    imageTextButtons[position + 1].updateAlpha((int) (255 * positionOffset));
                }
                if(positionOffset==0){
                    select(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
//                System.out.println("onPageSelected --> pos=" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                System.out.println("onPageScrollStateChanged --> state=" + state);
            }
        });

        imageTextButtons[0] = (ImageTextButton) findViewById(R.id.itb1);
        imageTextButtons[1] = (ImageTextButton) findViewById(R.id.itb2);
        imageTextButtons[2] = (ImageTextButton) findViewById(R.id.itb3);
        imageTextButtons[3] = (ImageTextButton) findViewById(R.id.itb4);

        for (int i = 0; i < 4; i++) {
            imageTextButtons[i].setOnClickListener(new MyOnClickListener(i));
        }

        select(0);
    }

    private void select(int position) {
        for (int i = 0; i < 4; i++) {
            imageTextButtons[i].updateAlpha(0);
        }
        imageTextButtons[position].updateAlpha(255);
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
            viewPager.setCurrentItem(index, false);
        }
    }
}