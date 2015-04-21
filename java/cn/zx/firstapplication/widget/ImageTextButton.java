package cn.zx.firstapplication.widget;

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
    private ImageView img;
    private TextView tv;

    public ImageTextButton(Context context) {
        super(context, null);
    }

    public ImageTextButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_image_text_button, this, true);
        img = (ImageView) findViewById(R.id.img);
        tv = (TextView) findViewById(R.id.tv);
        setClickable(true);
        setFocusable(true);


        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.ITButton);
        int imgId = a.getResourceId(R.styleable.ITButton_img, -1);
        String s = a.getString(R.styleable.ITButton_text);
        int color = a.getColor(R.styleable.ITButton_textColor, -1);
        float size = a.getDimension(R.styleable.ITButton_textSize, -1.0f);

        setImgResource(imgId);
        setText(s);
        setTextColor(color);
        setTextSize(size);

        a.recycle();

    }

    public void setImgResource(int id) {
        img.setImageResource(id);
    }

    public void setText(String s) {
        tv.setText(s);
    }

    public void setTextColor(int color) {
        tv.setTextColor(color);
    }

    public void setTextSize(float size) {
        tv.setTextSize(size);
    }
}
