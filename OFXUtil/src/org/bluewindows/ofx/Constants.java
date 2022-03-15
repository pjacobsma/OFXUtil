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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Constants {
	
	public final static String CLASS_NAME_PREFIX = "org.bluewindows.ofx.";
	public static final BigDecimal MISSING_AMOUNT = new BigDecimal("0.001");
	public static final BigInteger MISSING_INTEGER = new BigInteger("0");
	public static final LocalDate MISSING_DATE = LocalDate.MIN;
	public static final LocalDateTime MISSING_DATE_TIME = LocalDateTime.MIN;

}
