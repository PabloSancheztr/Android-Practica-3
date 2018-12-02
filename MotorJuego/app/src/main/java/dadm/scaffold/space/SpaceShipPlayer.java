package dadm.scaffold.space;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.input.InputController;

public class SpaceShipPlayer extends Sprite {

    private static final int INITIAL_BULLET_POOL_AMOUNT = 6;
    private static final long TIME_BETWEEN_BULLETS = 250;
    List<Bullet> bullets = new ArrayList<Bullet>();
    private long timeSinceLastFire;

    private int maxX;
    private int maxY;
    private double speedFactor;


    public SpaceShipPlayer(GameEngine gameEngine){
        super(gameEngine, R.drawable.ship, bodyType.Circular);
        speedFactor = pixelFactor * 100d / 1000d; // We want to move at 100px per second on a 400px tall screen
        maxX = gameEngine.width - width;
        maxY = gameEngine.height - height;

        initBulletPool(gameEngine);
    }

    private void initBulletPool(GameEngine gameEngine) {
        for (int i=0; i<INITIAL_BULLET_POOL_AMOUNT; i++) {
            bullets.add(new Bullet(gameEngine));
        }
    }

    private Bullet getBullet() {
        if (bullets.isEmpty()) {
            return null;
        }
        return bullets.remove(0);
    }

    void releaseBullet(Bullet bullet) {
        bullets.add(bullet);
    }


    @Override
    public void startGame() {
        X = maxX / 2;
        Y = maxY / 2;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        // Get the info from the inputController
        updatePosition(elapsedMillis, gameEngine.theInputController);
        checkFiring(elapsedMillis, gameEngine);
    }

    @Override
    public void addToGameEngine(GameEngine gameEngine) {
        super.addToGameEngine(gameEngine);
    }

    @Override
    public void removeFromGameEngine(GameEngine gameEngine) {
        super.removeFromGameEngine(gameEngine);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if(otherObject instanceof Asteroid) {
            removeFromGameEngine(gameEngine);
            //gameEngine.stopGame();
            Asteroid a = (Asteroid) otherObject;
            a.removeFromGameEngine(gameEngine);
        }
    }

    private void updatePosition(long elapsedMillis, InputController inputController) {
        X += speedFactor * inputController.horizontalFactor * elapsedMillis;
        if (X < 0) {
            X = 0;
        }
        if (X > maxX) {
            X = maxX;
        }
        Y += speedFactor * inputController.verticalFactor * elapsedMillis;
        if (Y < 0) {
            Y = 0;
        }
        if (Y > maxY) {
            Y = maxY;
        }
    }

    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {
        if (gameEngine.theInputController.isFiring && timeSinceLastFire > TIME_BETWEEN_BULLETS) {
            Bullet bullet = getBullet();
            if (bullet == null) {
                return;
            }
            bullet.init(this, X + width/2, Y);
            gameEngine.addGameObject(bullet);
            timeSinceLastFire = 0;
        }
        else {
            timeSinceLastFire += elapsedMillis;
        }
    }

}
