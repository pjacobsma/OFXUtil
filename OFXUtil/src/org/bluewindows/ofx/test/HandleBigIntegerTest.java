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

import java.math.BigInteger;
import java.text.ParseException;

import org.bluewindows.ofx.HandleBigInteger;
import org.bluewindows.ofx.HandlerResult;
import org.bluewindows.ofx.OFXStatementTrans;
import org.bluewindows.ofx.Tag;

public class HandleBigIntegerTest extends TagHandlerTestCase {

	private HandleBigInteger handler = HandleBigInteger.INSTANCE;
	private Tag tag = new Tag("<BIGINTEGER>", "BigInteger", "setStdIndustrialCode");
	private OFXStatementTrans transaction;

	public void setUp(){
		transaction = new OFXStatementTrans();
	}
	
	public void testHandle() throws Exception{
		BigInteger expectedInteger = new BigInteger("1234");
		initialize(" 1234 </OFX>");
		try {
			HandlerResult result = handler.handle(parser, transaction, tag);
			assertEquals(expectedInteger, transaction.getStdIndustrialCode());
			assertFalse(transaction.isComplete);
			assertFalse(result.isNewAggregate());
		} catch (Exception e) {
			fail();
		}
	}
	
	public void testHandleBadData() throws Exception {
		initialize("xxx</OFX>");
		try {
			handler.handle(parser, transaction, tag);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof ParseException);
			assertEquals("Invalid BigInteger value: [xxx] found in <BIGINTEGER> tag at record 1, column 1.", e.getLocalizedMessage());
		}
	}
}
