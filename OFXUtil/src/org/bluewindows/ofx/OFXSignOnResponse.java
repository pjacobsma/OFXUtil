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
import java.time.LocalDateTime;

public class OFXSignOnResponse extends AbstractAggregate {
	//OFX SONRS object
	
	OFXStatus status = new OFXStatus();
	LocalDateTime serverDate = Constants.MISSING_DATE_TIME;
	String language = "";
	OFXFinancialInstitution financialInstitution = new OFXFinancialInstitution();
	LocalDateTime dateOfLastAcctUpdate = Constants.MISSING_DATE_TIME;
	LocalDateTime dateOfLastProfUpdate = Constants.MISSING_DATE_TIME;
	String userKey = "";
	LocalDateTime tsKeyExpire = Constants.MISSING_DATE_TIME;
	String sessionCookie = "";
	String accessKey = "";
	
	public OFXStatus getStatus() {
		return status;
	}
	public void setStatus(OFXStatus status) throws ParseException {
		dupeCheck(this.status);
		this.status = status;
	}
	public LocalDateTime getServerDate() {
		return serverDate;
	}
	public void setServerDate(LocalDateTime serverDate) throws ParseException {
		dupeCheck(this.serverDate);
		this.serverDate = serverDate;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) throws ParseException {
		dupeCheck(this.language);
		this.language = language;
	}
	public OFXFinancialInstitution getFinancialInstitution() {
		return financialInstitution;
	}
	public void setFinancialInstitution(OFXFinancialInstitution fi) throws ParseException {
		dupeCheck(this.financialInstitution);
		this.financialInstitution = fi;
	}
	public LocalDateTime getDateOfLastAcctUpdate() {
		return dateOfLastAcctUpdate;
	}
	public void setDateOfLastAcctUpdate(LocalDateTime dateOfLastAcctUpdate) throws ParseException {
		dupeCheck(this.dateOfLastAcctUpdate);
		this.dateOfLastAcctUpdate = dateOfLastAcctUpdate;
	}
	public LocalDateTime getDateOfLastProfUpdate() {
		return dateOfLastProfUpdate;
	}
	public void setDateOfLastProfUpdate(LocalDateTime dateOfLastProfUpdate) throws ParseException {
		dupeCheck(this.dateOfLastProfUpdate);
		this.dateOfLastProfUpdate = dateOfLastProfUpdate;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) throws ParseException  {
		dupeCheck(this.userKey);
		this.userKey = userKey;
	}
	public LocalDateTime getTsKeyExpire() {
		return tsKeyExpire;
	}
	public void setTsKeyExpire(LocalDateTime tsKeyExpire) throws ParseException {
		dupeCheck(this.tsKeyExpire);
		this.tsKeyExpire = tsKeyExpire;
	}
	public String getSessionCookie() {
		return sessionCookie;
	}
	public void setSessionCookie(String sessionCookie) throws ParseException {
		dupeCheck(this.sessionCookie);
		this.sessionCookie = sessionCookie;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) throws ParseException {
		dupeCheck(this.accessKey);
		this.accessKey = accessKey;
	}
	@Override
	protected void exportContent(BufferedWriter bw) throws IOException {
		status.export(bw);
		writeLine(bw, "<DTSERVER>" + serverDate.format(DATE_TIME_FORMAT));
		if (!dateOfLastAcctUpdate.equals(Constants.MISSING_DATE_TIME)) {
			writeLine(bw, "<DTACCTUP>" + dateOfLastAcctUpdate.format(DATE_TIME_FORMAT));
		}
		if (!dateOfLastProfUpdate.equals(Constants.MISSING_DATE_TIME)) {
			writeLine(bw, "<DTPROFUP>" + dateOfLastProfUpdate.format(DATE_TIME_FORMAT));
		}
		if (!userKey.isBlank()) {
			writeLine(bw, "<USERKEY>" + userKey);
		}
		if (!tsKeyExpire.equals(Constants.MISSING_DATE_TIME)) {
			writeLine(bw, "<TSKEYEXPIRE>" + tsKeyExpire.format(DATE_TIME_FORMAT));
		}
		if (!sessionCookie.isBlank()) {
			writeLine(bw, "<SESSCOOKIE>" + sessionCookie);
		}
		if (!accessKey.isBlank()) {
			writeLine(bw, "<ACCESSKEY>" + accessKey);
		}
		if (!language.isBlank()) {
			writeLine(bw, "<LANGUAGE>" + language);
		}
		financialInstitution.export(bw);
	}

	
}
