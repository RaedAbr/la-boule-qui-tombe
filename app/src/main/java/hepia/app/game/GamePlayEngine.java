package hepia.app.game;

import android.app.Service;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

import hepia.app.activities.GamePlayActivity;
import hepia.app.model.Block;

public class GamePlayEngine {
    private SensorManager sensorManager;
    private Sensor accelerometre;
    private SensorEventListener sensorEventListener;
    private List<Block> blocks;

    public GamePlayEngine(GamePlayActivity activity) {
        sensorManager = (SensorManager) activity.getBaseContext().getSystemService(Service.SENSOR_SERVICE);
        assert sensorManager != null;
        accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent pEvent) {
//                float x = pEvent.values[0];
//                float y = pEvent.values[1];

//            if(mBoule != null) {
                // On met � jour les coordonn�es de la boule
//                RectF hitBox = mBoule.putXAndY(x, y);

                // Pour tous les blocs du labyrinthe
//                for(Block block : blocks) {
                // On cr�e un nouveau rectangle pour ne pas modifier celui du Block
//                    RectF inter = new RectF(block.getRectangle());
//					if(inter.intersect(hitBox)) {
//						// On agit diff�rement en fonction du type de Block
//						switch(block.getType()) {
//						case TROU:
//							mActivity.showDialog(LabyrintheActivity.DEFEAT_DIALOG);
//							break;
//
//						case DEPART:
//							break;
//
//						case ARRIVEE:
//							mActivity.showDialog(LabyrintheActivity.VICTORY_DIALOG);
//							break;
//						}
//						break;
//					}
//                }
//            }
            }

