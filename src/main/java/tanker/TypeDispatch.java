package tanker;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TypeDispatch implements Closeable {
	private Map<Class<? extends Storable>, Buffer<? extends Storable>> buffers = new HashMap<>();
	
	public void put(Storable o) throws IOException {
		Class<? extends Storable> c = o.getClass();
		getBuffer(c).put(o);
	}
	
	private Buffer<? extends Storable> getBuffer(Class<? extends Storable> c) {	
		if(!buffers.containsKey(c)) {
			buffers.put(c, new Buffer(c));
		}
		return buffers.get(c);
	}

	public void close() throws IOException {
		for(Buffer b : buffers.values()) {
			b.close();
		}
	}

}
