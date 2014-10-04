package ch.javasoft.decimal.scale;

/**
 * Scale class for decimals with 13 {@link #getScale() fraction digit} and
 * {@link #getScaleFactor() scale factor} 10,000,000,000,000.
 */
public final class Scale13f extends AbstractScale {
	public static final Scale13f INSTANCE = new Scale13f();

	@Override
	public int getScale() {
		return 13;
	}

	@Override
	public long getScaleFactor() {
		return 10000000000000L;
	}

	@Override
	public long multiplyByScaleFactor(long factor) {
		return factor * 10000000000000L;
	}

	@Override
	public long mulloByScaleFactor(int factor) {
		return (factor & LONG_MASK) * 1316134912;//(scaleFactor & LONG_MASK)
	}

	@Override
	public long mulhiByScaleFactor(int factor) {
		return (factor & LONG_MASK) * 2328;//(scaleFactor >>> 32)
	}

	@Override
	public long divideByScaleFactor(long dividend) {
		return dividend / 10000000000000L;
	}

	@Override
	public long moduloByScaleFactor(long dividend) {
		return dividend % 10000000000000L;
	}
}