package cn.zx.firstapplication.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import cn.zx.firstapplication.R;

public class MomentsActivity extends Activity {
    private ListView lv;
    private LinearLayout llHeader;
    private View vCover;
    private RelativeLayout rr;
    private ImageView ivCircle;

    private boolean isRefreshing;
    private boolean firstRefresh = true;
    private boolean isTop;
    private int lvY;
    private boolean dragging = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg2 == ANIM_TO_TOP) {
                scrollTo(msg.arg1);
            } else if (msg.arg2 == CIRCLE_ROTATE) {
                rotateCircle(msg.arg1);
            } else if (msg.arg2 == CIRCLE_MOVE) {
                moveCircleTo(msg.arg1);
            } else if (msg.arg2 == CIRCLE_ROTATE_END) {
                moveCircle();
            } else if (msg.arg2 == CIRCLE_MOVE_END) {
                scrollTo(0);
                isRefreshing = false;
                firstRefresh = false;
            }
        }
    };

    private int ANIM_TO_TOP = 0;
    private int CIRCLE_ROTATE = 1;
    private int CIRCLE_MOVE = 2;
    private int CIRCLE_ROTATE_END = 3;
    private int CIRCLE_MOVE_END = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);

        lv = (ListView) findViewById(R.id.lv_moments);
        llHeader = (LinearLayout) findViewById(R.id.ll_moments_header);
        vCover = findViewById(R.id.v_moments_cover);
        rr = (RelativeLayout) findViewById(R.id.rr_moments);
        ivCircle = (ImageView) findViewById(R.id.iv_moments_circle);


        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}));
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lv.getCount() > 0) {
                    if (firstVisibleItem == 0) {
                        View firstItem = lv.getChildAt(0);
                        if (null != firstItem) {
                            isTop = firstItem.getTop() == 0;
                        }
                    } else {
                        isTop = false;
                    }
                } else {
                    isTop = true;
                }
            }
        });
        lv.setOnTouchListener(new View.OnTouchListener() {
            private int lastY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isRefreshing) {
                    return false;
                }

                int currentY = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (dragging && lv.getTop() > vCover.getMeasuredHeight() + llHeader.getMeasuredHeight()) {
//                            animToTop();
                            refresh();
                            lvY = 0;
                        }
                        dragging = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        boolean isToBottom = currentY - lastY >= 0;
                        int step = Math.abs(currentY - lastY);
                        lastY = currentY;
                        // 向下划
                        if (isToBottom && isTop) {
                            // 可以看见顶部
                            if (lv.getTop() >= vCover.getMeasuredHeight() + llHeader.getMeasuredHeight()) {
                                dragging = true;
                                lvY += step;
                                scrollTo(lvY);
                            }
                        }
                        //向上划
                        else if (!isToBottom) {
                            // 顶部在下面
                            if (lv.getTop() > vCover.getMeasuredHeight() + llHeader.getMeasuredHeight()) {
                                lvY -= step;
                                scrollTo(lvY);
                            }
                            // 顶部归位
                            else {
                                if (dragging) {
                                    scrollTo(0);
                                }
                            }
                        }
                        break;
                }
                return false;
            }
        });

        refresh();
    }

    public void scrollTo(int y) {
        lv.layout(lv.getLeft(), vCover.getMeasuredHeight() + llHeader.getMeasuredHeight() + y, lv.getRight(), lv.getMeasuredHeight() + vCover.getMeasuredHeight() + llHeader.getMeasuredHeight() + y);
        vCover.layout(vCover.getLeft(), llHeader.getMeasuredHeight() + y, vCover.getRight(), llHeader.getMeasuredHeight() + vCover.getMeasuredHeight() + y);
        rr.layout(rr.getLeft(), y, rr.getRight(), y + rr.getMeasuredHeight());
    }

    public void animToTop() {
        long duration = 3000;
        long steps = 100;
        int y = lvY;
        for (int i = 0; i < steps; i++) {
            Message msg = new Message();
            msg.arg1 = (int) (y - (double) y / (double) steps * (double) i);
            msg.arg2 = ANIM_TO_TOP;
            handler.sendMessageDelayed(msg, i * duration / steps);
        }
        Message msg = new Message();
        msg.arg1 = 0;
        msg.arg2 = ANIM_TO_TOP;
        handler.sendMessageDelayed(msg, duration);
    }

    public void rotateCircle(int angle) {
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.moments_circle)).getBitmap();
        // 变换矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // ┏ (゜ω゜)=☞
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        ivCircle.setImageBitmap(bitmap2);
    }

    public void refresh() {
        if (!firstRefresh) {
            moveCircleTo(ivCircle.getMeasuredHeight());
        }
        isRefreshing = true;
        long duration = 2000;
        long steps = 50;
        double degrees = 360 * 2;
        for (int i = 0; i < steps; i++) {
            Message msg = new Message();
            msg.arg1 = (int) (i * degrees / steps);
            msg.arg2 = CIRCLE_ROTATE;
            handler.sendMessageDelayed(msg, i * duration / steps);
        }
        Message msg = new Message();
        msg.arg2 = CIRCLE_ROTATE_END;
        handler.sendMessageDelayed(msg, duration);
    }

    public void moveCircle() {
        long duration = 1000;
        long steps = 50;
        double distance = ivCircle.getMeasuredHeight();
        for (int i = 0; i < steps; i++) {
            Message msg = new Message();
            msg.arg1 = (int) (llHeader.getMeasuredHeight() - i * distance / steps);
            msg.arg2 = CIRCLE_MOVE;
            handler.sendMessageDelayed(msg, i * duration / steps);
        }
        Message msg = new Message();
        msg.arg2 = CIRCLE_MOVE_END;
        handler.sendMessageDelayed(msg, duration);
    }

    public void moveCircleTo(int y) {
        ivCircle.layout(ivCircle.getLeft(), y, ivCircle.getRight(), y + ivCircle.getMeasuredHeight());
    }
}