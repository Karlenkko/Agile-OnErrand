package View;

import Model.Observable;

public interface Observer {
	void update(Observable observed, Object arg);
}
