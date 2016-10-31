package com.example.seongmin.terminal;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class MainActivity extends Activity {
    private boolean root_access;
    private Shell.Interactive shell;
    private boolean shell_active = false;

    private ScrollView console_scrollview;
    private TextView root_output;
    private TextView console_output;
    private EditText console_input;
    private Button submit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize View components.
        console_scrollview = (ScrollView) findViewById(R.id.console_scrollview);
        root_output = (TextView) findViewById(R.id.root_output);
        console_output = (TextView) findViewById(R.id.console_output);
        console_input = (EditText) findViewById(R.id.console_input);
        submit_button = (Button) findViewById(R.id.submit_button);

        // Set onclick listeners.
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_clicked();
            }
        });

        // Other setup.
        console_input.setEnabled(shell_active);

        (new Startup()).setContext(this).execute();
        init_shell();
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

    private void init_shell() {
        if (shell != null) return;
        shell = new Shell.Builder().
                useSU().
                setWantSTDERR(true).
                setWatchdogTimeout(5).
                setMinimalLogging(true).
                open(new Shell.OnCommandResultListener() {

                    // Callback to report whether the shell was successfully started up
                    @Override
                    public void onCommandResult(int commandCode, int exitCode, List<String> output) {

                        if (exitCode != Shell.OnCommandResultListener.SHELL_RUNNING) {
                            report_error("Error opening root shell: exitCode " + exitCode);
                        } else {
                            // Shell is up.
                            shell_active = true;
                            console_input.setEnabled(shell_active);
                        }
                    }
                });
    }

    private void report_error(String error) {
        List<String> errorInfo = new ArrayList<String>();
        errorInfo.add(error);
        shell = null;
        shell_active = false;
    }

    private void updateResultStatus(List<String> suResult) {
        StringBuilder sb = new StringBuilder();
        if (suResult != null) {
            for (String line : suResult) {
                sb.append(line).append((char) 10);
            }
        }
        console_output.append(sb.toString());
        console_scrollview.post(new Runnable() {
            @Override
            public void run() {
                console_scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void submit_clicked() {
        if (shell == null) return;
        shell.addCommand(new String[]{console_input.getText().toString()}, 0,
                new Shell.OnCommandResultListener() {
                    public void onCommandResult(int commandCode, int exitCode, List<String> output) {
                        if (exitCode < 0) {
                            report_error("Error executing commands: exitCode " + exitCode);
                        } else {
                            updateResultStatus(output);
                        }
                    }
                });
    }

}