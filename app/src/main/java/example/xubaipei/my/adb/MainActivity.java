package example.xubaipei.my.adb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "shell";
    EditText mConsoleEt;
    String cmd = "";
    String CMD_HEAD = "######";
    TextWatcher mTextWatcher = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConsoleEt = (EditText)findViewById(R.id.console_et);
        mConsoleEt.setFocusable(true);
        mConsoleEt.setFocusableInTouchMode(true);
        mConsoleEt.requestFocus();

        mConsoleEt.append(CMD_HEAD);
        mConsoleEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    mConsoleEt.removeTextChangedListener(mTextWatcher);
                    mConsoleEt.setText(CMD_HEAD);
                    mConsoleEt.append("\n");
                    excute(cmd);
                    mConsoleEt.addTextChangedListener(mTextWatcher);
                }
                    return true;
            }
        });
        mConsoleEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mConsoleEt.setSelection(mConsoleEt.getText().length());
                    mConsoleEt.requestFocus();
                }
            }
        });
        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                int position = content.lastIndexOf(CMD_HEAD);
                int start = position + CMD_HEAD.length();
                int end = content.length();
                if (end - start > 0) {
                    cmd = content.substring(start, end);
                }
                Log.i(TAG,cmd);
            }
        };
        mConsoleEt.addTextChangedListener(mTextWatcher);
    }

    private void excute(String cmd){
        Process process = null;
        try {
            if (process == null) {
                process = Runtime.getRuntime().exec(cmd);
            }else {
                OutputStream outputStream = process.getOutputStream();
                outputStream.write(cmd.getBytes());
                outputStream.flush();
            }
            process.waitFor();
            InputStream inputStream = process.getInputStream();
            int size = inputStream.available();
            byte[] data = new byte[size];
            inputStream.read(data);
            String content = new String(data);
            Log.i(TAG,content);
            mConsoleEt.append(content);
            mConsoleEt.append("\n");
            mConsoleEt.append(CMD_HEAD);
            process.destroy();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
