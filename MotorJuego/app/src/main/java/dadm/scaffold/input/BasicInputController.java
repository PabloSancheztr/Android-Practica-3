package dadm.scaffold.input;

import android.view.MotionEvent;
import android.view.View;

import dadm.scaffold.R;

public class BasicInputController extends InputController implements View.OnTouchListener {

    //Atención: se usa OnTouchListener para saber cuando el botón
    //sigue siendo apretado
    public BasicInputController(View view) {
        view.findViewById(R.id.keypad_up).setOnTouchListener(this);
        view.findViewById(R.id.keypad_down).setOnTouchListener(this);
        view.findViewById(R.id.keypad_left).setOnTouchListener(this);
        view.findViewById(R.id.keypad_right).setOnTouchListener(this);
        view.findViewById(R.id.keypad_fire).setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //Se usa getActionMasked para manejar el multitouch
        int action = event.getActionMasked();
        int id = v.getId();
        if (action == MotionEvent.ACTION_DOWN) {
            // User started pressing a key
            if (id == R.id.keypad_up) {
                verticalFactor -= 1;
            }
            else if (id == R.id.keypad_down) {
                verticalFactor += 1;
            }
            else if (id == R.id.keypad_left) {
                horizontalFactor -= 1;
            }
            else if (id == R.id.keypad_right) {
                horizontalFactor += 1;
            }
            else if (id == R.id.keypad_fire) {
                isFiring = true;
            }
        }
        else if (action == MotionEvent.ACTION_UP) {
            if (id == R.id.keypad_up) {
                verticalFactor += 1;
            }
            else if (id == R.id.keypad_down) {
                verticalFactor -= 1;
            }
            else if (id == R.id.keypad_left) {
                horizontalFactor += 1;
            }
            else if (id == R.id.keypad_right) {
                horizontalFactor -= 1;
            }
            else if (id == R.id.keypad_fire) {
                isFiring = false;
            }
        }
        return false;
    }
}
