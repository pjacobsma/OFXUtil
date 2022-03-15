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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public abstract class AbstractAggregate {

	public boolean isComplete = false;
	protected static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
	protected static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	protected static final NumberFormat MONEY_FORMAT = NumberFormat.getNumberInstance();

	static {
		MONEY_FORMAT.setMaximumFractionDigits(2);
		MONEY_FORMAT.setMinimumFractionDigits(2);
		MONEY_FORMAT.setGroupingUsed(false);
	}
	
	//The following duplicate checks are used to detect a duplicate tag within a given aggregate
	protected void dupeCheck(String stringField) throws ParseException {
		if (stringField != null && (! stringField.isEmpty())){
			throw new ParseException("Duplicate", 0);
		}
	}
	
	protected void dupeCheck(BigDecimal bigDecField) throws ParseException {
		if (bigDecField != null && bigDecField != Constants.MISSING_AMOUNT){
			throw new ParseException("Duplicate", 0);
		}
	}
	
	protected void dupeCheck(LocalDate date) throws ParseException {
		if (date != Constants.MISSING_DATE){
			throw new ParseException("Duplicate", 0);
		}
	}
	
	protected void dupeCheck(LocalDateTime dateTime) throws ParseException {
		if (dateTime != Constants.MISSING_DATE_TIME){
			throw new ParseException("Duplicate", 0);
		}
	}
	
	protected void dupeCheck(BigInteger integer) throws ParseException {
		if (integer != null && integer != Constants.MISSING_INTEGER){
			throw new ParseException("Duplicate", 0);
		}
	}
	
	protected void dupeCheck(OFXCorrectActionType correctAction) throws ParseException {
		if (correctAction != null && correctAction != OFXCorrectActionType.MISSING){
			throw new ParseException("Duplicate", 0);
		}
	}
	
	protected void dupeCheck(OFXSeverityType severityType) throws ParseException {
		if (severityType != null && severityType != OFXSeverityType.MISSING){
			throw new ParseException("Duplicate", 0);
		}
	}
	
	protected void dupeCheck(OFXTransType tranType) throws ParseException {
		if (tranType != null && tranType != OFXTransType.MISSING){
			throw new ParseException("Duplicate", 0);
		}
	}


	protected void dupeCheck(OFXAccountType accountType) throws ParseException {
		if (accountType != null && accountType != OFXAccountType.MISSING){
			throw new ParseException("Duplicate", 0);
		}
	}
	
	protected void dupeCheck(OFXLedgerBalance balance) throws ParseException {
		dupeCheck(balance.getAmount());
	}
	
	protected void dupeCheck(OFXAvailBalance balance) throws ParseException {
		dupeCheck(balance.getAmount());
	}

	protected void dupeCheck(OFXStatus status) throws ParseException {
		dupeCheck(status.getCode());
	}

	protected void dupeCheck(OFXBankAcctFrom bankAcctFrom) throws ParseException {
		dupeCheck(bankAcctFrom.getBankID());
	}

	protected void dupeCheck(OFXCardAcctFrom cardAcctFrom) throws ParseException {
		dupeCheck(cardAcctFrom.getAcctID());
	}

	protected void dupeCheck(OFXBankTransList bankTranList) throws ParseException {
		dupeCheck(bankTranList.getDateStart());
	}
	
	protected void dupeCheck(OFXFinancialInstitution fi) throws ParseException {
		dupeCheck(fi.getFid());
	}
	
	protected void dupeCheck(OFXSignOnResponse signOnResponse) throws ParseException {
		dupeCheck(signOnResponse.getServerDate());
	}
	
	protected void dupeCheck(OFXSignOnMsgResponse signOnMsgResponse) throws ParseException {
		dupeCheck(signOnMsgResponse.getSignOnResponse());
	}

	protected void dupeCheck(OFXBankMsgResponse signOnMsgResponse) throws ParseException {
		if (signOnMsgResponse.getBankStatementResponses().size() > 0) {
			throw new ParseException("Duplicate", 0);
		}
	}
	protected void dupeCheck(OFXCardMsgResponse signOnMsgResponse) throws ParseException {
		if (signOnMsgResponse.getCardStatementResponses().size() > 0) {
			throw new ParseException("Duplicate", 0);
		}
	}
	
	protected void dupeCheck(HashMap<String, String> hashMap) throws ParseException {
		if (hashMap.size() > 0) {
			throw new ParseException("Duplicate", 0);
		}
	}
	
	protected void dupeCheck(OFXCurrency currency) throws ParseException {
		if (currency.getCurrRate() != Constants.MISSING_AMOUNT) {
			throw new ParseException("Duplicate", 0);
		}
	}
	
	public void export(BufferedWriter bw) throws IOException {
		writeLine(bw, TagMap.getInstance().getOpenTag(this));
		exportContent(bw);
		writeLine(bw, TagMap.getInstance().getCloseTag(this));
	}
	
	protected abstract void exportContent(BufferedWriter bw) throws IOException;
	
	protected void writeLine(BufferedWriter bw, String content) throws IOException {
		bw.write(content);
		bw.newLine();
	}


}
