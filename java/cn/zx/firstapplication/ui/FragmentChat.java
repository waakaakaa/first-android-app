package cn.zx.firstapplication.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import cn.zx.firstapplication.R;
import cn.zx.firstapplication.widget.EyeView;
import cn.zx.firstapplication.widget.PullDownListView;

/**
 * Created by apple on 15-4-2.
 */
public class FragmentChat extends Fragment {

    private PullDownListView pullDownListView;
    private EyeView eyeView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        pullDownListView = (PullDownListView) rootView.findViewById(R.id.pullDownListView);
        eyeView = (EyeView) rootView.findViewById(R.id.eyeView);

        pullDownListView.getListView().setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}));
        pullDownListView.setOnPullHeightChangeListener(new PullDownListView.OnPullHeightChangeListener() {
            @Override
            public void onTopHeightChange(int headerHeight, int pullHeight) {
                float progress = (float) pullHeight / (float) headerHeight;
                if (progress < 0.5) {
                    progress = 0.0f;
                } else {
                    progress = (progress - 0.5f) / 0.5f;
                }
                if (progress > 1.0f) {
                    progress = 1.0f;
                }
                if (!pullDownListView.isRefreshing()) {
                    eyeView.setProgress(progress);
                }
            }

            @Override
            public void onRefreshing(final boolean isTop) {
                if (isTop) {
                    eyeView.startAnimate();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullDownListView.pullUp();
                        if (isTop) {
                            eyeView.stopAnimate();
                        }
                    }
                }, 3000);
            }
        });

        return rootView;
    }
}