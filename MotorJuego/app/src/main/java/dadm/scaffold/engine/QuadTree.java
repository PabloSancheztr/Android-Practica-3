package dadm.scaffold.engine;

import android.graphics.Rect;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class QuadTree {

    private static int MAX_OBJECTS_TO_CHECK = 8;
    private static List<QuadTree> QuadTreePool = new ArrayList<QuadTree>();

    private Rect areaRect = new Rect();
    private Rect tempRect = new Rect();
    private List<ScreenGameObject> gameObjects = new ArrayList<ScreenGameObject>();
    private QuadTree[] children = new QuadTree[4];

    public void setArea(Rect area) {
        area.set(area);
    }

    public void checkCollisions(GameEngine gameEngine, List<Collision> detectedCollision) {
        int numObjects = gameObjects.size();
        if(numObjects > MAX_OBJECTS_TO_CHECK && QuadTreePool.size() >= 4) {
            splitAndCheck(gameEngine, detectedCollision);
        }
        else {
            for(int i = 0; i < numObjects; i++) {
                ScreenGameObject objectA = gameObjects.get(i);
                for (int j = i + 1; j < numObjects; j++) {
                    ScreenGameObject objectB = gameObjects.get(j);
                    if(objectA.checkCollision(objectB)) {
                        Collision c = Collision.init(objectA, objectB);
                        if(!hasBeenDetected(detectedCollision, c)) {
                            detectedCollision.add(c);
                            objectA.onCollision(gameEngine, objectB);
                            objectB.onCollision(gameEngine, objectA);
                        }
                    }
                }
            }
        }
    }

    public void checkObjects(List<ScreenGameObject> gameObjectsList) {
        gameObjects.clear();
        int numObjects = gameObjectsList.size();
        for(int i = 0; i < numObjects; i++) {
            ScreenGameObject current = gameObjectsList.get(i);
            Rect boundingRect = current.boundingRect;
            if(Rect.intersects(boundingRect, areaRect)) {
                gameObjects.add(current);
            }
        }
    }

    public void addGameObject(ScreenGameObject objectToAdd) {
        gameObjects.add(objectToAdd);
    }

    public void removeGameObject(ScreenGameObject objectToRemove) {
        gameObjects.remove(objectToRemove);
    }

    private boolean hasBeenDetected(List<Collision> detectedCollision, Collision c) {
        int numCollisions = detectedCollision.size();
        for(int i = 0; i < numCollisions; i++) {
            if(detectedCollision.get(i).equals(c)) {
                return true;
            }
        }
        return false;
    }

    private void splitAndCheck(GameEngine gameEngine, List<Collision> detectedCollision) {
        for(int i=0; i<4; i++) {
            children[i] = QuadTreePool.remove(0);
        }
        for(int i=0; i<4; i++) {
            children[i].setArea(getArea(i));
            children[i].checkObjects(gameObjects);
            children[i].checkCollisions(gameEngine, detectedCollision);
            children[i].gameObjects.clear();
            QuadTreePool.add(children[i]);
        }
    }

    private Rect getArea(int area) {
        int startX = areaRect.left;
        int startY = areaRect.top;
        int width = areaRect.width();
        int height = areaRect.height();

        switch (area) {
            case 0:
                tempRect.set(startX, startY, startX+width/2, startY+height/2);
                break;

            case 1:
                tempRect.set(startX+width/2, startY, startX+width, startY+height/2);
                break;

            case 2:
                tempRect.set(startX, startY+height/2, startX+width/2, startY+height);
                break;

            case 3:
                tempRect.set(startX+width/2, startY+height/2, startX+width, startY+height);
                break;
        }
        return tempRect;
    }
}
