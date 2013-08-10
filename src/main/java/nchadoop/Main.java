package nchadoop;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import nchadoop.fs.HdfsScanner;
import nchadoop.ui.MainWindow;
import nchadoop.ui.ScanningPopup;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen;

public class Main
{
//	public static final String	namenode	= "hdfs://carolin:8020/user/";
	public static final String	namenode	= "file:///home/christian/Desktop/shared";

	public static void main(final String[] args) throws URISyntaxException, IOException, InterruptedException
	{
		final URI uri = new URI(Main.namenode);

		// Create Hdfs
		final HdfsScanner hdfsScanner = new HdfsScanner(uri, "hdfs");

		// Create windows
		final GUIScreen gui = TerminalFacade.createGUIScreen();
		final MainWindow mainWindow = new MainWindow(gui);
		final ScanningPopup scanningPopup = new ScanningPopup(gui);

		final Controller controller = new Controller(mainWindow, scanningPopup, hdfsScanner);

		controller.start(uri);
	}
}
