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
public class HdfsScanner
{
	private final FileSystem	fileSystem;

	private SearchRoot			searchRoot;

	public HdfsScanner(final URI namenode, final String user) throws IOException, InterruptedException
	{
		this.fileSystem = FileSystem.get(namenode, new Configuration(), user);
	}

	public void refresh(URI searchUri) throws FileNotFoundException, IOException
	{
		final RemoteIterator<LocatedFileStatus> fileList = this.fileSystem.listFiles(new Path(searchUri), true);

		this.searchRoot = new SearchRoot(searchUri.toString());

		while (fileList.hasNext())
		{
			final LocatedFileStatus file = fileList.next();

			addFile(searchRoot, file);
		}
	}

	public void addFile(SearchRoot searchRoot, final LocatedFileStatus file)
	{
		final Path directorPath = file.getPath().getParent();

		final Directory directory = searchRoot.addPath(directorPath, file.getLen());

		directory.addFile(file);
	}

}
