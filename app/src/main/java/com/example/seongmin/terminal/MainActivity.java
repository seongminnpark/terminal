package com.example.seongmin.terminal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView console_output;
    private Button ls_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize View components.
        console_output = (TextView) findViewById(R.id.console_output);
        ls_button = (Button) findViewById(R.id.ls_button);

        // Set onclick listeners.
        ls_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                console_output.setText("Clicked");
            }
        });
    }
}