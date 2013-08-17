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

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.hadoop.fs.Path;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchRoot extends Directory
{
	private long	totalDiskUsage;
	private long	totalFiles;

	public SearchRoot(final String name)
	{
		super(null, name);
	}

	public Directory addPath(final Path path, final long size)
	{
		// TODO: removed this "new Path()". maybe there is a more elegant way to check this.
		final String rootPath = new Path(this.name).toUri().getPath();
		final String currentPath = path.toUri().getPath();

		if (path.isRoot() || rootPath.equals(currentPath))
		{
			this.totalDiskUsage += size;
			this.totalFiles++;
			this.size += size;
			return this;
		}

		final Directory parentDirectory = addPath(path.getParent(), size);

		final String pathName = path.getName();

		Directory directory = parentDirectory.findDirectoryByName(pathName);

		if (directory == null)
		{
			directory = parentDirectory.addDirectory(pathName);
		}

		directory.setSize(directory.getSize() + size);

		return directory;
	}

	@Override
	protected void adjustSizeRecursive(final long correction)
	{
		super.adjustSizeRecursive(correction);

		this.totalDiskUsage += correction;
	}

}
