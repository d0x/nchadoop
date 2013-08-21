/*******************************************************************************
 * Copyright 2013 Christian Schneider
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package nchadoop.cli;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.beust.jcommander.Parameter;

public class CliConfig
{
	@Parameter(names = {"-h","--help"})
	public boolean		help;

	@Parameter(names = "--exclude")
	public List<String>	filter;

	@Parameter(description = "dirs")
	public List<String>	dir;

	public URI getDir() throws URISyntaxException
	{
		if(dir == null || dir.isEmpty())
		{
			throw new URISyntaxException("", "Dir is empty");
		}
		return new URI(dir.get(0));
	}

	public String[] getFilter()
	{
		if (filter == null)
			return new String[0];

		return filter.toArray(new String[0]);
	}
}
