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
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

public class OFXImporter { 
	
	private HashMap<String, String> headerMap = new HashMap<String, String>();
	private OFXParser streamParser;
	private AbstractTagMap tagMap = TagMap.getInstance();
	private ArrayList<String> validTagStrings = new ArrayList<String>();
	//The OFX stream consists of aggregates which contain elements.
	//This stack is used to store the aggregates as they are being parsed.
	private Stack<AbstractAggregate> aggregateStack = new Stack<AbstractAggregate>();
	private String curAggregateName = "";

	public OFXCallResult importOFX(InputStream inStream)  {
		
		streamParser = new OFXParser(inStream);
		OFXCallResult result = new OFXCallResult();
		try {
			result = processHeaders(streamParser);
		} catch (ParseException e) {
			result.setCallBad(e);
		}
		if (result.isCallBad()) return result;
		initializeForTagProcessing();
		try {
			result = processTags(streamParser);
		} catch (ParseException e) {
			result.setCallBad(e);
		}
		if (result.isCallBad()) return result;
		while (! (aggregateStack.peek() instanceof OFXContext)){
			aggregateStack.pop();
		}
		OFXContext ofxContext = (OFXContext)aggregateStack.pop();
		try {
			ofxContext.setHeaderMap(headerMap);
		} catch (ParseException e) {
			result.setCallBad(e);
			return result;
		}
		result.setContext(ofxContext);
		return result;
	}
	
	private OFXCallResult processHeaders(OFXParser streamParser) throws ParseException {
		OFXCallResult result = new OFXCallResult();
		while (result.isCallOK()){
			result = getHeader();
		}
		if (result.getException() != null) return result;
		if (headerMap.entrySet().isEmpty()) {
			result.setCallBad(new ParseException("Invalid OFX file.  No headers found.", 0));
			return result;
		}
		String ofxHeaderVersion = headerMap.get("OFXHEADER");
		if (ofxHeaderVersion == null || ofxHeaderVersion.isEmpty()) {
			result.setCallBad(new ParseException("Invalid OFX file.  OFXHEADER not found.", 0));
			return result;
		}
		String ofxVersion = headerMap.get("VERSION");
		if (ofxVersion == null || ofxVersion.isEmpty()) {
			result.setCallBad(new ParseException("Invalid OFX file.  VERSION header not found.", 0));
			return result;
		}
		result.setCallOK(true);
		return result;
	}


	private OFXCallResult getHeader() throws ParseException {
		OFXCallResult result = new OFXCallResult();
		try {
			String headerTag = streamParser.getHeaderTag();
			if (headerTag.isEmpty()){
				result.setCallBad(null);
				return result;
			}else{
				String headerContent = streamParser.getHeaderContent();
				headerMap.put(headerTag, headerContent);
			}
		} catch (IOException e) {
			result.setCallBad(e);
		} catch (ParseException e) {
			result.setCallBad(e);
		}
		return result;
	}
	
	private void initializeForTagProcessing() {
		//Collect a list of all the tag strings in this tag map.
		//This is used by the stream parser to decide whether or not to ignore a tag.
		Set<TagMapKey> tagMapKeys = tagMap.getTags().keySet();
		for (TagMapKey tagMapKey : tagMapKeys) {
			validTagStrings.add(tagMapKey.getTag());
		}
	}

