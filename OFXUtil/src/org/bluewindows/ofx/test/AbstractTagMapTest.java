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

import org.bluewindows.ofx.AbstractTagMap;

import junit.framework.TestCase;

public class AbstractTagMapTest extends TestCase {
	
	private class MockTagMap extends AbstractTagMap {

		public void addTag(String tagName, String contentObjectName, String targetObjectName, String targetObjectSetterName){
			super.addTag(tagName, contentObjectName, targetObjectName, targetObjectSetterName);
		}
	}

	public void testAddTag() throws Exception {
		MockTagMap tagMap = new MockTagMap();
		tagMap.addTag("<TAG>", "String", "Aggregate", "setString");
		try {
			tagMap.addTag("<TAG>", "String", "Aggregate", "setString");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Duplicate tag definition for tag <TAG> with target object Aggregate in tag map.", e.getLocalizedMessage());
		}
	}
	
	public void testGetOpenTag() {
		MockTagMap tagMap = new MockTagMap();
		tagMap.addTag("<TAG>", "String", "", "");
		tagMap.addTag("<\\TAG>", "String", "", "");
		assertEquals("<TAG>", tagMap.getOpenTag("String"));
	}
	
	public void testGetCloseTag() {
		MockTagMap tagMap = new MockTagMap();
		tagMap.addTag("<TAG>", "String", "", "");
		tagMap.addTag("</TAG>", "String", "", "");
		assertEquals("</TAG>", tagMap.getCloseTag("String"));
	}
	
}
