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

import org.bluewindows.ofx.HandleOFXAccountType;
import org.bluewindows.ofx.HandlerResult;
import org.bluewindows.ofx.OFXAccountType;
import org.bluewindows.ofx.OFXBankAcctFrom;
import org.bluewindows.ofx.Tag;

public class HandleACCTTYPETest extends TagHandlerTestCase{

	private HandleOFXAccountType handler = HandleOFXAccountType.INSTANCE;
	private Tag tag = new Tag("<ACCTTYPE>", "OFXAccountType", "setAccountType");
	private OFXBankAcctFrom bankAcctFrom;
	
	public void testHandle() throws Exception{
		
		OFXAccountType[] accountTypes = OFXAccountType.values();
		for (OFXAccountType accountType : accountTypes) {
			initialize(" " + accountType.name() + " </OFX>");
			bankAcctFrom = new OFXBankAcctFrom();
			HandlerResult result = handler.handle(parser, bankAcctFrom, tag);
			assertEquals(accountType, bankAcctFrom.getAccountType());
			assertFalse(bankAcctFrom.isComplete);
			assertFalse(result.isNewAggregate());
		}
		
	}
	
	public void testHandleInvalidTranType() throws Exception{
		initialize("badAcctType</OFX>");
		bankAcctFrom = new OFXBankAcctFrom();
		try {
			handler.handle(parser, bankAcctFrom, tag);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof ParseException);
			assertEquals("Invalid Account Type: [badAcctType] found in <ACCTTYPE> tag at record 1, column 1.", e.getMessage());
		}
	}
}
