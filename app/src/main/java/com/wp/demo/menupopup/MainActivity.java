package com.wp.demo.menupopup;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    PopupWindow mPopupWindow;
    MyAdapter mAdapter;
    PopMenuAdapter mPopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        final ContentResolver contentResolver = getContentResolver();
        mAdapter = new MyAdapter(this, null);
        mPopAdapter = new PopMenuAdapter(this);

        if (null != actionBar) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            actionBar.getCustomView().
                    findViewById(R.id.action_bar_more_btn).setOnClickListener(this);
            ((TextView) actionBar.getCustomView().
                    findViewById(R.id.action_bar_title_tv)).setText(getString(R.string.app_name));
        }

        View popView = LayoutInflater.from(this).inflate(R.layout.popup_main_layout, null);
        popView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (null != mPopupWindow && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        ListView popLv = (ListView) popView.findViewById(R.id.lv);
        final List<String> items = new ArrayList<String>();
        items.add("Sort by time");
        items.add("Settings");
        items.add("Feedback");
        items.add("About");
        popLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (items.size() > position) {
                    if (null != mPopupWindow && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    final String itemId = items.get(position);
                    if (TextUtils.equals(itemId, "Sort by time")) {
                        Cursor cursor = contentResolver.query(Message.URI_MESSAGE,
                                null, null, null, Message.TIME + " desc");
                        notifyCursorChanged(cursor);
                    } else if (TextUtils.equals(itemId, "Settings")) {
                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.equals(itemId, "Feedback")) {
                        Toast.makeText(MainActivity.this, "Feedback", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.equals(itemId, "About")) {
                        Toast.makeText(MainActivity.this, "About", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        popLv.setAdapter(mPopAdapter);
        mPopAdapter.update(items);

        mPopupWindow = new PopupWindow(popView, 600, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setAnimationStyle(R.style.showPopupAnimation);

        boolean insert = true;
        try {
            Cursor cursor = contentResolver.query(Message.URI_MESSAGE, null, null, null, null);
            if (null != cursor && cursor.moveToFirst()) {
                insert = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (insert) {
            for (int i = 0; i < 10; i++) {
                final String msg = "Test message [" + i + "]";
                final String time = System.currentTimeMillis() + i * 1000 * 60 * 60 * 24 + "";
                final String miniType = "Mini [" + i + "] Type";

                final Message message = new Message(msg, time, miniType);
                contentResolver.insert(Message.URI_MESSAGE, message.toValues());
            }
        }

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(mAdapter);

        Cursor cursor = contentResolver.query(Message.URI_MESSAGE, null, null, null, null);
        notifyCursorChanged(cursor);
    }

    private void notifyCursorChanged(final Cursor cursor) {
        new MyHandler(MainActivity.this, cursor).sendEmptyMessage(0);
    }

    private final static class MyHandler extends Handler {
        final MainActivity mActivity;
        final Cursor cursor;

        public MyHandler(MainActivity activity, Cursor cursor) {
            WeakReference<MainActivity>
                    activities = new WeakReference<MainActivity>(activity);
            mActivity = activities.get();
            this.cursor = cursor;
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (null != mActivity) {
                mActivity.mAdapter.changeCursor(cursor);
                mActivity.mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.action_bar_more_btn) {
            if (mPopupWindow != null) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                } else {
                    mPopupWindow.showAsDropDown(v, Gravity.BOTTOM, 0, 0);
                }
            }
        }
    }

    class MyAdapter extends CursorAdapter {

        Context mContext;

        public MyAdapter(Context context, Cursor c) {
            super(context, c);
            mContext = context;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false);

            holder.title = (TextView) view.findViewById(R.id.title);
            holder.miniType = (TextView) view.findViewById(R.id.mini_type);
            holder.time = (TextView) view.findViewById(R.id.time);

            view.setTag(holder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder = (ViewHolder) view.getTag();
            final String title = cursor.getString(cursor.getColumnIndex(Message.MESSAGE_DATA));
            final String miniType = cursor.getString(cursor.getColumnIndex(Message.MINI_TYPE));
            final String utcTime = cursor.getString(cursor.getColumnIndex(Message.TIME));
            try {
                long lt = Long.parseLong(utcTime);
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月-dd日");
                final String time = simpleDateFormat.format(lt);
                holder.time.setText(time);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.title.setText(title);
            holder.miniType.setText(miniType);
        }

        class ViewHolder {
            TextView title;
            TextView miniType;
            TextView time;
        }
    }

    class PopMenuAdapter extends BaseAdapter {
        List<String> items = new ArrayList<>();
        Context ctx;

        public PopMenuAdapter(Context ctx) {
            this.ctx = ctx;
        }

        public void update(List<String> dates) {
            items.clear();
            items.addAll(dates);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null != convertView && null != convertView.getTag()) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(ctx).inflate(R.layout.pop_menu_item_layout, null);
                holder.tv = (TextView) convertView.findViewById(R.id.pop_tv);
                holder.iv = (ImageView) convertView.findViewById(R.id.image);
            }

            final String itemTitle = items.get(position);
            holder.tv.setText(itemTitle);
            return convertView;
        }

        class ViewHolder {
            TextView tv;
            ImageView iv;
        }
    }
}
