package project.android.snek.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import project.android.snek.R;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}