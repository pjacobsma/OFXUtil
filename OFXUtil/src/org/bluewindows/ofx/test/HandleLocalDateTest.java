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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.bluewindows.ofx.HandleLocalDate;
import org.bluewindows.ofx.OFXBankTransList;
import org.bluewindows.ofx.Tag;

public class HandleLocalDateTest extends TagHandlerTestCase{
	
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMdd");
	private HandleLocalDate handler = HandleLocalDate.INSTANCE;
	private Tag tag = new Tag("<DTSTART>", "LocalDate", "setDateStart");
	private OFXBankTransList transList;
	
	public void setUp() {
		transList = new OFXBankTransList();
	}
	
	public void testHandleDateYYYYMMDD() throws Exception{
		String dateString = "20090102";
		initialize(dateString + "</OFX>");
		LocalDate expectedDate = LocalDate.parse(dateString, DTF);
		try {
			handler.handle(parser, transList, tag);
			assertEquals(expectedDate, transList.getDateStart());
		} catch (Exception e) {
			fail();
		}
	}
	
	public void testHandleNonNumericDate() throws Exception {
		initialize("xxx</OFX>");
		try {
			handler.handle(parser, transList, tag);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof ParseException);
			assertEquals("Invalid date value: [xxx] found in <DTSTART> tag at record 1, column 1.", e.getMessage());
		}
	}
	
	public void testDateTooShort() throws Exception {
		initialize("2009010</OFX>");
		try {
			handler.handle(parser, transList, tag);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof ParseException);
			assertEquals("Invalid date value: [2009010] found in <DTSTART> tag at record 1, column 1.", e.getMessage());
		}
	}


}