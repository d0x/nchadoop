package ncdu.fs;

import lombok.Data;

@Data
public class File
{
	private final Folder	parent;
	private final String	name;
	private final long		size;

	@Override
	public String toString()
	{
		return this.name;
	}

}
