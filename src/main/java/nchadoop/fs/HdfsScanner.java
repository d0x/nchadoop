package nchadoop.fs;

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

	public SearchRoot refresh(URI searchUri) throws IOException
	{
		interrupted = false;
		final RemoteIterator<LocatedFileStatus> fileList = this.fileSystem.listFiles(new Path(searchUri), true);

		SearchRoot searchRoot = new SearchRoot(searchUri.toString());

		NoSuchElementException stop = null;

		// This way is required because .hasNext() throws an IOException if its not possible
		// to access the file...
		while (stop == null && !interrupted)
		{
			try
			{
				addFile(searchRoot, fileList.next());
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
		return searchRoot;
	}

	public boolean deleteDirectory(Directory directory) throws IOException
	{
		boolean deleteSuccess = fileSystem.delete(directory.toPath(), true);

		if (deleteSuccess)
		{
			directory.remove();
		}

		return deleteSuccess;
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

}
