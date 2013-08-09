package nchadoop;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import nchadoop.fs.HdfsScanner;
import nchadoop.fs.SearchRoot;
import nchadoop.ui.MainWindow;

public class Main
{
//	public static final String	namenode	= "hdfs://carolin:8020/user/";
	public static final String	namenode	= "file:///home/christian/Desktop/shared/";

	public static void main(final String[] args) throws URISyntaxException, IOException, InterruptedException
	{
		final URI uri = new URI(Main.namenode);
		final MainWindow mainWindow = new MainWindow();
		final HdfsScanner hdfsScanner = new HdfsScanner(uri, "hdfs");
		final SearchRoot searchRoot = hdfsScanner.refresh(uri);

		mainWindow.refresh(searchRoot);
	}
}
