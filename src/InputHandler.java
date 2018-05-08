import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

public class InputHandler {
    ArrayList<String> keysPressed;
    Scene scene;

    public InputHandler(Scene scene){
        this.scene = scene;
    }

    private static void prepareActionHandlers(Scene scene)
    {
        // use a set so duplicates are not possible
        ArrayList<String> keysPressed = new ArrayList<String>();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                keysPressed.add(event.getCode().toString());
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                keysPressed.remove(event.getCode().toString());
            }
        });
    }


}
