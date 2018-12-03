package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.counter.GameEvent;
import dadm.scaffold.engine.GameController;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.Sprite;

public class Asteroid extends Sprite {

    private final GameController mController;
    private final double mSpeed;

    private double mSpeedX;
    private double mSpeedY;
    private double rotationSpeed;

    public Asteroid(GameController gameController, GameEngine gameEngine) {
        super(gameEngine, R.drawable.asteroid, bodyType.Circular);
        mSpeed = 200d*pixelFactor/1000d;
        mController = gameController;
    }

    @Override
    public void startGame(GameEngine gameEngine) {

    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        X += mSpeedX * elapsedMillis;
        Y += mSpeedY * elapsedMillis;
        rotation += rotationSpeed * elapsedMillis;
        if(rotation > 360) {
            rotation = 0;
        }
        else if(rotation < 0) {
            rotation = 360;
        }

        // Check if the sprite goes out of the screen
        if(Y > gameEngine.height) {
            gameEngine.onGameEnvent(GameEvent.asteroidMissed);
            removeFromGameEngine(gameEngine);
        }
    }

    @Override
    public void addToGameEngine(GameEngine gameEngine) {
        super.addToGameEngine(gameEngine);
    }

    @Override
    public void onRemoveFromGameEngine() {
        mController.returnToPool(this);
    }

    @Override
    public void removeFromGameEngine(GameEngine gameEngine) {
        super.removeFromGameEngine(gameEngine);
    }

    public void init(GameEngine gameEngine) {
        double angle = gameEngine.mRandom.nextDouble()*Math.PI/3d-Math.PI/6d;
        mSpeedX = mSpeed * Math.sin(angle);
        mSpeedY = mSpeed * Math.cos(angle);
        X = gameEngine.mRandom.nextInt(gameEngine.width/2) + gameEngine.width/4;
        Y = -height;
        rotationSpeed = angle*(180d / Math.PI) / 250d;
        rotation = gameEngine.mRandom.nextInt(360);
    }

    public void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        mController.returnToPool(this);
    }
}
