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
import java.time.LocalDate;
import java.util.ArrayList;

public class OFXBankTransList extends AbstractAggregate {
	//OFX BANKTRANLIST object
	
	LocalDate dateStart = Constants.MISSING_DATE;
	LocalDate dateEnd = Constants.MISSING_DATE;
	ArrayList<OFXStatementTrans> transactions = new ArrayList<OFXStatementTrans>();

	public LocalDate getDateStart() {
		return dateStart;
	}

	public void setDateStart(LocalDate dateStart) throws ParseException {
		dupeCheck(this.dateStart);
		this.dateStart = dateStart;
	}

	public LocalDate getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(LocalDate dateEnd) throws ParseException {
		dupeCheck(this.dateEnd);
		this.dateEnd = dateEnd;
	}

	public ArrayList<OFXStatementTrans> getTransactions() {
		return transactions;
	}
	
	public OFXStatementTrans getCurrentTransaction() {
		if (transactions.size() == 0){
			OFXStatementTrans response = new OFXStatementTrans();
			transactions.add(response);
			return response;
		}
		return transactions.get(transactions.size()-1);
	}
	
	public void addTransaction(OFXStatementTrans transaction){
		transactions.add(transaction);
	}

	@Override
	protected void exportContent(BufferedWriter bw) throws IOException {
		writeLine(bw, "<DTSTART>" + DATE_FORMAT.format(dateStart));
		writeLine(bw, "<DTEND>" + DATE_FORMAT.format(dateEnd));
		for (OFXStatementTrans trans : transactions) {
			trans.export(bw);
		}
	}
	
}
