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


public class OFXCardAcctFrom extends AbstractAggregate {
	//OFX CCACCTFROM object
	
	String acctID = "";
	OFXAccountType accountType = OFXAccountType.CREDITCARD;
	String acctKey = "";
	
	public String getAcctID() {
		return acctID;
	}
	
	public void setAcctID(String acctID) throws ParseException {
		dupeCheck(this.acctID);
		this.acctID = acctID;
	}
	
	public OFXAccountType getAccountType() {
		return accountType;
	}
	
	public void setAccountType(OFXAccountType accountType) throws ParseException {
		dupeCheck(this.accountType);
		this.accountType = accountType;
	}
	
	public String getAcctKey() {
		return acctKey;
	}
	
	public void setAcctKey(String acctKey) throws ParseException {
		dupeCheck(this.acctKey);
		this.acctKey = acctKey;
	}

	@Override
	protected void exportContent(BufferedWriter bw) throws IOException {
		if (acctID.isBlank()) acctID = "1";
		writeLine(bw, "<ACCTID>" + acctID);
		if (!accountType.equals(OFXAccountType.MISSING)) {
			writeLine(bw, "<ACCTTYPE>" + accountType.toString());
		}
		if (!acctKey.isBlank()) {
			writeLine(bw, "<ACCTKEY>" + acctKey);
		}
	}
	

}
