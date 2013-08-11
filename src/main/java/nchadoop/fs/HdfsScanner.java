package nchadoop.fs;

import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;
import java.net.URI;
import java.util.NoSuchElementException;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

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

		final RemoteIterator<LocatedFileStatus> fileList = this.fileSystem.listFiles(new Path(searchUri), true);

		SearchRoot searchRoot = new SearchRoot(searchUri.toString());

		// This way is required because .hasNext() throws an IOException if its not possible
		// to access the file...
		NoSuchElementException stop = null;
		while (stop == null && !interrupted)
		{
			try
			{
				LocatedFileStatus next = fileList.next();
				if (callback != null)
				{
					callback.onVisitFile(next);
				}
				addFile(searchRoot, next);
			}
			catch (IOException e)
			{
				log.warn("IOException on file: {}", e.getMessage());
			}
			catch (NoSuchElementException e)
			{
				stop = e;
			}
		}

		if (callback != null)
		{
			callback.onScanFinished(searchRoot);
		}
		return searchRoot;
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

	private void addFile(SearchRoot searchRoot, final LocatedFileStatus file)
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
		void onVisitFile(LocatedFileStatus next);

		void onScanFinished(SearchRoot searchRoot);

		void onScanStarted(URI searchUri);
	}

}
