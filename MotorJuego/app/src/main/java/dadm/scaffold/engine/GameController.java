package dadm.scaffold.engine;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.space.Asteroid;

public class GameController extends GameObject {

    private final long TIME_BETWEEN_ENEMIES = 500;

    private long mCurrentMillis;
    private int mEnemiesSpawned;
    private List<Asteroid> mAsteroidPool = new ArrayList<Asteroid>();

    public GameController(GameEngine gameEngine) {
        double pixelFactor = gameEngine.pixelFactor;
        for (int i=0; i<10; i++) {
            mAsteroidPool.add(new Asteroid(this, gameEngine));
        }
    }

    @Override
    public void startGame() {
        mCurrentMillis = 0;
        mEnemiesSpawned = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mCurrentMillis += elapsedMillis;
        long waveTimestamp = mEnemiesSpawned*TIME_BETWEEN_ENEMIES;

        if(mCurrentMillis > waveTimestamp) {
            // Spawn a new asteroid
            Asteroid a = mAsteroidPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            mEnemiesSpawned++;
            return;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {

    }

    public void returnToPool(Asteroid asteroid) {
        mAsteroidPool.add(asteroid);
    }
}
