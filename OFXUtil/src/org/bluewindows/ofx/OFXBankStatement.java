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

package org.bluewindows.ofx;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

public class OFXBankStatement extends AbstractAggregate {
	// OFX STMTRS Object
	
	String curDef = "";
	OFXBankAcctFrom bankAcctFrom = new OFXBankAcctFrom();
	OFXBankTransList bankTransList = new OFXBankTransList();
	OFXLedgerBalance ledgerBalance = new OFXLedgerBalance();
	OFXAvailBalance availableBalance = new OFXAvailBalance();
	String marketingInfo = "";

	public String getCurDef() {
		return curDef;
	}

	public void setCurDef(String curDef) throws ParseException {
		dupeCheck(this.curDef);
		this.curDef = curDef;
	}

	public OFXBankAcctFrom getBankAcctFrom() {
		return bankAcctFrom;
	}

	public OFXBankTransList getBankTransList() {
		return bankTransList;
	}

	public void setBankTransList(OFXBankTransList bankTranList) throws ParseException {
		dupeCheck(this.bankTransList); 
		this.bankTransList = bankTranList;
	}

	public void setBankAcctFrom(OFXBankAcctFrom bankAcctFrom) throws ParseException {
		dupeCheck(this.bankAcctFrom);
		this.bankAcctFrom = bankAcctFrom;
	}

	public OFXLedgerBalance getLedgerBalance() {
		return ledgerBalance;
	}

	public void setLedgerBalance(OFXLedgerBalance ledgerBalance) throws ParseException {
		dupeCheck(this.ledgerBalance);
		this.ledgerBalance = ledgerBalance;
	}

	public OFXAvailBalance getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(OFXAvailBalance availableBalance) throws ParseException {
		dupeCheck(this.availableBalance);
		this.availableBalance = availableBalance;
	}

	public String getMarketingInfo() {
		return marketingInfo;
	}

	public void setMarketingInfo(String marketingInfo) throws ParseException {
		dupeCheck(this.marketingInfo);
		this.marketingInfo = marketingInfo;
	}

	@Override
	protected void exportContent(BufferedWriter bw) throws IOException {
		if (curDef.isEmpty()) curDef = Currency.getInstance(Locale.getDefault()).getCurrencyCode();
		writeLine(bw, "<CURDEF>" + curDef);
		bankAcctFrom.export(bw);
		bankTransList.export(bw);
		if (!ledgerBalance.getAmount().equals(Constants.MISSING_AMOUNT)) {
			ledgerBalance.export(bw);
		}
		if (!availableBalance.getAmount().equals(Constants.MISSING_AMOUNT)) {
			availableBalance.export(bw);
		}
		if (!marketingInfo.isEmpty()) {
			writeLine(bw, "<MKTGINFO>" + marketingInfo);
		}
	}
	
}