            @Override
            public void onAccuracyChanged(Sensor pSensor, int pAccuracy) {

            }
        };
    }

    // Remet � z�ro l'emplacement de la boule
    public void reset() {
//        mBoule.reset();
    }

    // Arr�te le capteur
    public void stop() {
        sensorManager.unregisterListener(sensorEventListener, accelerometre);
    }

    // Red�marre le capteur
    public void resume() {
        sensorManager.registerListener(sensorEventListener, accelerometre, SensorManager.SENSOR_DELAY_GAME);
    }

    // Construit le labyrinthe
    public List<Block> buildLabyrinthe(int rayon) {
        blocks = new ArrayList<Block>();
        blocks.add(new Block(Block.Type.TROU, 0, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 0, 1, rayon));
        blocks.add(new Block(Block.Type.TROU, 0, 2, rayon));
        blocks.add(new Block(Block.Type.TROU, 0, 3, rayon));
        blocks.add(new Block(Block.Type.TROU, 0, 4, rayon));
        blocks.add(new Block(Block.Type.TROU, 0, 5, rayon));
        blocks.add(new Block(Block.Type.TROU, 0, 6, rayon));
        blocks.add(new Block(Block.Type.TROU, 0, 7, rayon));
        blocks.add(new Block(Block.Type.TROU, 0, 8, rayon));
        blocks.add(new Block(Block.Type.TROU, 0, 9, rayon));
        blocks.add(new Block(Block.Type.TROU, 0, 10, rayon));
        blocks.add(new Block(Block.Type.TROU, 0, 11, rayon));
        blocks.add(new Block(Block.Type.TROU, 0, 12, rayon));
        blocks.add(new Block(Block.Type.TROU, 0, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 1, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 1, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 2, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 2, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 3, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 3, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 4, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 4, 1, rayon));
        blocks.add(new Block(Block.Type.TROU, 4, 2, rayon));
        blocks.add(new Block(Block.Type.TROU, 4, 3, rayon));
        blocks.add(new Block(Block.Type.TROU, 4, 4, rayon));
        blocks.add(new Block(Block.Type.TROU, 4, 5, rayon));
        blocks.add(new Block(Block.Type.TROU, 4, 6, rayon));
        blocks.add(new Block(Block.Type.TROU, 4, 7, rayon));
        blocks.add(new Block(Block.Type.TROU, 4, 8, rayon));
        blocks.add(new Block(Block.Type.TROU, 4, 9, rayon));
        blocks.add(new Block(Block.Type.TROU, 4, 10, rayon));
        blocks.add(new Block(Block.Type.TROU, 4, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 5, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 5, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 6, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 6, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 7, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 7, 1, rayon));
        blocks.add(new Block(Block.Type.TROU, 7, 2, rayon));
        blocks.add(new Block(Block.Type.TROU, 7, 5, rayon));
        blocks.add(new Block(Block.Type.TROU, 7, 6, rayon));
        blocks.add(new Block(Block.Type.TROU, 7, 9, rayon));
        blocks.add(new Block(Block.Type.TROU, 7, 10, rayon));
        blocks.add(new Block(Block.Type.TROU, 7, 11, rayon));
        blocks.add(new Block(Block.Type.TROU, 7, 12, rayon));
        blocks.add(new Block(Block.Type.TROU, 7, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 8, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 8, 5, rayon));
        blocks.add(new Block(Block.Type.TROU, 8, 9, rayon));
        blocks.add(new Block(Block.Type.TROU, 8, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 9, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 9, 5, rayon));
        blocks.add(new Block(Block.Type.TROU, 9, 9, rayon));
        blocks.add(new Block(Block.Type.TROU, 9, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 10, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 10, 5, rayon));
        blocks.add(new Block(Block.Type.TROU, 10, 9, rayon));
        blocks.add(new Block(Block.Type.TROU, 10, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 11, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 11, 5, rayon));
        blocks.add(new Block(Block.Type.TROU, 11, 9, rayon));
        blocks.add(new Block(Block.Type.TROU, 11, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 12, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 12, 1, rayon));
        blocks.add(new Block(Block.Type.TROU, 12, 2, rayon));
        blocks.add(new Block(Block.Type.TROU, 12, 3, rayon));
        blocks.add(new Block(Block.Type.TROU, 12, 4, rayon));
        blocks.add(new Block(Block.Type.TROU, 12, 5, rayon));
        blocks.add(new Block(Block.Type.TROU, 12, 9, rayon));
        blocks.add(new Block(Block.Type.TROU, 12, 8, rayon));
        blocks.add(new Block(Block.Type.TROU, 12, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 13, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 13, 8, rayon));
        blocks.add(new Block(Block.Type.TROU, 13, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 14, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 14, 8, rayon));
        blocks.add(new Block(Block.Type.TROU, 14, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 15, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 15, 8, rayon));
        blocks.add(new Block(Block.Type.TROU, 15, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 16, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 16, 4, rayon));
        blocks.add(new Block(Block.Type.TROU, 16, 5, rayon));
        blocks.add(new Block(Block.Type.TROU, 16, 6, rayon));
        blocks.add(new Block(Block.Type.TROU, 16, 7, rayon));
        blocks.add(new Block(Block.Type.TROU, 16, 8, rayon));
        blocks.add(new Block(Block.Type.TROU, 16, 9, rayon));
        blocks.add(new Block(Block.Type.TROU, 16, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 17, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 17, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 18, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 18, 13, rayon));

        blocks.add(new Block(Block.Type.TROU, 19, 0, rayon));
        blocks.add(new Block(Block.Type.TROU, 19, 1, rayon));
        blocks.add(new Block(Block.Type.TROU, 19, 2, rayon));
        blocks.add(new Block(Block.Type.TROU, 19, 3, rayon));
        blocks.add(new Block(Block.Type.TROU, 19, 4, rayon));
        blocks.add(new Block(Block.Type.TROU, 19, 5, rayon));
        blocks.add(new Block(Block.Type.TROU, 19, 6, rayon));
        blocks.add(new Block(Block.Type.TROU, 19, 7, rayon));
        blocks.add(new Block(Block.Type.TROU, 19, 8, rayon));
        blocks.add(new Block(Block.Type.TROU, 19, 9, rayon));
        blocks.add(new Block(Block.Type.TROU, 19, 10, rayon));
        blocks.add(new Block(Block.Type.TROU, 19, 11, rayon));
        blocks.add(new Block(Block.Type.TROU, 19, 12, rayon));
        blocks.add(new Block(Block.Type.TROU, 19, 13, rayon));

        Block b = new Block(Block.Type.DEPART, 2, 2, rayon);
//        mBoule.setInitialRectangle(new RectF(b.getRectangle()));
        blocks.add(b);

        blocks.add(new Block(Block.Type.ARRIVEE, 8, 11, rayon));

        return blocks;
    }
}
