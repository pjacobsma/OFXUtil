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


public class OFXCallResult {
	
	private OFXContext context;
	private boolean callOK = true;
	private Exception exception;

	public boolean isCallOK() {
		return callOK;
	}

	public boolean isCallBad() {
		return ! callOK;
	}
	
	public void setCallOK(boolean ok) {
		this.callOK = ok;
	}
	
	public void setCallBad(Exception exception) {
		this.callOK = false;
		this.exception = exception;
	}
	
	public Exception getException() {
		return exception;
	}
	
	public OFXContext getContext() {
		return context;
	}

	public void setContext(OFXContext context) {
		this.context = context;
	}

}
