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
import java.text.ParseException;
import java.time.LocalDateTime;

public class OFXAvailBalance extends AbstractAggregate {
	// AVAILBAL object
	
	BigDecimal amount = Constants.MISSING_AMOUNT;
	LocalDateTime dateOf = Constants.MISSING_DATE_TIME;
	OFXCurrency currency = new OFXCurrency();

	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) throws ParseException {
		dupeCheck(this.amount);
		this.amount = amount;
	}
	
	public LocalDateTime getDateOf() {
		return dateOf;
	}
	
	public void setDateOf(LocalDateTime dateOf) throws ParseException {
		dupeCheck(this.dateOf);
		this.dateOf = dateOf;
	}
	
	public OFXCurrency getCurrency() {
		return currency;
	}

	public void setCurrency(OFXCurrency currency) throws ParseException {
		dupeCheck(this.currency);
		this.currency = currency;
	}

	@Override
	protected void exportContent(BufferedWriter bw) throws IOException {
		writeLine(bw, "<BALAMT>" + MONEY_FORMAT.format(amount));
		writeLine(bw, "<DTASOF>" + DATE_TIME_FORMAT.format(dateOf));
		currency.export(bw);
	}

}
