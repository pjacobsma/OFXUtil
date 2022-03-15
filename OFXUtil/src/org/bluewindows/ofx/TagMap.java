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


public class TagMap extends AbstractTagMap {

	//This class maps the OFX tag string to its content object, target object, and the setter on the target object  
	//used to store the content object.
	
	//If the parser finds a tag that is not defined here, that tag and its contents will be 
	//ignored (as per the OFX specification).

	//If you add a new version tag map class, add a test for it to the testOFXTagMaps method in TestOFXTagMaps
	//so you can verify all the object and method mappings.
	
	//Note that some tags can occur within more than one target object.  So tags by themselves are not unique, but the 
	//combination of tag and target object must be unique.
	
	//Each entry specifies:
	// 1) The tag string.
	// 2) The content object type to be instantiated for this tag's content.
	// 3) The parent aggregate type which stores the content object (or "" if there isn't one).
	// 4) The setter method on the parent aggregate used to store the content object (or "" if there isn't one).

	private static TagMap instance = new TagMap();

	private TagMap(){
//         Tag                  	Content Object Type         Parent Object Type          Setter on Parent Object         
//         -------------------- 	--------------------------  --------------------------- ---------------------------
		addTag("</AVAILBAL>",       "OFXAvailBalance",          "OFXAvailBalance",          "");
		addTag("</BANKACCTFROM>",   "OFXBankAcctFrom",          "OFXBankAcctFrom",          "");
		addTag("</BANKMSGSRSV1>",   "OFXBankMsgResponse",       "OFXBankMsgResponse",       "");
		addTag("</BANKTRANLIST>",   "OFXBankTransList",         "OFXBankTransList",         "");
		addTag("</CCACCTFROM>",   	"OFXCardAcctFrom",          "OFXCardAcctFrom",          "");
		addTag("</CCSTMTTRNRS>",    "OFXCardStatement", 		"OFXCardStatement", 		"");
		addTag("</CREDITCARDMSGSRSV1>","OFXCardMsgResponse", 	"OFXCardStatementResponse", "");
		addTag("</CURRENCY>",       "OFXCurrency",              "OFXCurrency",              "");
		addTag("</FI>",             "OFXFinancialInstitution",  "OFXFinancialInstitution",  "");
		addTag("</LEDGERBAL>",      "OFXLedgerBalance",         "OFXLedgerBalance",         "");
		addTag("</OFX>",            "OFXContext",               "",                         ""); 
		addTag("</SIGNONMSGSRSV1>", "OFXSignOnMsgResponse",     "OFXSignOnMsgResponse",     "");
		addTag("</SONRS>",          "OFXSignOnResponse",        "OFXSignOnResponse",        "");
		addTag("</STATUS>",         "OFXStatus",                "OFXStatus",                "");
		addTag("</STMTRS>",         "OFXBankStatement",         "OFXBankStatement",         "");
		addTag("</STMTTRN>",        "OFXStatementTrans",        "OFXStatementTrans",        "");
		addTag("</STMTTRNRS>",      "OFXBankStatementResponse", "OFXBankStatementResponse", "");
		addTag("<ACCESSKEY>",       "String",                   "OFXSignOnResponse",        "setAccessKey");
		addTag("<ACCTID>",          "String",                   "OFXBankAcctFrom",          "setAcctID");
		addTag("<ACCTID>",          "String",                   "OFXCardAcctFrom",          "setAcctID");
		addTag("<ACCTKEY>",         "String",                   "OFXBankAcctFrom",          "setAcctKey");
		addTag("<ACCTKEY>",         "String",                   "OFXCardAcctFrom",          "setAcctKey");
		addTag("<ACCTTYPE>",        "OFXAccountType",           "OFXBankAcctFrom",          "setAccountType");
		addTag("<AVAILBAL>",        "OFXAvailBalance",          "OFXBankStatement",         "setAvailableBalance");
		addTag("<AVAILBAL>",        "OFXAvailBalance",          "OFXCardStatement",         "setAvailableBalance");
		addTag("<BALAMT>",          "BigDecimal",               "OFXAvailBalance",          "setAmount");
		addTag("<BALAMT>",          "BigDecimal",               "OFXLedgerBalance",         "setAmount");
		addTag("<BANKACCTFROM>",    "OFXBankAcctFrom",          "OFXBankStatement",         "setBankAcctFrom");
		addTag("<BANKID>",          "String",                   "OFXBankAcctFrom",          "setBankID");
		addTag("<BANKMSGSRSV1>",    "OFXBankMsgResponse",       "OFXContext",               "setBankMsgResponse");
		addTag("<BANKTRANLIST>",    "OFXBankTransList",         "OFXBankStatement",         "setBankTransList");
		addTag("<BANKTRANLIST>",    "OFXBankTransList",         "OFXCardStatement",         "setBankTransList");
		addTag("<BRANCHID>",        "String",                   "OFXBankAcctFrom",          "setBranchID");
		addTag("<CCACCTFROM>",      "OFXCardAcctFrom",          "OFXCardStatement",         "setCardAcctFrom");
		addTag("<CCSTMTRS>",        "OFXCardStatement",         "OFXCardStatementResponse", "addStatement");
		addTag("<CCSTMTTRNRS>",     "OFXCardStatementResponse", "OFXCardMsgResponse", 		"addCardStatementResponse");
		addTag("<CHECKNUM>",        "String",                   "OFXStatementTrans",        "setCheckNum");
		addTag("<CODE>",            "BigInteger",               "OFXStatus",                "setCode");
		addTag("<CORRECTACTION>",   "OFXCorrectActionType",     "OFXStatementTrans",        "setCorrectAction");
		addTag("<CORRECTFITID>",    "String",                   "OFXStatementTrans",        "setCorrectFitID");
		addTag("<CREDITCARDMSGSRSV1>","OFXCardMsgResponse", 	"OFXContext", 				"setCardMsgResponse");
		addTag("<CURDEF>",          "String",                   "OFXBankStatement",         "setCurDef");
		addTag("<CURDEF>",          "String",                   "OFXCardStatement",         "setCurDef");
		addTag("<CURRATE>",         "BigDecimal",               "OFXCurrency",              "setCurrRate");
		addTag("<CURRENCY>",        "OFXCurrency",              "OFXAvailBalance",          "setCurrency");
		addTag("<CURRENCY>",        "OFXCurrency",              "OFXLedgerBalance",         "setCurrency");
		addTag("<CURSYM>",          "String",                   "OFXCurrency",              "setCurrSym");
		addTag("<DTACCTUP>",        "LocalDateTime",            "OFXSignOnResponse",        "setDateOfLastAcctUpdate");
		addTag("<DTASOF>",          "LocalDateTime",            "OFXAvailBalance",          "setDateOf");
		addTag("<DTASOF>",          "LocalDateTime",            "OFXLedgerBalance",         "setDateOf");
		addTag("<DTAVAIL>",         "LocalDateTime",            "OFXStatementTrans",        "setDateAvail");
		addTag("<DTEND>",           "LocalDate",                "OFXBankTransList",         "setDateEnd");
		addTag("<DTPOSTED>",        "LocalDateTime",            "OFXStatementTrans",        "setDatePosted");
		addTag("<DTPROFUP>",        "LocalDateTime",            "OFXSignOnResponse",        "setDateOfLastProfUpdate");
		addTag("<DTSERVER>",        "LocalDateTime",            "OFXSignOnResponse",        "setServerDate");
		addTag("<DTSTART>",         "LocalDate",                "OFXBankTransList",         "setDateStart");
		addTag("<DTUSER>",          "LocalDateTime",            "OFXStatementTrans",        "setDateUser");
		addTag("<FI>",              "OFXFinancialInstitution",  "OFXSignOnResponse",        "setFinancialInstitution");
		addTag("<FID>",             "String",                   "OFXFinancialInstitution",  "setFid");
		addTag("<FITID>",           "String",                   "OFXStatementTrans",        "setFitID");
		addTag("<LANGUAGE>",        "String",                   "OFXSignOnResponse",        "setLanguage");
		addTag("<LEDGERBAL>",       "OFXLedgerBalance",         "OFXBankStatement",         "setLedgerBalance");
		addTag("<LEDGERBAL>",       "OFXLedgerBalance",         "OFXCardStatement",         "setLedgerBalance");
		addTag("<MEMO>",            "String",                   "OFXStatementTrans",        "setMemo");
		addTag("<MESSAGE>",         "String",                   "OFXStatus",                "setMessage");
		addTag("<NAME>",            "String",                   "OFXStatementTrans",        "setName");
		addTag("<OFX>",             "OFXContext",               "",                         "");
		addTag("<ORG>",             "String",                   "OFXFinancialInstitution",  "setOrg");
		addTag("<PAYEEID>",         "String",                   "OFXStatementTrans",        "setPayeeID");
		addTag("<REFNUM>",          "String",                   "OFXStatementTrans",        "setRefNum");
		addTag("<SESSCOOKIE>",      "String",                   "OFXSignOnResponse",        "setSessionCookie");
		addTag("<SEVERITY>",        "OFXSeverityType",          "OFXStatus",                "setSeverity");
		addTag("<SIC>",             "BigInteger",               "OFXStatementTrans",        "setStdIndustrialCode");
		addTag("<SIGNONMSGSRSV1>",  "OFXSignOnMsgResponse",     "OFXContext",               "setSignOnMsgResponse");
		addTag("<SONRS>",           "OFXSignOnResponse",        "OFXSignOnMsgResponse",     "setSignOnResponse");
		addTag("<SRVRTID>",         "String",                   "OFXStatementTrans",        "setServerTranID");
		addTag("<STATUS>",          "OFXStatus",                "OFXSignOnResponse",        "setStatus");
		addTag("<STATUS>",          "OFXStatus",                "OFXBankStatementResponse", "setStatus");
		addTag("<STATUS>",          "OFXStatus",                "OFXCardStatementResponse", "setStatus");
		addTag("<STMTRS>",          "OFXBankStatement",         "OFXBankStatementResponse", "addStatement");
		addTag("<STMTTRN>",         "OFXStatementTrans",        "OFXBankTransList",         "addTransaction");
		addTag("<STMTTRNRS>",       "OFXBankStatementResponse", "OFXBankMsgResponse",       "addBankStatementResponse");
		addTag("<TRNAMT>",          "BigDecimal",               "OFXStatementTrans",        "setAmount");
		addTag("<TRNTYPE>",         "OFXTransType",             "OFXStatementTrans",        "setTransType");
		addTag("<TRNUID>",          "String",                   "OFXBankStatementResponse", "setTransUID");
		addTag("<TRNUID>",          "String",                   "OFXCardStatementResponse", "setTransUID");
		addTag("<TSKEYEXPIRE>",     "LocalDateTime",            "OFXSignOnResponse",        "setTsKeyExpire");
		addTag("<USERKEY>",         "String",                   "OFXSignOnResponse",        "setUserKey");

	}

	public static TagMap getInstance() {
		return instance;
	}
}
