package values;

import jkind.lustre.ArrayType;
import jkind.lustre.EnumType;
import jkind.lustre.NamedType;
import jkind.lustre.RecordType;
import jkind.lustre.SubrangeIntType;
import jkind.lustre.TupleType;
import jkind.lustre.Type;
import jkind.lustre.values.EnumValue;
import jkind.lustre.values.IntegerValue;
import jkind.lustre.values.Value;
import jkind.lustre.visitors.TypeVisitor;

/**
 * Convert a value from Value to String. This class assumes that ArrayType,
 * RecordType,and TupleType have been flattened. Also Convert EnumType values
 * from integer back to EnumValue. Assuming type has been resolved.
 */
public final class ValueToString implements TypeVisitor<String> {
	public static String get(Value value, Type type) {
		ValueToString visitor = new ValueToString(value);
		String valueStr = type.accept(visitor);
		if (valueStr == null) {
			throw new IllegalArgumentException("Null value for value " + value
					+ " and type " + type);
		}
		return valueStr;
	}

	private final Value value;

	private ValueToString(Value value) {
		this.value = value;
	}

	@Override
	public String visit(ArrayType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(EnumType type) {
		// If this is already an EnumValue
		if (value instanceof EnumValue) {
			return value.toString();
		}
		// If this is an integer
		else if (value instanceof IntegerValue) {
			IntegerValue iv = (IntegerValue) value;
			return type.values.get(iv.value.intValue());
		}
		// Others should be error
		else {
			throw new IllegalArgumentException("Invalid EnumType value: "
					+ value);
		}
	}

	@Override
	public String visit(NamedType type) {
		return value.toString();
	}

	@Override
	public String visit(RecordType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(TupleType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(SubrangeIntType type) {
		return value.toString();
	}

}
