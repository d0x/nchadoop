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

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class ParserTest
{

	Parser	cut	= new Parser();

	@Test
	public void shouldHandleInvalidParameter() throws URISyntaxException, IOException
	{
		assertThat(cut.parse(new String[]{""}), is(nullValue()));
		assertThat(cut.parse(null), is(nullValue()));
	}
	
	@Test
	public void shouldReadAllValues() throws URISyntaxException, IOException
	{
		String[] argv = {"--exclude", "*.jpg", "--exclude", "*.png", "file:///tmp"};

		CliConfig cliConfig = cut.parse(argv);

		assertThat(cliConfig.help, is(false));
		assertThat(cliConfig.getFilter(), arrayContainingInAnyOrder("*.jpg", "*.png"));
		assertThat(cliConfig.getDir(), is(new URI(("file:///tmp"))));
	}

	@Test
	public void shouldReadJustDir() throws URISyntaxException, IOException
	{
		String[] argv = {"/tmp"};

		CliConfig cliConfig = cut.parse(argv);

		assertThat(cliConfig.help, is(false));
		assertThat(cliConfig.getFilter(), is(emptyArray()));
		assertThat(cliConfig.getDir(), is(new URI(("/tmp"))));
	}

	@Test
	public void shouldPrintHelp() throws URISyntaxException, IOException
	{
		assertThat(cut.parse(new String[]{"-h"}), is(nullValue()));
		assertThat(cut.parse(new String[]{"--help"}), is(nullValue()));
	}

}
