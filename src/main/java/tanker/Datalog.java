package tanker;

import java.util.ArrayList;
import java.util.List;

public class Datalog implements Storable {
	private String domain;
	private String type;
	private long date;
	private String message;
	
	public Datalog(String domain, String type, long date, String message) {
		super();
		this.domain = domain;
		this.type = type;
		this.date = date;
		this.message = message;
	}

	public Datalog() {
		
	}
	
	public List<String> getStoragePath() {
		List<String> ret = new ArrayList<String>();
		ret.add(domain);
		ret.add(type);
		return ret;
	}
	
	public String toString() {
		return this.domain + " " + this.type + " " + this.date + " " + this.message;
	}
}
