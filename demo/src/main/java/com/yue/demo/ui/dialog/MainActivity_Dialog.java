package com.yue.demo.ui.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.android.framework.base.BaseActivity;
import com.yue.demo.R;
import com.yue.demo.RootActivity;

public class MainActivity_Dialog extends RootActivity {
	private Button dialog_simpleBtn, dialog_simpleListBtn, dialog_singleChoice,
			dialog_multiChoice, dialog_myAdapter, dialog_myDialog;
	// private Button login;
	private String[] classes = { "java", ".net", "android" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controller_dialogtest);

		dialog_simpleBtn = (Button) findViewById(R.id.dialog_simpleBtn);
		dialog_simpleListBtn = (Button) findViewById(R.id.dialog_simpleListBtn);
		dialog_singleChoice = (Button) findViewById(R.id.dialog_singleChoice);
		dialog_multiChoice = (Button) findViewById(R.id.dialog_multiChoice);
		dialog_myAdapter = (Button) findViewById(R.id.dialog_myAdapter);
		dialog_myDialog = (Button) findViewById(R.id.dialog_myDialog);

		dialog_simpleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new Builder(
						MainActivity_Dialog.this);

				builder.setTitle("简单dialog")
						.setIcon(R.drawable.ic_launcher)
						.setMessage("简单dialog")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										Toast.makeText(
												MainActivity_Dialog.this,
												"你点击了确定按钮！", Toast.LENGTH_LONG)
												.show();
									}

								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// 锟斤拷钮锟斤拷锟斤拷锟铰硷拷锟斤拷钮锟斤拷锟斤拷锟铰硷拷
										Toast.makeText(
												MainActivity_Dialog.this,
												"你点击了取消按钮！", Toast.LENGTH_LONG)
												.show();
									}
								}).create().show();

			}
		});

		dialog_simpleListBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(
						MainActivity_Dialog.this);

				builder.setTitle("普通列表对话框");
				builder.setIcon(R.drawable.ic_launcher);
				builder.setItems(classes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Toast.makeText(MainActivity_Dialog.this,
										"你点击了" + classes[which],
										Toast.LENGTH_LONG).show();
							}
						});
				builder.setPositiveButton("确定", null);
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});

		dialog_singleChoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(
						MainActivity_Dialog.this);
				builder.setTitle("单选列表dialog")
						.setSingleChoiceItems(classes, 1,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Toast.makeText(
												MainActivity_Dialog.this,
												"你选择了" + classes[which],
												Toast.LENGTH_LONG).show();
									}
								}).create().show();
			}
		});
		dialog_multiChoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(
						MainActivity_Dialog.this);
				builder.setTitle("多选列表dialog")
						.setIcon(R.drawable.ic_launcher)
						.setMultiChoiceItems(
								classes,
								new boolean[] { true, false, true },
								new DialogInterface.OnMultiChoiceClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which, boolean isChecked) {
										// TODO Auto-generated method stub
										Toast.makeText(
												MainActivity_Dialog.this,
												"你点击选择了" + classes[which],
												Toast.LENGTH_LONG).show();
									}
								});
				builder.setPositiveButton("确定", null);
				builder.setNegativeButton("取消", null);
				builder.create().show();

			}
		});
		dialog_myAdapter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Builder builder = new Builder(MainActivity_Dialog.this);

				ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
						MainActivity_Dialog.this,
						android.R.layout.simple_list_item_1, classes);
				AlertDialog dialog = builder
						.setTitle("自定义列表对话框")
						.setIcon(R.drawable.ic_launcher)
						.setAdapter(arrayAdapter,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Toast.makeText(
												MainActivity_Dialog.this,
												"你单机选择了" + classes[which],
												Toast.LENGTH_LONG).show();
									}
								}).setPositiveButton("确定", null)
						.setNegativeButton("取消", null).create();
				dialog.show();
			}
		});
		dialog_myDialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Builder builder = new Builder(MainActivity_Dialog.this);
				LayoutInflater inflater = LayoutInflater
						.from(MainActivity_Dialog.this);
				View view = inflater.inflate(R.layout.controller_logindialog,
						null);
				AlertDialog dialog = builder.setTitle("自定义布局对话框").setView(view)
						.setPositiveButton("确定", null)
						.setNegativeButton("取消", null).create();
				dialog.show();

				Button btnlogin = (Button) view.findViewById(R.id.login);
				final EditText eName = (EditText) view
						.findViewById(R.id.login_name);
				final EditText ePassWord = (EditText) view
						.findViewById(R.id.login_pwd);
				final EditText ePhone = (EditText) view
						.findViewById(R.id.login_phone);
				btnlogin.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String info = "用户名" + eName.getText().toString() + "\n"
								+ "密码：" + ePassWord.getText().toString() + "\n"
								+ "电话:" + ePhone.getText().toString();
						Toast.makeText(MainActivity_Dialog.this, info,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});

		findViewById(R.id.dialog_style).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Dialog dialog = new Dialog(MainActivity_Dialog.this,
								R.style.recorddialog);
						dialog.setContentView(R.layout.controller_logindialog);
						dialog.show();
					}
				});
		findViewById(R.id.dialog_style1).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Dialog dialog = new Dialog(MainActivity_Dialog.this,
								R.style.dialog);
						dialog.setContentView(R.layout.controller_logindialog);
						dialog.show();
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
