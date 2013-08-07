package ncdu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;

import org.junit.experimental.theories.DataPoint;

import ncdu.fs.File;
import ncdu.fs.Folder;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.component.ActionListBox;

public class FolderActionListBox extends ActionListBox
{
	private static final String	BLANK	= "                                                                                                                                                           ";

	private List<Folder>		folders;
	private List<File>			files;
	private Folder				currentfolder;

	public void addFolder(NcWindow ncWindow, List<Folder> folders)
	{
		this.folders = folders;
	}

	public void addFile(NcWindow ncWindow, List<File> files)
	{
		this.files = files;
		for (File file : files)
		{
			String name = "/" + file.getName();
			long size = file.getSize();
			long largest = file.getParent() != null ? file.getParent().getSize() : 0;

			String formatted = Utils.format(name, size, largest);
			addAction(formatted, null);
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
		public int compareTo(Displayable o)
		{
			return (int) (o.size - size);
		}

	}

	public void refresh(NcWindow ncWindow)
	{
		clearItems();

		List<Displayable> items = new ArrayList<>();

		long max = 0;
		for (Folder folder : folders)
		{
			items.add(new Displayable("/" + folder.getName(), folder.getSize(), new ChangeFolder(folder, ncWindow)));
			if (max < folder.getSize())
				max = folder.getSize();
		}

		for (File file : files)
		{
			items.add(new Displayable(file.getName(), file.getSize(), null));
			if (max < file.getSize())
				max = file.getSize();
		}

		for (Displayable displayable : items)
		{
			displayable.setLargest(max);
		}

		Collections.sort(items);

		if (currentfolder.getParent() != null)
		{
			addAction("                       /.." + BLANK, new ChangeFolder(currentfolder.getParent(), ncWindow));
		}

		for (Displayable displayable : items)
		{
			addAction(Utils.format(displayable.getName(), displayable.getSize(), displayable.getLargest()) + BLANK, displayable.getAction());
		}
	}

	public void setCurrent(Folder currentFolder)
	{
		this.currentfolder = currentFolder;

	}

}
