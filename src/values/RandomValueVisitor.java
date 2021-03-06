package values;

import java.math.BigDecimal;
import java.math.BigInteger;

import jkind.lustre.EnumType;
import jkind.lustre.NamedType;
import jkind.lustre.SubrangeIntType;
import jkind.lustre.Type;
import jkind.lustre.values.BooleanValue;
import jkind.lustre.values.IntegerValue;
import jkind.lustre.values.RealValue;
import jkind.lustre.values.Value;
import jkind.util.BigFraction;

/**
 * Generate a random value for a type. Random values are restricted to the range
 * 0 - 255. Used to fill in null values for test suites. Assuming Lustre is in
 * simple format, EnumType is treated as integers.
 */
public final class RandomValueVisitor extends ValueVisitor {
	public static Value get(Type type) {
		RandomValueVisitor visitor = new RandomValueVisitor();
		return type.accept(visitor);
	}

	@Override
	public Value visit(EnumType type) {
		int randomNum = this.randomInt(0, type.values.size() - 1);
		return new IntegerValue(new BigInteger("" + randomNum));
	}

	@Override
	public Value visit(NamedType type) {
		int randomNum = this.randomInt(0, 255);

		if (type.equals(NamedType.BOOL)) {
			if (randomNum < 128) {
				return BooleanValue.FALSE;
			} else {
				return BooleanValue.TRUE;
			}
		} else if (type.equals(NamedType.INT)) {
			return new IntegerValue(new BigInteger("" + randomNum));
		} else if (type.equals(NamedType.REAL)) {
			String randReal = String.format("%.8f", (double) randomNum);
			return new RealValue(BigFraction.valueOf(new BigDecimal(randReal)));
		} else {
			throw new IllegalArgumentException("Unknown type: " + type + " "
					+ type.getClass());
		}
	}

	@Override
	public Value visit(SubrangeIntType type) {
		String randInteger = ""
				+ randomInt(type.low.intValue(), type.high.intValue());
		return new IntegerValue(new BigInteger(randInteger));
	}

	// Generate a random integer between UPPER and LOWER (inclusive)
	private int randomInt(int lower, int upper) {
		return (int) ((upper - lower + 1) * Math.random()) + lower;
	}
}
