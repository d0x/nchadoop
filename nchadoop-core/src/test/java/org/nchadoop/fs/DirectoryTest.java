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
package org.nchadoop.fs;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

import org.junit.Test;
import org.nchadoop.fs.Directory;

public class DirectoryTest
{

	@Test
	public void shouldGetCorrectPath()
	{
		Directory root = new Directory(null, "fs://root");
		Directory child = root.addDirectory("child");
		Directory grandchild = child.addDirectory("grandchild");
		
		assertThat(grandchild.absolutDirectoryName(), is("fs://root/child/grandchild/"));
	}
	
	@Test
	public void shouldGetCorrectPathOnlyRoot()
	{
		Directory root = new Directory(null, "fs://root");
		assertThat(root.absolutDirectoryName(), is("fs://root/"));
	}
	
	@Test
	public void shouldFindDirectories()
	{
		Directory root = new Directory(null, "fs://root");
		Directory child1 = root.addDirectory("child1");
		Directory child2 = root.addDirectory("child2");
		
		root.directories.add(child1);
		root.directories.add(child2);
		
		assertThat(root.findDirectoryByName("child1"), is(child1));
		assertThat(root.findDirectoryByName("child2"), is(child2));
	}

}
