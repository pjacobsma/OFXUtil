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

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public abstract class AbstractTagMap {
	
	// This abstract class is needed for unit testing the tag map
	
	protected HashMap<TagMapKey, Tag> tags = new HashMap<TagMapKey, Tag>();

	protected void addTag(String tagName, String contentObjectName, String targetObjectName,  String targetObjectSetterName){
		
		if (tags.get(new TagMapKey(tagName, targetObjectName)) != null){
			throw new IllegalArgumentException("Duplicate tag definition for tag " + tagName +
					" with target object " + targetObjectName + " in tag map.");
		}
		tags.put(new TagMapKey(tagName, targetObjectName), new Tag(tagName, contentObjectName, targetObjectSetterName));
	}
	
	public Tag getTag(String tagString, String aggregateName) {
		return getTag(new TagMapKey(tagString, aggregateName));
	}
	
	public Tag getTag(TagMapKey tagMapKey){
		return tags.get(tagMapKey);
	}
	
	public HashMap<TagMapKey, Tag> getTags(){
		return tags;
	}
	
	public String getOpenTag(Object tagObject) {
		Set<Entry<TagMapKey, Tag>> tagEntries = tags.entrySet();
		for (Entry<TagMapKey, Tag> entry : tagEntries) {
			if (entry.getValue().getObjectClass().getSimpleName().equals(tagObject.getClass().getSimpleName())) {
				if (!entry.getValue().getName().contains("/")) return entry.getValue().getName();
			}
		}
		return null;
	}
	
	public String getCloseTag(Object tagObject) {
		Set<Entry<TagMapKey, Tag>> tagEntries = tags.entrySet();
		for (Entry<TagMapKey, Tag> entry : tagEntries) {
			if (entry.getValue().getObjectClass().getSimpleName().equals(tagObject.getClass().getSimpleName())) {
				if (entry.getValue().getName().contains("/")) return entry.getValue().getName();
			}
		}
		return null;
	}
}
