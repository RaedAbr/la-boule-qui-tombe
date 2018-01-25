package hepia.app.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import hepia.app.resources.GameDifficulty;
import hepia.app.resources.IntentConst;
import hepia.app.R;

public class MainActivity extends AppCompatActivity {
    private Menu gameMenu;
    private GameDifficulty difficultyOption;
    private EditText userNameEditText;

    /**
     * Called on create the activity
     * @param savedInstanceState Saved instance state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        difficultyOption = GameDifficulty.EASY;
        userNameEditText = findViewById(R.id.user_name);
    }

    /**
     * Called on create the option gameMenu for the activity
     * @param menu Default app gameMenu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        this.gameMenu = menu;
        menu.findItem(difficultyOption.getMenuItemId()).setChecked(true);
        return true;
    }

    /**
     * Called on menu option item selected
     * @param item Selected menu item
     * @return Return false to allow normal menu processing to proceed, true to consume it here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.easy_option:
            case R.id.medium_option:
            case R.id.hard_option:
                difficultyOption = GameDifficulty.updateValue(item.getItemId());
                if (difficultyOption != null) {
                    this.gameMenu.findItem(difficultyOption.getMenuItemId()).setChecked(true);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickPlayButton(View view) {
        if (TextUtils.isEmpty(userNameEditText.getText())) {
            userNameEditText.setError("Please put your name");
        } else {
            Intent intent = new Intent(MainActivity.this, GamePlayActivity.class);
            intent.putExtra(IntentConst.DIFFICULTY.name(), MainActivity.this.difficultyOption.getMenuItemId());
            intent.putExtra(IntentConst.USER_NAME.name(), userNameEditText.getText().toString());
            startActivity(intent);
        }
    }

    public void onClickHiScoreButton(View view) {
        restoreHightScore();
    }

    private void restoreHightScore() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("hight_score", MODE_PRIVATE);
        String highScore = sharedPref.getString(IntentConst.HIDHT_SCORE.name(), "No score saved");

        TextView titleTextView = new TextView(this);
        titleTextView.setText("Hight score");
        titleTextView.setGravity(Gravity.CENTER);
        titleTextView.setPadding(10, 10, 10, 10);
        titleTextView.setBackgroundColor(Color.DKGRAY);
        titleTextView.setTextColor(Color.WHITE);
        titleTextView.setTextSize(30);

        TextView msgTextView = new TextView(this);
        msgTextView.setText(highScore);
        msgTextView.setGravity(Gravity.CENTER);
        msgTextView.setTextSize(20);
        msgTextView.setPadding(20, 20, 20, 20);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setCustomTitle(titleTextView);
        dialog.setView(msgTextView);
        dialog.create().show();
    }
}
