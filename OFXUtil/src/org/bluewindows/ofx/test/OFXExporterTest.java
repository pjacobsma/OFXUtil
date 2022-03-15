/**
 * Copyright 2010 Phil Jacobsma
 * 
 * This file is part of Figures.
 *
 * Figures is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Figures is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Figures; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.bluewindows.ofx.test;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bluewindows.ofx.OFXAccountType;
import org.bluewindows.ofx.OFXBankMsgResponse;
import org.bluewindows.ofx.OFXBankStatement;
import org.bluewindows.ofx.OFXBankStatementResponse;
import org.bluewindows.ofx.OFXBankTransList;
import org.bluewindows.ofx.OFXContext;
import org.bluewindows.ofx.OFXCorrectActionType;
import org.bluewindows.ofx.OFXCurrency;
import org.bluewindows.ofx.OFXExporter;
import org.bluewindows.ofx.OFXFinancialInstitution;
import org.bluewindows.ofx.OFXSeverityType;
import org.bluewindows.ofx.OFXSignOnMsgResponse;
import org.bluewindows.ofx.OFXSignOnResponse;
import org.bluewindows.ofx.OFXStatementTrans;
import org.bluewindows.ofx.OFXTransType;

import junit.framework.TestCase;

public class OFXExporterTest extends TestCase {
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	private OFXExporter exporter = new OFXExporter();
	MockBufferedWriter bw;
	
	public void testExportOFX() {
		OFXContext context = new OFXContext();
		OFXSignOnMsgResponse signOnMsgResponse = context.getSignOnMsgResponse();
		OFXSignOnResponse signOnResponse = signOnMsgResponse.getSignOnResponse();
		OFXFinancialInstitution fi = signOnResponse.getFinancialInstitution();
		OFXBankMsgResponse bankMsgResponse = context.getBankMsgResponse();
		OFXBankStatementResponse bankStmtResponse = new OFXBankStatementResponse();
		bankMsgResponse.getBankStatementResponses().add(bankStmtResponse);
		OFXBankStatement statement = new OFXBankStatement();
		bankStmtResponse.addStatement(statement);
		OFXBankTransList transList = statement.getBankTransList();
		try {
			fi.setFid("091000019");
			fi.setOrg("ORCC");
			signOnResponse.setServerDate(LocalDateTime.parse("20191202015900", DATE_TIME_FORMATTER));
			signOnResponse.setDateOfLastAcctUpdate(LocalDateTime.parse("20191201015900", DATE_TIME_FORMATTER));
			signOnResponse.setDateOfLastProfUpdate(LocalDateTime.parse("20191130015900", DATE_TIME_FORMATTER));
			signOnResponse.getStatus().setCode(BigInteger.ZERO);
			signOnResponse.getStatus().setSeverity(OFXSeverityType.INFO);
			signOnResponse.setAccessKey("Access Key");
			signOnResponse.setSessionCookie("Cookie");
			signOnResponse.setLanguage("ENG");
			signOnResponse.setTsKeyExpire(LocalDateTime.parse("20191202015900", DATE_TIME_FORMATTER));
			signOnResponse.setUserKey("User Key");
			bankStmtResponse.setTransUID("0");
			bankStmtResponse.getStatus().setCode(BigInteger.ZERO);
			bankStmtResponse.getStatus().setSeverity(OFXSeverityType.INFO);
			statement.getBankAcctFrom().setAccountType(OFXAccountType.CHECKING);
			statement.getBankAcctFrom().setBankID("091000019");
			statement.getBankAcctFrom().setAcctID("123456789");
			statement.getAvailableBalance().setAmount(BigDecimal.valueOf(1000.99));
			statement.getAvailableBalance().setDateOf(LocalDateTime.parse("20200101015900", DATE_TIME_FORMATTER));
			OFXCurrency currency = new OFXCurrency();
			currency.setCurrRate(BigDecimal.valueOf(.55));
			currency.setCurrSym("USD");
			statement.getAvailableBalance().setCurrency(currency);
			statement.getLedgerBalance().setAmount(BigDecimal.valueOf(1000.99));
			statement.getLedgerBalance().setDateOf(LocalDateTime.parse("20200101015900", DATE_TIME_FORMATTER));
			statement.getLedgerBalance().setCurrency(currency);
			statement.setCurDef("USD");
			statement.setMarketingInfo("Marketing Info");
			transList.setDateStart(LocalDate.parse("20191201", DATE_FORMATTER));
			transList.setDateEnd(LocalDate.parse("20191201", DATE_FORMATTER));
			OFXStatementTrans trans = new OFXStatementTrans();
			trans.setFitID("0987654321");
			trans.setTransType(OFXTransType.CHECK);
			trans.setCheckNum("1009");
			trans.setAmount(BigDecimal.valueOf(123.45));
			trans.setDatePosted(LocalDateTime.parse("20190101015900", DATE_TIME_FORMATTER));
			trans.setDateAvail(LocalDateTime.parse("20190102015900", DATE_TIME_FORMATTER));
			trans.setDateUser(LocalDateTime.parse("20191130015900", DATE_TIME_FORMATTER));
			trans.setName("Description");
			trans.setMemo("Memo");
			trans.setCorrectAction(OFXCorrectActionType.REPLACE);
			trans.setCorrectFitID("6543");
			trans.setPayeeID("Payee ID");
			trans.setRefNum("RefNum");
			trans.setServerTranID("Server Tran ID");
			trans.setStdIndustrialCode(BigInteger.valueOf(123456));
			transList.addTransaction(trans);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		bw = new MockBufferedWriter(new StringWriter());
		exporter.exportOFX(context, bw);
//		for (String record : bw.getRecords()) {
//			System.out.println(record);
//		}
		assertRecord("OFXHEADER:100");
		assertRecord("DATA:OFXSGML");
		assertRecord("VERSION:102");
		assertRecord("SECURITY:NONE");
		assertRecord("ENCODING:USASCII");
		assertRecord("CHARSET:1252");
		assertRecord("COMPRESSION:NONE");
		assertRecord("OLDFILEUID:NONE");
		assertRecord("NEWFILEUID:NONE");
		assertRecord("<OFX>");
		assertRecord("<SIGNONMSGSRSV1>");
		assertRecord("<SONRS>");
		assertRecord("<STATUS>");
		assertRecord("<CODE>0");
		assertRecord("<SEVERITY>INFO");
		assertRecord("</STATUS>");
		assertRecord("<DTSERVER>20191202015900");
		assertRecord("<DTACCTUP>20191201015900");
		assertRecord("<DTPROFUP>20191130015900");
		assertRecord("<USERKEY>User Key");
		assertRecord("<TSKEYEXPIRE>20191202015900");
		assertRecord("<SESSCOOKIE>Cookie");
		assertRecord("<ACCESSKEY>Access Key");
		assertRecord("<LANGUAGE>ENG");
		assertRecord("<FI>");
		assertRecord("<ORG>ORCC");
		assertRecord("<FID>091000019");
		assertRecord("</FI>");
		assertRecord("</SONRS>");
		assertRecord("</SIGNONMSGSRSV1>");
		assertRecord("<BANKMSGSRSV1>");
		assertRecord("<STMTTRNRS>");
		assertRecord("<TRNUID>0");
		assertRecord("<STATUS>");
		assertRecord("<CODE>0");
		assertRecord("<SEVERITY>INFO");
		assertRecord("</STATUS>");
		assertRecord("<STMTRS>");
		assertRecord("<CURDEF>USD");
		assertRecord("<BANKACCTFROM>");
		assertRecord("<BANKID>091000019");
		assertRecord("<ACCTID>123456789");
		assertRecord("<ACCTTYPE>CHECKING");
		assertRecord("</BANKACCTFROM>");
		assertRecord("<BANKTRANLIST>");
		assertRecord("<DTSTART>20191201");
		assertRecord("<DTEND>20191201");
		assertRecord("<STMTTRN>");
		assertRecord("<TRNTYPE>CHECK");
		assertRecord("<DTPOSTED>20190101015900");
		assertRecord("<DTUSER>20191130015900");
		assertRecord("<DTAVAIL>20190102015900");
		assertRecord("<TRNAMT>123.45");
		assertRecord("<FITID>0987654321");
		assertRecord("<CORRECTFITID>6543");
		assertRecord("<CORRECTACTION>REPLACE");
		assertRecord("<SRVRTID>Server Tran ID");
		assertRecord("<NAME>Description");
		assertRecord("<MEMO>Memo");
		assertRecord("<CHECKNUM>1009");
		assertRecord("<REFNUM>RefNum");
		assertRecord("<SIC>123456");
		assertRecord("<PAYEEID>Payee ID");
		assertRecord("</STMTTRN>");
		assertRecord("</BANKTRANLIST>");
		assertRecord("<LEDGERBAL>");
		assertRecord("<BALAMT>1000.99");
		assertRecord("<DTASOF>20200101015900");
		assertRecord("<CURRENCY>");
		assertRecord("<CURRATE>0.55");
		assertRecord("<CURSYM>USD");
		assertRecord("</CURRENCY>");
		assertRecord("</LEDGERBAL>");
		assertRecord("<AVAILBAL>");
		assertRecord("<BALAMT>1000.99");
		assertRecord("<DTASOF>20200101015900");
		assertRecord("<CURRENCY>");
		assertRecord("<CURRATE>0.55");
		assertRecord("<CURSYM>USD");
		assertRecord("</CURRENCY>");
		assertRecord("</AVAILBAL>");
		assertRecord("<MKTGINFO>Marketing Info");
		assertRecord("</STMTRS>");
		assertRecord("</STMTTRNRS>");
		assertRecord("</BANKMSGSRSV1>");
		assertRecord("</OFX>");
	}
	
	private void assertRecord(String record) {
		assertEquals(record, bw.getRecord());
	}

	private class MockBufferedWriter extends BufferedWriter {

		private List<String> recordList = new ArrayList<String>();
		private String record = "";
		private int currentRec = 0;
		
		public MockBufferedWriter(Writer arg0) {
			super(arg0);
		}
		
		public void write(String string){
			record = record + string;	
		}
		
		public void newLine(){
			recordList.add(record);
			record = new String("");
		}
		
		public String getRecord(){
			return recordList.get(currentRec++);
		}
		
		@SuppressWarnings("unused")
		public List<String> getRecords() {
			return recordList;
		}
	}
}
