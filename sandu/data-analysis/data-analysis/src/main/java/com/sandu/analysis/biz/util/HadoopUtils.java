package com.sandu.analysis.biz.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.sandu.analysis.biz.constant.AnalysisConstants;

public class HadoopUtils {

	/**
	 * 检测hdfs中有没有该目录, 有 -> return true; 没有 -> return false
	 * 
	 * @author huangsongbo
	 * @param hdfsFileDir
	 */
	public static boolean getIsExist(String hdfsFileDir) {
		boolean result = false;
		if (StringUtils.isEmpty(hdfsFileDir)) {
			System.out.println("error, function = mkdirsIfNotExist, message = StringUtils.isEmpty(hdfsFileDir) = true");
			return result;
		}

		Configuration conf = new Configuration();
		FileSystem fs = null;
		try {
			fs = FileSystem.get(URI.create(hdfsFileDir), conf);
			Path dfs = new Path(hdfsFileDir);
			if (fs.exists(dfs)) {
				result = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/**
	 * 删除hdfs目录下的文件包括目录
	 * 
	 * @author huangsongbo
	 * @param path eg: "/usr/local/applogs/newUserIdentity/201905/09" or "hdfs://192.168.1.240:8020/usr/local/applogs/events/201905/09"
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public static void deleteHdfsDir(String pathStr) throws IOException, URISyntaxException {
		pathStr = pathStr.replace(AnalysisConstants.HDFS_DOMAIN, "");
		URI uri = new URI(AnalysisConstants.HDFS_DOMAIN);
		/*Path path = new Path("hdfs://192.168.1.240:8020/usr/local/applogs/events/201905/09");*/
		Path path = new Path(uri.toString() + pathStr);
		
		FileSystem fileSystem = FileSystem.get(uri, new Configuration());
		if(fileSystem.exists(path)) {
			fileSystem.delete(path, true);
		}
	}
	
}
