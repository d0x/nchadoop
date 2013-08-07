package ncdu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;

import org.apache.commons.lang.StringUtils;

@Data
public class Folder
{
	private final String	name;

	private List<Folder>	folders	= new ArrayList<>();
	private Set<String>		files	= new HashSet<>();

	public Folder getChildFolder(final String name)
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
}
