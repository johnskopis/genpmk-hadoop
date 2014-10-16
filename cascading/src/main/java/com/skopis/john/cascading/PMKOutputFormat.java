package com.skopis.john.cascading;

import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordWriter;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.Progressable;

import cascading.tuple.Tuple;

public class PMKOutputFormat extends FileOutputFormat<Tuple, Tuple> {
	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(PMKOutputFormat.class);
	public static final String SSID_KEY = "ssid";

	private static class PMKRecordWriter implements RecordWriter<Tuple, Tuple> {
		private DataOutputStream out = null;
		private String ssid;

		public PMKRecordWriter(FSDataOutputStream fileOut, String ssid)
				throws IOException {
			this.out = fileOut;
			this.ssid = ssid;

			// ByteBuffer buffer = ByteBuffer.allocate(40);
			//
			// String magic = "APWC";
			// buffer.put(magic.getBytes());
			// buffer.putInt(ssid.length());
			// buffer.put(ssid.getBytes());
			// this.out.write(buffer.array(), 0, 40);
		}

		@Override
		public void close(Reporter arg0) throws IOException {
			out.close();
		}

		@Override
		public void write(Tuple k, Tuple v) throws IOException {
			String pass = v.getString(0);
			byte[] hash = (byte[]) v.getObject(1);
			out.write(33 + pass.length());
			out.writeBytes(pass);
			out.write(hash);
		}
	}

	public RecordWriter<Tuple, Tuple> getRecordWriter(FileSystem ignored,
			JobConf job, String name, Progressable progress) throws IOException {
		String ssid = job.get(PMKOutputFormat.SSID_KEY);
		Path file = FileOutputFormat.getTaskOutputPath(job, name);
		FileSystem fs = file.getFileSystem(job);
		FSDataOutputStream fileOut = fs.create(file, progress);
		return new PMKRecordWriter(fileOut, ssid);
	}
}
