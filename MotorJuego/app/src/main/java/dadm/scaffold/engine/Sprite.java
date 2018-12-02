package dadm.scaffold.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public abstract class Sprite extends ScreenGameObject {

    private final Matrix matrix = new Matrix();
    private final Paint paint = new Paint();
    private final Bitmap bitmap;

    protected double rotation;
    protected double rotationSpeed;

    protected double pixelFactor;

    protected Sprite (GameEngine gameEngine, int drawableRes, bodyType OBodyType) {
        Resources r = gameEngine.getContext().getResources();
        Drawable spriteDrawable = r.getDrawable(drawableRes);

        this.pixelFactor = gameEngine.pixelFactor;

        height = (int) (spriteDrawable.getIntrinsicHeight() * this.pixelFactor);
        width = (int) (spriteDrawable.getIntrinsicWidth() * this.pixelFactor);
        radius = Math.max(height, width)/2;

        this.bitmap = ((BitmapDrawable) spriteDrawable).getBitmap();

        objectBodyType = OBodyType;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (X > canvas.getWidth()
                || Y > canvas.getHeight()
                || X < -width
                || Y < -height) {
            return;
        }
        /*paint.setColor(Color.YELLOW);
        if(objectBodyType == bodyType.Circular) {
            canvas.drawCircle((int) (X+width/2),
                    (int) (Y+height/2),
                    (int) radius,
                    paint);
        }
        else if(objectBodyType == bodyType.Rectangular) {
            canvas.drawRect(boundingRect, paint);
        }*/

        matrix.reset();
        matrix.postScale((float) pixelFactor, (float) pixelFactor);
        matrix.postTranslate((float) X, (float) Y);
        matrix.postRotate((float) rotation, (float) (X + width/2), (float) (Y + height/2));
        canvas.drawBitmap(bitmap, matrix, null);
    }
}
