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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bluewindows.ofx.HandleLocalDateTime;
import org.bluewindows.ofx.OFXSignOnResponse;
import org.bluewindows.ofx.Tag;

public class HandleLocalDateTimeTest extends TagHandlerTestCase{
	
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	private HandleLocalDateTime handler = HandleLocalDateTime.INSTANCE;
	private Tag tag = new Tag("<DTSERVER>", "LocalDateTime", "setServerDate");
	private OFXSignOnResponse response;
	
	public void setUp() {
		response = new OFXSignOnResponse();
	}
	
	public void testHandleDateTime() throws Exception{
		String dateString = "20090102015900";
		initialize(dateString + "</OFX>");
		LocalDateTime expectedDate = LocalDateTime.parse(dateString, DTF);
		try {
			handler.handle(parser, response, tag);
			assertEquals(expectedDate, response.getServerDate());
		} catch (Exception e) {
			fail();
		}
	}
	
	public void testHandleNonNumericDate() throws Exception {
		initialize("xxx</OFX>");
		try {
			handler.handle(parser, response, tag);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof ParseException);
			assertEquals("Invalid date-time value: [xxx] found in <DTSERVER> tag at record 1, column 1.", e.getMessage());
		}
	}
	
	public void testDateTooShort() throws Exception {
		initialize("2009010</OFX>");
		try {
			handler.handle(parser, response, tag);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof ParseException);
			assertEquals("Invalid date-time value: [2009010] found in <DTSERVER> tag at record 1, column 1.", e.getMessage());
		}
	}


}