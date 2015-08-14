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

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link FileVisitor} that moves target files to their corresponding target
 * destination
 * 
 * @author Edge Dalmacio
 * 
 * @since 1.0
 */
public class YadmFileVisitor extends SimpleFileVisitor<Path> {

	private final static Logger logger = LoggerFactory.getLogger(YadmFileVisitor.class);

	private final String targetDirectory;
	
	private final String fileSeparator;

	private final CopyOption[] options = { StandardCopyOption.REPLACE_EXISTING };
	
	private final DestinationResolver destinationResolver;
	
	private boolean dryRun = false;
	
	public YadmFileVisitor(String targetDirectory, 
			DestinationResolver destinationResolver) {
		this(targetDirectory, destinationResolver, FileSystems.getDefault().getSeparator());
	}
	
	public YadmFileVisitor(String targetDirectory,
			DestinationResolver destinationResolver,
			String fileSeparator) {
		Objects.requireNonNull(targetDirectory, "'target' must not be null");
		Objects.requireNonNull(destinationResolver, "'destinationResolver' must not be null");
		Objects.requireNonNull(fileSeparator, "'fileSeparator' must not be null");
		this.targetDirectory = targetDirectory;
		this.destinationResolver = destinationResolver;
		this.fileSeparator = fileSeparator;
	}

	@Override
	public FileVisitResult visitFile(Path sourcePath, BasicFileAttributes attrs)
			throws IOException {
		
		Objects.equals(FileVisitResult.CONTINUE, super.visitFile(sourcePath, attrs));
		
		String fileName = sourcePath.getFileName().toString();
		String destination = destinationResolver.resolveDestination(fileSeparator, fileName);
		
		if (destination == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Skipping {} ...", fileName);
			}
			return FileVisitResult.CONTINUE;
		}
		
		String targetUri = new StringBuilder(targetDirectory)
				.append(fileSeparator)
				.append(destination)
				.toString();
		
		Path targetPath = Paths.get(targetUri);
		
		if (sourcePath.equals(targetPath)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Skipping {} ...", fileName);
			}
			return FileVisitResult.CONTINUE;
		}

		if (logger.isInfoEnabled()) {
			logger.info("Moving {} to {} ...", sourcePath, targetPath);
		}

		if (!dryRun) {
			try {
				Files.move(sourcePath, targetPath, options);
			} catch (Exception e) {
				if (logger.isWarnEnabled()) {
					logger.warn(e.getMessage());
				}
			}
		}
		
		return FileVisitResult.CONTINUE;
	}

	public boolean isDryRun() {
		return dryRun;
	}

	public void setDryRun(boolean dryRun) {
		this.dryRun = dryRun;
	}

}
