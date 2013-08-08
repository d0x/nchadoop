package ncdu;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import ncdu.fs.NcFileSystem;

public class Main
{
//	public static final String	namenode	= "hdfs://carolin:8020/user/";
	public static final String	namenode	= "file:///home/christian/Desktop/shared";

	public static void main(final String[] args) throws URISyntaxException, IOException, InterruptedException
	{
		final URI uri = new URI(namenode);

		final NcFileSystem fileSystemScanner = new NcFileSystem(uri, "hdfs");
		fileSystemScanner.refresh();

	}
}
