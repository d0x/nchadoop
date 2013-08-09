package nchadoop.ui.listbox;

import java.text.DecimalFormat;
import java.text.MessageFormat;

import lombok.Data;
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

	public String formatDisplayText(final int width, final long maximumSize)
	{
		final String readableSize = StringUtils.leftPad(readableFileSize(this.size), 9);
		final String guage = createGuage(this.size, maximumSize);

		return StringUtils.rightPad(MessageFormat.format("{0} {1} {2}", readableSize, guage, this.name), width);
	}

	private String readableFileSize(final long size)
	{
		if (size < 0)
			return "";

		if (size == 0)
			return "0.0";

		final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
		final int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		final DecimalFormat decimalFormat = new DecimalFormat("#,##0.0");

		return decimalFormat.format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	private String createGuage(final long size, final long largest)
	{
		if (size < 0)
			return "            ";

		final double percent = ((double) size) / largest;

		final long length = Math.round(percent * 10);

		String inside = "";

		for (int i = 0; i < 10; i++)
		{
			inside += i < length ? "#" : " ";
		}

		return "[" + inside + "]";

	}

	public void navigate()
	{
		this.mainWindow.changeFolder(this.target);
	}

}
