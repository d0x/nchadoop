package ncdu.fs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import lombok.Data;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

@Data
public class NcFileSystem
{
	private FileSystem	fileSystem;
	private URI			searchUri;

	private Folder		searchRoot;

	public NcFileSystem(final URI namenode, final String user) throws IOException, InterruptedException
	{
		this.fileSystem = FileSystem.get(namenode, new Configuration(), user);
		this.searchUri = namenode;
	}

	/**
	 * Scans the hdfs on the given search path.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void refresh() throws FileNotFoundException, IOException
	{
		final RemoteIterator<LocatedFileStatus> fileList = readAllFiles();

		this.searchRoot = new Folder(null, this.searchUri.toString());

		while (fileList.hasNext())
		{
			final LocatedFileStatus file = fileList.next();

			this.searchRoot.add(file);
		}

	}

	private RemoteIterator<LocatedFileStatus> readAllFiles() throws FileNotFoundException, IOException
	{
		return this.fileSystem.listFiles(new Path(this.searchUri), true);
	}

}
