package dadm.scaffold.engine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dadm.scaffold.counter.GameEvent;
import dadm.scaffold.input.InputController;

public class GameEngine {

    private List<GameObject> gameObjects = new ArrayList<GameObject>();
    private List<GameObject> objectsToAdd = new ArrayList<GameObject>();
    private List<GameObject> objectsToRemove = new ArrayList<GameObject>();
    private List<ScreenGameObject> collisionableObjects = new ArrayList<ScreenGameObject>();
    private List<Collision> detectedCollision = new ArrayList<Collision>();

    private UpdateThread theUpdateThread;
    private DrawThread theDrawThread;
    public InputController theInputController;
    public Random mRandom = new Random();
    private final GameView theGameView;
    private QuadTree quadTree = new QuadTree();

    public int width;
    public int height;
    public double pixelFactor;

    public enum GameEnvent {

    }

    private Activity mainActivity;

    public GameEngine(Activity activity, GameView gameView) {
        mainActivity = activity;

        theGameView = gameView;
        theGameView.setGameObjects(this.gameObjects);
        this.width = theGameView.getWidth()
                - theGameView.getPaddingRight() - theGameView.getPaddingLeft();
        this.height = theGameView.getHeight()
                - theGameView.getPaddingTop() - theGameView.getPaddingTop();

        this.pixelFactor = this.height / 400d;

        quadTree.setArea(new Rect(0, 0, width, height));
    }

    public void setTheInputController(InputController inputController) {
        theInputController = inputController;
    }

    public void startGame() {
        // Stop a game if it is running
        stopGame();

        // Setup the game objects
        int numGameObjects = gameObjects.size();
        for (int i = 0; i < numGameObjects; i++) {
            gameObjects.get(i).startGame(this);
        }

        // Start the update thread
        theUpdateThread = new UpdateThread(this);
        theUpdateThread.start();

        // Start the drawing thread
        theDrawThread = new DrawThread(this);
        theDrawThread.start();
    }

    public void stopGame() {
        if (theUpdateThread != null) {
            theUpdateThread.stopGame();
        }
        if (theDrawThread != null) {
            theDrawThread.stopGame();
        }
    }

    public void pauseGame() {
        if (theUpdateThread != null) {
            theUpdateThread.pauseGame();
        }
        if (theDrawThread != null) {
            theDrawThread.pauseGame();
        }
    }

    public void resumeGame() {
        if (theUpdateThread != null) {
            theUpdateThread.resumeGame();
        }
        if (theDrawThread != null) {
            theDrawThread.resumeGame();
        }
    }

    public void addGameObject(GameObject gameObject) {
        if (isRunning()) {
            objectsToAdd.add(gameObject);
        } else {
            addToGame(gameObject);
        }
        mainActivity.runOnUiThread(gameObject.onAddedRunnable);
    }

    public void removeGameObject(GameObject gameObject) {
        objectsToRemove.add(gameObject);
        mainActivity.runOnUiThread(gameObject.onRemovedRunnable);
    }

    public void onUpdate(long elapsedMillis) {
        int numGameObjects = gameObjects.size();
        for (int i = 0; i < numGameObjects; i++) {
            gameObjects.get(i).onUpdate(elapsedMillis, this);
            gameObjects.get(i).onPostUpdate(this);
        }
        checkCollisions();
        synchronized (gameObjects) {
            while (!objectsToRemove.isEmpty()) {
                GameObject objectToRemove = objectsToRemove.remove(0);
                gameObjects.remove(objectToRemove);
                if(objectToRemove instanceof ScreenGameObject) {
                    quadTree.removeGameObject((ScreenGameObject) objectToRemove);
                }
                objectToRemove.onRemoveFromGameEngine();
            }
            while (!objectsToAdd.isEmpty()) {
                GameObject gameObject = objectsToAdd.remove(0);
                addToGame(gameObject);
            }
        }
    }

    private void checkCollisions() {
        while(!detectedCollision.isEmpty()) {
            Collision.release(detectedCollision.remove(0));
        }
        quadTree.checkCollisions(this, detectedCollision);
    }

    private void addToGame(GameObject gameObject) {
        gameObjects.add(gameObject);
        if(gameObject instanceof ScreenGameObject) {
            ScreenGameObject sgo = (ScreenGameObject) gameObject;
            if(sgo.objectBodyType != ScreenGameObject.bodyType.None) {
                quadTree.addGameObject(sgo);
            }
        }
        gameObject.onAddedToGameEngine();
    }

    public void onDraw() {
        theGameView.draw();
    }

    public boolean isRunning() {
        return theUpdateThread != null && theUpdateThread.isGameRunning();
    }

    public boolean isPaused() {
        return theUpdateThread != null && theUpdateThread.isGamePaused();
    }

    public Context getContext() {
        return theGameView.getContext();
    }

    public void onGameEnvent(GameEvent gameEvent) {
        int numObjects = gameObjects.size();
        for(int i = 0; i < numObjects; i++) {
            gameObjects.get(i).onGameEvent(gameEvent);
        }
    }
}
