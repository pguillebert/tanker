package tanker;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Buffer<T extends Storable> implements Closeable {
	Map<List<String>, Queue<T>> paths = new HashMap<>();
	private QueuePolicy<T> queuePolicy;
	private SchemaHelper<T> helper;
	
	public Buffer(Class<T> c) {
		this.helper = new SchemaHelper<>(c);
		this.queuePolicy = new QueuePolicy<>();
	}
	
	public void put(Storable o) throws IOException {
		List<String> path = getStoragePath(o);
		Queue<T> targetQueue = getOrCreateQueue(path);
		targetQueue.add((T) o);
		
		// Check if we need to flush the queue
		if(queuePolicy.shouldFlush(targetQueue)) {
			close(path, targetQueue);
		}
	}

	private Queue<T> getOrCreateQueue(List<String> path) {	
		if(!paths.containsKey(path)) {
			paths.put(path, queuePolicy.getNewQueue());
		}
		return paths.get(path);
	}

	public void close(List<String> path, Collection<T> q) throws IOException {
		helper.write(path, q);
		paths.remove(path);
	}

	public void close() throws IOException {
		for(Map.Entry<List<String>, Queue<T>> s : paths.entrySet()) {
			close(s.getKey(), s.getValue());
		}
	}
	
	private List<String> getStoragePath(Storable o) {
		List<String> path = new ArrayList<String>();
		path.add(o.getClass().getName());
		path.addAll(o.getStoragePath());
		return path;
	}
}
