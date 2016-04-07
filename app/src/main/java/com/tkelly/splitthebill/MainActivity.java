package com.tkelly.splitthebill;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

/**
 * The main activity, which allows the user to access either EvenSplitActivity or SplitActivity
 *
 * @see EvenSplitActivity
 * @see SplitActivity
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_activity_main);
            getSupportActionBar().setHomeButtonEnabled(false);
        }

        Button even_split_btn = (Button) findViewById(R.id.even_split_btn);
        even_split_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EvenSplitActivity.class));
            }
        });

        Button split_btn = (Button) findViewById(R.id.split_btn);
        split_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SplitActivity.class));
            }
        });
    }

}
