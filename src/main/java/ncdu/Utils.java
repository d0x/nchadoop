package ncdu;

import java.text.DecimalFormat;
import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;

import ncdu.fs.File;
import ncdu.fs.Folder;

public class Utils
{

	public static void displayFolder(final Folder rootFolder)
	{
		displayFolder(0, rootFolder);
	}

	private static void displayFolder(final int depth, final Folder rootFolder)
	{
		System.out.print(readableFileSize(rootFolder.getSize()));

		for (int i = 0; i < depth; i++)
		{
			System.out.print("\t");
		}

		System.out.println(rootFolder.getName());

		for (final Folder folder : rootFolder.getFolders())
		{
			displayFolder(depth + 1, folder);
		}

		for (final File file : rootFolder.getFiles())
		{
			for (int i = 0; i < depth + 1; i++)
			{
				System.out.print("\t");
			}

			System.out.println("+" + file);
		}
	}

	public static String readableFileSize(final long size)
	{
		if (size <= 0)
			return "0";
		final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
		final int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.0");
		return decimalFormat.format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	public static String createGuage(long size, long largest)
	{
		double percent = ((double) size) / largest;

		long length = Math.round(percent * 10);

		String inside = "";

		for (int i = 0; i < 10; i++)
		{
			inside += i < length ? "#" : " ";
		}

		return "[" + inside + "]";

	}

	public static String format(String name, long size, long largest)
	{
		String readableFileSize = Utils.readableFileSize(size);
		String padded = StringUtils.leftPad(readableFileSize, 9);
		String guage = Utils.createGuage(size, largest);

		return MessageFormat.format("{0} {1} {2}", padded, guage, name);
	}

}
