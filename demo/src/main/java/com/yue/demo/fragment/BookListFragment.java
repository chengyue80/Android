package com.yue.demo.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yue.demo.activity.MainActivity_Activity;
import com.yue.demo.fragment.model.BookContent;
import com.yue.demo.util.LogUtil;

/**
 * Activity加载布局时会加载该fragment（被定义到布局文件中了）
 * 
 * @author chengyue
 * @version 1.0
 */
public class BookListFragment extends ListFragment {
    private Callbacks mCallbacks;
    private final String tag = "BookListFragment ";

    // 定义一个回调接口，该Fragment所在Activity需要实现该接口
    // 该Fragment将通过该接口与它所在的Activity交互
    public interface Callbacks {
        public void onItemSelected(Integer id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(MainActivity_Activity.Tag + tag + "onCreate");
        // 为该ListFragment设置Adapter
        setListAdapter(new ArrayAdapter<BookContent.Book>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1, BookContent.ITEMS));
    }

    // 当该Fragment被添加、显示到Activity时，回调该方法
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.d(MainActivity_Activity.Tag + tag + "onAttach");
        // 如果Activity没有实现Callbacks接口，抛出异常
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "BookListFragment所在的Activity必须实现Callbacks接口!");
        }
        // 把该Activity当成Callbacks对象
        mCallbacks = (Callbacks) activity;
    }

    // 当该Fragment从它所属的Activity中被删除时回调该方法
    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d(MainActivity_Activity.Tag + tag + "onDetach");
        // 将mCallbacks赋为null。
        mCallbacks = null;
    }

    // 当用户点击某列表项时激发该回调方法
    @Override
    public void onListItemClick(ListView listView, View view, int position,
            long id) {
        super.onListItemClick(listView, view, position, id);
        LogUtil.d(MainActivity_Activity.Tag + tag + "onListItemClick");
        // 激发mCallbacks的onItemSelected方法
        mCallbacks.onItemSelected(BookContent.ITEMS.get(position).id);
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        LogUtil.d(MainActivity_Activity.Tag + tag + "setActivateOnItemClick");

        getListView().setChoiceMode(
                activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
                        : ListView.CHOICE_MODE_NONE);
    }
}
