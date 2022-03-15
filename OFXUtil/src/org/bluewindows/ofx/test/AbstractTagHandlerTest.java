/**
 * Copyright 2010 Phil Jacobsma
 * 
 * This file is part of OFXUtil.
 *
 * OFXUtil is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * OFXUtil is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OFXUtil; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.bluewindows.ofx.test;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import org.bluewindows.ofx.AbstractAggregate;
import org.bluewindows.ofx.AbstractTagHandler;
import org.bluewindows.ofx.AbstractTagMap;
import org.bluewindows.ofx.HandlerResult;
import org.bluewindows.ofx.OFXContext;
import org.bluewindows.ofx.OFXParser;
import org.bluewindows.ofx.Tag;
import org.bluewindows.ofx.TagMapKey;

import junit.framework.TestCase;

public class AbstractTagHandlerTest extends TestCase {
	
	private class MockOFXTagHandler extends AbstractTagHandler {
		@Override
		public Object convertContentToObject(String contentString, Tag tag, OFXParser parser) throws ParseException {
			return null;
		}
		@Override
		public HandlerResult handle(OFXParser parser, AbstractAggregate aggregate, Tag tag) throws ParseException { 
			return null;
		}
	}
	
	private class MockTagMap extends AbstractTagMap {
		public MockTagMap(TagMapKey tagMapKey, Tag tag){
			tags.put(tagMapKey, tag);
		}

	}
	
	private class MockOFXParser extends OFXParser {
		public MockOFXParser(InputStream inStream) {
			super(inStream);
		}
		public String getTagContent() throws IOException, ParseException{
			throw new IOException();
		}
	}
	
	public class MockAggregate extends OFXContext {
		public void setTestString(String string){}
	}
	
	
	private MockOFXTagHandler handler = new MockOFXTagHandler();
	private TagMapKey tagMapKey;
	private MockTagMap tagMap;
	private Tag tag;
	
	public void setUp() {
		tagMapKey = new TagMapKey("<TAG>", "MockAggregate");
		tagMap = new MockTagMap(tagMapKey, new Tag("<TAG>", "String", "setTestString"));
		tag = tagMap.getTag(tagMapKey);
	}
	
	public void testGetTagContentIOError() throws Exception {
		MockInputStream mockStream = new MockInputStream("");
		MockOFXParser parser = new MockOFXParser(mockStream);
		try {
			handler.getTagContent(parser, tag);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof ParseException);
			assertEquals("IO Error occurred retrieving " + tag.getName() + " tag content at record 1, column 0.", e.getMessage());
		}
	}
	public void testGetTagContentMissingContent() throws Exception {
		MockInputStream mockStream = new MockInputStream("</OFX>");
		OFXParser parser = new OFXParser(mockStream);
		try {
			handler.getTagContent(parser, tag);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof ParseException);
			assertEquals("Missing content for <TAG> tag at record 1, column 0.", e.getMessage());
		}
	}
	
	public void testPutContentObjectIntoTargetObject() throws Exception {
		MockAggregate aggregate = new MockAggregate();
		String contentObject = "abc";
		try {
			handler.putContentObjectIntoTargetObject(aggregate, tag, contentObject);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testPutContentObjectIntoTargetObjectTargetMethodNotFound() throws Exception {
		tagMap = new MockTagMap(tagMapKey, new Tag("<TAG>", "String", "setBogusMethod"));
		tag = tagMap.getTag(tagMapKey);
		MockAggregate aggregate = new MockAggregate();
		String contentObject = "abc";
		try {
			handler.putContentObjectIntoTargetObject(aggregate, tag, contentObject);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertTrue(e.getMessage().contains("There is no setBogusMethod method"));
		}
	}
}
