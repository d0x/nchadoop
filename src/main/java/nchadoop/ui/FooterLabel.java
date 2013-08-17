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
