package nchadoop.fs;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;

@Data
public class Directory
{
	protected final Directory			parent;
	protected final String				name;

	protected List<Directory>			directories	= new ArrayList<>();
	protected List<LocatedFileStatus>	files		= new ArrayList<>();

	protected long						size		= 0;

	public boolean isRoot()
	{
		return getParent() == null;
	}

	public String absolutDirectoryName()
	{
		if (isRoot())
		{
			if (name.endsWith("/"))
				return name;
			else
				return name + "/";
		}

		return getParent().absolutDirectoryName() + this.name + "/";
	}

	public Directory findDirectoryByName(final String name)
	{
		for (final Directory directory : this.directories)
		{
			if (StringUtils.equals(directory.getName(), name))
			{
				return directory;
			}
		}
		return null;
	}

	protected Directory addDirectory(final String name)
	{
		final Directory newDirectory = new Directory(this, name);

		this.directories.add(newDirectory);

		return newDirectory;
	}

	protected void addFile(final LocatedFileStatus file)
	{
		this.files.add(file);
	}

	@Override
	public String toString()
	{
		return this.name;
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

	protected void adjustSizeRecursive(long correction)
	{
		size += correction;

		if (isRoot() == false)
		{
			parent.adjustSizeRecursive(correction);
		}
	}
}
