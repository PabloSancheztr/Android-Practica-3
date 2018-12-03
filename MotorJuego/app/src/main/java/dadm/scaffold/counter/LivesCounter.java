package dadm.scaffold.counter;

import android.graphics.Canvas;
import android.view.View;
import android.widget.LinearLayout;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameObject;

public class LivesCounter extends GameObject {

    private final LinearLayout layout;

    public LivesCounter(View view, int layoutId) {
        layout = (LinearLayout) view.findViewById(layoutId);
    }

    @Override
    public void startGame(GameEngine gameEngine) {
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
    }

    @Override
    public void onDraw(Canvas canvas) {

    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if(gameEvent == GameEvent.lifeLost) {
            layout.post(removeLifeRunnable);
        }
        else if(gameEvent == GameEvent.lifeAdded) {
            layout.post(addLifeRunnable);
        }
    }

    private Runnable removeLifeRunnable = new Runnable() {
        @Override
        public void run() {
            layout.removeViewAt(layout.getChildCount()-1);
        }
    };

    private Runnable addLifeRunnable = new Runnable() {
        @Override
        public void run() {
            View spaceship = View.inflate(layout.getContext(), R.layout.view_ship, layout);
        }
    };
}
