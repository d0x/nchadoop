package ncdu.ui;

import ncdu.fs.Directory;

import com.googlecode.lanterna.gui.Action;

public class ChangeFolder implements Action
{

	private Directory		folder;
	private MainWindow	ncWindow;

	public ChangeFolder(final Directory folder, final MainWindow ncWindow)
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
