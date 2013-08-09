package ncdu.fs;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.LocatedFileStatus;

@Data
public class Directory
{
	private final Directory			parent;
	private final String			name;

	private List<Directory>			directories	= new ArrayList<>();
	private List<LocatedFileStatus>	files		= new ArrayList<>();

	protected long					size		= 0;

	public boolean isRoot()
	{
		return getParent() == null;
	}

	public String absolutDirectoryName()
	{
		if (isRoot())
		{
			return "/";
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
}
