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

	public SearchRoot refresh(final URI namenode) throws IOException
	{
		return refresh(namenode, null);
	}

	public SearchRoot refresh(final URI searchUri, final StatusCallback callback) throws IOException
	{
		if (callback != null)
		{
			callback.onScanStarted(searchUri);
		}

		this.interrupted = false;

		final SearchRoot searchRoot = new SearchRoot(searchUri.toString());

		walkThroughDirectories(callback, searchRoot, this.fileSystem.listStatus(new Path(searchUri)));

		if (callback != null)
		{
			callback.onScanFinished(searchRoot);
		}
		return searchRoot;
	}

	private void walkThroughDirectories(final StatusCallback callback, final SearchRoot searchRoot, final FileStatus[] listLocatedStatus) throws FileNotFoundException, IOException
	{
		for (final FileStatus fileStatus : listLocatedStatus)
		{
			if (fileStatus.isDirectory())
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
