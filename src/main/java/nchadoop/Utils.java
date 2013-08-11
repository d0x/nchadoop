package nchadoop;

import java.text.DecimalFormat;

import nchadoop.fs.Directory;

import org.apache.hadoop.fs.LocatedFileStatus;

public class Utils
{
	public static final String[]		UNITS			= new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB"};
	public static final DecimalFormat	DECIMAL_FORMAT	= new DecimalFormat("#,##0.0");

	private Utils()
	{
		// hide utility class constructor
	}

	public static String readableFileSize(final long size)
	{
		if (size < 0)
			return "";

		if (size == 0)
			return "0.0";

		final int digitGroup = (int) (Math.log10(size) / Math.log10(1024));

		return DECIMAL_FORMAT.format(size / Math.pow(1024, digitGroup)) + " " + UNITS[digitGroup];
	}
}
