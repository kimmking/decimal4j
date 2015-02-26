/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 decimal4j (tools4j), Marco Terzer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.decimal4j.scale;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.decimal4j.api.Decimal;
import org.decimal4j.api.DecimalArithmetic;
import org.decimal4j.truncate.OverflowMode;
import org.decimal4j.truncate.TruncationPolicy;

/**
 * <tt>ScaleMetrics</tt> defines various metrics associated with the scale of a
 * {@link Decimal} number. It is mainly used internally from code implementing
 * the arithmetic operations of a {@code Decimal}.
 * <p>
 * The {@link #getScale() scale} determines the number of fraction digits of the
 * {@code Decimal}. The {@link #getScaleFactor() scale factor} is the
 * multiplier/divisor for conversions between the {@code Decimal} value and the
 * unscaled {@code long} value underlying every {@code Decimal}.
 * <p>
 * Operations such as {@link #multiplyByScaleFactor(long)
 * multiplyByScaleFactor(..)} are defined here as separate methods to allow for
 * compiler optimizations. Multiplications and divisions are for instance
 * translated into shifts and adds by the compiler instead of the more expensive
 * multiplication and division operations with non-constant long values.
 * <p>
 * {@code ScaleMetrics} also provides access to {@link DecimalArithmetic}
 * instances for different rounding modes and overflow policies.
 * {@code DecimalArithmetic} objects can be used to deal with decimal numbers
 * <i>natively</i>; in native mode {@code Decimal} numbers are passed to the
 * arithmetic class in their native form as unscaled {@code long} values.
 */
public interface ScaleMetrics {
	/**
	 * Returns the scale, the number of fraction digits to the right of the
	 * decimal point of a {@link Decimal} value.
	 * 
	 * @return the scale also known as number of fraction digits
	 */
	int getScale();

	/**
	 * Returns the scale factor, which is 10<sup>f</sup> where {@code f} stands
	 * for the {@link #getScale() scale}.
	 * 
	 * @return the scale factor
	 */
	long getScaleFactor();

	/**
	 * Returns the {@link #getScaleFactor() scale factor} as a
	 * {@link BigInteger} value.
	 * 
	 * @return the scale factor as big integer
	 */
	BigInteger getScaleFactorAsBigInteger();

	/**
	 * Returns the {@link #getScaleFactor() scale factor} as a
	 * {@link BigDecimal} value.
	 * 
	 * @return the scale factor as big decimal
	 */
	BigDecimal getScaleFactorAsBigDecimal();

	/**
	 * Returns the number of leading zeros of the scale factor
	 * 
	 * @return {@link Long#numberOfLeadingZeros(long)} applied to the scale
	 *         factor
	 */
	int getScaleFactorNumberOfLeadingZeros();

	/**
	 * Returns the largest integer value that can be represented using this
	 * scale.
	 * 
	 * @return {@code Long.MAX_VALUE / scaleFactor}
	 */
	long getMaxIntegerValue();

	/**
	 * Returns the smallest integer value that can be represented using this
	 * scale.
	 * 
	 * @return {@code Long.MIN_VALUE / scaleFactor}
	 */
	long getMinIntegerValue();

	/**
	 * Returns {@code factor*scaleFactor}.
	 * 
	 * @param factor
	 *            the factor
	 * @return {@code factor*scaleFactor}
	 */
	long multiplyByScaleFactor(long factor);

	/**
	 * Returns {@code factor*scaleFactor}, checking for lost information. If the
	 * result is out of the range of the {@code long} type, then an
	 * {@code ArithmeticException} is thrown.
	 * 
	 * @param factor
	 *            the factor
	 * @return {@code factor*scaleFactor}
	 * @throws ArithmeticException
	 *             if an overflow occurs
	 */
	long multiplyByScaleFactorExact(long factor);

	/**
	 * Returns {@code factor*low32(scaleFactor)} where low32 refers to the low
	 * 32 bits of the factor.
	 * 
	 * @param factor
	 *            the factor
	 * @return {@code factor*low32(scaleFactor)}
	 */
	long mulloByScaleFactor(int factor);

	/**
	 * Returns {@code factor*high32(scaleFactor)} where high32 refers to the
	 * high 32 bits of the factor.
	 * 
	 * @param factor
	 *            the factor
	 * @return {@code factor*high32(scaleFactor)}
	 */
	long mulhiByScaleFactor(int factor);

	/**
	 * Returns {@code dividend/scaleFactor}.
	 * 
	 * @param dividend
	 *            the dividend
	 * @return {@code dividend/scaleFactor}
	 */
	long divideByScaleFactor(long dividend);

	/**
	 * Returns {@code unsignedDividend/scaleFactor} using unsigned division.
	 * 
	 * @param unsignedDividend
	 *            the unsigned dividend
	 * @return {@code unsignedDividend/scaleFactor}
	 */
	long divideUnsignedByScaleFactor(long unsignedDividend);

	/**
	 * Returns {@code dividend % scaleFactor} also known as reminder.
	 * 
	 * @param dividend
	 *            the dividend
	 * @return {@code dividend % scaleFactor}
	 */
	long moduloByScaleFactor(long dividend);

	/**
	 * Returns the default arithmetic for this scale performing unchecked
	 * operations with rounding mode {@link RoundingMode#HALF_UP HALF_UP}.
	 * 
	 * @return default arithmetic for this scale rounding HALF_UP without
	 *         overflow checks
	 */
	DecimalArithmetic getDefaultArithmetic();

	/**
	 * Returns the truncating arithmetic for this scale and with the specified
	 * {@code overflowMode} that performs all operations without rounding.
	 * 
	 * @param overflowMode
	 *            the overflow mode used by the returned arithmetic
	 * @return truncating arithmetic for this scale
	 * @see RoundingMode#DOWN
	 */
	DecimalArithmetic getTruncatingArithmetic(OverflowMode overflowMode);

	/**
	 * Returns the arithmetic for this scale that performs all operations with
	 * the specified {@code roundingMode} and without overflow checks.
	 *
	 * @param roundingMode
	 *            the rounding mode used by the returned arithmetic
	 * @return arithmetic for this scale with specified rounding mode and
	 *         without overflow checks
	 */
	DecimalArithmetic getArithmetic(RoundingMode roundingMode);

	/**
	 * Returns the arithmetic for this scale that performs all operations with
	 * the specified {@code truncationPolicy}.
	 *
	 * @param truncationPolicy
	 *            the truncation policy used by the returned arithmetic
	 * @return arithmetic for this scale with specified truncation policy
	 */
	DecimalArithmetic getArithmetic(TruncationPolicy truncationPolicy);
}