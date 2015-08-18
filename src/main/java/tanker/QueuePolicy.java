package tanker;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueuePolicy<T> {

	public boolean shouldFlush(Queue<T> targetQueue) {
		// TODO Auto-generated method stub
		return true;
	}

	public Queue<T> getNewQueue() {
		// TODO Auto-generated method stub
		return new LinkedBlockingQueue<T>();
	}

}
