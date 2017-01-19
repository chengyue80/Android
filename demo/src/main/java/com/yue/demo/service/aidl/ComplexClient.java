package com.yue.demo.service.aidl;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yue.demo.MainActivity;
import com.yue.demo.aidl.IPet;
import com.yue.demo.aidl.Person;
import com.yue.demo.aidl.Pet;

import java.util.List;

public class ComplexClient extends Activity {

    private IPet petService;
    private Button button;
    private EditText editText;
    private TextView color, weight;

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            petService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            petService = IPet.Stub.asInterface(service);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        Intent intent = new Intent("org.yue.test.action.COMPLEX_SERVICE");
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String name = editText.getText().toString();
                try {
                    List<Pet> pets = petService.getPets(new Person(1, name,
                            name) {
                    });
                    StringBuffer sb = new StringBuffer();
                    for (Pet pet : pets) {
                        sb.append("name : " + pet.getName());
                        sb.append("   weight : " + pet.getWeight());
                        sb.append("\n");
                    }
                    color.setText(sb.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        button = new Button(this);
        button.setLayoutParams(MainActivity.layoutParams_mw);
        button.setText("获取aidlservice的状态");
        ll.addView(button);

        editText = new EditText(this);
        editText.setLayoutParams(MainActivity.layoutParams_mw);
        editText.setText("sun");
        ll.addView(editText);

        color = new TextView(this);
        color.setLayoutParams(MainActivity.layoutParams_mw);
        color.setText("color");
        ll.addView(color);

        weight = new TextView(this);
        weight.setLayoutParams(MainActivity.layoutParams_mw);
        weight.setText("weight");
        ll.addView(weight);

        setContentView(ll);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
