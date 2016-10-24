package com.example.seongmin.terminal;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import eu.chainfire.libsuperuser.Shell;

public class MainActivity extends AppCompatActivity {
    private boolean root_access;

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

        (new Startup()).setContext(this).execute();
    }

    private class Startup extends AsyncTask<Void, Void, Void> {
        private Context context;

        public Startup setContext(Context context) {
            this.context = context;
            return this;
        }

        @Override
        public Void doInBackground(Void... params) {
            check_root_status();
            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            // Display root status.
            if (root_output == null) return;

            String root_status_text;
            if (root_access) {
                root_status_text = "Root available.";
            } else {
                root_status_text = "Root unavailable.";
            }
            root_output.setText(root_status_text);
        }

        private void check_root_status() {
            root_access = Shell.SU.available();
        }

    }

}