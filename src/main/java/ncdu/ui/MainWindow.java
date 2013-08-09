package ncdu.ui;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import ncdu.Main;
import ncdu.fs.Directory;
import ncdu.fs.HdfsScanner;
import ncdu.fs.SearchRoot;
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
	private FolderActionListBox	items	= new FolderActionListBox();

	private Label				header	= new Label("nchdfs 1.0.0-SNAPSHOT ~ Use the arrow keys to navigate, press ? for help", true);
	private Label				footer	= new Label("Total Disk usage: 26 GB", true);

	public MainWindow()
	{
		this.header.setAlignment(Alignment.LEFT_CENTER);
		this.footer.setAlignment(Alignment.LEFT_CENTER);

		this.contentPane.addComponent(this.header, BorderLayout.TOP);
		this.contentPane.addComponent(this.items, BorderLayout.CENTER);
		this.contentPane.addComponent(this.footer, BorderLayout.BOTTOM);
	}

	public void refresh(SearchRoot searchRoot)
	{
		this.items.refresh(this, searchRoot);

		// Those have to be exectued at last
		addComponent(this.contentPane, LinearLayout.MAXIMIZES_HORIZONTALLY, LinearLayout.MAXIMIZES_VERTICALLY);
		this.gui.showWindow(this, GUIScreen.Position.FULL_SCREEN);
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

	public void changeFolder(final Directory directory)
	{
		header.setText("Current Path: " + absolutFolderName(directory));
		items.refresh(this, directory);
	}

	private String absolutFolderName(final Directory folder)
	{
		if (folder.getParent() != null)
		{
			return absolutFolderName(folder.getParent()) + folder.getName() + "/";
		}

		return "/";
	}
}
