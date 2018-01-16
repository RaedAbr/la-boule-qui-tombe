package hepia.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import hepia.app.resources.GameDifficulty;
import hepia.app.resources.IntentConst;
import hepia.app.R;

public class MainActivity extends AppCompatActivity {
    private Menu gameMenu;
    private GameDifficulty difficultyOption;

    /**
     * Called on create the activity
     * @param savedInstanceState Saved instance state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        difficultyOption = GameDifficulty.EASY;
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
        Intent intent = new Intent(MainActivity.this, GamePlayActivity.class);
        intent.putExtra(IntentConst.DIFFICULTY.name(), this.difficultyOption.getMenuItemId());
        startActivity(intent);
    }

    public void onClickHiScoreButton(View view) {
    }
}
