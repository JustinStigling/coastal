package za.ac.sun.cs.coastal.reporting;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.Logger;

import za.ac.sun.cs.coastal.Configuration;
import za.ac.sun.cs.coastal.listener.ConfigurationListener;

public class ReporterManager implements ConfigurationListener {

	private Logger log = null;

	private final List<Reporter> reporters = new LinkedList<>();

	public ReporterManager() {
		// We expect configurationLoaded(...) to be called shortly.
		// This will initialize this instance.
	}

	@Override
	public void configurationLoaded(Configuration configuration) {
		log = configuration.getLog();
	}

	@Override
	public void collectProperties(Properties properties) {
		// do nothing
	}

	public void register(Reporter reporter) {
		reporters.add(0, reporter);
	}

	public void report() {
		for (Reporter reporter : reporters) {
			report(reporter);
		}
	}

	private static final String LS = System.getProperty("line.separator");

	private void report(Reporter reporter) {
		if (log != null) {
			log.info(Banner.getBannerLine(reporter, '='));
			final StringWriter infoWriter = new StringWriter();
			final StringWriter traceWriter = new StringWriter();
			reporter.report(new PrintWriter(infoWriter), new PrintWriter(traceWriter));
			String report = infoWriter.toString();
			if (report.length() > 0) {
				String[] reportLines = report.split(LS);
				for (String line : reportLines) {
					log.info(line);
				}
			}
			report = traceWriter.toString();
			if (report.length() > 0) {
				String[] reportLines = report.split(LS);
				for (String line : reportLines) {
					log.trace(line);
				}
			}
		}
	}

}