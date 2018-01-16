package hepia.app.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import hepia.app.R;
import hepia.app.resources.GameDifficulty;
import hepia.app.resources.IntentConst;

public class GamePlayActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        textView = findViewById(R.id.difficulty_text_view);
        Intent intent = getIntent();
        int retreivedValue = intent.getIntExtra(IntentConst.DIFFICULTY.name(), 0);
        GameDifficulty difficultyValue = GameDifficulty.updateValue(retreivedValue);
        assert difficultyValue != null;
        textView.setText(difficultyValue.toString());
        draw();
    }

    private void draw() {
        Bitmap b = Bitmap.createBitmap(128,128, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        
    }
}
