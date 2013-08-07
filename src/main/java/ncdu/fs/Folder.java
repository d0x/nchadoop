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
public class Folder 
{
	private final Folder	parent;
	private final String	name;

	private List<Folder>	folders	= new ArrayList<>();
	private List<File>		files	= new ArrayList<>();

	private long			size	= 0;

	public Folder getFolderByName(final String name)
	{
		for (final Folder folder : this.folders)
		{
			if (StringUtils.equals(name, folder.getName()))
			{
				return folder;
			}
		}
		return null;
	}

	public void add(final LocatedFileStatus file)
	{
//		System.out.println();
//		System.out.println("---------- " + file.getPath() + " --------------");

		final Path folders;

		if (file.isFile())
		{
			folders = file.getPath().getParent();
		}
		else
		{
			folders = file.getPath();
		}

		// das koennte nach unten oder weg
		if (folders.toString().equals(this.name))
		{
			return;
		}

		final Folder folder = addParentFolders(this, folders);

		if (file.isFile())
		{
//			System.out.println("Increase size for file: " + file.getPath());
			increaseParentFolderSize(folder, file.getLen());

			folder.files.add(new File(folder, file.getPath().getName(), file.getLen()));
		}
	}

	private void increaseParentFolderSize(final Folder folder, final long sizeToAdd)
	{
//		System.out.println("\tAdding " + sizeToAdd + " to " + folder.size + " in " + folder);

		folder.size += sizeToAdd;

		if (folder.getParent() != null)
		{
			increaseParentFolderSize(folder.getParent(), sizeToAdd);
		}
	}

	private Folder addParentFolders(final Folder folder, final Path path)
	{
		if (path.getParent() == null || StringUtils.equals(folder.getName(), path.toString()))
		{
			return folder;
		}

		final Folder parent = addParentFolders(folder, path.getParent());

		final String name = path.getName();

		final Folder childFolder = parent.getFolderByName(name);

		if (childFolder != null)
		{
			return childFolder;
		}

		final Folder newChildFolder = new Folder(parent, name);

//		System.out.println("Add folder " + name);

		parent.folders.add(newChildFolder);

		return newChildFolder;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
