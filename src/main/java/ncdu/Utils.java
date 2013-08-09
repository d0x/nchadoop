package ncdu;

import java.text.DecimalFormat;

import ncdu.fs.Directory;

import org.apache.hadoop.fs.LocatedFileStatus;

public class Utils
{

	public static void displayFolder(final Directory rootFolder)
	{
		displayFolder(0, rootFolder);
	}

	private static void displayFolder(final int depth, final Directory rootFolder)
	{
		System.out.print(readableFileSize(rootFolder.getSize()));

		for (int i = 0; i < depth; i++)
		{
			System.out.print("  ");
		}

		System.out.println(rootFolder.getName());

		for (final Directory folder : rootFolder.getDirectories())
		{
			displayFolder(depth + 1, folder);
		}

		for (final LocatedFileStatus file : rootFolder.getFiles())
		{
			for (int i = 0; i < depth + 1; i++)
			{
				System.out.print("  ");
			}

			System.out.println("+" + file.getPath().getName() + " [" + file.getOwner() + "]");
		}
	}

	public static String readableFileSize(final long size)
	{
		if (size <= 0)
			return "0";
		final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
		final int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		final DecimalFormat decimalFormat = new DecimalFormat("#,##0.0");
		return decimalFormat.format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	public static String createGuage(final long size, final long largest)
	{
		final double percent = ((double) size) / largest;

		final long length = Math.round(percent * 10);

		String inside = "";

		for (int i = 0; i < 10; i++)
		{
			inside += i < length ? "#" : " ";
		}

		return "[" + inside + "]";

	}

}
