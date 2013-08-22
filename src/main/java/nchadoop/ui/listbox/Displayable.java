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
package nchadoop.ui.listbox;

import java.text.MessageFormat;

import lombok.Data;
import nchadoop.Utils;
import nchadoop.fs.Directory;
import nchadoop.ui.MainWindow;

import org.apache.commons.lang.StringUtils;

@Data
public class Displayable implements Comparable<Displayable>
{
	private final MainWindow	mainWindow;
	private final Directory		parent;
	private final Directory		target;
	private final String		name;
	private final long			size;

	private final Object		reference;

	@Override
	public int compareTo(final Displayable o)
	{
		return Long.compare(o.size, size);
	}

	public String formatDisplayText(final int width, final long maximumSize)
	{
		final String readableSize = StringUtils.leftPad(Utils.readableFileSize(this.size), 9);
		final String guage = createGuage(this.size, maximumSize);

		return StringUtils.rightPad(MessageFormat.format("{0} {1} {2}", readableSize, guage, this.name), width);
	}

	private String createGuage(final long size, final long largest)
	{
		if (size < 0)
			return "            ";

		final int length = (int) Math.round(((double) size) / largest * 10);

		return "[" + StringUtils.rightPad("##########".substring(0, length), 10) + "]";
	}

	public void navigate()
	{
		if (target != null)
		{
			this.mainWindow.changeFolder(this.target);
		}
	}

	public void navigateToParent()
	{
		if (parent != null)
		{
			this.mainWindow.changeFolder(parent);
		}
	}

}
