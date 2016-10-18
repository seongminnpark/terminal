package com.example.seongmin.terminal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import eu.chainfire.libsuperuser.Shell;

public class MainActivity extends AppCompatActivity {
    private TextView console_output;
    private TextView root_output;
    private Button ls_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize View components.
        console_output = (TextView) findViewById(R.id.console_output);
        root_output = (TextView) findViewById(R.id.root_output);
        ls_button = (Button) findViewById(R.id.ls_button);

        // Set onclick listeners.
        ls_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                console_output.setText("Clicked");
            }
        });

        display_root_status();
    }

    private void display_root_status() {
        String rootStatus;
        if (Shell.SU.available()) {
            rootStatus = "Root available.";
        } else {
            rootStatus = "Root unavailable.";
        }
        root_output.setText(rootStatus);
    }
}