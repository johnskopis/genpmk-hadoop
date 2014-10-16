package com.skopis.john.cascading;

import static net.sourceforge.argparse4j.impl.Arguments.storeTrue;

import java.util.Properties;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import cascading.flow.FlowDef;
import cascading.flow.hadoop.HadoopFlowConnector;
import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.pipe.assembly.Unique;
import cascading.property.AppProps;
import cascading.scheme.Scheme;
import cascading.scheme.hadoop.TextLine;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.hadoop.GlobHfs;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

import com.skopis.john.crypto.WPA2Hash;

public class App {
	// private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		ArgumentParser parser = ArgumentParsers
				.newArgumentParser("CascadingApp").defaultHelp(true)
				.description("Generate cowpatty files");
		parser.addArgument("-i", "--input").type(String.class);
		parser.addArgument("-o", "--output").type(String.class);
		parser.addArgument("-s", "--ssid").type(String.class);
		parser.addArgument("-p", "--preprocess").action(storeTrue());
		Namespace ns = null;
		try {
			ns = parser.parseArgs(args);
		} catch (ArgumentParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Properties properties = new Properties();
		AppProps.setApplicationJarClass(properties, App.class);
		// TupleSerialization.addSerialization(properties,
		// BytesSerialization.class.getName());
		HadoopFlowConnector flowConnector = new HadoopFlowConnector(properties);

		Fields inF = new Fields("pass");
		Fields outF = new Fields("pass", "hash");

		Scheme sourceScheme = new TextLine(inF);
		Tap inTap = new GlobHfs(sourceScheme, ns.getString("input"));

		Scheme sinkScheme = new PMKScheme(outF, ns.getString("ssid"));
		Tap outTap = new Hfs(sinkScheme, ns.getString("output"),
				SinkMode.REPLACE);

		Pipe assembly = new Pipe("password");

		if (ns.getBoolean("preprocess")) {
			assembly = new Each(assembly, new WPAFilter());
			assembly = new Unique(assembly, inF);
		}

		// specify a pipe to connect the taps
		assembly = new Each(assembly, inF, new Hasher(outF, new WPA2Hash(
				ns.getString("ssid"))));

		// connect the taps, pipes, etc., into a flow
		FlowDef flowDef = FlowDef.flowDef().addSource(assembly, inTap)
				.addTailSink(assembly, outTap);

		// run the flow
		flowConnector.connect(flowDef).complete();

	}
}
