package nchadoop.ui;

import java.text.MessageFormat;

import nchadoop.fs.Directory;

import com.googlecode.lanterna.gui.component.Label;

public class DirectoryLabel extends Label
{

	public DirectoryLabel()
	{
		setAlignment(Alignment.LEFT_CENTER);
	}

	public void updateDirectory(final Directory directory)
	{
		String text;
		if (directory == null)
		{
			text = "";
		}
		else
		{
			text = MessageFormat.format(" --- {0}", directory.absolutDirectoryName());
		}

		setText(text);
	}
}
