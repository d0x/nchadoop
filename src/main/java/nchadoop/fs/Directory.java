package nchadoop.fs;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;

@Data
public class Directory
{
	protected final Directory			parent;
	protected final String				name;

	protected List<Directory>			directories	= new ArrayList<>();
	protected List<FileStatus>	files		= new ArrayList<>();

	protected long						size		= 0;

	public boolean isRoot()
	{
		return getParent() == null;
	}

	public String absolutDirectoryName()
	{
		StringBuilder builder = new StringBuilder();

		Directory directory = this;
		do
		{
			builder.insert(0, directory.name + "/");
		} while ((directory = directory.getParent()) != null);

		return builder.toString();
	}

	public Directory findDirectoryByName(final String name)
	{
		for (final Directory directory : this.directories)
		{
			if (StringUtils.equals(directory.name, name))
			{
				return directory;
			}
		}
		return null;
	}

	public Path toPath()
	{
		return new Path(absolutDirectoryName());
	}

	public void remove()
	{
		if (isRoot() == false)
		{
			parent.directories.remove(this);
		}

		adjustSizeRecursive(size * -1);
	}

	public void removeFile(FileStatus file)
	{
		files.remove(file);

		adjustSizeRecursive(file.getLen() * -1);
	}

	protected Directory addDirectory(final String name)
	{
		final Directory directory = new Directory(this, name);

		this.directories.add(directory);

		return directory;
	}

	protected void addFile(final FileStatus file)
	{
		this.files.add(file);
	}

	protected void adjustSizeRecursive(long correction)
	{
		size += correction;

		if (isRoot() == false)
		{
			parent.adjustSizeRecursive(correction);
		}
	}
}
