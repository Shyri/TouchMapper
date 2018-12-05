package es.shyri.touchmapper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonOpenTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControllerTestActivity.openActivity(MainActivity.this);
            }
        });

        findViewById(R.id.buttonOpenInputMethodSelector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputMethodSelector();
            }
        });
    }

    private void showInputMethodSelector() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        InputMethodManager imeManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imeManager != null) {
            imeManager.showInputMethodPicker();
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG)
                 .show();
        }
    }
}
