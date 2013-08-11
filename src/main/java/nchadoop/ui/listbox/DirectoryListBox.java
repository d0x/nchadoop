package nchadoop.ui.listbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nchadoop.fs.Directory;
import nchadoop.ui.MainWindow;

import org.apache.hadoop.fs.LocatedFileStatus;

import com.googlecode.lanterna.gui.TextGraphics;
import com.googlecode.lanterna.gui.Theme;
import com.googlecode.lanterna.gui.component.AbstractListBox;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.TerminalPosition;

public class DirectoryListBox extends AbstractListBox
{
	private long	maximumSize;

	public DirectoryListBox()
	{
		super(null);
	}

	public void updateDirectory(final MainWindow ncWindow, final Directory directory)
	{
		clearItems();
		if (directory == null)
		{
			return;
		}

		final List<Displayable> items = new ArrayList<>();

		for (final Directory folder : directory.getDirectories())
		{
			items.add(new Displayable(ncWindow, folder, "/" + folder.getName(), folder.getSize(), folder));
		}

		for (final LocatedFileStatus file : directory.getFiles())
		{
			items.add(new Displayable(ncWindow, null, file.getPath().getName(), file.getLen(), file));
		}

		Collections.sort(items);

		if (!items.isEmpty())
		{
			this.maximumSize = items.get(0).getSize();
		}

		if (!directory.isRoot())
		{
			items.add(0, new Displayable(ncWindow, directory.getParent(), "/..", -1, null));
		}

		for (final Displayable displayable : items)
		{
			addItem(displayable);
		}

		if (directory.isRoot())
		{
			setSelectedItem(0);
		}
		else
		{
			setSelectedItem(1);
		}

	}

	@Override
	protected void printItem(final TextGraphics graphics, final int x, final int y, final int index)
	{
		final Displayable item = (Displayable) getItemAt(index);

		final String text = item.formatDisplayText(graphics.getWidth(), this.maximumSize);

		graphics.drawString(x, y, text);
	}

	@Override
	protected Result unhandledKeyboardEvent(final Key key)
	{
		if (key.getKind() == Key.Kind.Enter)
		{
			final Displayable selectedItem = (Displayable) getSelectedItem();

			selectedItem.navigate();

			return Result.EVENT_HANDLED;
		}
		return Result.EVENT_NOT_HANDLED;
	}

	@Override
	protected Theme.Definition getListItemThemeDefinition(final Theme theme)
	{
		return theme.getDefinition(Theme.Category.DIALOG_AREA);
	}

	@Override
	protected Theme.Definition getSelectedListItemThemeDefinition(final Theme theme)
	{
		return theme.getDefinition(Theme.Category.TEXTBOX_FOCUSED);
	}

	@Override
	public TerminalPosition getHotspot()
	{
		return null; // No hotspot
	}

	@Override
	protected String createItemString(final int index)
	{
		// We don't create those strings
		return "";
	}

}
