package Controller;

import View.Window;

public interface State {

    default void loadMap(Controller c, Window w){}

}
