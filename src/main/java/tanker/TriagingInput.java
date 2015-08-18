package tanker;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class TriagingInput implements Closeable {
	Map<List<String>, Queue<Storable>> 
		paths = new HashMap<List<String>, Queue<Storable>>();
	private QueuePolicy queuePolicy;
	private SchemaHelper schemaHelper;
	
	public TriagingInput(QueuePolicy qp, SchemaHelper s) {
		this.queuePolicy = qp;
		this.schemaHelper = s;
	}
	
	public void put(Storable o) throws IOException {
		Class<? extends Storable> c = o.getClass();
		List<String> path = new ArrayList<String>();
		path.add(c.getName());
		path.addAll(o.getStoragePath());
		
		Queue<Storable> targetQueue = getOrCreateQueue(path);
		targetQueue.add(o);
		
		// Check if we need to flush the queue
		if(queuePolicy.shouldFlush(targetQueue)) {
			close(path, targetQueue);
		}
	}

	private Queue<Storable> getOrCreateQueue(List<String> path) {	
		if(!paths.containsKey(path)) {
			paths.put(path, queuePolicy.getNewQueue());
		}
		return paths.get(path);
	}

	public void close(List<String> path, Collection<Storable> q) throws IOException {
		schemaHelper.write(path, q);
		paths.remove(path);
	}

	public void close() throws IOException {
		for(Map.Entry<List<String>, Queue<Storable>> s : paths.entrySet()) {
			close(s.getKey(), s.getValue());
		}
	}
}
