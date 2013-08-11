package nchadoop;

import java.io.IOException;
import java.net.URI;

import lombok.Data;
import nchadoop.fs.Directory;
import nchadoop.fs.HdfsScanner;
import nchadoop.fs.SearchRoot;
import nchadoop.ui.MainWindow;
import nchadoop.ui.ScanningPopup;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;

@Data
public class Controller
{
	private GUIScreen		guiScreen;
	private MainWindow		mainWindow;
	private ScanningPopup	scanningPopup;
	private HdfsScanner		hdfsScanner;

	public void start(final URI uri)
	{
		// TODO: need to ensure that the init thread runs first!
		this.mainWindow.init();
		this.scanningPopup.show();

		try
		{
			final SearchRoot searchRoot = Controller.this.hdfsScanner.refresh(uri);
			
			Controller.this.mainWindow.getOwner().runInEventThread(new Action() {

				@Override
				public void doAction()
				{
					scanningPopup.close();
					Controller.this.mainWindow.updateSearchRoot(searchRoot);
				}
			});
		}
		catch (final Exception e)
		{
			scanningPopup.close();
			MessageBox.showMessageBox(guiScreen, "Error", "Error: " + e);
			shutdown();
		}
	}

	public void shutdown()
	{
		scanningPopup.close();
		mainWindow.close();
		guiScreen.getScreen().stopScreen();
		hdfsScanner.close();
	}

	public boolean handleGlobalKeyPressed(Window sender, Key key)
	{
		if (key.getCharacter() == 'q' || key.getKind() == Kind.Escape)
		{
			shutdown();
			return true;
		}

		return false;

	}

	public void deleteDiretory(Directory directory)
	{
		if (directory.isRoot())
		{
			MessageBox.showMessageBox(guiScreen, "Error", "Couldn't the search root.");
			return;
		}

		try
		{
			if (!hdfsScanner.deleteDirectory(directory))
			{
				MessageBox.showMessageBox(guiScreen, "Error", "Couldn't delete this.");
			}
			else
			{
				mainWindow.changeFolder(directory.getParent());
			}
		}
		catch (IOException e)
		{
			MessageBox.showMessageBox(guiScreen, "Error", "Error: " + e.getMessage());
		}
	}
}
