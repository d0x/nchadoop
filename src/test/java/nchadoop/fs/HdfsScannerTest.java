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
package nchadoop.fs;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import nchadoop.fs.util.TestEnviorment;

import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HdfsScannerTest
{
	URI				namenode;
	HdfsScanner		cut;
	String			pwd;
	SearchRoot		refresh;
	TestEnviorment	testFolder;

	@Before
	public void setup() throws URISyntaxException, IOException, InterruptedException
	{
		this.testFolder = new TestEnviorment("/simple.fs");

		this.namenode = new URI("file://" + this.testFolder.getTempDirectory().toString());

		this.cut = new HdfsScanner(this.namenode, "hdfs");

		this.refresh = this.cut.refresh(this.namenode);
	}

	@After
	public void tearDown() throws IOException
	{
		this.testFolder.close();
	}

	@Test
	public void shouldCountAllFiles() throws FileNotFoundException, IOException
	{
		assertThat(this.refresh.getTotalFiles(), is(3L));
	}

	@Test
	public void shouldSumTotalDiskUsage()
	{
		assertThat(this.refresh.getTotalDiskUsage(), is(26L));
	}

	@Test
	public void shouldSumRootFolder()
	{
		assertThat(this.refresh.getSize(), is(26L));
	}

	@Test
	public void shouldSumChildFolder()
	{
		assertThat(this.refresh.getDirectories().get(0).getSize(), is(26L));
	}

	@Test
	public void shouldStartAtSearchFolder()
	{
		final List<Directory> directories = this.refresh.getDirectories();
		assertThat(directories.size(), is(1));

		final Directory directory = directories.get(0);

		assertThat(directory.getName(), equalTo("simple"));
	}

	@Test
	public void shouldStartAtSearchFolderWithoutProtocoll() throws FileNotFoundException, IOException, InterruptedException, URISyntaxException
	{
		this.namenode = new URI(this.testFolder.getTempDirectory().toString());
		this.cut = new HdfsScanner(this.namenode, "hdfs");
		this.refresh = this.cut.refresh(this.namenode);

		final List<Directory> directories = refresh.getDirectories();
		assertThat(directories.size(), is(1));

		final Directory directory = directories.get(0);

		assertThat(directory.getName(), equalTo("simple"));
	}

	@Test
	public void shouldDeleteDirectories() throws IOException
	{
		Directory directory = refresh.directories.get(0);
		String dirToDeleteString = directory.absolutDirectoryName();
		String pathWithoutProtocol = new Path(dirToDeleteString).toUri().getPath();

		assertThat(new File(pathWithoutProtocol).exists(), is(true));
		assertTrue(cut.deleteDirectory(directory));
		assertThat(new File(pathWithoutProtocol).exists(), is(false));
	}

	@Test
	public void shouldDeleteDirectoriesAndAdjustFileSizes() throws IOException
	{
		Directory directory = refresh.findDirectoryByName("simple").findDirectoryByName("26byte").findDirectoryByName("10byte");

		long oldTotalDiskUsage = refresh.getTotalDiskUsage();
		long directorySize = directory.size;

		cut.deleteDirectory(directory);

		assertThat(refresh.getTotalDiskUsage(), is(oldTotalDiskUsage - directorySize));

	}

}
