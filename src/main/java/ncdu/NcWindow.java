package ncdu;

import ncdu.fs.Folder;

import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Separator;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;

public class NcWindow extends Window
{

	private FolderActionListBox	listBox	= new FolderActionListBox();
	private Label				label	= new Label();

	private Folder				currentFolder;

	public NcWindow(final Screen screen, Folder folder)
	{
		super("nchdfs");
		setBorder(new Border.Invisible());
		setSoloWindow(true);
		addComponent(label);
		addComponent(new Separator());
		addComponent(listBox);

		this.currentFolder = folder;

		refresh();
	}

	private void refresh()
	{
		listBox.setCurrent(currentFolder);
		listBox.addFolder(this, currentFolder.getFolders());
		listBox.addFile(this, currentFolder.getFiles());

		listBox.refresh(this);
	}

	@Override
	public void onKeyPressed(final Key key)
	{
		switch (key.getKind())
		{
			case Escape:
				close();
				getOwner().getScreen().stopScreen();
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
				getOwner().getScreen().stopScreen();
				break;

			default:
				break;
		}
		
		super.onKeyPressed(key);
	}

	public void changeFolder(Folder folder)
	{
		this.currentFolder = folder;
		
		label.setText("Current Path: " + absolutFolderName(folder));
		
		refresh();
	}

	private String absolutFolderName(Folder folder)
	{
		if(folder.getParent() != null)
		{
			return absolutFolderName(folder.getParent()) + folder.getName() + "/";
		}
		
		return "/";
	}
}
