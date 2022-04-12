package project.android.snek.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import project.android.snek.R;
import project.android.snek.multiplayer.MultiplayerActivity;
import project.android.snek.multiplayer.conectivity.AndroidClient;
import project.android.snek.utils.ActivityUtils;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityUtils.fullscreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textView = findViewById(R.id.text_view_game_title);
        this.progressBar = findViewById(R.id.activity_main_spinner);

        final SpannableString text = new SpannableString("Ori");

        text.setSpan(new ForegroundColorSpan(Color.RED), 1, 2, 0);
        textView.setText(text, TextView.BufferType.SPANNABLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ActivityUtils.fullscreen(this);
    }

    public void onStartGameButton(View view) {
        final Intent gameIntent = new Intent(this, GameActivity.class);

        startActivity(gameIntent);
    }

    public void onMultiplayerButtonClicked(View view) {
        progressBar.setVisibility(View.VISIBLE);

        findGame();
    }

    public void onSkinButtonClicked(View view) {
        final Intent intent = new Intent(this, SkinActivity.class);

        startActivity(intent);
    }

    public void onConfigButtonClicked(View view) {
        final Intent intent = new Intent(this, ConfigActivity.class);

        startActivity(intent);
    }

    @SuppressLint("CheckResult")
    private void findGame() {
        final Intent intent = new Intent(this, MultiplayerActivity.class);

        AndroidClient.getInstance(this)
                .findGame().subscribe(gameId -> {
            Log.d("CLIENT", "GameId: [" + gameId + "]");
            intent.putExtra(MultiplayerActivity.INTENT_GEY_GAME_ID, gameId);
            progressBar.setVisibility(View.INVISIBLE);

            startActivity(intent);
        }, error -> {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Failed to find game.", Toast.LENGTH_SHORT).show();
        });

    }
}