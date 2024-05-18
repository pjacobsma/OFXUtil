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

import org.bluewindows.ofx.HandleOFXSeverityType;
import org.bluewindows.ofx.HandlerResult;
import org.bluewindows.ofx.OFXSeverityType;
import org.bluewindows.ofx.OFXStatus;
import org.bluewindows.ofx.Tag;

public class HandleOFXSeverityTypeTest extends TagHandlerTestCase{

	private HandleOFXSeverityType handler = HandleOFXSeverityType.INSTANCE;
	private Tag tag = new Tag("<OFXSEVERITYTYPE>", "OFXSeverityType", "setSeverity");
	private OFXStatus status;
	
	public void testHandle() throws Exception{
		OFXSeverityType[] severityTypes = OFXSeverityType.values();
		for (OFXSeverityType severityType : severityTypes) {
			initialize(" " + severityType.name() + " </OFX>");
			status = new OFXStatus();
			HandlerResult result = handler.handle(parser, status, tag);
			assertEquals(severityType, status.getSeverity());
			assertFalse(status.isComplete);
			assertFalse(result.isNewAggregate());
		}
	}
	
	public void testHandleBadData() throws Exception {
		initialize("badseverity</OFX>");
		status = new OFXStatus();
		try {
			handler.handle(parser, status, tag);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof ParseException);
			assertEquals("Invalid Severity Type: [badseverity] found in <OFXSEVERITYTYPE> tag at record 1, column 1.", e.getLocalizedMessage());
		}
	}
}
