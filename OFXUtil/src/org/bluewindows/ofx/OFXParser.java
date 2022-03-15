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
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;

public class OFXParser {
	
	private InputStream inStream;
	public static final char SPACE = (char)32; // Char = ' '
	private static final char COLON = (char)58; // Char = ':'
	private static final char START_TAG = (char)60; // Char = '<'
	private static final char END_TAG = (char)62; // Char = '>'
	public static final char CR = (char)13; //Carriage Return
	public static final char EOR = (char)10; //Line Feed - End of Record
	private static final char EOF = (char)-1;
	private char curChar;
	private int recordNumber = 1;
	private int bytePosition;
	private int tagRecord = 1;
	private int tagStart;
	private int contentRecord = 1;
	private int contentStart;
	private StringWriter stringWriter = new StringWriter();
	private boolean eof = false;


	public OFXParser(InputStream inStream){
		if (inStream == null){
			throw new IllegalArgumentException("InputStream is null.");
		}
		this.inStream = inStream;
	}
	
	public String getHeaderTag() throws IOException, ParseException {
		stringWriter.getBuffer().setLength(0);
		curChar = getNonSpaceChar(); //Bypass any empty records
		if (curChar == START_TAG) return "";
		tagRecord = recordNumber;
		tagStart = bytePosition;
		while ((! eof) && (curChar != EOR) && curChar != COLON){
			stringWriter.append(curChar);
			curChar = getHeaderChar();
		}
		if ((eof || curChar == EOR) && stringWriter.getBuffer().length() > 0){
			throw new ParseException("Premature end of header at record " + 
					tagRecord + ", column " + stringWriter.getBuffer().length() + ".", 0);
		}
		return stringWriter.toString();
	}

	public String getHeaderContent() throws IOException, ParseException {

		stringWriter.getBuffer().setLength(0);
		curChar = getHeaderChar();
		if ((! eof)) {
			contentRecord = recordNumber;
			contentStart = bytePosition;
		}
		while ((! eof) && (curChar != EOR)){
			stringWriter.append((char)curChar);
			curChar = getHeaderChar();
		}
		if (curChar == EOR) {
			return stringWriter.toString();
		}
		throw new ParseException("Premature end of header content at record " + recordNumber + ", column " +
					stringWriter.getBuffer().length() + ".", 0);
	}

	public String getTag() throws IOException, ParseException {
		
		stringWriter.getBuffer().setLength(0);
		while ((! eof) && curChar != START_TAG){
			curChar = getChar();
		}
		if ((! eof)) {
			tagRecord = recordNumber;
			tagStart = bytePosition;
		}
		while ((! eof) && curChar != END_TAG){
			stringWriter.append(curChar);
			curChar = getChar();
		}
		if (eof){
			throw new ParseException("Premature EOF reached processing " + stringWriter.toString() + " tag" + getTagPosition(), 0);
		}
		stringWriter.append(END_TAG);
		return stringWriter.toString();
	}
	
	public String getTag(ArrayList<String> validTagStrings) throws IOException, ParseException {
		//This method ignores tags that are not defined in the tag map
		
		boolean isNotValidTagString = true;
		String tagString = null;
		while (isNotValidTagString){
			tagString = getTag();
			if (validTagStrings.contains(tagString)){ 
				isNotValidTagString = false;
			}
		}
		return tagString;
	}
	
	public String getTagContent() throws IOException, ParseException {

		stringWriter.getBuffer().setLength(0);
		curChar = getChar();
		if ((! eof)) {
			contentRecord = recordNumber;
			contentStart = bytePosition;
		}
		while ((! eof)  && curChar != START_TAG){
			stringWriter.append(curChar);
			curChar = getChar();
		}
		if (eof){
			throw new ParseException("OFX file does not end with closing tag", 0);
		}
		return stringWriter.toString();
	}
	
	private char getChar() throws IOException { //Bypasses CR and EOR.  Returns all other including EOF.
		
		curChar = (char)inStream.read();
		while (curChar == CR || curChar == EOR){
			if (curChar == EOR) {
				recordNumber++;
				bytePosition = 0;
			}
			curChar = (char)inStream.read();
		}
		if (curChar == EOF){
			eof = true;
		}else{
			bytePosition++;
		}
		return curChar;
	}
	
	private char getNonSpaceChar() throws IOException {
		do {
			curChar = getChar();
		}while (curChar == SPACE);
		return curChar;
	}

	private char getHeaderChar() throws IOException { //Bypasses CR and spaces.  Returns all other including EOR and EOF.
		
		curChar = (char)inStream.read();
		while (curChar == CR || curChar == SPACE){curChar = (char)inStream.read();}
		if (curChar == EOR) {
			recordNumber++;
			bytePosition = 0;
		}
		if (curChar == EOF){
			eof = true;
		}else if (curChar != EOR){
			bytePosition++;
		}
		return curChar;
	}
	
	public boolean isNotEOF() {
		return (! eof);
	}
	
	public String getTagPosition() {
		return " at record " + tagRecord + ", column " + tagStart + ".";
	}

	public String getContentPosition() {
		return " at record " + contentRecord + ", column " + contentStart + ".";
	}

}
