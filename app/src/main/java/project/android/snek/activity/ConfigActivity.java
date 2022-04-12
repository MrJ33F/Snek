package project.android.snek.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import project.android.snek.R;
import project.android.snek.customization.Config;
import project.android.snek.utils.ActivityUtils;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.fullscreen(this);
        setContentView(R.layout.activity_config);

        final EditText nickEditText = findViewById(R.id.nick_edit_text);

        nickEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Config.getInstance(ConfigActivity.this).setName(s.toString());
            }
        });
    }
}