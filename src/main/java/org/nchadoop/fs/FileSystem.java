package org.nchadoop.fs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

public class FileSystem implements FileSystemAPI
{
	private org.apache.hadoop.fs.FileSystem	fileSystem;
	private GlobFilterConverter				globFilterConverter	= new GlobFilterConverter();

	@Override
	public void init(URI namenode, String user) throws IOException, InterruptedException
	{
		org.apache.hadoop.fs.FileSystem fileSystem = org.apache.hadoop.fs.FileSystem.get(namenode, new Configuration(), user);

		this.fileSystem = fileSystem;
	}

	@Override
	public FileStatus[] listStatus(URI searchUri, String[] globFilter) throws IOException
	{
		PathFilter pathFilter = globFilterConverter.toGlobFilter(globFilter);

		return fileSystem.listStatus(new Path(searchUri), pathFilter);
	}

	@Override
	public FileStatus[] listStatus(Path path) throws FileNotFoundException, IOException
	{
		return fileSystem.listStatus(path);
	}

	@Override
	public boolean delete(Path path, boolean recursive) throws IOException
	{
		return fileSystem.delete(path, recursive);
	}

}
