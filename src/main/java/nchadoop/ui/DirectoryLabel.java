/*******************************************************************************
 * Copyright 2013 Christian Schneider
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
