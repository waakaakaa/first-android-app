package cn.zx.firstapplication.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.zx.firstapplication.R;
import cn.zx.firstapplication.util.ContactUtil;

/**
 * Created by apple on 15-4-2.
 */
public class FragmentContacts extends Fragment {
    private ContactUtil contactUtil = new ContactUtil();
    private Map<Character, List<String>> map = contactUtil.processNames(contactUtil.getNames());
    private RelativeLayout rr;
    private LinearLayout ll;
    private ScrollView sv;
    private TextView tvContactShortcut;
    private TextViewLetter tvl;
    private Item item;
    private Divider divider;
    private String[] shortcuts = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private Rect rect = new Rect();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        rr = (RelativeLayout) rootView.findViewById(R.id.rr_contact);
        ll = (LinearLayout) rootView.findViewById(R.id.ll_contact);
        tvContactShortcut = (TextView) rootView.findViewById(R.id.tv_contact_shortcut);
        sv = (ScrollView) rootView.findViewById(R.id.sv_contacts);

        /**
         * main panel
         */
        int count = 0;
        Set<Character> set = map.keySet();
        for (Character c : set) {
            List<String> list = map.get(c);
            tvl = new TextViewLetter(getActivity(), c.toString());
            ll.addView(tvl);
            for (int i = 0; i < list.size(); i++) {
                item = new Item(getActivity(), list.get(i));
                ll.addView(item);
                if (i < list.size() - 1) {
                    divider = new Divider(getActivity());
                    ll.addView(divider);
                }
            }
            count += list.size();
        }
        TextViewEnd tve = new TextViewEnd(getActivity(), count + " contacts");
        ll.addView(tve);

        /**
         * shortcut panel
         */
        final LinearLayout llShortcut = (LinearLayout) rootView.findViewById(R.id.ll_contact_shortcut);
        for (String s : shortcuts) {
            llShortcut.addView(new ContactShortcutItem(getActivity(), s));
        }
        llShortcut.post(new Runnable() {
            @Override
            public void run() {
                llShortcut.getLocalVisibleRect(rect);
                final int top = rect.top;
                final int bottom = rect.bottom;

                final int heightOfTVL = tvl.getHeight();
                final int heightOfItem = item.getHeight();
                final int heightOfDivider = divider.getHeight();

                llShortcut.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        float y = motionEvent.getY();
                        int i = -1;
                        if (y > top && y < bottom) {
                            while (y > top) {
                                y -= (bottom - top) / shortcuts.length;
                                i++;
                            }
                            if (i >= shortcuts.length) {
                                i = shortcuts.length - 1;
                            }
                            tvContactShortcut.setText(shortcuts[i]);

                            int offset = 0;
                            Set<Character> set = map.keySet();
                            for (Character c : set) {
                                if (shortcuts[i].equals(String.valueOf(c))) {
                                    break;
                                }
                                if(shortcuts[i].charAt(0)<c){
                                    break;
                                }
                                offset += heightOfTVL;
                                offset += heightOfItem * map.get(c).size() + heightOfDivider * (map.get(c).size() - 1);
                            }
                            sv.scrollTo(0,offset);
                        }
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                rr.setVisibility(View.VISIBLE);
                                llShortcut.setBackgroundColor(getResources().getColor(R.color.quite_dark));
                                break;
                            case MotionEvent.ACTION_UP:
                                rr.setVisibility(View.GONE);
                                llShortcut.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                                break;
                            case MotionEvent.ACTION_MOVE:
                                break;
                        }
                        return true;
                    }
                });
            }
        });


        //done
        return rootView;
    }


    public class TextViewLetter extends LinearLayout {
        public TextViewLetter(Context context, String text) {
            super(context);
            LayoutInflater.from(context).inflate(R.layout.view_contact_letter, this, true);
            TextView tv = (TextView) findViewById(R.id.tv_contact_letter);
            tv.setText(text);
        }
    }

    public class Item extends LinearLayout implements View.OnTouchListener {
        private String text;

        public Item(Context context, String text) {
            super(context);
            LayoutInflater.from(context).inflate(R.layout.view_contact_item, this, true);
            TextView tv = (TextView) findViewById(R.id.tv_contact_item);
            this.text = text;
            tv.setText(text);
            setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view.setBackgroundColor(getResources().getColor(R.color.little_dark));
                    break;
                case MotionEvent.ACTION_UP:
                    view.setBackgroundColor(getResources().getColor(R.color.tiny_dark));
                    Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_MOVE:
                    view.setBackgroundColor(getResources().getColor(R.color.tiny_dark));
                    break;
            }
            return true;
        }
    }

    public class Divider extends LinearLayout {
        public Divider(Context context) {
            super(context);
            LayoutInflater.from(context).inflate(R.layout.view_contact_devider, this, true);
        }
    }

    public class TextViewEnd extends LinearLayout {
        public TextViewEnd(Context context, String text) {
            super(context);
            LayoutInflater.from(context).inflate(R.layout.view_contact_end, this, true);
            TextView tv = (TextView) findViewById(R.id.tv_contact_end);
            tv.setText(text);
        }
    }

    public class ContactShortcutItem extends TextView {
        private String c;

        public ContactShortcutItem(Context context, String c) {
            super(context);
            this.c = c;
            setText(c);
            setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        }
    }


}