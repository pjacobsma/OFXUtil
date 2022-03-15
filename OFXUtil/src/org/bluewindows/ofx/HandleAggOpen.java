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

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

public class HandleAggOpen extends AbstractTagHandler {

	public final static HandleAggOpen INSTANCE = new HandleAggOpen();
	
	@Override
	public HandlerResult handle(OFXParser parser, AbstractAggregate aggregate, Tag tag )
		throws ParseException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		HandlerResult result = new HandlerResult();
		AbstractAggregate contentObject;
		contentObject = (AbstractAggregate)tag.getObjectClass().getDeclaredConstructor().newInstance();
		if (aggregate != null) putContentObjectIntoTargetObject(aggregate, tag, contentObject);
		result.setAggregate(contentObject);
		return result;
	}

	@Override //This method doesn't do anything because all this handler does is instantiate the aggregate.
	public Object convertContentToObject(String contentString, Tag tag, OFXParser parser) throws ParseException {
		return null;
	}

}
