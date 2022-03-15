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

import java.text.ParseException;

public class HandleOFXBalance extends AbstractTagHandler {
	//This class handles both LEDGERBAL and AVAILBAL tags, instantiating a OFXLedgerBalance
	//for either tag.  The tag mapping determines which target aggregate will be used
	//by specifying the setter (setLedgerBalance or setAvailableBalance).
		
	//Set this class up as a Singleton
	public final static HandleOFXBalance INSTANCE = new HandleOFXBalance();
	protected HandleOFXBalance() {}

	@Override
	public HandlerResult handle(OFXParser parser, AbstractAggregate aggregate, Tag tag) throws ParseException {
		OFXLedgerBalance contentObject = convertContentToObject("", tag, parser);
		putContentObjectIntoTargetObject(aggregate, tag, contentObject);
		HandlerResult result = new HandlerResult();
		result.setAggregate(contentObject);
		return result;
	}

	@Override
	public OFXLedgerBalance convertContentToObject(String contentString, Tag tag, OFXParser parser) throws ParseException {
		return new OFXLedgerBalance();
	}

}
