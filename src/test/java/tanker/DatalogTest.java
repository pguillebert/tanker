package tanker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

public class DatalogTest extends TestCase {

	public void testWrite() throws IOException {
		Datalog dl = new Datalog("dom", "type", System.currentTimeMillis(), "mmssgg");

		TypeDispatch ti = new TypeDispatch();
		ti.put(dl);
		System.out.println("Written 1 elmt");
		ti.close();
	}	


	public void testRead() throws IOException {
		List<String> path = new ArrayList<String>();
		path.add(Datalog.class.getName());
		path.add("dom");
		path.add("type");
		
		SchemaHelper<Datalog> sh = new SchemaHelper<Datalog>(Datalog.class);
		
		Collection<Datalog> coll = sh.read(path);
		for(Datalog d : coll) {
			System.out.println(d);
		}
	}	
}
