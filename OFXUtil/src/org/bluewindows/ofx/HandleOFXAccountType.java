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

public class HandleOFXAccountType extends AbstractTagHandler {

	//Set this class up as a Singleton
	public final static HandleOFXAccountType INSTANCE = new HandleOFXAccountType();
	protected HandleOFXAccountType() {}

	@Override
	public HandlerResult handle(OFXParser parser, AbstractAggregate aggregate, Tag tag ) throws ParseException {
		String contentString = getTagContent(parser, tag).trim();
		OFXAccountType contentObject = convertContentToObject(contentString, tag, parser);
		putContentObjectIntoTargetObject(aggregate, tag, contentObject);
		return new HandlerResult();
	}

	@Override
	public OFXAccountType convertContentToObject(String contentString, Tag tag, OFXParser parser) throws ParseException {
		OFXAccountType contentObject;
		try {
			contentObject = OFXAccountType.valueOf(contentString);
		} catch (IllegalArgumentException e) {
			throw new ParseException("Invalid Account Type: [" + contentString + "] found in " + tag.getName() + " tag" + parser.getContentPosition(),0);
		}
		return contentObject;
	}
}
