package nchadoop.ui.listbox;

import java.text.MessageFormat;

import lombok.Data;
import nchadoop.Utils;
import nchadoop.fs.Directory;
import nchadoop.ui.MainWindow;

import org.apache.commons.lang.StringUtils;

@Data
public class Displayable implements Comparable<Displayable>
{
	private final MainWindow	mainWindow;
	private final Directory		target;
	private final String		name;
	private final long			size;

	private final Object		reference;

	@Override
	public int compareTo(final Displayable o)
	{
		return Long.compare(o.size, size);
	}

	public String formatDisplayText(final int width, final long maximumSize)
	{
		final String readableSize = StringUtils.leftPad(Utils.readableFileSize(this.size), 9);
		final String guage = createGuage(this.size, maximumSize);

		return StringUtils.rightPad(MessageFormat.format("{0} {1} {2}", readableSize, guage, this.name), width);
	}

	private String createGuage(final long size, final long largest)
	{
		if (size < 0)
			return "            ";

		final int length = (int) Math.round(((double) size) / largest * 10);

		return "[" + StringUtils.rightPad("##########".substring(0, length), 10) + "]";
	}

	public void navigate()
	{
		if (target != null)
		{
			this.mainWindow.changeFolder(this.target);
		}
	}

}
