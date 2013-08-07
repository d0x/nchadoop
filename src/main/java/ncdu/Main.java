package ncdu;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Main
{
//	public static final String	namenode	= "hdfs://carolin:8020/hbase/steamGame";
	public static final String	namenode	= "file:/home/christian/git/nchdfs";

	public static void main(final String[] args) throws URISyntaxException, IOException, InterruptedException
	{
		final URI uri = new URI(namenode);

		final NcFileSystem fileSystemScanner = new NcFileSystem(uri, "hdfs");

		fileSystemScanner.refresh();

		Utils.displayFolder(fileSystemScanner.getSearchRoot());
	}
}
