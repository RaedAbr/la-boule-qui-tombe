package hepia.app.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import hepia.app.activities.GamePlayActivity;

public class GameTimer {
    private GamePlayActivity activity;
    private int timeValueSeconds = 30;
    private Timer timer;
    private TimerTask timerTask;
    private final Handler handler = new Handler();

    public GameTimer(GamePlayActivity activity) {
        this.activity = activity;
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 0, 1000); //
    }

    public void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        timeValueSeconds--;
                        activity.getView().setTime(timeValueSeconds);
                        if (timeValueSeconds == 0) {
                            stopTimerTask();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    activity);

                            // set title
                            alertDialogBuilder.setTitle("Your Title");

                            // set dialog message
                            alertDialogBuilder
                                    .setMessage("Click yes to exit!")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // if this button is clicked, close
                                            // current activity
//                                            MainActivity.this.finish();
                                        }
                                    })
                                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // if this button is clicked, just close
                                            // the dialog box and do nothing
                                            dialog.cancel();
                                        }
                                    });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();
                        }
                    }
                });
            }
        };
    }

    public int getTimeValueSeconds() {
        return timeValueSeconds;
    }

    public void setTimeValueSeconds(int timeValueSeconds) {
        this.timeValueSeconds = timeValueSeconds;
    }
}
