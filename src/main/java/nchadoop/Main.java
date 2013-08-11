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
	public static void main(final String... args) throws URISyntaxException, IOException, InterruptedException
	{
		final URI uri = new URI(args[0]);

		// Create components
		final HdfsScanner hdfsScanner = new HdfsScanner(uri, "hdfs");

		final GUIScreen guiScreen = TerminalFacade.createGUIScreen();
		final MainWindow mainWindow = new MainWindow(guiScreen);
		final ScanningPopup scanningPopup = new ScanningPopup();

		final Controller controller = new Controller();

		// wire them
		mainWindow.setController(controller);

		scanningPopup.setGui(guiScreen);
		scanningPopup.setController(controller);

		controller.setGuiScreen(guiScreen);
		controller.setMainWindow(mainWindow);
		controller.setScanningPopup(scanningPopup);
		controller.setHdfsScanner(hdfsScanner);

		controller.start(uri);
	}
}
