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
package org.nchadoop.cli;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.beust.jcommander.JCommander;

public class Parser
{
	public CliConfig parse(String[] args) throws IOException, URISyntaxException
	{
		if (args == null || args.length == 0)
		{
			printHelp();
			return null;
		}
		
		CliConfig config = new CliConfig();

		JCommander jCommander = new JCommander(config, args);

		if (!jCommander.getUnknownOptions().isEmpty() || config.help || isDirInvalid(config))
		{
			printHelp();
			return null;
		}

		return config;
	}

	private boolean isDirInvalid(CliConfig config)
	{
		try
		{
			config.getDir();
			return false;
		}
		catch (URISyntaxException e)
		{
			return true;
		}
	}

	private void printHelp() throws IOException, URISyntaxException
	{
		List<String> readAllLines = Files.readAllLines(Paths.get(Parser.class.getClassLoader().getResource("help.txt").toURI()), Charset.forName("UTF8"));
		for (String string : readAllLines)
		{
			System.out.println(string);
		}
	}

}
