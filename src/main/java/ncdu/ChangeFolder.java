package ncdu;

import ncdu.fs.Folder;

import com.googlecode.lanterna.gui.Action;

public class ChangeFolder implements Action
{

	private Folder		folder;
	private NcWindow	ncWindow;

	public ChangeFolder(Folder folder, NcWindow ncWindow)
	{
		this.folder = folder;
		this.ncWindow = ncWindow;
	}

	@Override
	public void doAction()
	{
		ncWindow.changeFolder(folder);
	}

}
