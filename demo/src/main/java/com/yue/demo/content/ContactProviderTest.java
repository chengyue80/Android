package com.yue.demo.content;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Data;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yue.demo.R;
import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

/**
 * 使用contentprovider管理联系人,联系人需要存储在手机中
 * 
 * @author chengyue
 * 
 */
public class ContactProviderTest extends Activity {

    private final String tag = "ContactProviderTest";

    private ContentResolver resolver = null;

    private Button add, search;
    private EditText et_name, et_phone, et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        resolver = getContentResolver();
        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 获取程序界面中的3个文本框
                String name = et_name.getText().toString();
                String phone = et_phone.getText().toString();
                String email = et_email.getText().toString();
                // 创建一个空的ContentValues
                ContentValues values = new ContentValues();
                // 向RawContacts.CONTENT_URI执行一个空值插入，
                // 目的是获取系统返回的rawContactId
                Uri uri = resolver.insert(RawContacts.CONTENT_URI, values);
                long rawContactId = ContentUris.parseId(uri);
                LogUtil.d(tag, "rawContactId : " + rawContactId);

                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                // 设置内容类型 : vnd.android.cursor.item/contact_directory
                values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);

                // 设置联系人名字 key:data2
                values.put(StructuredName.GIVEN_NAME, name);
                // 向联系人URI添加联系人名字
                LogUtil.d(tag,
                        "联系人URI : "
                                + android.provider.ContactsContract.Data.CONTENT_URI
                                        .toString());
                Uri uri_name = resolver.insert(
                        android.provider.ContactsContract.Data.CONTENT_URI,
                        values);
                if (uri_name == null) {
                    Toast.makeText(getApplicationContext(), "联系人名称不正确",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    LogUtil.d(tag, "uri_name : " + uri_name.toString());
                }

                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
                // 设置联系人的电话号码
                values.put(Phone.NUMBER, phone);
                // 设置电话类型 key : data2
                values.put(Phone.TYPE, Phone.TYPE_MOBILE);
                // 向联系人电话号码URI添加电话号码
                Uri uri_phone = resolver.insert(
                        android.provider.ContactsContract.Data.CONTENT_URI,
                        values);
                if (uri_phone == null) {
                    Toast.makeText(getApplicationContext(), "电话号码格式不正确",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    LogUtil.d(tag, "uri_phone : " + uri_phone.toString());
                }
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(android.provider.ContactsContract.Data.MIMETYPE,
                        Email.CONTENT_ITEM_TYPE);

                // 设置联系人的Email地址 key : data1
                values.put(Email.DATA, email);
                // 设置该电子邮件的类型
                values.put(Email.TYPE, Email.TYPE_WORK);
                // 向联系人Email URI添加Email数据
                Uri uri_email = resolver.insert(
                        android.provider.ContactsContract.Data.CONTENT_URI,
                        values);
                if (uri_email == null) {
                    Toast.makeText(getApplicationContext(), "邮箱格式不正确",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    LogUtil.d(tag, "uri_email : " + uri_email.toString());
                }
                Toast.makeText(ContactProviderTest.this, "联系人数据添加成功",
                        Toast.LENGTH_SHORT).show();
            }
        });
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 获取程序界面中的3个文本框
                String name = et_name.getText().toString();
                String phone = et_phone.getText().toString();
                LogUtil.d(tag, "name : " + name + "  phone : " + phone);
                // 定义两个List来封装系统的联系人信息、指定联系人的电话号码、Email等详情
                final ArrayList<String> names = new ArrayList<String>();
                final ArrayList<ArrayList<String>> details = new ArrayList<ArrayList<String>>();
                // 使用ContentResolver查找联系人数据
                Cursor cursor = getContentResolver().query(
                        ContactsContract.Contacts.CONTENT_URI, null, null,
                        null, null);
                // 遍历查询结果，获取系统中所有联系人
                while (cursor.moveToNext()) {
                    LogUtil.d(tag, "====query start====");
                    // 获取联系人ID
                    String contactId = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.Contacts._ID));
                    // 获取联系人的名字
                    String contactName = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    names.add(contactName);

                    LogUtil.d(tag, "contactId : " + contactId + "  contactName : "
                            + contactName);

                    ArrayList<String> detail = new ArrayList<String>();
                    // 使用ContentResolver查找联系人的电话号码
                    Cursor phones = resolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = " + contactId, null, null);
                    // 遍历查询结果，获取该联系人的多个电话号码
                    while (phones.moveToNext()) {
                        // 获取查询结果中电话号码列中数据。
                        String phoneNumber = phones.getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        detail.add("电话号码： " + phoneNumber);
                        LogUtil.i(tag, "phoneNumber : " + phoneNumber);
                    }
                    phones.close();

                    // 使用ContentResolver查找联系人的Email地址
                    Cursor emails = resolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                    + " = " + contactId, null, null);

                    // 遍历查询结果，获取该联系人的多个Email地址
                    while (emails.moveToNext()) {
                        // 获取查询结果中Email地址列中数据。
                        String emailAddress = emails.getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        detail.add("邮件地址： " + emailAddress);
                    }
                    emails.close();
                    details.add(detail);
                }

                cursor.close();
                // 加载result.xml界面布局代表的视图
                View dialog = getLayoutInflater().inflate(
                        R.layout.contracts_result, null);
                ExpandableListView listView = (ExpandableListView) dialog
                        .findViewById(R.id.contact_list);
                ExpandableListAdapter adapter = new BaseExpandableListAdapter() {

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
                    public View getGroupView(int groupPosition,
                            boolean isExpanded, View convertView,
                            ViewGroup parent) {
                        TextView textView = getTextView();
                        textView.setText(names.get(groupPosition).toString());
                        return textView;
                    }

                    @Override
                    public long getGroupId(int groupPosition) {
                        return groupPosition;
                    }

                    @Override
                    public int getGroupCount() {
                        return names.size();
                    }

                    // 获取指定组位置处的组数据
                    @Override
                    public Object getGroup(int groupPosition) {
                        return names.get(groupPosition);
                    }

                    @Override
                    public int getChildrenCount(int groupPosition) {
                        return details.get(groupPosition).size();
                    }

                    @Override
                    public View getChildView(int groupPosition,
                            int childPosition, boolean isLastChild,
                            View convertView, ViewGroup parent) {

                        TextView textView = getTextView();
                        textView.setText(details.get(groupPosition)
                                .get(childPosition).toString());
                        return textView;
                    }

                    @Override
                    public long getChildId(int groupPosition, int childPosition) {
                        return childPosition;
                    }

                    @Override
                    public Object getChild(int groupPosition, int childPosition) {
                        return details.get(groupPosition).get(childPosition);
                    }

                    private TextView getTextView() {
                        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        TextView textView = new TextView(
                                ContactProviderTest.this);
                        textView.setLayoutParams(lp);
                        textView.setGravity(Gravity.CENTER_VERTICAL
                                | Gravity.LEFT);
                        textView.setPadding(80, 0, 0, 0);
                        textView.setTextSize(20);
                        return textView;
                    }
                };

                listView.setAdapter(adapter);

                new AlertDialog.Builder(ContactProviderTest.this)
                        .setTitle("联系人信息：").setView(dialog)
                        .setPositiveButton("确定", null).show();
            }
        });

    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        add = new Button(this);
        add.setLayoutParams(MainActivity.layoutParams_ww);
        add.setText("add");
        ll.addView(add);

        search = new Button(this);
        search.setLayoutParams(MainActivity.layoutParams_ww);
        search.setText("search");
        ll.addView(search);

        et_name = new EditText(this);
        et_name.setLayoutParams(MainActivity.layoutParams_mw);
        et_name.setHint("name");
        ll.addView(et_name);

        et_phone = new EditText(this);
        et_phone.setLayoutParams(MainActivity.layoutParams_mw);
        et_phone.setHint("phone");
        ll.addView(et_phone);

        et_email = new EditText(this);
        et_email.setLayoutParams(MainActivity.layoutParams_mw);
        et_email.setHint("email");
        ll.addView(et_email);

        setContentView(ll);
    }
}
