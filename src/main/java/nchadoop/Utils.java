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
package nchadoop;

import java.text.DecimalFormat;

import nchadoop.fs.Directory;

import org.apache.hadoop.fs.LocatedFileStatus;

public class Utils
{
	public static final String[]		UNITS			= new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB"};
	public static final DecimalFormat	DECIMAL_FORMAT	= new DecimalFormat("#,##0.0");

	private Utils()
	{
		// hide utility class constructor
	}

	public static String readableFileSize(final long size)
	{
		if (size < 0)
			return "";

		if (size == 0)
			return "0.0";

		final int digitGroup = (int) (Math.log10(size) / Math.log10(1024));

		return DECIMAL_FORMAT.format(size / Math.pow(1024, digitGroup)) + " " + UNITS[digitGroup];
	}
}
