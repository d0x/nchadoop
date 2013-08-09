package ncdu.ui;

import ncdu.fs.Directory;
import ncdu.fs.SearchRoot;
import ncdu.ui.components.SingleWindowUi;

import com.googlecode.lanterna.gui.Component.Alignment;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.gui.layout.BorderLayout;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.input.Key;

public class MainWindow extends SingleWindowUi
{
	private HeaderLabel			header	= new HeaderLabel();
	private FolderActionListBox	items	= new FolderActionListBox();
	private FooterLabel			footer	= new FooterLabel();

	public MainWindow()
	{
		this.header.setAlignment(Alignment.LEFT_CENTER);

		this.contentPane.addComponent(this.header, BorderLayout.TOP);
		this.contentPane.addComponent(this.items, BorderLayout.CENTER);
		this.contentPane.addComponent(this.footer, BorderLayout.BOTTOM);
	}

	public void refresh(final SearchRoot searchRoot)
	{
		this.items.refresh(this, searchRoot);
		this.footer.refresh(this, searchRoot);

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
		this.items.refresh(this, directory);
	}
}
