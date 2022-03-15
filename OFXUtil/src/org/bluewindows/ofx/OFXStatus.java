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
import java.math.BigInteger;
import java.text.ParseException;

public class OFXStatus extends AbstractAggregate {
	
	private BigInteger code = Constants.MISSING_INTEGER;
	private OFXSeverityType severity = OFXSeverityType.MISSING;
	private String message = "";

	public BigInteger getCode() {
		return code;
	}

	public void setCode(BigInteger code) throws ParseException {
		dupeCheck(this.code);
		this.code = code;
	}

	public OFXSeverityType getSeverity() {
		return severity;
	}

	public void setSeverity(OFXSeverityType severity) throws ParseException {
		dupeCheck(this.severity);
		this.severity = severity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) throws ParseException {
		dupeCheck(this.message);
		this.message = message;
	}

	@Override
	protected void exportContent(BufferedWriter bw) throws IOException {
		if (code.equals(Constants.MISSING_INTEGER)) code = BigInteger.valueOf(0);
		if (severity.equals(OFXSeverityType.MISSING)) severity = OFXSeverityType.INFO;
		writeLine(bw, "<CODE>" + code.toString());
		writeLine(bw, "<SEVERITY>" + severity.toString());
	}
}
