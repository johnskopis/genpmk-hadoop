package com.skopis.john.cascading;

import java.util.regex.Pattern;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Filter;
import cascading.operation.FilterCall;
import cascading.tuple.TupleEntry;

public class WPAFilter extends BaseOperation implements Filter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9034421279291882168L;
	private static final Pattern VALID_PASSWORD = Pattern
			.compile("[\\w$@\\^`,|%;\\.~\\(\\)/\\{\\}:\\[\\]=\\-\\+#!]+");

	public WPAFilter() {
		// expects 2 arguments, fail otherwise
		super(1);
	}

	@Override
	public boolean isRemove(FlowProcess flowProcess, FilterCall call) {
		// get the arguments TupleEntry
		TupleEntry arguments = call.getArguments();
		String password = arguments.getString(0);
		int len = password.length();

		// validate the password using the regex
		if (!VALID_PASSWORD.matcher(password).matches()) {
			return true;
		}

		return len < 8 || len > 63;
	}

}
