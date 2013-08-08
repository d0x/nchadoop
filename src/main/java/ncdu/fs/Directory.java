package ncdu.fs;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;

@Slf4j
@Data
public class Directory
{
	private final Directory			parent;
	private final String			name;

	private List<Directory>			directories	= new ArrayList<>();
	private List<LocatedFileStatus>	files		= new ArrayList<>();

	protected long					size		= 0;

	public Directory findDirectoryByName(final String name)
	{
		for (final Directory folder : this.directories)
		{
			if (StringUtils.equals(folder.getName(), name))
			{
				return folder;
			}
		}
		return null;
	}

	protected Directory addDirectory(String name)
	{
		final Directory newDirectory = new Directory(this, name);

		directories.add(newDirectory);

		return newDirectory;
	}

	protected void addFile(LocatedFileStatus file)
	{
		files.add(file);
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
