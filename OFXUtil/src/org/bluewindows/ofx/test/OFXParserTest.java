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

import java.text.ParseException;
import java.util.ArrayList;

import org.bluewindows.ofx.OFXParser;

import junit.framework.TestCase;

public class OFXParserTest extends TestCase{
	
	public void testConstructor() throws Exception {
		
		try {
			@SuppressWarnings("unused")
			OFXParser parser = new OFXParser(null);
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	public void testGetTag() throws Exception {
		
		MockInputStream mockStream = new MockInputStream("abc<TAG>xyz");
		OFXParser parser = new OFXParser(mockStream);
		String tag = parser.getTag();
		assertEquals("<TAG>", tag);
		assertEquals(" at record 1, column 4.", parser.getTagPosition());
		mockStream = new MockInputStream("abc" + OFXParser.CR + OFXParser.EOR + "<TAG>xyz");
		parser = new OFXParser(mockStream);
		tag = parser.getTag();
		assertEquals("<TAG>", tag);
		assertEquals(" at record 2, column 1.", parser.getTagPosition());
	}

	public void testGetTagNoTagFound() throws Exception {
		
		MockInputStream mockStream = new MockInputStream("NoTag");
		OFXParser parser = new OFXParser(mockStream);
		try {
			parser.getTag();
			fail("Should throw ParseException");
		} catch (Exception e) {
			assertEquals(" at record 1, column 0.", parser.getTagPosition());
		}
	}

	public void testGetTagPrematureEOF() throws Exception {
		
		MockInputStream mockStream = new MockInputStream("<TAG");
		OFXParser parser = new OFXParser(mockStream);
		try {
			parser.getTag();
			fail("Should throw ParseException");
		} catch (ParseException e) {
			assertEquals(" at record 1, column 1.", parser.getTagPosition());
		}
	}
	
	public void testGetTagValidTagsOnly() throws Exception {

		String expectedTagString = "<VALIDTAG>";
		MockInputStream mockStream = new MockInputStream("<INVALIDTAG>asdf" + expectedTagString + "asdf");
		OFXParser parser = new OFXParser(mockStream);
		ArrayList<String> validTagStrings = new ArrayList<String>();
		validTagStrings.add(expectedTagString);
		String actualTagString = parser.getTag(validTagStrings);
		assertEquals(expectedTagString, actualTagString);
		
	}
	
	public void testGetTagContentHappyPath() throws Exception {
		
		MockInputStream mockStream = new MockInputStream("abc<");
		OFXParser parser = new OFXParser(mockStream);
		String content = parser.getTagContent();
		assertTrue(content.equals("abc"));
		assertEquals(" at record 1, column 1.", parser.getContentPosition());
	}

	public void testGetTagContentEOR() throws Exception {
		
		MockInputStream mockStream = new MockInputStream("a" + OFXParser.CR + OFXParser.EOR + "bc<");
		OFXParser parser = new OFXParser(mockStream);
		String content = parser.getTagContent();
		assertTrue(content.equals("abc"));
		assertEquals(" at record 1, column 1.", parser.getContentPosition());
	}

	public void testGetTagContentEmpty() throws Exception {
		
		MockInputStream mockStream = new MockInputStream("<");
		OFXParser parser = new OFXParser(mockStream);
		String content = parser.getTagContent();
		assertTrue(content.isEmpty());
		assertEquals(" at record 1, column 1.", parser.getContentPosition());
	}

	public void testGetContentPrematureEOF() throws Exception {
		
		MockInputStream mockStream = new MockInputStream("abc");
		OFXParser parser = new OFXParser(mockStream);
		try {
			parser.getTagContent();
			fail("Should throw ParseException");
		} catch (ParseException e) {
			assertEquals(" at record 1, column 1.", parser.getContentPosition());
		}
	}
	
	public void testGetHeaderTag() throws Exception {
		
		MockInputStream mockStream = new MockInputStream("abc:xyz");
		OFXParser parser = new OFXParser(mockStream);
		String tag = parser.getHeaderTag();
		assertEquals("abc", tag);
		mockStream = new MockInputStream("abc" + OFXParser.CR + OFXParser.EOR + "xyz");
		parser = new OFXParser(mockStream);
		try {
			tag = parser.getHeaderTag();
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof ParseException);
			ParseException err = (ParseException)e;
			assertEquals("Premature end of header at record 1, column 3." , err.getMessage());
		}
		assertEquals("abc", tag);
	}
	
	public void testGetHeaderContent() throws Exception {
		
		MockInputStream mockStream = new MockInputStream("abc" + OFXParser.EOR);
		OFXParser parser = new OFXParser(mockStream);
		String content = parser.getHeaderContent();
		assertTrue(content.equals("abc"));
		assertEquals(" at record 1, column 1.", parser.getContentPosition());
	}
	
	public void testGetHeaderContentPrematureEOF() throws Exception {
		
		MockInputStream mockStream = new MockInputStream("a");
		OFXParser parser = new OFXParser(mockStream);
		try {
			@SuppressWarnings("unused")
			String content = parser.getHeaderContent();
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof ParseException);
			ParseException err = (ParseException)e;
			assertEquals("Premature end of header content at record 1, column 1.",err.getMessage());
		}
	}
	
	public void testGetTagPosition() throws Exception {

		MockInputStream mockStream = new MockInputStream("abc:xyz" + OFXParser.EOR);
		OFXParser parser = new OFXParser(mockStream);
		parser.getHeaderTag();
		assertEquals(" at record 1, column 1.", parser.getTagPosition());
		
	}
	
}
