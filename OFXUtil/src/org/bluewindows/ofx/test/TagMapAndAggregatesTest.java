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

package org.bluewindows.ofx.test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.bluewindows.ofx.AbstractAggregate;
import org.bluewindows.ofx.AbstractTagMap;
import org.bluewindows.ofx.Constants;
import org.bluewindows.ofx.OFXAccountType;
import org.bluewindows.ofx.OFXAvailBalance;
import org.bluewindows.ofx.OFXBankAcctFrom;
import org.bluewindows.ofx.OFXBankMsgResponse;
import org.bluewindows.ofx.OFXBankStatement;
import org.bluewindows.ofx.OFXBankStatementResponse;
import org.bluewindows.ofx.OFXBankTransList;
import org.bluewindows.ofx.OFXCardAcctFrom;
import org.bluewindows.ofx.OFXCardMsgResponse;
import org.bluewindows.ofx.OFXCardStatement;
import org.bluewindows.ofx.OFXCardStatementResponse;
import org.bluewindows.ofx.OFXContext;
import org.bluewindows.ofx.OFXCorrectActionType;
import org.bluewindows.ofx.OFXCurrency;
import org.bluewindows.ofx.OFXFinancialInstitution;
import org.bluewindows.ofx.OFXLedgerBalance;
import org.bluewindows.ofx.OFXSeverityType;
import org.bluewindows.ofx.OFXSignOnMsgResponse;
import org.bluewindows.ofx.OFXSignOnResponse;
import org.bluewindows.ofx.OFXStatementTrans;
import org.bluewindows.ofx.OFXStatus;
import org.bluewindows.ofx.OFXTransType;
import org.bluewindows.ofx.Tag;
import org.bluewindows.ofx.TagMap;
import org.bluewindows.ofx.TagMapKey;

import junit.framework.TestCase;

public class TagMapAndAggregatesTest extends TestCase {
	
	//This test class verifies the tag maps, checking that all objects have the setters as defined in the map.
	//It also verifies all aggregates referenced in the tag maps.  All aggregates should instantiate the objects
	//they contain (null object pattern), and their setters should do duplicate checks to detect badly formed OFX streams.
	
	public void testOFXTagMaps() throws Exception {
		//Add a line here for each tag map version
		testMapping(TagMap.getInstance());
	}
	
