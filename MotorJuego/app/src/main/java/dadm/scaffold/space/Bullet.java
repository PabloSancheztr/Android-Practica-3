package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;

public class Bullet extends Sprite {

    private double speedFactor;

    private SpaceShipPlayer parent;

    public Bullet(GameEngine gameEngine){
        super(gameEngine, R.drawable.bullet, bodyType.Rectangular);

        speedFactor = gameEngine.pixelFactor * -300d / 1000d;
    }

    @Override
    public void startGame() {}

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        Y += speedFactor * elapsedMillis;
        if (Y < -height) {
            //gameEngine.removeGameObject(this);
            // And return it to the pool
            //parent.releaseBullet(this);
            removeFromGameEngine(gameEngine);
        }
    }

    @Override
    public void onRemoveFromGameEngine() {
        parent.releaseBullet(this);
    }

    public void init(SpaceShipPlayer parentPlayer, double initPositionX, double initPositionY) {
        X = initPositionX - width/2;
        Y = initPositionY - height/2;
        parent = parentPlayer;
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if(otherObject instanceof Asteroid) {
            removeFromGameEngine(gameEngine);
            Asteroid asteroid = (Asteroid) otherObject;
            asteroid.removeFromGameEngine(gameEngine);
        }
    }
}
