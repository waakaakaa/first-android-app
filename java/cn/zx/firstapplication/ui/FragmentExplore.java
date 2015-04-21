package cn.zx.firstapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.zx.firstapplication.R;

/**
 * Created by apple on 15-4-2.
 */
public class FragmentExplore extends Fragment {
    private LinearLayout llMoments;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);

        llMoments = (LinearLayout) rootView.findViewById(R.id.ll_explore_moments);
        llMoments.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        llMoments.setBackgroundColor(getResources().getColor(R.color.quite_dark));
                        break;
                    case MotionEvent.ACTION_UP:
                        llMoments.setBackgroundColor(getResources().getColor(R.color.tiny_dark));
                        Intent intent = new Intent(getActivity(), MomentsActivity.class);
                        startActivity(intent);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        llMoments.setBackgroundColor(getResources().getColor(R.color.tiny_dark));
                        break;
                }
                return true;
            }
        });

        return rootView;
    }
}
