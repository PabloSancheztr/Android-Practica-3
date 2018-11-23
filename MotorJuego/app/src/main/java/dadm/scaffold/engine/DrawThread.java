package dadm.scaffold.engine;


public class DrawThread extends Thread {

    private final GameEngine gameEngine;
    private boolean isGameRunning = true;
    private boolean isGamePaused = false;

    private Object synchroLock = new Object();

    public DrawThread(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public void start() {
        isGameRunning = true;
        isGamePaused = false;
        super.start();
    }

    public void stopGame() {
        isGameRunning = false;
        resumeGame();
    }

    @Override
    public void run() {
        long elapsedMillis;
        long currentTimeMillis;
        long previousTimeMillis = System.currentTimeMillis();

        while (isGameRunning) {
            currentTimeMillis = System.currentTimeMillis();
            elapsedMillis = currentTimeMillis - previousTimeMillis;
            if (isGamePaused) {
                while (isGamePaused) {
                    try {
                        synchronized (synchroLock) {
                            synchroLock.wait();
                        }
                    } catch (InterruptedException e) {
                        // We stay on the loop
                    }
                }
                currentTimeMillis = System.currentTimeMillis();
            }
            if (elapsedMillis < 20) { // This is 50 fps
                try {
                    Thread.sleep(20-elapsedMillis);
                } catch (InterruptedException e) {
                    // We just continue
                }
            }
            gameEngine.onDraw();
            previousTimeMillis = currentTimeMillis;
        }
    }

    public void pauseGame() {
        isGamePaused = true;
    }

    public void resumeGame() {
        if (isGamePaused == true) {
            isGamePaused = false;
            synchronized (synchroLock) {
                synchroLock.notify();
            }
        }
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public boolean isGamePaused() {
        return isGamePaused;
    }
}

