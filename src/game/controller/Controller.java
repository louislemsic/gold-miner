package game.controller;

/**
 * An Abstract class for all controllers to extend. This allows the controllers to share the existing GoldMiner Game.
 * @author James Lemsic
 * @author Jordan Sibug
 * @version 1.0
 */
public abstract class Controller {

    protected MainController controller;

    /**
     * Allows every controller to access the Main Controller by setting their ControllerMain as the main controller.
     * @param controller the generated ControllerMain in Driver.java
     */
    public void setMainController(MainController controller) {
        this.controller = controller;
    }

}
