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
import java.util.ArrayList;

public class OFXCardStatementResponse extends AbstractAggregate {
	//OFX CCSTMTRNRS object
	
	private String transUID = "";
	private OFXStatus status = new OFXStatus();
	private ArrayList<OFXCardStatement> statements = new ArrayList<OFXCardStatement>();
	
	public String getTransUID() {
		return transUID;
	}

	public void setTransUID(String transUID) throws ParseException {
		dupeCheck(this.transUID);
		this.transUID = transUID;
	}

	public OFXStatus getStatus() {
		return status;
	}

	public void setStatus(OFXStatus status) throws ParseException {
		dupeCheck(this.status);
		this.status = status;
	}

	public ArrayList<OFXCardStatement> getStatements() {
		return statements;
	}
	
	public void addStatement(OFXCardStatement statement){
		statements.add(statement);
	}

	@Override
	protected void exportContent(BufferedWriter bw) throws IOException {
		if (transUID.isBlank()) transUID = "0";
		writeLine(bw, "<TRNUID>" + transUID);
		status.export(bw);
		for (OFXCardStatement statement : statements) {
			statement.export(bw);
		}
	}

}
