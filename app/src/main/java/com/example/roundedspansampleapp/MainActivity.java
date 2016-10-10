package com.example.roundedspansampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG ";
    public static final String TEXT = "TEXT ";

    EditText input;
    TextView tags;
    Button appendTag, appendText;

    int count = 0;
    int[] colorArr = {android.R.color.holo_red_light,
            android.R.color.holo_blue_light,
            android.R.color.holo_green_light};

    int[] iconArr = {0,
            R.drawable.icon_cash,
            R.drawable.icon_snowflake,
            R.drawable.icon_batch};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.input);
        tags = (TextView) findViewById(R.id.tags);
        appendTag = (Button) findViewById(R.id.append_tag);
        appendText = (Button) findViewById(R.id.append_text);

        appendTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String text = input.getText().toString();
                setSpan("");
            }
        });

        appendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tags.append(TEXT);
            }
        });

    }

    private void setSpan(String text) {
        SpannableString spannableString = RoundedTagSpannableUtils.getSpan(getBaseContext(),
                TAG + count,
                getcolor(count),
                getIcon(count),
                tags);
        count++;
        tags.append(spannableString);
    }

    private int getcolor(int count) {
        return colorArr[count % colorArr.length];
    }

    private int getIcon(int count) {
        return iconArr[count % iconArr.length];
    }
}
