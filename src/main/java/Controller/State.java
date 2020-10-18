package Controller;

import View.Window;

public interface State {

    default void loadMap(Controller controller, Window window){}

}
