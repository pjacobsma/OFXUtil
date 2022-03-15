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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HandleLocalDateTime extends AbstractTagHandler {

	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	
	//Set this class up as a Singleton
	public final static HandleLocalDateTime INSTANCE = new HandleLocalDateTime();
	protected HandleLocalDateTime() {}
	
	@Override
	public HandlerResult handle(OFXParser parser, AbstractAggregate aggregate, Tag tag) throws ParseException {
		String contentString = getTagContent(parser, tag).trim();
		LocalDateTime contentObject = convertContentToObject(contentString, tag, parser);
		putContentObjectIntoTargetObject(aggregate, tag, contentObject);
		return new HandlerResult();
	}

	public LocalDateTime convertContentToObject(String contentString, Tag tag, OFXParser parser) throws ParseException {
		if (contentString.length() < 14){
			throw new ParseException("Invalid date-time value: [" + contentString + "] found in " +
				tag.getName() + " tag" + parser.getContentPosition(),0);
		} else if (contentString.length() > 14){
			contentString = contentString.substring(0, 14);
		}
		return LocalDateTime.parse(contentString, DTF);
	}
}
