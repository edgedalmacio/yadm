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

import static org.junit.Assert.*;

import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.edgedalmacio.yadm.DestinationResolver;
import com.github.edgedalmacio.yadm.YadmFileVisitor;

/**
 * 
 * @author Edge Dalmacio
 * 
 */
public class YadmFileVisitorTest {

	private FileVisitor<Path> fileVisitor;

	private Path source = Paths.get("src/test/resources/x.txt");

	private Path target = Paths.get("target/x.txt");

	@Before
	public void setUp() throws Exception {
		tearDown();
		Files.createFile(source);
		fileVisitor = new YadmFileVisitor("target", new DestinationResolver() {
			
			@Override
			public String resolveDestination(String fileSeparator, String fileName) {
				return fileName;
			}
		});
	}

	@After
	public void tearDown() throws Exception {
		Files.deleteIfExists(source);
		Files.deleteIfExists(target);
	}
	
	@Test
	public void testVisitFile() throws Exception {
		Path start = Paths.get("src/test/resources");
		Files.walkFileTree(start, fileVisitor);

		boolean actual = Files.isRegularFile(target);
		assertTrue(actual);
	}
}
