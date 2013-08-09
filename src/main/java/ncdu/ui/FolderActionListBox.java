package ncdu.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;
import ncdu.Utils;
import ncdu.fs.Directory;

import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.component.ActionListBox;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.layout.LinearLayout;

public class FolderActionListBox extends Panel
{
	private static final String	BLANK	= "                                                                                                                                                           ";

	private Directory			currentfolder;

	private Label				label	= new Label();
	private ActionListBox		listBox	= new ActionListBox();

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
	}

	public void addFile(final MainWindow ncWindow, final List<LocatedFileStatus> files)
	{
		for (final LocatedFileStatus file : files)
		{
			final String name = "/" + file.getPath().getName();
			final long size = file.getLen();
			final long largest = 0;// file.getParent() != null ? file.getParent().getSize() : 0;

			final String formatted = Utils.format(name, size, largest);
			this.listBox.addAction(formatted, null);
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
			// we need to break this down because we need to return an int...
			if (o.size > size)
			{
				return 1;
			}
			else if (o.size < size)
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}

	}

	public void refresh(final MainWindow ncWindow, Directory directory)
	{
		this.currentfolder = directory;
		this.listBox.clearItems();

		final List<Displayable> items = new ArrayList<>();

		long max = 0;
		for (final Directory folder : directory.getDirectories())
		{
			items.add(new Displayable("/" + folder.getName(), folder.getSize(), new ChangeFolder(folder, ncWindow)));
			if (max < folder.getSize())
				max = folder.getSize();
		}

		for (final LocatedFileStatus file : directory.getFiles())
		{
			items.add(new Displayable(file.getPath().getName(), file.getLen(), null));
			if (max < file.getLen())
				max = file.getLen();
		}

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

		if (listBox.getNrOfItems() > 1)
		{
			listBox.setSelectedItem(1);
		}
	}

	public void setCurrent(final Directory currentFolder)
	{
		this.currentfolder = currentFolder;

	}

}
