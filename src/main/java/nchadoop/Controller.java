package nchadoop;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import lombok.Data;
import nchadoop.fs.HdfsScanner;
import nchadoop.fs.SearchRoot;
import nchadoop.ui.MainWindow;
import nchadoop.ui.ScanningPopup;

import com.googlecode.lanterna.gui.Action;

@Data
public class Controller
{
	private final MainWindow	mainWindow;
	private final ScanningPopup	scanningPopup;

	private final HdfsScanner	hdfsScanner;

	public void start(final URI uri)
	{
		// TODO: need to ensure that the init thread runs first!
		this.mainWindow.init();
		this.scanningPopup.show();

		try
		{
			final SearchRoot searchRoot = Controller.this.hdfsScanner.refresh(uri);

			this.scanningPopup.close();

			Controller.this.mainWindow.getOwner().runInEventThread(new Action() {

				@Override
				public void doAction()
				{
					Controller.this.mainWindow.updateSearchRoot(searchRoot);
				}
			});
		}
		catch (final FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
}
