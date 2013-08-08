package ncdu.ui;

import ncdu.fs.Folder;

import com.googlecode.lanterna.gui.Action;

public class ChangeFolder implements Action
{

	private Folder		folder;
	private MainWindow	ncWindow;

	public ChangeFolder(final Folder folder, final MainWindow ncWindow)
	{
		this.folder = folder;
		this.ncWindow = ncWindow;
	}

	@Override
	public void doAction()
	{
		this.ncWindow.changeFolder(this.folder);
	}

}
