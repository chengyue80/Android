package com.yue.demo.ui.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;

import com.yue.demo.R;

public class GridLayoutTest extends Activity {
    
    GridLayout gridLayout;
    /**定义16个字符数字*/
    private String[] chars = {
            
            "7","8","9","/",
            "4","5","6","*",
            "1","2","3","-",
            ".","0","=","+"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gridlayouttest);
        gridLayout = (GridLayout) findViewById(R.id.root);
        for (int i = 0; i < chars.length; i++) {
            Button button = new Button(this);
            button.setText(chars[i]);
            // 指定该组件所在的行
            GridLayout.Spec rowSpec = GridLayout.spec(i / 4 + 2);
//            指定该组件所在的列
            GridLayout.Spec columnSpec = GridLayout.spec(i%4);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
            button.setGravity(Gravity.CENTER);
            gridLayout.addView(button, params);
        }

    }

}
