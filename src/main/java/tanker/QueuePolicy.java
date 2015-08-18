package tanker;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueuePolicy {

	public boolean shouldFlush(Queue<Storable> targetQueue) {
		// TODO Auto-generated method stub
		return true;
	}

	public Queue<Storable> getNewQueue() {
		// TODO Auto-generated method stub
		return new LinkedBlockingQueue<Storable>();
	}

}
