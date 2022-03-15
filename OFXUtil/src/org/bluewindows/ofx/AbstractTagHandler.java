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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;

public abstract class AbstractTagHandler {
	
	abstract protected Object convertContentToObject(String contentString, Tag tag, OFXParser parser) throws ParseException;

	abstract public HandlerResult handle(OFXParser parser, AbstractAggregate aggregate, Tag tag) 
		throws ParseException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException;
	
	public String getTagContent(OFXParser parser, Tag tag) throws ParseException {
		String contentString = "";
		try {
			contentString = parser.getTagContent();
		} catch (IOException e) {
			throw new ParseException("IO Error occurred retrieving " + tag.getName() + " tag content" + parser.getContentPosition(), 0);
		}
		if (contentString.isEmpty()){
			throw new ParseException("Missing content for " + tag.getName() + " tag" + parser.getTagPosition(), 0);
		}
		return contentString;
	}
	
	public void putContentObjectIntoTargetObject(AbstractAggregate aggregate, Tag tag, Object contentObject) {
	    Method method = null;
		try {
			Class<? extends AbstractAggregate> targetClass = aggregate.getClass();
			method = targetClass.getMethod(tag.getTargetObjectMethod(), new Class[]{tag.getObjectClass()});
		} catch (SecurityException e) {
			throw new IllegalArgumentException("Cannot invoke " + tag.getTargetObjectMethod() + " on " + aggregate.getClass().getName());
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("There is no " + tag.getTargetObjectMethod() + " method on " +
					aggregate.getClass().getSimpleName() + " with parameter of type " +	tag.getObjectClass().getSimpleName());
		}
		try {
			method.invoke(aggregate, contentObject);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Cannot invoke " + tag.getTargetObjectMethod() + " on " + aggregate.getClass().getName());
		} catch (InvocationTargetException e) {
			if(e.getTargetException().getMessage() == "Duplicate"){
				throw new IllegalArgumentException("Duplicate call to " + tag.getTargetObjectMethod() + " method on " +
						aggregate.getClass().getSimpleName()+ " indicates duplicate element tag or missing aggregate end tag.");
			}else{
				throw new IllegalArgumentException("There is no " + tag.getTargetObjectMethod() + " method on " +
						aggregate.getClass().getSimpleName()+ " with parameter of type " + tag.getObjectClass().getSimpleName());
			}
		}	
	}
}