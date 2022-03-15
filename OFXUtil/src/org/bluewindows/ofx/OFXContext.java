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
import java.util.HashMap;

public class OFXContext extends AbstractAggregate {
	//This is the top-level container for all the objects created in parsing the OFX stream

	private HashMap<String, String> headerMap = new HashMap<String, String>();
	private OFXSignOnMsgResponse signOnMsgResponse = new OFXSignOnMsgResponse();
	private OFXBankMsgResponse bankMessageResponse = new OFXBankMsgResponse();
	private OFXCardMsgResponse cardMessageResponse = new OFXCardMsgResponse();
	
	public void setHeaderMap(HashMap<String, String> headerMap) throws ParseException {
		dupeCheck(this.headerMap);
		this.headerMap = headerMap;
	}

	public HashMap<String, String> getHeaderMap() {
		return headerMap;
	}

	public void setSignOnMsgResponse(OFXSignOnMsgResponse signOnMsgResponse) throws ParseException {
		dupeCheck(this.signOnMsgResponse);
		this.signOnMsgResponse = signOnMsgResponse;
	}

	public OFXSignOnMsgResponse getSignOnMsgResponse() {
		return signOnMsgResponse;
	}

	public OFXBankMsgResponse getBankMsgResponse() {
		return bankMessageResponse;
	}
	
	public void setBankMsgResponse(OFXBankMsgResponse bankMessageResponse) throws ParseException{
		dupeCheck(this.bankMessageResponse);
		this.bankMessageResponse = bankMessageResponse;
	}

	public OFXCardMsgResponse getCardMsgResponse() {
		return cardMessageResponse;
	}

	public void setCardMsgResponse(OFXCardMsgResponse cardMessageResponse) throws ParseException{
		dupeCheck(this.cardMessageResponse);
		this.cardMessageResponse = cardMessageResponse;
	}

	@Override
	protected void exportContent(BufferedWriter bw) throws IOException {
		signOnMsgResponse.export(bw);
		if (!bankMessageResponse.getBankStatementResponses().isEmpty()) {
			bankMessageResponse.export(bw);
		}
		if (!cardMessageResponse.getCardStatementResponses().isEmpty()) {
			cardMessageResponse.export(bw);
		}
	}
	
}
