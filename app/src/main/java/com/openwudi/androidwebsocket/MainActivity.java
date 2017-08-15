package com.openwudi.androidwebsocket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by diwu on 17/7/28.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText;
    EditText addr;
    TextView blackReq;
    TextView blackBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blackReq = (TextView) findViewById(R.id.black_box_req);
        blackBox = (TextView) findViewById(R.id.black_box);
        addr = (EditText) findViewById(R.id.addr);
        findViewById(R.id.button).setOnClickListener(this);
        editText = (EditText) findViewById(R.id.editText);
        findViewById(R.id.send).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                WebsocketClient.startRequest(addr.getText().toString());
                break;
            case R.id.send:
                WebsocketClient.sendMessage(editText.getText().toString());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        /* Do something */
//        Toast.makeText(this, event.msg, Toast.LENGTH_SHORT).show();
        blackReq.setText(event.req);
        blackBox.setText(event.msg);
    }
}
