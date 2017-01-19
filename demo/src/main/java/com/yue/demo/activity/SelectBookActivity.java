package com.yue.demo.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.yue.demo.R;
import com.yue.demo.fragment.BookDetailFragment;
import com.yue.demo.fragment.BookListFragment;

/**
 * fragment的基本使用
 * 
 * @author chengyue
 * 
 */
public class SelectBookActivity extends Activity implements
        BookListFragment.Callbacks {
    FrameLayout layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 加载/res/layout目录下的activity_book_twopane.xml布局文件
        setContentView(R.layout.activity_book_twopane);
        layout = (FrameLayout) findViewById(R.id.book_detail_container);
        // layout.setVisibility(View.GONE);

        BookListFragment fragment = new BookListFragment();
        // 使用fragment替换book_detail_container容器当前显示的Fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.book_list_container, fragment);
        ft.addToBackStack(null);
        ft.commit(); // ①
    }

    // 实现Callbacks接口必须实现的方法
    @Override
    public void onItemSelected(Integer id) {
        layout.setVisibility(View.VISIBLE);
        // 创建Bundle，准备向Fragment传入参数
        Bundle arguments = new Bundle();
        arguments.putInt(BookDetailFragment.ITEM_ID, id);
        // 创建BookDetailFragment对象
        BookDetailFragment fragment = new BookDetailFragment();
        // 向Fragment传入参数
        fragment.setArguments(arguments);
        // 使用fragment替换book_detail_container容器当前显示的Fragment
        getFragmentManager().beginTransaction()
                .replace(R.id.book_detail_container, fragment)
                .addToBackStack(null).commit(); // ①
    }
}
