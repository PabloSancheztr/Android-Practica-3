package dadm.scaffold.engine;

import java.util.ArrayList;
import java.util.List;

public class Collision {

    private static List<Collision> collisionPool = new ArrayList<Collision>();

    public ScreenGameObject screenObjectA;
    public ScreenGameObject screenObjectB;

    public static Collision init(ScreenGameObject objectA, ScreenGameObject objectB) {
        if(collisionPool.isEmpty()) {
            return new Collision(objectA, objectB);
        }
        return collisionPool.remove(0);
    }

    public static void release(Collision c) {
        collisionPool.add(c);
    }

    public Collision(ScreenGameObject objectA, ScreenGameObject objectB) {
        screenObjectA = objectA;
        screenObjectB = objectB;
    }

    public boolean equals(Collision c) {
        return (screenObjectA == c.screenObjectA && screenObjectB == c.screenObjectB)
                || (screenObjectA == c.screenObjectB && screenObjectB == c.screenObjectA);
    }
}
