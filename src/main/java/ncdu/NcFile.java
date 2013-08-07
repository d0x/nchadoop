package ncdu;

import lombok.Data;

import org.apache.hadoop.fs.LocatedFileStatus;

@Data
public class NcFile
{
	private String	name;
	private boolean	directory;

	private long	size;

	public NcFile(final LocatedFileStatus file)
	{
		this.name = file.getPath().getName();
		this.directory = file.isDirectory();

		if (file.isFile())
		{
			this.size = file.getLen();
		}
	}

}
