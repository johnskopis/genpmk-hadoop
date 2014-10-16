package com.skopis.john.cascading;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Function;
import cascading.operation.FunctionCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

import com.skopis.john.crypto.WPA2Hash;

public class Hasher extends BaseOperation implements Function {
	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(Hasher.class);

	private WPA2Hash hasher;

	public Hasher(Fields fieldDeclaration, WPA2Hash hasher) {
		super(1, fieldDeclaration);
		this.hasher = hasher;
	}

	public void operate(FlowProcess flowProcess, FunctionCall functionCall) {
		TupleEntry argument = functionCall.getArguments();
		String pass = argument.getString(0);
		byte[] hash;
		try {
			hash = hasher.getHash(argument.getString(0));
		} catch (Exception e) {
			hash = null;
		}

		Tuple result = new Tuple();
		result.add(pass);
		result.add(hash);
		functionCall.getOutputCollector().add(result);
	}
}
