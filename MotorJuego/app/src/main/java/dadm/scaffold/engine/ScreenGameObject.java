package dadm.scaffold.engine;

import android.graphics.Paint;
import android.graphics.Rect;

public abstract class ScreenGameObject extends GameObject {

    protected double X;
    protected double Y;
    protected int height;
    protected int width;
    protected double radius;

    public Rect boundingRect = new Rect(-1, -1, -1, -1);

    public enum bodyType {
        None,
        Circular,
        Rectangular
    }
    public bodyType objectBodyType;

    public boolean checkCollision(ScreenGameObject otherObject) {
        if(objectBodyType == bodyType.Circular && otherObject.objectBodyType == bodyType.Circular) {
            return checkCircularCollision(otherObject);
        }
        else if(objectBodyType == bodyType.Rectangular && otherObject.objectBodyType == bodyType.Rectangular) {
            return checkRectangularCollision(otherObject);
        }
        else {
            return checkMixedCollidion(otherObject);
        }
    }

    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {

    }

    @Override
    public void onPostUpdate(GameEngine gameEngine) {
        boundingRect.set((int) X, (int) Y, (int) X+width, (int) Y+height);
    }

    private boolean checkRectangularCollision(ScreenGameObject otherObject) {
        return Rect.intersects(boundingRect, otherObject.boundingRect);
    }

    private boolean checkCircularCollision(ScreenGameObject otherObject) {
        double distanceX = (X+width/2) - (otherObject.X + otherObject.width/2);
        double distanceY = (Y+height/2) - (otherObject.Y + otherObject.height/2);
        double squareDistance = distanceX*distanceX + distanceY*distanceY;
        double collisionDistance = (radius + otherObject.radius);
        return squareDistance <= collisionDistance*collisionDistance;
    }

    private boolean checkMixedCollidion(ScreenGameObject otherObject) {
        ScreenGameObject circularSprite;
        ScreenGameObject rectangularSprite;
        if(objectBodyType == bodyType.Rectangular) {
            circularSprite = this;
            rectangularSprite = otherObject;
        }
        else {
            circularSprite = otherObject;
            rectangularSprite = this;
        }

        double circleCenterX = circularSprite.X + circularSprite.width/2;
        double positionXToCheck = circleCenterX;
        if(circleCenterX < rectangularSprite.X) {
            positionXToCheck = rectangularSprite.X;
        }
        else if(circleCenterX > rectangularSprite.X + rectangularSprite.width) {
            positionXToCheck = rectangularSprite.X + rectangularSprite.width;
        }
        double distanceX = circleCenterX - positionXToCheck;

        double circleCenterY = circularSprite.Y + circularSprite.height/2;
        double positionYToCheck = circleCenterY;
        if(circleCenterY < rectangularSprite.Y) {
            positionYToCheck = rectangularSprite.Y;
        }
        else if(circleCenterY > rectangularSprite.Y + rectangularSprite.height) {
            positionYToCheck = rectangularSprite.Y + rectangularSprite.height;
        }
        double distanceY = circleCenterY - positionYToCheck;

        double squareDistance = distanceX*distanceX + distanceY*distanceY;
        if(squareDistance <= circularSprite.radius*circularSprite.radius) {
            return true;
        }
        return false;
    }
}
