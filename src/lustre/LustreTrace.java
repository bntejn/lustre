package lustre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jkind.lustre.values.Value;
import jkind.results.Signal;
import jkind.util.StringNaturalOrdering;

public final class LustreTrace {
	private final Map<String, Signal<Value>> variables;
	private int length;

	public LustreTrace(int length) {
		this.variables = new HashMap<String, Signal<Value>>();
		this.length = length;
	}

	public int getLength() {
		return this.length;
	}

	public void addVariable(Signal<Value> variable) {
		this.variables.put(variable.getName(), variable);
	}

	public Signal<Value> getVariable(String name) {
		return this.variables.get(name);
	}

	public Set<String> getVariableNames() {
		return this.variables.keySet();
	}

	// This method does not convert EnumValue back from IntegerValue
	// For writing traces, use WriteTrace
	@Override
	public String toString() {
		String output = "";

		List<String> names = new ArrayList<String>();
		names.addAll(this.getVariableNames());

		Collections.sort(names, new StringNaturalOrdering());

		// Write variable names
		Iterator<String> variableIter = names.iterator();

		while (variableIter.hasNext()) {
			output += variableIter.next();
			if (variableIter.hasNext()) {
				output += ",";
			}
		}

		output += "\n";

		// Write values
		// Iterate from step 0 to (length - 1)
		for (int step = 0; step < length; step++) {
			variableIter = names.iterator();

			// Iterate all input variables
			while (variableIter.hasNext()) {
				// Value can be null
				output += this.variables.get(variableIter.next())
						.getValue(step);

				// Add comma if not ending
				if (variableIter.hasNext()) {
					output += ",";
				}
			}

			if (step < length - 1) {
				output += "\n";
			}
		}
		return output;
	}

	// This method is used to concatenate this test with other
	// The very initial step of other is ignored if this test is not empty
	public void addLustreTrace(LustreTrace other) {
		int origEnding = 0;
		int initialStep = 0;

		if (this.length == 0) {
			this.length = other.length;
		} else {
			origEnding = this.length - 1;
			initialStep = 1;
			this.length += other.length - 1;
		}

		for (String variable : this.variables.keySet()) {
			Signal<Value> signal = this.variables.get(variable);
			Signal<Value> otherSignal = other.variables.get(variable);
			for (int step = initialStep; step < other.length; step++) {
				signal.putValue(origEnding + step, otherSignal.getValue(step));
			}
		}
	}
}
