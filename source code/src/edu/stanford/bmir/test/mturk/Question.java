package edu.stanford.bmir.test.mturk;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.amazonaws.mturk.addon.HITDataCSVReader;
import com.amazonaws.mturk.addon.HITDataInput;
import com.amazonaws.mturk.addon.HITQuestion;

public class Question {
	private static final String QUESTION_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
			+ "<QuestionForm xmlns=\"http://mechanicalturk.amazonaws.com/AWSMechanicalTurkDataSchemas/2005-10-01/QuestionForm.xsd\">";
	private static final String QUESTION_FOOTER = "</QuestionForm>";

	private String questionFileField = "questionFile";
	private String categorizationHeaderFileField = "categorizationHeaderFile";
	private String oneQuestionPerHITField = "oneQuestionPerHIT";
	protected String ontologyField = "ontology";
	
	private String questionFile;
	private String categorizationHeaderFile;
	private String inputFile;
	private Boolean oneQuestionPerHIT = null; 
	private int currentQuestion = 0; 
	private String ontology = null;
	private String header;
	private String footer;
	private HITDataInput input;
	private String nextQuestion = null;
	private boolean done = false;

	public Question(Properties props, String inputFile) throws IOException {
		if (props.getProperty(oneQuestionPerHITField) != null)
			oneQuestionPerHIT = new Boolean (props.getProperty(oneQuestionPerHITField));
		if (oneQuestionPerHIT == null)
			oneQuestionPerHIT = new Boolean ("false");

		questionFile = MTurkUtil.getFileName(props, questionFileField);
		categorizationHeaderFile = MTurkUtil.getFileName(props,
				categorizationHeaderFileField);
		this.inputFile = inputFile;
		ontology = props.getProperty(ontologyField);
		header = QUESTION_HEADER + MTurkUtil.getString(categorizationHeaderFile);
		footer = QUESTION_FOOTER;
		input = new HITDataCSVReader(inputFile);
		
	}


	public String getNextQuestion() throws IOException, Exception {
		String result;
		
		if (done) return null;
		
		if (oneQuestionPerHIT.booleanValue() == false) { //all questions in one form
			result = MTurkUtil
					.generateMultipleQuestionsFromInputMap(
							questionFile,
							inputFile,
							header,
							footer, ontology);
			done = true;
			return result;
		} else {
			result = "";
			HITQuestion question = new HITQuestion (questionFile);	
			
			while (result.equals("") && currentQuestion < input.getNumRows() - 1) {
			
				Map<String, String> inputMap = input.getRowAsMap(currentQuestion + 1);
				
				if ((ontology == null) ||
					inputMap.get ("ontology").equals(ontology)) {
					result += question.getQuestion(inputMap);
					result += "\n";
				}
				currentQuestion++;
			}
			if (result.equals("")) {
				done = true; 
				return null;
			}
		}

		result = (header == null ? "" : header) + result + (footer == null ? "" : footer);

		return result;
		
	}


}

