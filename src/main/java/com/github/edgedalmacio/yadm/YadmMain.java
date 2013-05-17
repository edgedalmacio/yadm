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

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 
 * @author Edge Dalmacio
 *
 */
public class YadmMain {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: <sourceDirectory> <targetDirectory> [dryRun]");
			System.exit(0);
		}
		String sourceDirectory = args[0];
		String targetDirectory = args[1];
		Boolean dryRun = args.length == 3 ? Boolean.valueOf(args[2]) : false;
		DestinationResolver destinationResolver = new TvSeriesDestinationResolver();
		YadmFileVisitor fileVisitor = new YadmFileVisitor(targetDirectory, destinationResolver);
		fileVisitor.setDryRun(dryRun);
		try {
			Files.walkFileTree(Paths.get(sourceDirectory), fileVisitor);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
