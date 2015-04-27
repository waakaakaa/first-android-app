package cn.zx.firstapplication.widget;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zx.firstapplication.R;

/**
 * Created by apple on 15-4-1.
 */
public class ImageTextButton extends RelativeLayout {
    private ImageView ivNormal;
    private ImageView ivSelected;
    private TextView tvNormal;
    private int colorNormal;
    private int colorSelected;
    private ArgbEvaluator argbEvaluator;

    public ImageTextButton(Context context) {
        super(context, null);
    }

    public ImageTextButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_image_text_button, this, true);
        ivNormal = (ImageView) findViewById(R.id.iv_normal);
        ivSelected = (ImageView) findViewById(R.id.iv_selected);
        tvNormal = (TextView) findViewById(R.id.tv_normal);
        argbEvaluator = new ArgbEvaluator();
        setClickable(true);
        setFocusable(true);


        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.widget);
        int imgNormalId = a.getResourceId(R.styleable.widget_img_normal, -1);
        int imgSelectedId = a.getResourceId(R.styleable.widget_img_selected, -1);
        String s = a.getString(R.styleable.widget_text);
        colorNormal = a.getColor(R.styleable.widget_textColor_normal, -1);
        colorSelected = a.getColor(R.styleable.widget_textColor_selected, -1);
        float size = a.getDimension(R.styleable.widget_textSize, -1.0f);

        ivNormal.setImageResource(imgNormalId);
        ivSelected.setImageResource(imgSelectedId);
        tvNormal.setText(s);
        //tvNormal.setTextColor(colorNormal);
        tvNormal.setTextSize(size);

        a.recycle();

    }

    public void updateAlpha(int alpha) {
        ivNormal.setImageAlpha(255 - alpha);
        ivSelected.setImageAlpha(alpha);
        tvNormal.setTextColor((int) (argbEvaluator.evaluate((float) alpha / 255.0f, colorNormal, colorSelected)));
        invalidate();
    }
}
