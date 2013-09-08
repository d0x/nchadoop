package org.nchadoop.fs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;

/**
 * Abstracts the file system access for various versions.
 * A valid impl. could be the CDH FileSystem, Hortonworks, Vinalla, ...
 */
public interface FileSystemAPI
{

	void init(URI namenode, String user) throws IOException, InterruptedException;

	FileStatus[] listStatus(URI searchUri, String[] globFilter) throws IOException;

	FileStatus[] listStatus(Path path) throws FileNotFoundException, IOException;

	boolean delete(Path path, boolean b) throws IOException;

}
