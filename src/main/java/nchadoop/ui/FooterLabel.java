package nchadoop.ui;

import java.text.MessageFormat;

import nchadoop.Utils;
import nchadoop.fs.SearchRoot;

import com.googlecode.lanterna.gui.component.Label;

public class FooterLabel extends Label
{

	public FooterLabel()
	{
		super("Loading...", true);

		setAlignment(Alignment.LEFT_CENTER);
	}

	public void updateSearchRoot(final MainWindow mainWindow, final SearchRoot searchRoot)
	{
		String text;

		if (searchRoot == null)
		{
			text = "";
		}
		else
		{
			final String readableFileSize = Utils.readableFileSize(searchRoot.getTotalDiskUsage());
			final long totalFiles = searchRoot.getTotalFiles();

			text = MessageFormat.format("Total disk usage {0} in {1} Files", readableFileSize, totalFiles);
		}

		setText(text);
	}
}
