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
import java.text.ParseException;
import java.time.LocalDateTime;

public class OFXStatementTrans extends AbstractAggregate {
	//OFX STMTRN object
	
	private String fitID = "";
	private String correctFitID = "";
	private OFXCorrectActionType correctAction = OFXCorrectActionType.MISSING;
	private String serverTranID = "";
	private OFXTransType transType = OFXTransType.MISSING;
	private LocalDateTime datePosted = Constants.MISSING_DATE_TIME;
	private LocalDateTime dateUser = Constants.MISSING_DATE_TIME;
	private LocalDateTime dateAvail = Constants.MISSING_DATE_TIME;
	private BigDecimal amount = Constants.MISSING_AMOUNT;
	private String name = "";
	private String memo = "";
	private String checkNum = "";
	private String refNum = "";
	private BigInteger stdIndustrialCode = Constants.MISSING_INTEGER;
	private String payeeID = "";


	public String getFitID() {
		return fitID;
	}
	
	public void setFitID(String fitID) throws ParseException {
		dupeCheck(this.fitID);
		this.fitID = fitID;
	}
	
	public String getCorrectFitID() {
		return correctFitID;
	}

	public void setCorrectFitID(String correctFitID) throws ParseException {
		dupeCheck(this.correctFitID);
		this.correctFitID = correctFitID;
	}

	public OFXCorrectActionType getCorrectAction() {
		return correctAction;
	}

	public void setCorrectAction(OFXCorrectActionType correctAction) throws ParseException  {
		dupeCheck(this.correctAction);
		this.correctAction = correctAction;
	}

	public String getServerTranID() {
		return serverTranID;
	}

	public void setServerTranID(String serverTranID) throws ParseException  {
		dupeCheck(this.serverTranID);
		this.serverTranID = serverTranID;
	}

	public OFXTransType getTransType() {
		return transType;
	}
	
	public void setTransType(OFXTransType tranType) throws ParseException  {
		dupeCheck(this.transType);
		this.transType = tranType;
	}
	
	public LocalDateTime getDatePosted() {
		return datePosted;
	}
	
	public void setDatePosted(LocalDateTime datePosted) throws ParseException  {
		dupeCheck(this.datePosted);
		this.datePosted = datePosted;
	}
	
	public LocalDateTime getDateUser() {
		return dateUser;
	}

	public void setDateUser(LocalDateTime dateUser) throws ParseException  {
		dupeCheck(this.dateUser);
		this.dateUser = dateUser;
	}

	public LocalDateTime getDateAvail() {
		return dateAvail;
	}

	public void setDateAvail(LocalDateTime dateAvail) throws ParseException  {
		dupeCheck(this.dateAvail);
		this.dateAvail = dateAvail;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) throws ParseException  {
		dupeCheck(this.amount);
		this.amount = amount;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) throws ParseException  {
		dupeCheck(this.name);
		this.name = name;
	}
	
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) throws ParseException  {
		dupeCheck(this.memo);
		this.memo = memo;
	}

	public String getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(String checkNum) throws ParseException  {
		dupeCheck(this.checkNum);
		this.checkNum = checkNum;
	}

	public String getRefNum() {
		return refNum;
	}
	
	public void setRefNum(String refNum) throws ParseException  {
		dupeCheck(this.refNum);
		this.refNum = refNum;
	}

	
	public void setStdIndustrialCode(BigInteger stdIndustrialCode) throws ParseException {
		dupeCheck(this.stdIndustrialCode);
		this.stdIndustrialCode = stdIndustrialCode;
	}

	public BigInteger getStdIndustrialCode() {
		return stdIndustrialCode;
	}

	public String getPayeeID() {
		return payeeID;
	}

	public void setPayeeID(String payeeID) throws ParseException {
		dupeCheck(this.payeeID);
		this.payeeID = payeeID;
	}

	@Override
	protected void exportContent(BufferedWriter bw) throws IOException {
		writeLine(bw, "<TRNTYPE>" + transType.toString());
		writeLine(bw, "<DTPOSTED>" + DATE_TIME_FORMAT.format(datePosted));
		if (!dateUser.equals(Constants.MISSING_DATE_TIME)) {
			writeLine(bw, "<DTUSER>" + DATE_TIME_FORMAT.format(dateUser));
		}
		if (!dateAvail.equals(Constants.MISSING_DATE_TIME)) {
			writeLine(bw, "<DTAVAIL>" + DATE_TIME_FORMAT.format(dateAvail));
		}
		writeLine(bw, "<TRNAMT>" + MONEY_FORMAT.format(amount));
		if (!fitID.isBlank()) {
			writeLine(bw, "<FITID>" + fitID);
		}
		if (!correctFitID.isBlank()) {
			writeLine(bw, "<CORRECTFITID>" + correctFitID);
		}
		if (!correctAction.equals(OFXCorrectActionType.MISSING)) {
			writeLine(bw, "<CORRECTACTION>" + correctAction.toString());
		}
		if (!serverTranID.isBlank()) {
			writeLine(bw, "<SRVRTID>" + serverTranID);
		}
		writeLine(bw, "<NAME>" + name);
		if (!memo.isBlank()) {
			writeLine(bw, "<MEMO>" + memo);
		}
		if (!checkNum.isBlank()) {
			writeLine(bw, "<CHECKNUM>" + checkNum);
		}
		if (!refNum.isBlank()) {
			writeLine(bw, "<REFNUM>" + refNum);
		}
		if (!stdIndustrialCode.equals(Constants.MISSING_INTEGER)) {
			writeLine(bw, "<SIC>" + stdIndustrialCode);
		}
		if (!payeeID.isBlank()) {
			writeLine(bw, "<PAYEEID>" + payeeID);
		}
	}
	

}
