package nchadoop.fs;

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
		// TODO: removed this "new Path()". maybe there is a more elegant way to check this.
		String rootPath = new Path(name).toUri().getPath();
		String currentPath = path.toUri().getPath();

		if (path.isRoot() || rootPath.equals(currentPath))
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

	@Override
	protected void adjustSizeRecursive(long correction)
	{
		super.adjustSizeRecursive(correction);

		totalDiskUsage += correction;
	}

}
