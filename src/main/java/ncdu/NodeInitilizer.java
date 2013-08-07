package ncdu;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

public class NodeInitilizer
{
	public static final String	namenode	= "hdfs://carolin:8020/hbase/steamGame";

	public static URI			uri;

	public static void main(final String[] args) throws IOException, InterruptedException, URISyntaxException
	{
		uri = new URI(namenode);

		final FileSystem fileSystem = FileSystem.get(uri, new Configuration(), "hdfs");

		final RemoteIterator<LocatedFileStatus> fileList = fileSystem.listFiles(new Path(uri.getPath()), true);

		final Folder rootFolder = new Folder(uri.toString());

		while (fileList.hasNext())
		{
			final LocatedFileStatus file = fileList.next();

			addFile(rootFolder, file);
		}

		displayFolder(0, rootFolder);

	}

	private static void displayFolder(final int depth, final Folder rootFolder)
	{
		for (int i = 0; i < depth; i++)
		{
			System.out.print("\t");
		}

		System.out.println(rootFolder.getName());

		for (final Folder folder : rootFolder.getFolders())
		{
			displayFolder(depth + 1, folder);
		}

		for (final String file : rootFolder.getFiles())
		{
			for (int i = 0; i < depth + 1; i++)
			{
				System.out.print("\t");
			}

			System.out.println("+" + file);
		}

	}

	private static void addFile(final Folder rootFolder, final LocatedFileStatus file)
	{
		final Path folders;

		if (file.isFile())
		{
			folders = file.getPath().getParent();
		}
		else
		{
			folders = file.getPath();
		}

		if (folders.toString().equals(rootFolder.getName()))
		{
			return;
		}

		final Folder folder = attachFolders(rootFolder, folders);

		if (file.isFile())
		{
			folder.getFiles().add(file.getPath().getName());
		}

	}

	private static Folder attachFolders(final Folder rootFolder, final Path folders)
	{
		Folder parent;

		if (folders.getParent() == null || rootFolder.getName().equals(folders.toString()))
		{
			return rootFolder;
		}
		else
		{
			parent = attachFolders(rootFolder, folders.getParent());
		}

		final String name = folders.getName();

		final Folder childFolder = parent.getChildFolder(name);

		if (childFolder != null)
		{
			return childFolder;
		}

		final Folder newChildFolder = new Folder(name);

		parent.getFolders().add(newChildFolder);

		return newChildFolder;

	}
}
