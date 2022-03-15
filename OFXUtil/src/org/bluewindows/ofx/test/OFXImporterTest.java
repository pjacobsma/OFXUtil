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
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.bluewindows.ofx.OFXAccountType;
import org.bluewindows.ofx.OFXAvailBalance;
import org.bluewindows.ofx.OFXBankAcctFrom;
import org.bluewindows.ofx.OFXBankStatement;
import org.bluewindows.ofx.OFXBankStatementResponse;
import org.bluewindows.ofx.OFXBankTransList;
import org.bluewindows.ofx.OFXCallResult;
import org.bluewindows.ofx.OFXContext;
import org.bluewindows.ofx.OFXFinancialInstitution;
import org.bluewindows.ofx.OFXImporter;
import org.bluewindows.ofx.OFXLedgerBalance;
import org.bluewindows.ofx.OFXParser;
import org.bluewindows.ofx.OFXSeverityType;
import org.bluewindows.ofx.OFXSignOnMsgResponse;
import org.bluewindows.ofx.OFXSignOnResponse;
import org.bluewindows.ofx.OFXStatementTrans;
import org.bluewindows.ofx.OFXStatus;
import org.bluewindows.ofx.OFXTransType;

import junit.framework.TestCase;

public class OFXImporterTest extends TestCase {

	private OFXImporter importer = new OFXImporter();
	
