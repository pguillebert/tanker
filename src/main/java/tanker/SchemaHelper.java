package tanker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.reflect.ReflectDatumWriter;

import com.google.common.base.Joiner;

public class SchemaHelper<T> {
	private Schema schema;
	private ReflectDatumWriter<T> datumWriter;
	private ReflectDatumReader<T> datumReader;
	
	public SchemaHelper(Class<T> c) {
		this.schema = ReflectData.get().getSchema(c);
		this.datumWriter = new ReflectDatumWriter<T>(schema);
		this.datumReader = new ReflectDatumReader<T>(schema);
	}
	
	public void write(List<String> path, Collection<T> q) throws IOException {
		List<String> lpath = new ArrayList<String>(path);
		lpath.add(UUID.randomUUID().toString() + ".avro");
		String finalPath = Joiner.on("/").join(lpath);
		File file = new File(finalPath);
		file.getParentFile().mkdirs();

		DataFileWriter<T> dataFileWriter = new DataFileWriter<T>(datumWriter);
		DataFileWriter<T> dfw = dataFileWriter.create(schema, file);

		for(T obj : q) {
			dfw.append(obj);
		}
		dfw.close();
		dataFileWriter.close();
	}

    public Collection<T> read(List<String> path) throws IOException {
		List<String> lpath = new ArrayList<String>(path);
		Collection<T> all = new ArrayList<T>();
		File dir = new File(Joiner.on("/").join(lpath));
		// TODO ensure dir is actually a directory
		System.out.println(dir);
		for(File file : dir.listFiles()) {
			// TODO do not fill ram
			DataFileReader<T> dfr = new DataFileReader<T>(file, datumReader);
			all.addAll(consume(dfr));
		}
		return all;
    }

	private Collection<T> consume(DataFileReader<T> dfr) {
		Collection<T> a = new ArrayList<T>();		
		while(dfr.hasNext()) {
			a.add(dfr.next());
		}
		return a;
	}
}
