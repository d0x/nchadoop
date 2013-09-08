package org.nchadoop.fs;

import java.io.IOException;

import org.apache.hadoop.fs.GlobFilter;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

public class GlobFilterConverter
{
	public PathFilter toGlobFilter(final String[] globFilter) throws IOException
	{
		// TODO: Remove this one
		PathFilter pathFilter = new PathFilter() {

			@Override
			public boolean accept(final Path path)
			{
				return true;
			}
		};

		for (final String filterPattern : globFilter)
		{
			pathFilter = new GlobFilter(filterPattern, pathFilter);
		}

		return pathFilter;
	}
}
