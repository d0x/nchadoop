package ncdu.ui;

import ncdu.fs.Folder;
import ncdu.ui.components.SingleWindowUi;

import com.googlecode.lanterna.gui.Component.Alignment;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.gui.layout.BorderLayout;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.input.Key;

public class MainWindow extends SingleWindowUi
{
	public static void main(final String[] args)
	{
		new MainWindow();
	}

	private FolderActionListBox	items	= new FolderActionListBox();

	private Label				header	= new Label("nchdfs 1.0.0-SNAPSHOT ~ Use the arrow keys to navigate, press ? for help", true);
	private Label				footer	= new Label("Total Disk usage: 26 GB", true);

	private Folder				currentFolder;

	public MainWindow()
	{
		this.header.setAlignment(Alignment.LEFT_CENTER);
		this.footer.setAlignment(Alignment.LEFT_CENTER);

		this.contentPane.addComponent(this.header, BorderLayout.TOP);
		this.contentPane.addComponent(this.items, BorderLayout.CENTER);
		this.contentPane.addComponent(this.footer, BorderLayout.BOTTOM);

		// Those have to be exectued at last
		addComponent(this.contentPane, LinearLayout.MAXIMIZES_HORIZONTALLY, LinearLayout.MAXIMIZES_VERTICALLY);
		this.gui.showWindow(this, GUIScreen.Position.FULL_SCREEN);
	}

	private void refresh()
	{
		this.items.setCurrent(this.currentFolder);
		this.items.addFolder(this, this.currentFolder.getFolders());
		this.items.addFile(this, this.currentFolder.getFiles());

		this.items.refresh(this);
	}

	@Override
	public void onKeyPressed(final Key key)
	{
		switch (key.getKind())
		{
			case Escape:
				close();
				break;

			default:
				break;
		}

		switch (key.getCharacter())
		{
			case 'd':
				MessageBox.showMessageBox(getOwner(), "Error", "Delete is not supported yet.");
				break;

			case 'q':
				close();
				break;

			default:
				break;
		}

		super.onKeyPressed(key);
	}

	public void changeFolder(final Folder folder)
	{
		this.currentFolder = folder;

		this.header.setText("Current Path: " + absolutFolderName(folder));

		refresh();
	}

	private String absolutFolderName(final Folder folder)
	{
		if (folder.getParent() != null)
		{
			return absolutFolderName(folder.getParent()) + folder.getName() + "/";
		}

		return "/";
	}
}
