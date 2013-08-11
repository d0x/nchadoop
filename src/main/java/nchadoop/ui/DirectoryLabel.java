package nchadoop.ui;

import java.text.MessageFormat;

import nchadoop.fs.Directory;

import com.googlecode.lanterna.gui.TextGraphics;
import com.googlecode.lanterna.gui.Theme;
import com.googlecode.lanterna.gui.component.Label;

public class DirectoryLabel extends Label
{

	public DirectoryLabel()
	{
		setAlignment(Alignment.LEFT_CENTER);
		setStyle(Theme.Category.SCREEN_BACKGROUND);
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
	
    @Override
    public void repaint(TextGraphics graphics)
    {
    	graphics.applyTheme(Theme.Category.SCREEN_BACKGROUND);
    	graphics.fillArea(' ');
    	super.repaint(graphics);
    }

}
