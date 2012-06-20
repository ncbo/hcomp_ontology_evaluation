package edu.stanford.bmir.test.mturk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.amazonaws.mturk.addon.HITDataCSVReader;
import com.amazonaws.mturk.addon.HITDataInput;
import com.amazonaws.mturk.addon.HITQuestion;

public class MTurkUtil {
	protected static Logger log = Logger
			.getLogger(MTurkUtil.class);

	private static String rootDirField = "rootDir";
	private static String rootDir = null;

	public static Properties loadPropertiesFile(String fileName)
			throws IOException {
		// Read properties file.
		Properties props = new Properties();
		props.load(new InputStreamReader(new FileInputStream(new java.io.File(
				fileName)), "UTF-8"));
		return props;
	}

	public static String generateMultipleQuestionsFromInputMap(
			String questionFile, String inputDataFile,
			String header, String footer, String ontologyFilter) throws Exception {

		String result = (header == null ? "" : header);

		HITQuestion question = new HITQuestion (questionFile);
		HITDataInput input = new HITDataCSVReader(inputDataFile);
		int numRecords = input.getNumRows() - 1;

		for (int i = 0; i < numRecords; i++) {

			Map<String, String> inputMap = input.getRowAsMap(i + 1);

			if ((ontologyFilter == null) ||
					inputMap.get ("ontology").equals(ontologyFilter))
				result += question.getQuestion(inputMap);
				result += "\n";
		}

		result += (footer == null ? "" : footer);

		return result;
		
	}

	public static String getFileName(Properties props,
			String fieldName) {
		if (rootDir == null)
			rootDir = props.getProperty(rootDirField);

		return rootDir + props.getProperty(fieldName);
	}

	public static String getString(String headerFile) throws IOException {
		byte[] buffer = new byte[(int) new File(headerFile).length()];
		FileInputStream f = new FileInputStream(headerFile);
	    f.read(buffer);
	    return new String(buffer);	
	  }

	
	
}
