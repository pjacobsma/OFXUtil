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

import org.bluewindows.ofx.HandleOFXBalance;
import org.bluewindows.ofx.HandlerResult;
import org.bluewindows.ofx.OFXBankStatement;
import org.bluewindows.ofx.Tag;

public class HandleBalanceTest extends TagHandlerTestCase{
	

	public void testHandle() throws Exception{
		HandleOFXBalance handler = HandleOFXBalance.INSTANCE;
		Tag tag = new Tag("<LEDGERBAL>", "OFXLedgerBalance", "setLedgerBalance");
		OFXBankStatement account = new OFXBankStatement();
		account.getLedgerBalance().isComplete = true;
		initialize("</OFX>");
		HandlerResult result = handler.handle(parser, account, tag);
		assertTrue(result.isNewAggregate());
		assertFalse(account.getLedgerBalance().isComplete);
	}
}
