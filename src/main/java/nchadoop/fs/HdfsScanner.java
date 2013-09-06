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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

@Slf4j
@Data
public class HdfsScanner
{
	private final FileSystem	fileSystem;
	private boolean				interrupted;

	public HdfsScanner(final URI namenode, final String user) throws IOException, InterruptedException
	{
		Configuration configuration = new Configuration();
		configuration.set("fs.default.name", namenode.toString());
		
		this.fileSystem = FileSystem.get(configuration);
	}

	public SearchRoot refresh(final URI namenode, String... globFilter) throws IOException
	{
		return refresh(namenode, null, globFilter);
	}

	public SearchRoot refresh(final URI searchUri, final StatusCallback callback, String... globFilter) throws IOException
	{
		PathFilter filter = convertToPathFilter(globFilter);

		if (callback != null)
		{
			callback.onScanStarted(searchUri);
		}

		this.interrupted = false;

		final SearchRoot searchRoot = new SearchRoot(searchUri.toString());

		walkThroughDirectories(callback, searchRoot, this.fileSystem.listStatus(new Path(searchUri.toString()), filter));

		if (callback != null)
		{
			callback.onScanFinished(searchRoot);
		}
		return searchRoot;
	}

	private PathFilter convertToPathFilter(String[] globFilter) throws IOException
	{
		// TODO: Remove this one
		PathFilter pathFilter = new PathFilter() {

			@Override
			public boolean accept(Path path)
			{
				return true;
			}
		};

// TODO: Enable this again.
//		for (String filterPattern : globFilter)
//		{
//			pathFilter = new GlobFilter(filterPattern, pathFilter);
//		}
		
		return pathFilter;
	}

	private void walkThroughDirectories(final StatusCallback callback, final SearchRoot searchRoot, final FileStatus[] listLocatedStatus) throws FileNotFoundException, IOException
	{
		for (final FileStatus fileStatus : listLocatedStatus)
		{
			if (fileStatus.isDir())
			{
				try
				{
					walkThroughDirectories(callback, searchRoot, this.fileSystem.listStatus(fileStatus.getPath()));
				}
				catch (final IOException e)
				{
					log.warn("Couldn't open directory {}. Exception: {}", fileStatus.getPath(), e.getMessage());
				}
			}
			else
			{
				if (callback != null)
				{
					callback.onVisitFile(fileStatus);
				}
				addFile(searchRoot, fileStatus);
			}
		}

	}

	public boolean deleteDirectory(final Directory directory) throws IOException
	{
		final boolean deleteSuccess = this.fileSystem.delete(new Path(directory.absolutDirectoryName()), true);

		if (deleteSuccess)
		{
			directory.remove();
		}

		return deleteSuccess;
	}

	public boolean deleteFile(final Directory parent, final FileStatus fileStatus) throws IOException
	{
		final boolean delete = this.fileSystem.delete(fileStatus.getPath(), false);

		if (delete)
		{
			parent.removeFile(fileStatus);
		}

		return delete;

	}

	private void addFile(final SearchRoot searchRoot, final FileStatus file)
	{
		final Path directorPath = file.getPath().getParent();

		final Directory directory = searchRoot.addPath(directorPath, file.getLen());

		directory.addFile(file);
	}

	public void close()
	{
		this.interrupted = true;
	}

	public static interface StatusCallback
	{
		void onVisitFile(final FileStatus next);

		void onScanFinished(final SearchRoot searchRoot);

		void onScanStarted(final URI searchUri);
	}

}
