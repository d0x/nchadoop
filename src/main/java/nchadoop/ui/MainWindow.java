package nchadoop.ui;

import lombok.Data;
import nchadoop.Controller;
import nchadoop.fs.Directory;
import nchadoop.fs.SearchRoot;
import nchadoop.ui.listbox.Displayable;

import org.apache.hadoop.fs.LocatedFileStatus;

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
	protected GUIScreen		gui;
	protected Screen		screen;

	private HeaderLabel		header			= new HeaderLabel();
	private DirectoryPanel	directoryPanel	= new DirectoryPanel();
	private FooterLabel		footer			= new FooterLabel();

	private Controller		controller;

	public MainWindow(GUIScreen guiScreen)
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

		Panel contentPane = new Panel();
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
		this.directoryPanel.updateDirectory(this, searchRoot);
		this.footer.updateSearchRoot(this, searchRoot);
	}

	@Override
	public void onKeyPressed(final Key key)
	{
		if (controller.handleGlobalKeyPressed(this, key))
		{
			return;
		}

		if (key.getCharacter() != 'd')
		{
			super.onKeyPressed(key);
			return;
		}

		Object selectedItem = directoryPanel.getListBox().getSelectedItem();

		if (selectedItem == null)
		{
			MessageBox.showMessageBox(gui, "Error", "No item selected.");
			return;
		}

		Displayable displayable = (Displayable) selectedItem;

		Object reference = displayable.getReference();

		if (reference instanceof Directory)
		{
			controller.deleteDiretory((Directory) reference);
		}
		else if(reference instanceof LocatedFileStatus)
		{
			controller.deleteFile(directoryPanel.getCurrentDirectory(), (LocatedFileStatus) reference);
		}
		else
		{
			MessageBox.showMessageBox(gui, "Error", "Can't delete this.");
		}

	}

	public void changeFolder(final Directory directory)
	{
		this.directoryPanel.updateDirectory(this, directory);
	}
}
