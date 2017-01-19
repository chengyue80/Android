package com.yue.demo.content;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * ContentProvider 协议
 * 
 * @author chengyue
 * 
 */
public final class Words {
    // 定义该ContentProvider的Authority
    public static final String AUTHORITY = "org.yue.test.dictprovider";

    // 定义一个静态内部类，定义该ContentProvider所包的数据列的列名
    public static final class Word implements BaseColumns {
        public final static String _ID = "_id";
        public static String DB_NAME = "MyDict.db3";
        // 定义Content所允许操作的3个数据列
        public static final String TABLE_NAME = "dict";
        public static final String KEY_WORD = "word";
        public static final String KEY_DETIAL = "detail";
        // 定义该Content提供服务的两个Uri
        public final static Uri DICT_CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/words");
        public final static Uri WORD_CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/word");
    }
}
