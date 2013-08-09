package nchadoop.ui;

import nchadoop.fs.Directory;
import nchadoop.ui.listbox.DirectoryListBox;

import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.layout.LinearLayout;

public class DirectoryPanel extends Panel
{
	private DirectoryLabel		currentFolderLabel	= new DirectoryLabel();
	private DirectoryListBox	listBox				= new DirectoryListBox();

	public DirectoryPanel()
	{
		addComponent(this.currentFolderLabel, LinearLayout.MAXIMIZES_HORIZONTALLY);
		addComponent(this.listBox, LinearLayout.MAXIMIZES_HORIZONTALLY, LinearLayout.MAXIMIZES_VERTICALLY);
	}

	public void refresh(final MainWindow ncWindow, final Directory directory)
	{
		this.currentFolderLabel.refresh(directory);
		this.listBox.refresh(ncWindow, directory);
	}

}
