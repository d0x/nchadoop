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

	public void refresh(final Directory directory)
	{
		setText(MessageFormat.format(" --- {0}", directory.absolutDirectoryName()));
	}
}
