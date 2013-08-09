package ncdu.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;
import ncdu.Utils;
import ncdu.fs.Directory;
import ncdu.ui.actions.ChangeFolder;
import ncdu.ui.actions.ShowLocatedFileStatus;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.LocatedFileStatus;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.TextGraphics;
import com.googlecode.lanterna.gui.component.ActionListBox;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.layout.LinearLayout;

public class FolderActionListBox extends Panel
{
	private DirectoryLabel	currentFolderLabel	= new DirectoryLabel();
	private ActionListBox	listBox				= new ActionListBox() {

													@Override
													protected void printItem(final TextGraphics graphics, final int x, final int y, final int index)
													{
														String asText = createItemString(index);

														asText = StringUtils.rightPad(asText, graphics.getWidth());

														graphics.drawString(x, y, asText);

													}
												};

	public FolderActionListBox()
	{
		addComponent(this.currentFolderLabel, LinearLayout.MAXIMIZES_HORIZONTALLY);
		addComponent(this.listBox, LinearLayout.MAXIMIZES_HORIZONTALLY, LinearLayout.MAXIMIZES_VERTICALLY);
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
			if (o.size > this.size)
			{
				return 1;
			}
			else if (o.size < this.size)
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}

	}

	public void refresh(final MainWindow ncWindow, final Directory directory)
	{
		this.currentFolderLabel.refresh(directory);

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
			items.add(new Displayable(file.getPath().getName(), file.getLen(), new ShowLocatedFileStatus(ncWindow.getOwner(), file)));
			if (max < file.getLen())
				max = file.getLen();
		}

		for (final Displayable displayable : items)
		{
			displayable.setLargest(max);
		}

		Collections.sort(items);

		if (!directory.isRoot())
		{
			this.listBox.addAction("                       /..", new ChangeFolder(directory.getParent(), ncWindow));
		}

		for (final Displayable displayable : items)
		{
			this.listBox.addAction(Utils.format(displayable.getName(), displayable.getSize(), displayable.getLargest()), displayable.getAction());
		}

		if (this.listBox.getNrOfItems() > 1)
		{
			this.listBox.setSelectedItem(1);
		}
	}

}
