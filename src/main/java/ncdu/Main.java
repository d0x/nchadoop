package ncdu;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.google.common.base.Stopwatch;

import ncdu.fs.HdfsScanner;
import ncdu.fs.SearchRoot;
import ncdu.ui.MainWindow;

public class Main
{
//	public static final String	namenode	= "hdfs://carolin:8020/user/";
	public static final String	namenode	= "file:///home/christian/Desktop/";

	public static void main(final String[] args) throws URISyntaxException, IOException, InterruptedException
	{
		URI uri = new URI(Main.namenode);
		MainWindow mainWindow = new MainWindow();
		HdfsScanner hdfsScanner = new HdfsScanner(uri, "hdfs");
		SearchRoot searchRoot = hdfsScanner.refresh(uri);
		mainWindow.refresh(searchRoot);
	}
}
