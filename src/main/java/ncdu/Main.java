package ncdu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import ncdu.fs.NcFileSystem;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.GUIScreen.Position;
import com.googlecode.lanterna.screen.Screen;

public class Main
{
//	public static final String	namenode	= "hdfs://carolin:8020/hbase/steamGame";
	public static final String	namenode	= "file:/home/christian/Documents";

	public static void main(final String[] args) throws URISyntaxException, IOException, InterruptedException
	{
		final URI uri = new URI(namenode);
		
		final NcFileSystem fileSystemScanner = new NcFileSystem(uri, "hdfs");
		fileSystemScanner.refresh();
		
		final GUIScreen gui = TerminalFacade.createGUIScreen();
		
		gui.setTheme(new NcTheme());

		final Screen screen = gui.getScreen();
		screen.startScreen();

		gui.showWindow(new NcWindow(screen, fileSystemScanner.getSearchRoot()), Position.FULL_SCREEN);

	}
}
