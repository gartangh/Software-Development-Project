package eventbroker;

import java.util.ArrayList;

public interface EventListener {

	void handleEvent(Event e);

	void handleEvent(Event e, ArrayList<Integer> destinations);

}
