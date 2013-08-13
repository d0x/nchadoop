package nchadoop.ui;

import lombok.Data;
import nchadoop.Controller;
import nchadoop.fs.Directory;
import nchadoop.fs.SearchRoot;
import nchadoop.ui.listbox.Displayable;

import org.apache.hadoop.fs.FileStatus;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Component.Alignment;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.gui.layout.BorderLayout;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;

@Data
public class MainWindow extends Window
{
	protected final GUIScreen		gui;
	protected final Screen			screen;

	private final HeaderLabel		header			= new HeaderLabel();
	private final DirectoryPanel	directoryPanel	= new DirectoryPanel();
	private final FooterLabel		footer			= new FooterLabel();

	private Controller				controller;

	public MainWindow(final GUIScreen guiScreen)
	{
		super("");

		this.gui = guiScreen;

		this.screen = this.gui.getScreen();
		this.screen.startScreen();

		layout();
	}

	private void layout()
	{
		this.header.setAlignment(Alignment.LEFT_CENTER);

		setBorder(new Border.Invisible());

		final Panel contentPane = new Panel();
		contentPane.setLayoutManager(new BorderLayout());
		contentPane.addComponent(this.header, BorderLayout.TOP);
		contentPane.addComponent(this.directoryPanel, BorderLayout.CENTER);
		contentPane.addComponent(this.footer, BorderLayout.BOTTOM);

		addComponent(contentPane, LinearLayout.MAXIMIZES_HORIZONTALLY, LinearLayout.MAXIMIZES_VERTICALLY);
	}

	public void init()
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				MainWindow.this.gui.showWindow(MainWindow.this, GUIScreen.Position.FULL_SCREEN);
			}
		}, "ui-thread").start();
	}

	public void updateSearchRoot(final SearchRoot searchRoot)
	{
		getOwner().runInEventThread(new Action() {
			@Override
			public void doAction()
			{
				MainWindow.this.directoryPanel.updateDirectory(MainWindow.this, searchRoot);
				MainWindow.this.footer.updateSearchRoot(MainWindow.this, searchRoot);
			}
		});
	}

	@Override
	public void onKeyPressed(final Key key)
	{
		if (this.controller.handleGlobalKeyPressed(this, key))
		{
			return;
		}

		if (key.getCharacter() != 'd')
		{
			super.onKeyPressed(key);
			return;
		}

		final Object selectedItem = this.directoryPanel.getListBox().getSelectedItem();

		if (selectedItem == null)
		{
			MessageBox.showMessageBox(this.gui, "Error", "No item selected.");
			return;
		}

		final Displayable displayable = (Displayable) selectedItem;

		final Object reference = displayable.getReference();

		if (reference instanceof Directory)
		{
			this.controller.deleteDiretory((Directory) reference);
		}
		else if (reference instanceof FileStatus)
		{
			this.controller.deleteFile(this.directoryPanel.getCurrentDirectory(), (FileStatus) reference);
		}
		else
		{
			MessageBox.showMessageBox(this.gui, "Error", "Can't delete this.");
		}

	}

	public void changeFolder(final Directory directory)
	{
		this.directoryPanel.updateDirectory(this, directory);
	}
}
