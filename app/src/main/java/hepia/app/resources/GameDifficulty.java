package hepia.app.resources;

import hepia.app.R;

/**
 * Created by raed on 30.11.17.
 */

public enum GameDifficulty {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard");

    private String stringValue = "";

    GameDifficulty(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public int getMenuItemId() {
        switch (this.ordinal()) {
            case 0:
                return R.id.easy_option;
            case 1:
                return R.id.medium_option;
            case 2:
                return R.id.hard_option;
        }
        return 0;
    }

    public static GameDifficulty updateValue(int menuItemId) {
        switch (menuItemId) {
            case R.id.easy_option:
                return GameDifficulty.EASY;
            case R.id.medium_option:
                return GameDifficulty.MEDIUM;
            case R.id.hard_option:
                return GameDifficulty.HARD;
        }
        return null;
    }
}
