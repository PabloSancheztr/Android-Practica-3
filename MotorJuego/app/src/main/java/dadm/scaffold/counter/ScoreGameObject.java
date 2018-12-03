package dadm.scaffold.counter;

import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;

import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameObject;

public class ScoreGameObject extends GameObject {

    private static final int POINTS_LOSS_PER_ASTEROID = -1;
    private static final int POINTS_GAINED_PER_ASTEROID = 50;
    private final TextView textScore;

    private int points;
    private boolean pointsHaveChanged;

    public ScoreGameObject(View view, int imageId) {
        textScore = (TextView) view.findViewById(imageId);
    }

    @Override
    public void startGame(GameEngine gameEngine) {
        points = 0;
        textScore.post(updateTextRunnable);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {

    }

    @Override
    public void onDraw(Canvas canvas) {
        if(pointsHaveChanged) {
            textScore.post(updateTextRunnable);
            pointsHaveChanged = false;
        }
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if(gameEvent == GameEvent.asteroidHit) {
            points += POINTS_GAINED_PER_ASTEROID;
            pointsHaveChanged = true;
        }
        else if(gameEvent == GameEvent.asteroidMissed) {
            if(points > 0) {
                points -= POINTS_LOSS_PER_ASTEROID;
            }
            pointsHaveChanged = true;
        }
    }

    private Runnable updateTextRunnable = new Runnable() {
        @Override
        public void run() {
            String text = "Score: " + String.format("%06d", points);
            textScore.setText(text);
        }
    };
}
