/**
 * Copyright 2013 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.edgedalmacio.yadm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Edge Dalmacio
 * 
 * @since 1.0
 *
 */
public class TvSeriesDestinationResolver implements DestinationResolver {

	private final Pattern tvPattern = Pattern.compile("(\\S+)\\.S?+(\\d{1,2})(E|x)\\d{2}\\..*");
	
	private final int nameIndex = 1;
	private final int seasonIndex = 2;
	
	@Override
	public String resolveDestination(String fileSeparator, String fileName) {
		String cleanFileName = cleanFileName(fileName);

		Matcher matcher = tvPattern.matcher(cleanFileName);
		matcher.find();

		String name = null;
		if (matcher.find(nameIndex)) {
			name = matcher.group(nameIndex).replaceAll("\\.", " ");
		}
		
		Integer season = null;
		if (matcher.find(seasonIndex)) {
			season = Integer.parseInt(matcher.group(seasonIndex));
		}
		
		if (name == null || season == null || cleanFileName.endsWith(".utpart")) {
			return null;
		}
		
		String destination = new StringBuilder()
			.append(cleanFileName.charAt(0))
			.append(name)
			.append(fileSeparator)
			.append("Season ")
			.append(season)
			.append(fileSeparator)
			.append(cleanFileName)
			.toString();
		
		return destination;
	}

	private String cleanFileName(String fileName) {
		int indexOfHyphen = fileName.indexOf('-');
		if (indexOfHyphen > -1) {
			String firstPart = fileName.substring(0, indexOfHyphen);
			int lastIndexOfDot = fileName.lastIndexOf('.');
			String secondPart = lastIndexOfDot != -1 ? fileName.substring(lastIndexOfDot) : "";
			return firstPart + secondPart;
		}
		return fileName;
	}

}
