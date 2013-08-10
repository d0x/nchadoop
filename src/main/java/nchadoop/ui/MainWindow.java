package nchadoop.ui;

import nchadoop.fs.Directory;
import nchadoop.fs.SearchRoot;
import nchadoop.ui.components.SingleWindowUi;

import com.googlecode.lanterna.gui.Component.Alignment;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.gui.layout.BorderLayout;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.input.Key;

public class MainWindow extends SingleWindowUi
{
	private HeaderLabel		header	= new HeaderLabel();
	private DirectoryPanel	content	= new DirectoryPanel();
	private FooterLabel		footer	= new FooterLabel();

	public MainWindow(final GUIScreen gui)
	{
		super(gui);

		this.header.setAlignment(Alignment.LEFT_CENTER);

		this.contentPane.addComponent(this.header, BorderLayout.TOP);
		this.contentPane.addComponent(this.content, BorderLayout.CENTER);
		this.contentPane.addComponent(this.footer, BorderLayout.BOTTOM);
	}

	public void init()
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				// Those have to be exectued at last
				addComponent(MainWindow.this.contentPane, LinearLayout.MAXIMIZES_HORIZONTALLY, LinearLayout.MAXIMIZES_VERTICALLY);
				MainWindow.this.gui.showWindow(MainWindow.this, GUIScreen.Position.FULL_SCREEN);
				MainWindow.this.screen.stopScreen();
			}
		}, "ui-mainWindow").start();
	}

	public void updateSearchRoot(final SearchRoot searchRoot)
	{
		this.content.updateDirectory(this, searchRoot);
		this.footer.updateSearchRoot(this, searchRoot);
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

			case 'Q':
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
		this.content.updateDirectory(this, directory);
	}
}
