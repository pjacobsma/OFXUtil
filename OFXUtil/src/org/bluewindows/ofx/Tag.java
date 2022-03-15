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


public class Tag {

	private String name;
	private AbstractTagHandler handler;
	private String targetObjectMethod;
	private Class<? extends Object> objectClass;

	public Tag (String tagString, String contentObjectName, String targetObjectMethod)  {

		@SuppressWarnings("unused")
		String handlerName = "";
		
		this.name = tagString;
		findObjectClass(tagString, contentObjectName);
		findHandler(tagString, contentObjectName);
		this.targetObjectMethod = targetObjectMethod;
	}


	private void findObjectClass(String tagString, String contentObjectName) {
		try {
			objectClass = Class.forName(Constants.CLASS_NAME_PREFIX + contentObjectName);
		} catch (ClassNotFoundException e1) {
			try {
				objectClass = Class.forName("java.lang." + contentObjectName);
			} catch (ClassNotFoundException e2) {
				try {
					objectClass = Class.forName("java.util." + contentObjectName);
				} catch (ClassNotFoundException e3) {
					try {
						objectClass = Class.forName("java.time." + contentObjectName);
					} catch (ClassNotFoundException e4) {
						try {
							objectClass = Class.forName("java.math." + contentObjectName);
						} catch (ClassNotFoundException e5) {
							throw new IllegalArgumentException("Invalid object class " + contentObjectName +
								" specified in tag map for " + tagString + " tag.");
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void findHandler(String tagString, String contentObjectName) {
		String handlerName;
		if (objectClass.getSuperclass().getName() == Constants.CLASS_NAME_PREFIX + "AbstractAggregate"){ //This is an aggregate
			if (tagString.charAt(1) == '/'){
				handlerName = Constants.CLASS_NAME_PREFIX + "HandleAggClose";
			}else{
				handlerName = Constants.CLASS_NAME_PREFIX + "HandleAggOpen";
			}
		}else{
			handlerName = Constants.CLASS_NAME_PREFIX + "Handle" + contentObjectName;
		}
		try {
			Class<? extends AbstractTagHandler> handlerClass = (Class<? extends AbstractTagHandler>) Class.forName(handlerName);
			handler = (AbstractTagHandler) handlerClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("No handler found for " + contentObjectName +
					" specified in tag map for " + tagString + " tag.");
		}
	}

	public String getName() {
		return name;
	}

	public AbstractTagHandler getHandler() {
		return handler;
	}

	public String getTargetObjectMethod() {
		return targetObjectMethod;
	}

	public Class<?> getObjectClass() {
		return objectClass;
	}
}
