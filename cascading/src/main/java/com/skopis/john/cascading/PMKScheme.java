package com.skopis.john.cascading;

import java.io.IOException;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.RecordReader;

import cascading.flow.FlowProcess;
import cascading.scheme.Scheme;
import cascading.scheme.SinkCall;
import cascading.scheme.SourceCall;
import cascading.tap.Tap;
import cascading.tap.TapException;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;

public class PMKScheme
		extends
		Scheme<JobConf, RecordReader<Tuple, Tuple>, OutputCollector<Tuple, Tuple>, Object[], Void> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5932578740593834692L;
	private String ssid;
	private final String SSID = "ssid";

	public PMKScheme(Fields outF, String ssid) {
		super(outF, outF);
		this.ssid = ssid;
	}

	@Override
	public void sink(FlowProcess<JobConf> flowProcess,
			SinkCall<Void, OutputCollector<Tuple, Tuple>> sinkCall)
			throws IOException {
		sinkCall.getOutput().collect(Tuple.NULL,
				sinkCall.getOutgoingEntry().getTuple());
	}

	@Override
	public void sinkConfInit(
			FlowProcess<JobConf> flowProcess,
			Tap<JobConf, RecordReader<Tuple, Tuple>, OutputCollector<Tuple, Tuple>> tap,
			JobConf conf) {
		conf.setOutputKeyClass(Tuple.class);
		conf.setOutputValueClass(Tuple.class);
		conf.setOutputFormat(PMKOutputFormat.class);

		conf.set(PMKOutputFormat.SSID_KEY, ssid);

	}

	@Override
	public boolean source(FlowProcess<JobConf> arg0,
			SourceCall<Object[], RecordReader<Tuple, Tuple>> arg1)
			throws IOException {
		throw new TapException("not a source");
	}

	@Override
	public void sourceConfInit(
			FlowProcess<JobConf> arg0,
			Tap<JobConf, RecordReader<Tuple, Tuple>, OutputCollector<Tuple, Tuple>> arg1,
			JobConf arg2) {
		throw new TapException("not a source");

	}

	@Override
	public boolean isSink() {
		return true;
	}

	@Override
	public boolean isSource() {
		return false;
	}

}
