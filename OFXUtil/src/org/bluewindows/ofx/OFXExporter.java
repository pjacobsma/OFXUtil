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
package org.bluewindows.ofx;

import java.io.BufferedWriter;
import java.io.IOException;

public class OFXExporter {
	
	public OFXCallResult exportOFX(OFXContext context, BufferedWriter bw) {
		OFXCallResult result = new OFXCallResult();
		try {
			writeHeaders(bw);
			context.export(bw);
		} catch (IOException e) {
			result.setCallBad(e);
		}
		return result;
	}

	private void writeHeaders(BufferedWriter bw) throws IOException {
		writeLine(bw, "OFXHEADER:100");
		writeLine(bw, "DATA:OFXSGML");
		writeLine(bw, "VERSION:102");
		writeLine(bw, "SECURITY:NONE");
		writeLine(bw, "ENCODING:USASCII");
		writeLine(bw, "CHARSET:1252");
		writeLine(bw, "COMPRESSION:NONE");
		writeLine(bw, "OLDFILEUID:NONE");
		writeLine(bw, "NEWFILEUID:NONE");
	}
	
	private void writeLine(BufferedWriter bw, String content) throws IOException {
		bw.write(content);
		bw.newLine();
	}

}