	public void testImportGoodFile() throws Exception {
		//Valid headers and data
		String goodFile = " " + OFXParser.EOR +
			"OFXHEADER:100" + OFXParser.EOR +
			"DATA:OFXSGML" + OFXParser.EOR +
			"VERSION:102" + OFXParser.EOR + 
			"SECURITY:NONE" + OFXParser.EOR + 
			"ENCODING:USASCII" + OFXParser.EOR + 
			"CHARSET:1252" + OFXParser.EOR + 
			"COMPRESSION:NONE" + OFXParser.EOR + 
			"OLDFILEUID:NONE" + OFXParser.EOR + 
			"NEWFILEUID:NONE" + OFXParser.EOR +
			"<OFX><SIGNONMSGSRSV1><SONRS>" + 
			"<STATUS><CODE>0<SEVERITY>INFO</STATUS>" + 
			"<DTSERVER>20220101125129[-05:EST]<LANGUAGE>ENG<DTACCTUP>20211231112330" + 
			"<FI><ORG>ORCC<FID>12345</FI></SONRS></SIGNONMSGSRSV1>" + 
			"<BANKMSGSRSV1><STMTTRNRS><TRNUID>0<STATUS><CODE>0<SEVERITY>INFO</STATUS>" + 
			"<STMTRS><CURDEF>USD" + 
			"<BANKACCTFROM><BANKID>123456789<ACCTID>987654321<ACCTTYPE>CHECKING</BANKACCTFROM>" + 
			"<BANKTRANLIST><DTSTART>20220101<DTEND>20220101" + 
			"<STMTTRN><TRNTYPE>POS<DTPOSTED>20220101025929" + 
			"<TRNAMT>-13.17<FITID>6789|20220101025929" + 
			"<NAME>JIMMY JOE&#39;S VINTAGE STORE<MEMO>DebitCard, POS</STMTTRN>" + 
			"</BANKTRANLIST>" + 
			"<LEDGERBAL><BALAMT>5253.48<DTASOF>20220101125129</LEDGERBAL>" +
			"<AVAILBAL><BALAMT>5241.92<DTASOF>20220101125129</AVAILBAL>" +
			"</STMTRS></STMTTRNRS></BANKMSGSRSV1></OFX>";
		OFXCallResult result = importer.importOFX(stream(goodFile));
		assertEquals(null, result.getException());
		OFXContext context = result.getContext();
		// Retrieve the headers
		HashMap<String, String> headerMap = context.getHeaderMap();
		assertEquals("100", headerMap.get("OFXHEADER"));
		assertEquals("OFXSGML", headerMap.get("DATA"));
		assertEquals("102", headerMap.get("VERSION"));
		assertEquals("NONE", headerMap.get("SECURITY"));
		assertEquals("USASCII", headerMap.get("ENCODING"));
		assertEquals("1252", headerMap.get("CHARSET"));
		assertEquals("NONE", headerMap.get("COMPRESSION"));
		assertEquals("NONE", headerMap.get("OLDFILEUID"));
		assertEquals("NONE", headerMap.get("NEWFILEUID"));
		
		// The nested data structure in the OFXContext object mirrors the structure of the OFX file
		// SignOnMsgResponse
		OFXSignOnMsgResponse signOnMsgResponse = context.getSignOnMsgResponse();
		OFXSignOnResponse signOnResponse = signOnMsgResponse.getSignOnResponse();
		OFXStatus signOnResponseStatus = signOnResponse.getStatus();
		assertEquals(BigInteger.valueOf(0), signOnResponseStatus.getCode());
		assertEquals(OFXSeverityType.INFO, signOnResponseStatus.getSeverity());
		assertEquals(LocalDateTime.of(2022, 1, 1, 12, 51, 29), signOnResponse.getServerDate());
		assertEquals("ENG", signOnResponse.getLanguage());
		assertEquals(LocalDateTime.of(2021, 12, 31, 11, 23, 30), signOnResponse.getDateOfLastAcctUpdate());
		OFXFinancialInstitution financialInstitution = signOnResponse.getFinancialInstitution();
		assertEquals("ORCC", financialInstitution.getOrg());
		assertEquals("12345", financialInstitution.getFid());
		
		// BankMsgResponse List
		assertTrue(context.getBankMsgResponse().isComplete);
		List<OFXBankStatementResponse> responses = context.getBankMsgResponse().getBankStatementResponses();
		assertEquals(1, responses.size());
		
		// BankStatementResponse
		OFXBankStatementResponse response = responses.get(0);
		assertEquals("0", response.getTransUID());
		OFXStatus bankStatementResponseStatus = response.getStatus();
		assertEquals(BigInteger.valueOf(0), bankStatementResponseStatus.getCode());
		assertEquals(OFXSeverityType.INFO, bankStatementResponseStatus.getSeverity());
		
		// BankStatement List
		List<OFXBankStatement> statements = response.getStatements();
		assertEquals(1, statements.size());
		OFXBankStatement statement = statements.get(0);
		assertEquals("USD", statement.getCurDef());
		OFXBankAcctFrom bankAcctFrom = statement.getBankAcctFrom();
		assertEquals("123456789", bankAcctFrom.getBankID());
		assertEquals("987654321", bankAcctFrom.getAcctID());
		assertEquals(OFXAccountType.CHECKING, bankAcctFrom.getAccountType());
		OFXLedgerBalance ledgerBalance = statement.getLedgerBalance();
		assertEquals(BigDecimal.valueOf(5253.48), ledgerBalance.getAmount());
		assertEquals(LocalDateTime.of(2022, 1, 1, 12, 51, 29), ledgerBalance.getDateOf());
		OFXAvailBalance availBalance = statement.getAvailableBalance();
		assertEquals(BigDecimal.valueOf(5241.92), availBalance.getAmount());
		assertEquals(LocalDateTime.of(2022, 1, 1, 12, 51, 29), availBalance.getDateOf());
		
		// StatementTransaction List
		OFXBankTransList transList = statement.getBankTransList();
		assertEquals(LocalDate.of(2022, 1, 1), transList.getDateStart());
		assertEquals(LocalDate.of(2022, 1, 1), transList.getDateEnd());
		
		List<OFXStatementTrans> transactions = transList.getTransactions();
		assertEquals(1, transactions.size());
		OFXStatementTrans transaction = transactions.get(0);
		assertEquals(OFXTransType.POS, transaction.getTransType());
		assertEquals(LocalDateTime.of(2022, 1, 1, 2, 59, 29), transaction.getDatePosted());
		assertEquals(BigDecimal.valueOf(-13.17), transaction.getAmount());
		assertEquals("6789|20220101025929", transaction.getFitID());
		assertEquals("JIMMY JOE'S VINTAGE STORE", transaction.getName());
		assertEquals("DebitCard, POS", transaction.getMemo());
	}

	public void testImportBadHeaders() throws Exception {
		//Empty file
		String badFile = "";
		assertParseException(importer.importOFX(stream(badFile)), "Invalid OFX file.  No headers found.");
		//Missing headers
		badFile = "abcxyz";
		assertParseException(importer.importOFX(stream(badFile)), "Premature end of header at record 1, column 6.");
		//OFXHEADER missing
		badFile = "DATA:OFXSGML" + OFXParser.EOR + OFXParser.EOR;
		assertParseException(importer.importOFX(stream(badFile)), "Invalid OFX file.  OFXHEADER not found.");
		//VERSION missing
		badFile = "OFXHEADER:100" + OFXParser.EOR + OFXParser.EOR;
		assertParseException(importer.importOFX(stream(badFile)), "Invalid OFX file.  VERSION header not found.");
	}
	
	private MockInputStream stream(String streamContents){
		return new MockInputStream(streamContents);
	}
	
	private void assertParseException(OFXCallResult result, String message) {
		assertTrue(result.isCallBad());
		assertTrue(result.getException() instanceof ParseException);
		assertEquals(message, result.getException().getMessage());
	}

}
