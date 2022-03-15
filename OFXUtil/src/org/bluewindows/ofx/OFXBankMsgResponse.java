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
import java.util.ArrayList;

public class OFXBankMsgResponse extends AbstractAggregate {
	//OFX BANKMSGSRSV1 object
	
	private ArrayList<OFXBankStatementResponse> bankStatementResponses = new ArrayList<OFXBankStatementResponse>();

	public ArrayList<OFXBankStatementResponse> getBankStatementResponses() { 
		return bankStatementResponses;
	}
	
	public void addBankStatementResponse(OFXBankStatementResponse response){
		bankStatementResponses.add(response);
	}

	@Override
	protected void exportContent(BufferedWriter bw) throws IOException {
		for (OFXBankStatementResponse response : bankStatementResponses) {
			response.export(bw);
		}
	}

}
