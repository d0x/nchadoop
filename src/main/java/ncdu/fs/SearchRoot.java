package ncdu.fs;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.hadoop.fs.Path;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchRoot extends Directory
{
	private long	totalDiskUsage;
	private long	totalFiles;

	public SearchRoot(final String name)
	{
		super(null, name);
	}

	public Directory addPath(final Path path, final long size)
	{
		if (path.isRoot())
		{
			this.totalDiskUsage += size;
			this.totalFiles++;
			this.size += size;
			return this;
		}

		final Directory parentDirectory = addPath(path.getParent(), size);

		final String pathName = path.getName();

		Directory directory = parentDirectory.findDirectoryByName(pathName);

		if (directory == null)
		{
			directory = parentDirectory.addDirectory(pathName);
		}

		directory.setSize(directory.getSize() + size);

		return directory;
	}

}
