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

import java.math.BigDecimal;
import java.text.ParseException;

import org.bluewindows.ofx.HandleBigDecimal;
import org.bluewindows.ofx.HandlerResult;
import org.bluewindows.ofx.OFXStatementTrans;
import org.bluewindows.ofx.Tag;

public class HandleBigDecimalTest extends TagHandlerTestCase {

	private HandleBigDecimal handler = HandleBigDecimal.INSTANCE;
	private Tag tag = new Tag("<BIGDECIMAL>", "BigDecimal", "setAmount");
	private OFXStatementTrans transaction;

	public void setUp(){
		transaction = new OFXStatementTrans();
	}
	
	public void testHandle() throws Exception{
		BigDecimal expectedBigDecimal = new BigDecimal("-100.99");
		initialize(" -100.99 </OFX>");
		try {
			HandlerResult result = handler.handle(parser, transaction, tag);
			assertEquals(expectedBigDecimal, transaction.getAmount());
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
			assertEquals("Invalid BigDecimal value: [xxx] found in <BIGDECIMAL> tag at record 1, column 1.", e.getMessage());
		}
	}
}
