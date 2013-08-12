package nchadoop.fs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;

@Slf4j
@Data
public class HdfsScanner
{
	private final FileSystem	fileSystem;
	private boolean				interrupted;

	public HdfsScanner(final URI namenode, final String user) throws IOException, InterruptedException
	{
		this.fileSystem = FileSystem.get(namenode, new Configuration(), user);
	}

	public SearchRoot refresh(URI namenode) throws IOException
	{
		return refresh(namenode, null);
	}

	public SearchRoot refresh(URI searchUri, StatusCallback callback) throws IOException
	{
		if (callback != null)
		{
			callback.onScanStarted(searchUri);
		}

		interrupted = false;

		SearchRoot searchRoot = new SearchRoot(searchUri.toString());

		walkThroughDirectories(callback, searchRoot, fileSystem.listStatus(new Path(searchUri)));
		
		if (callback != null)
		{
			callback.onScanFinished(searchRoot);
		}
		return searchRoot;
	}

	private void walkThroughDirectories(StatusCallback callback, SearchRoot searchRoot, FileStatus[] listLocatedStatus) throws FileNotFoundException, IOException
	{
		for (FileStatus fileStatus : listLocatedStatus)
		{
			if (fileStatus.isDirectory())
			{
				try
				{
					walkThroughDirectories(callback, searchRoot, fileSystem.listStatus(fileStatus.getPath()));
				}
				catch (IOException e)
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

	public boolean deleteDirectory(Directory directory) throws IOException
	{
		boolean deleteSuccess = fileSystem.delete(new Path(directory.absolutDirectoryName()), true);

		if (deleteSuccess)
		{
			directory.remove();
		}

		return deleteSuccess;
	}

	public boolean deleteFile(Directory parent, LocatedFileStatus fileStatus) throws IOException
	{
		boolean delete = fileSystem.delete(fileStatus.getPath(), false);

		if (delete)
		{
			parent.removeFile(fileStatus);
		}

		return delete;

	}

	private void addFile(SearchRoot searchRoot, final FileStatus file)
	{
		final Path directorPath = file.getPath().getParent();

		final Directory directory = searchRoot.addPath(directorPath, file.getLen());

		directory.addFile(file);
	}

	public void close()
	{
		interrupted = true;
	}

	public static interface StatusCallback
	{
		void onVisitFile(FileStatus next);

		void onScanFinished(SearchRoot searchRoot);

		void onScanStarted(URI searchUri);
	}

}
