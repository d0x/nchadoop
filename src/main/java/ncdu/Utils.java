package ncdu;

import java.text.DecimalFormat;

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
		return new DecimalFormat("#,##0.0").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
}
