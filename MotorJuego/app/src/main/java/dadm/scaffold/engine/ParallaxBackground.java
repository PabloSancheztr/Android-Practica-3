package dadm.scaffold.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ParallaxBackground extends GameObject {

    private final Matrix matrix = new Matrix();

    private Bitmap bitmap;

    private double pixelFactor;
    private double speedY;
    private double positionY;
    private double imageHeight;
    private double imageWidth;
    private double screenHeight;
    private double screenWidth;
    private double targetWidth;

    public ParallaxBackground(GameEngine gameEngine, int speed, int drawableResId) {
        Drawable spriteDrawable = gameEngine.getContext().getResources().getDrawable(drawableResId);
        bitmap = ((BitmapDrawable) spriteDrawable).getBitmap();

        pixelFactor = gameEngine.pixelFactor;
        speedY = speed*pixelFactor/1000d;

        imageHeight = spriteDrawable.getIntrinsicHeight()*pixelFactor;
        imageWidth = spriteDrawable.getMinimumWidth()*pixelFactor;

        screenHeight = gameEngine.height;
        screenWidth = gameEngine.width;

        targetWidth = Math.min(imageWidth, screenWidth);
    }

    @Override
    public void startGame(GameEngine gameEngine) {
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        positionY += speedY * elapsedMillis;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(positionY > 0) {
            matrix.reset();
            matrix.postScale((float) (pixelFactor), (float) (pixelFactor));
            matrix.postTranslate(0, (float) (positionY-imageHeight));
            canvas.drawBitmap(bitmap, matrix, null);
        }
        matrix.reset();
        matrix.postScale((float) (pixelFactor), (float) (pixelFactor));
        matrix.postTranslate(0, (float) positionY);
        canvas.drawBitmap(bitmap, matrix, null);

        if(positionY > screenHeight) {
            positionY -= imageHeight;
        }
    }
}
