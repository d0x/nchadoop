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
package nchadoop.fs;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;

@Getter
@Setter
public class Directory
{
	protected final Directory	parent;
	protected final String		name;

	protected List<Directory>	directories	= new ArrayList<>();
	protected List<FileStatus>	files		= new ArrayList<>();

	protected long				size		= 0;

	public Directory(final Directory parent, final String name)
	{
		this.parent = parent;
		this.name = StringUtils.removeEnd(name, "/");
	}

	public boolean isRoot()
	{
		return getParent() == null;
	}

	public String absolutDirectoryName()
	{
		final StringBuilder builder = new StringBuilder();

		Directory directory = this;
		do
		{
			builder.insert(0, directory.name + "/");
		} while ((directory = directory.getParent()) != null);

		return builder.toString();
	}

	public Directory findDirectoryByName(final String name)
	{
		for (final Directory directory : this.directories)
		{
			if (StringUtils.equals(directory.name, name))
			{
				return directory;
			}
		}
		return null;
	}

	public Path toPath()
	{
		return new Path(absolutDirectoryName());
	}

	public void remove()
	{
		if (isRoot() == false)
		{
			this.parent.directories.remove(this);
		}

		adjustSizeRecursive(this.size * -1);
	}

	public void removeFile(final FileStatus file)
	{
		this.files.remove(file);

		adjustSizeRecursive(file.getLen() * -1);
	}

	protected Directory addDirectory(final String name)
	{
		final Directory directory = new Directory(this, name);

		this.directories.add(directory);

		return directory;
	}

	protected void addFile(final FileStatus file)
	{
		this.files.add(file);
	}

	protected void adjustSizeRecursive(final long correction)
	{
		this.size += correction;

		if (isRoot() == false)
		{
			this.parent.adjustSizeRecursive(correction);
		}
	}
}
