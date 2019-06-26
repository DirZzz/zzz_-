package com.sandu.analysis.test.offline;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class DeleteHdfsDirTest {

	public static void main(String[] args) throws URISyntaxException, IOException {
		URI uri = new URI("hdfs://192.168.1.240:8020");
		Path path = new Path("hdfs://192.168.1.240:8020/usr/local/applogs/events/201905/09");
		
		FileSystem fileSystem = FileSystem.get(uri, new Configuration());
		fileSystem.delete(path, true);
	}
	
}
