package dadm.scaffold.engine;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.counter.GameEvent;
import dadm.scaffold.space.Asteroid;
import dadm.scaffold.space.SpaceShipPlayer;

public class GameController extends GameObject {

    private final long TIME_BETWEEN_ENEMIES = 500;
    private static final int STOPPING_WAVE_WAITING_TIME = 2000;
    private final int INITIAL_LIVES = 3;
    private final int NEXT_WAVE_WAITING_TIME = 3000;

    private long mCurrentMillis;
    private int mEnemiesSpawned;
    private long waitingTime;
    private int playerLifes;
    private List<Asteroid> mAsteroidPool = new ArrayList<Asteroid>();
    private GameControllerStates gameControllerState;

    public GameController(GameEngine gameEngine) {
        double pixelFactor = gameEngine.pixelFactor;
        for (int i=0; i<10; i++) {
            mAsteroidPool.add(new Asteroid(this, gameEngine));
        }
    }

    @Override
    public void startGame(GameEngine gameEngine) {
        mCurrentMillis = 0;
        mEnemiesSpawned = 0;
        waitingTime = 0;

        for(int i = 0; i < INITIAL_LIVES; i++) {
            gameEngine.onGameEnvent(GameEvent.lifeAdded);
        }
        gameControllerState = GameControllerStates.placingSpaceship;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        if(gameControllerState == GameControllerStates.spawningEnemies) {
            mCurrentMillis += elapsedMillis;
            long waveTimestamp = mEnemiesSpawned * TIME_BETWEEN_ENEMIES;

            if (mCurrentMillis > waveTimestamp) {
                // Spawn a new asteroid
                Asteroid a = mAsteroidPool.remove(0);
                a.init(gameEngine);
                a.addToGameEngine(gameEngine);
                //gameEngine.addGameObject(a);
                mEnemiesSpawned++;
                return;
            }
        }
        else if(gameControllerState == GameControllerStates.stoppingWave) {
            waitingTime += elapsedMillis;

            if(waitingTime > STOPPING_WAVE_WAITING_TIME) {
                gameControllerState = GameControllerStates.placingSpaceship;
            }
        }
        else if(gameControllerState == GameControllerStates.placingSpaceship) {
            if(playerLifes == 0) {
                gameEngine.onGameEnvent(GameEvent.gameOver);
            }
            else {
                playerLifes--;
                gameEngine.onGameEnvent(GameEvent.lifeLost);
                SpaceShipPlayer newLife = new SpaceShipPlayer(gameEngine);
                newLife.addToGameEngine(gameEngine);
                newLife.startGame(gameEngine);
                gameControllerState = GameControllerStates.waiting;
                waitingTime = 0;
            }
        }
        else if(gameControllerState == GameControllerStates.waiting) {
            waitingTime += elapsedMillis;

            if(waitingTime > NEXT_WAVE_WAITING_TIME) {
                gameControllerState = GameControllerStates.spawningEnemies;
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if(gameEvent == GameEvent.spaceshipHit) {
            gameControllerState = GameControllerStates.stoppingWave;
            waitingTime = 0;
        }
        else if(gameEvent == GameEvent.gameOver) {
            gameControllerState = GameControllerStates.gameOver;
        }
        else if(gameEvent == GameEvent.lifeAdded) {
            playerLifes++;
        }
    }

    public void returnToPool(Asteroid asteroid) {
        mAsteroidPool.add(asteroid);
    }
}
