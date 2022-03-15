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


public class OFXSignOnMsgResponse extends AbstractAggregate {
	//OFX SIGNONMSGSRSV1 object
	
	OFXSignOnResponse signOnResponse = new OFXSignOnResponse();

	public OFXSignOnResponse getSignOnResponse() {
		return signOnResponse;
	}

	public void setSignOnResponse(OFXSignOnResponse signOnResponse) throws ParseException {
		dupeCheck(this.signOnResponse); 
		this.signOnResponse = signOnResponse;
	}

	@Override
	protected void exportContent(BufferedWriter bw) throws IOException {
		signOnResponse.export(bw);
	}

}
