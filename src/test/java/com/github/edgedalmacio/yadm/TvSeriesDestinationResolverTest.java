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

import org.junit.Before;
import org.junit.Test;

import com.github.edgedalmacio.yadm.DestinationResolver;
import com.github.edgedalmacio.yadm.TvSeriesDestinationResolver;

/**
 * 
 * @author Edge Dalmacio
 *
 */
public class TvSeriesDestinationResolverTest {

	private DestinationResolver parser;
	
	@Before
	public void setUp() {
		parser = new TvSeriesDestinationResolver();
	}
	
	@Test
	public void parse() {
		String input = "The.Walking.Dead.S03E14.HDTV.x264-ASAP.mp4";
		
		String actual = parser.resolveDestination("/", input);
		assertNotNull(actual);
		
		assertEquals("The Walking Dead/Season 3/The.Walking.Dead.S03E14.HDTV.x264.mp4", actual);
	}
	
}