	private OFXCallResult processTags(OFXParser parser) throws ParseException {
		
		String tagString = "";
		OFXCallResult result = new OFXCallResult();
		try {
			tagString = parser.getTag(validTagStrings);
			while (parser.isNotEOF() && (! tagString.equals("</OFX>"))) {
				Tag tag = tagMap.getTag(tagString, curAggregateName);
				if (tag == null){
					throw new ParseException(tagString + " found in invalid context: " + getAggregateContext(curAggregateName) + parser.getTagPosition(), 0);
				}else{
					AbstractAggregate aggregate;
					if (aggregateStack.isEmpty()){
						aggregate = null;
					}else{
						aggregate = aggregateStack.peek();
					}
					HandlerResult tagResult;
					try {
						tagResult = tag.getHandler().handle(parser, aggregate, tag);
					} catch (InstantiationException e) {
						throw new ParseException("Tag handler for " + tag.getName() + " tag could not instantiate " +
								tag.getObjectClass().getCanonicalName() + " object.", 0);
					} catch (IllegalAccessException e) {
						throw new ParseException("Tag handler for " + tag.getName() + " tag could not get access to " +
								tag.getTargetObjectMethod() + " method.", 0);
					} catch (IllegalArgumentException e) {
						throw new ParseException("Tag handler for " + tag.getName() + " tag could not instantiate " +
								tag.getObjectClass().getCanonicalName() + " object.", 0);
					} catch (InvocationTargetException e) {
						throw new ParseException("Tag handler for " + tag.getName() + " tag could not instantiate " +
								tag.getObjectClass().getCanonicalName() + " object.", 0);
					} catch (NoSuchMethodException e) {
						throw new ParseException("Tag handler for " + tag.getName() + " tag could not instantiate " +
								tag.getObjectClass().getCanonicalName() + " object.", 0);
					} catch (SecurityException e) {
						throw new ParseException("Tag handler for " + tag.getName() + " tag could not instantiate " +
								tag.getObjectClass().getCanonicalName() + " object.", 0);
					}
					if (aggregate != null && aggregate.isComplete) stackPop();
					if (tagResult.isNewAggregate()) stackPush(tagResult.getAggregate());
				}
				tagString = parser.getTag(validTagStrings);
			}
		} catch (IOException e) {
			result.setCallBad(e);
		} catch (ParseException e) {
			result.setCallBad(e);
		}
		if (aggregateStack.isEmpty()){
			result.setCallBad(new ParseException("Invalid OFX file.  No valid OFX tags found.", 0));
		}
		return result;
	}
	
	private void stackPush(AbstractAggregate aggregate){
		aggregateStack.push(aggregate);
		curAggregateName = getClassName(aggregate.getClass());
	}

	private void stackPop(){
		aggregateStack.pop();
		if (aggregateStack.isEmpty()){
			curAggregateName = null;
		}else{
			curAggregateName = getClassName(aggregateStack.peek().getClass());
		}
	}

	private String getClassName(Class<? extends Object> c) {
		String classNameIncludingPackage = c.getName();
		int firstCharOfNameExcludingPackage;
		
		firstCharOfNameExcludingPackage = classNameIncludingPackage.lastIndexOf ('.') + 1;
		return classNameIncludingPackage.substring(firstCharOfNameExcludingPackage);
	}
	
	private String getAggregateContext(String aggregateName) {
		if (aggregateName.equals("OFXAccountType")) return "<ACCTTYPE>";
		if (aggregateName.equals("OFXLedgerBalance")) return "<AVAILBAL>";
		if (aggregateName.equals("OFXBankAcctFrom")) return "<BANKACCTFROM>";
		if (aggregateName.equals("OFXBankMsgResponse")) return "<BANKMSGSRSV1>";
		if (aggregateName.equals("OFXBankStatement")) return "<STMTRS>";
		if (aggregateName.equals("OFXBankStatementResponse")) return "<STMTTRNRS>";
		if (aggregateName.equals("OFXStatementTrans")) return "<STMTTRN>";
		if (aggregateName.equals("OFXBankTransList")) return "<BANKTRANLIST>";
		if (aggregateName.equals("OFXContext")) return "<OFX>";
		if (aggregateName.equals("OFXCorrectActionType")) return "<CORRECTACTION>";
		if (aggregateName.equals("OFXCurrency")) return "<CURRENCY>";
		if (aggregateName.equals("OFXFinancialInstitution")) return "<FI>";
		if (aggregateName.equals("OFXSeverityType")) return "<SEVERITY>";
		if (aggregateName.equals("OFXSignOnMsgResponse")) return "<SIGNONMSGSRSV1>";
		if (aggregateName.equals("OFXSignOnResponse")) return "<SONRS>";
		if (aggregateName.equals("OFXStatus")) return "<STATUS>";
		if (aggregateName.equals("OFXTransType")) return "<TRNTYPE>";
		return aggregateName;
	}

}
