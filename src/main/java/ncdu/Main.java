package ncdu;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.google.common.base.Stopwatch;

import ncdu.fs.HdfsScanner;

public class Main
{
//	public static final String	namenode	= "hdfs://carolin:8020/user/";
	public static final String	namenode	= "file:///home/christian/Desktop/";

	public static void main(final String[] args) throws URISyntaxException, IOException, InterruptedException
	{
		final URI uri = new URI(namenode);

		final HdfsScanner fileSystemScanner = new HdfsScanner(uri, "hdfs");
		fileSystemScanner.refresh(uri);

		Utils.displayFolder(fileSystemScanner.getSearchRoot());
		
		System.out.println(fileSystemScanner.getSearchRoot());
	}
}
