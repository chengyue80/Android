package com.yue.demo.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yue.demo.R;

/**
 * 与 org.yue.mytest.activity_fragment.ExpandableListActivityTest 比较
 * 
 * @author chengyue
 * 
 */
public class ExpandableListViewTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandablelist);

        ExpandableListAdapter adapter = new ExpandableListAdapter() {

            int[] logs = new int[] { R.drawable.p, R.drawable.z, R.drawable.t };

            private String[] armTypes = new String[] { "神族兵种", "虫族兵种", "人族兵种" };
            private String[][] arms = new String[][] {
                    { "狂战士", "龙骑士", "黑暗圣堂", "电兵" },
                    { "小狗", "刺蛇", "飞龙", "自爆飞机" }, { "机枪兵", "护士MM", "幽灵" } };

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void onGroupExpanded(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        "被展开的列为 : " + groupPosition, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onGroupCollapsed(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        "被收缩的列为 : " + groupPosition, Toast.LENGTH_SHORT).show();

            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean isChildSelectable(int groupPosition,
                    int childPosition) {
                return true;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded,
                    View convertView, ViewGroup parent) {
                LinearLayout ll = new LinearLayout(ExpandableListViewTest.this);
                ll.setOrientation(0);
                ll.setGravity(Gravity.CENTER_VERTICAL);
                ImageView logo = new ImageView(ExpandableListViewTest.this);
                logo.setImageResource(logs[groupPosition]);
                ll.addView(logo);
                TextView textView = getTextView();
                textView.setText(getGroup(groupPosition).toString());
                ll.addView(textView);
                return ll;
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public int getGroupCount() {
                return armTypes.length;
            }

            // 获取指定组位置处的组数据
            @Override
            public Object getGroup(int groupPosition) {
                return armTypes[groupPosition];
            }

            @Override
            public long getCombinedGroupId(long groupId) {
                return 0;
            }

            @Override
            public long getCombinedChildId(long groupId, long childId) {
                return 0;
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return arms[groupPosition].length;
            }

            // 该方法决定每个子选项的外观
            @SuppressLint("NewApi")
            @Override
            public View getChildView(int groupPosition, int childPosition,
                    boolean isLastChild, View convertView, ViewGroup parent) {
                // TODO
                LinearLayout ll = new LinearLayout(getApplicationContext());
                ll.setOrientation(0);
                ll.setGravity(Gravity.CENTER_VERTICAL);
                ImageView imageView = new ImageView(getApplicationContext());
                // imageView.setBackgroundResource(R.drawable.ic_launcher);
                imageView.setImageResource(R.drawable.ic_launcher);
                imageView.setPadding(60, 0, 0, 0);
                // imageView.setLayoutParams(new LayoutParams(48, 48));
                ll.addView(imageView);
                TextView textView = getTextView();
                textView.setText(arms[groupPosition][childPosition].toString());
                ll.addView(textView);
                return ll;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            // 获取指定组位置、指定子列表项处的子列表项数据
            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return arms[groupPosition][childPosition];
            }

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            private TextView getTextView() {
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 64);
                TextView textView = new TextView(ExpandableListViewTest.this);
                textView.setLayoutParams(lp);
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                textView.setPadding(36, 0, 0, 0);
                textView.setTextSize(20);
                return textView;
            }
        };
        ExpandableListView expandListView = (ExpandableListView) findViewById(R.id.expand_list);

        expandListView.setChildIndicator(null);
        expandListView.setAdapter(adapter);

    }

}
