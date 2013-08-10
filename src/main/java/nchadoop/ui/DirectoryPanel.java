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

	public void updateDirectory(final MainWindow ncWindow, final Directory directory)
	{
		this.currentFolderLabel.updateDirectory(directory);
		this.listBox.updateDirectory(ncWindow, directory);
	}

}