	private void testMapping(AbstractTagMap tagMap) throws Exception {
		for (TagMapKey tagMapKey : tagMap.getTags().keySet()){
			if (tagMapKey.getAggregateName() != "None"){
				checkMapping(tagMap, tagMapKey);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void checkMapping(AbstractTagMap tagMap, TagMapKey tagMapKey) throws Exception {
		//This verifies that all aggregate objects have the setter methods specified in the mapping.
		Tag tag = tagMap.getTag(tagMapKey);
		if (! tag.getTargetObjectMethod().isEmpty()){
			try {
				Class<? extends AbstractAggregate> targetClass = (Class<? extends AbstractAggregate>) Class.forName(Constants.CLASS_NAME_PREFIX + tagMapKey.getAggregateName());
				@SuppressWarnings("unused")
				Method method = targetClass.getMethod(tag.getTargetObjectMethod(), new Class[]{tag.getObjectClass()});
				checkConstructor(targetClass);
				checkSetters(targetClass);
			} catch (NoSuchMethodException e) {
				fail("Error in tag map definition of " + tag.getName() + " tag. There is no " +
						tag.getTargetObjectMethod() + " method on " + tagMapKey.getAggregateName()
						+ " aggregate object with a setter for " + tag.getObjectClass().getName());
			}
		}
	}
	
	protected void checkConstructor(Class<? extends AbstractAggregate> aggregateClass) throws Exception {
		//Use reflection to test the constructor of the aggregate to verify that all elements
		//in the aggregate are initialized to missing values rather than null.
		Method[] methods = aggregateClass.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("get")){
				AbstractAggregate aggregate = (AbstractAggregate)createInstance(aggregateClass.getName());
				Object object = null;
				object = method.invoke(aggregate);
				assertFalse(method.getName() + " in " + aggregateClass.getName() +
						" returns null on instantiation.", object == null);
			}
		}
	}
	

	protected void checkSetters(Class<? extends AbstractAggregate> aggregateClass) throws Exception {
		//Use reflection to test all the setters in the aggregate to verify they check for duplicate set calls
		//A duplicate set call indicates the same element appears more than once in the aggregate
		//or that the aggregate is not properly terminated.
		AbstractAggregate aggregate = (AbstractAggregate)createInstance(aggregateClass.getName());
		Method[] methods = aggregateClass.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("set")){
				Class<?>[] parameterClasses = method.getParameterTypes();
				Class<?>parameterClass = parameterClasses[0];
				Object parameter = createInstance(parameterClass.getCanonicalName());
				try {
					method.invoke(aggregate, parameter);//First call to setter
				} catch (Exception e1) {
				}
				try {
					method.invoke(aggregate, parameter); //Second (duplicate) call to setter
					fail(method.getName() + " in " + aggregateClass.getName() + " does not do a duplicate check.");
				} catch (Exception e2) {
					assertEquals("java.text.ParseException", e2.getCause().getClass().getName());
					assertEquals("Duplicate", e2.getCause().getMessage());
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private Object createInstance(String className) throws Exception {
		Object object = null;
		if (className.contains("String")){
			object = new String("a");
		}else if (className.contains("LocalDateTime")){
			object = LocalDateTime.MAX;
		}else if (className.contains("LocalDate")){
			object = LocalDate.MAX;
		}else if (className.contains("BigDecimal")){
			object = new BigDecimal(1);
		}else if (className.contains("BigInteger")){
			object = new BigInteger("1");
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXSignOnMsgResponse")){
			object = new OFXSignOnMsgResponse();
			((OFXSignOnMsgResponse)object).getSignOnResponse().setServerDate(LocalDateTime.MAX);
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXSignOnResponse")){
			object = new OFXSignOnResponse();
			((OFXSignOnResponse)object).setServerDate((LocalDateTime.MAX));
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXBankStatementResponse")){
			object = new OFXBankStatementResponse();
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXCardStatementResponse")){
			object = new OFXCardStatementResponse();
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXBankMsgResponse")){
			object = new OFXBankMsgResponse();
			((OFXBankMsgResponse)object).addBankStatementResponse(new OFXBankStatementResponse());
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXCardMsgResponse")){
			object = new OFXCardMsgResponse();
			((OFXCardMsgResponse)object).addCardStatementResponse(new OFXCardStatementResponse());
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXBankStatement")){
			object = new OFXBankStatement();
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXCardStatement")){
			object = new OFXCardStatement();
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXStatementTrans")){
			object = new OFXStatementTrans();
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXBankTransList")){
			object = new OFXBankTransList();
			((OFXBankTransList)object).setDateStart(LocalDate.MAX);
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXBankAcctFrom")){
			object = new OFXBankAcctFrom();
			((OFXBankAcctFrom)object).setBankID("1");
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXCardAcctFrom")){
			object = new OFXCardAcctFrom();
			((OFXCardAcctFrom)object).setAcctID("1");
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXStatus")){
			object = new OFXStatus();
			((OFXStatus)object).setCode(new BigInteger("1"));
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXAccountType")){
			object = OFXAccountType.CHECKING;
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXSeverityType")){
			object = OFXSeverityType.WARN;
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXTransType")){
			object = OFXTransType.ATM;
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXLedgerBalance")){
			object = new OFXLedgerBalance();
			((OFXLedgerBalance)object).setAmount(new BigDecimal(1.00));
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXAvailBalance")){
			object = new OFXAvailBalance();
			((OFXAvailBalance)object).setAmount(new BigDecimal(1.00));
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXCorrectActionType")){
			object = OFXCorrectActionType.DELETE;
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXFinancialInstitution")){
			object = new OFXFinancialInstitution();
			((OFXFinancialInstitution)object).setFid("1");
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXContext")){
			object = new OFXContext();
		}else if (className.equals(Constants.CLASS_NAME_PREFIX + "OFXCurrency")){
			object = new OFXCurrency();
			((OFXCurrency)object).setCurrRate(new BigDecimal(1.00));
		}else if (className.contains("HashMap")){
			object = new HashMap<String, String>();
			((HashMap<String, String>)object).put("a", "b");
		}else{
			throw new Exception("Unrecognized class: " + className +
					" in TestTagMapsAndAggregates createInstance method.");
		}
		return object;
	}

}
