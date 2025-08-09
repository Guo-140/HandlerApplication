package com.example.myapplication.base;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = BaseActivity.class.getSimpleName();

    private GridLayout layout;
    private TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        layout = findViewById(R.id.container);
        textView1 = findViewById(R.id.textView1);
        createBtn("clean", view -> {
            text("");
        });
        init();
    }

    protected abstract void init();

    protected void createBtn(String title, View.OnClickListener onClickListener) {
        Button button = new Button(this);
        button.setAllCaps(false);
        button.setText(title);
        button.setOnClickListener(view -> {
            append(title);
            onClickListener.onClick(view);
        });
        layout.addView(button);
    }

    /**
     * 直接把 textView 修改成 str
     * @param str
     */
    protected void text(String str) {
        textView1.setText(str);
    }

    /**
     * 添加字符 txt 在 textView 的后面，一般加上 \n 进行换行
     * @param txt
     */
    protected void append(Object txt) {
        textView1.append(String.valueOf(txt) + "\n");
        Log.i(TAG, "append: " + txt + "\n");
    }
}