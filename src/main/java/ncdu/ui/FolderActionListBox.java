package ncdu.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;
import ncdu.Utils;
import ncdu.fs.Directory;

import org.apache.hadoop.fs.LocalFileSystem;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.component.ActionListBox;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.layout.LinearLayout;

public class FolderActionListBox extends Panel
{
	private static final String		BLANK	= "                                                                                                                                                           ";

	private List<Directory>			folders;
	private List<LocalFileSystem>	files;
	private Directory				currentfolder;

	private Label					label	= new Label();
	private ActionListBox			listBox	= new ActionListBox();

	public FolderActionListBox()
	{
		this.label.setText(" --- /home/christian/");
		this.label.setAlignment(Alignment.LEFT_CENTER);

		this.listBox.addAction("foo", new Action() {

			@Override
			public void doAction()
			{

			}
		});

		addComponent(this.label, LinearLayout.MAXIMIZES_HORIZONTALLY);
		addComponent(this.listBox, LinearLayout.MAXIMIZES_HORIZONTALLY, LinearLayout.MAXIMIZES_VERTICALLY);
	}

	public void addFolder(final MainWindow ncWindow, final List<Directory> folders)
	{
		this.folders = folders;
	}

	public void addFile(final MainWindow ncWindow, final List<LocalFileSystem> files)
	{
		this.files = files;
		for (final LocalFileSystem file : files)
		{
//			final String name = "/" + file.getName();
//			final long size = file.getSize();
//			final long largest = 0;// file.getParent() != null ? file.getParent().getSize() : 0;
//
//			final String formatted = Utils.format(name, size, largest);
//			this.listBox.addAction(formatted, null);
		}
	}

	@Data
	class Displayable implements Comparable<Displayable>
	{
		private final String	name;
		private final long		size;
		private final Action	action;
		private long			largest;

		@Override
		public int compareTo(final Displayable o)
		{
			return (int) (o.size - this.size);
		}

	}

	public void refresh(final MainWindow ncWindow)
	{
		this.listBox.clearItems();

		final List<Displayable> items = new ArrayList<>();

		long max = 0;
		for (final Directory folder : this.folders)
		{
			items.add(new Displayable("/" + folder.getName(), folder.getSize(), new ChangeFolder(folder, ncWindow)));
			if (max < folder.getSize())
				max = folder.getSize();
		}

//		for (final File file : this.files)
//		{
//			items.add(new Displayable(file.getName(), file.getSize(), null));
//			if (max < file.getSize())
//				max = file.getSize();
//		}

		for (final Displayable displayable : items)
		{
			displayable.setLargest(max);
		}

		Collections.sort(items);

		if (this.currentfolder.getParent() != null)
		{
			this.listBox.addAction("                       /.." + BLANK, new ChangeFolder(this.currentfolder.getParent(), ncWindow));
		}

		for (final Displayable displayable : items)
		{
			this.listBox.addAction(Utils.format(displayable.getName(), displayable.getSize(), displayable.getLargest()) + BLANK, displayable.getAction());
		}
	}

	public void setCurrent(final Directory currentFolder)
	{
		this.currentfolder = currentFolder;

	}

}
