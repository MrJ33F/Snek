package project.android.snek.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import cdflynn.android.library.turn.TurnLayoutManager;
import project.android.snek.R;
import project.android.snek.customization.SkinAdapter;
import project.android.snek.utils.ActivityUtils;

public class SkinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.fullscreen(this);
        setContentView(R.layout.activity_skin);


        final RecyclerView recyclerView = findViewById(R.id.skin_recycler_view);
        final RecyclerView.LayoutManager layoutManager = new TurnLayoutManager(this, TurnLayoutManager.Gravity.START,
                TurnLayoutManager.Orientation.VERTICAL, 1000, 500, true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new SkinAdapter(this));

    }
}