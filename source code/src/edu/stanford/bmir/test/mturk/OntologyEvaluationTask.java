package edu.stanford.bmir.test.mturk;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.amazonaws.mturk.addon.HITProperties;
import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.mturk.requester.QualificationRequirement;
import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.util.PropertiesClientConfig;

public class OntologyEvaluationTask {
	protected static Logger log = Logger
			.getLogger(OntologyEvaluationTask.class);

	private String propertiesFile = "categorization.properties";
	private String turkPropertiesField = "turkProperties";
	private String inputFileField = "inputFile";
	protected String ontologyField = "ontology";

	private String turkPropertiesFile;
	private String inputFile;
	private static String ontology = null;
	

	private RequesterService service;
	private Properties props;
	private Question question;

	public OntologyEvaluationTask() throws IOException {
		props = MTurkUtil.loadPropertiesFile(propertiesFile);

		turkPropertiesFile = props.getProperty(turkPropertiesField);
		inputFile = MTurkUtil.getFileName(props, inputFileField);
		ontology = props.getProperty(ontologyField);

		question = new Question (props, inputFile);

		service = new RequesterService(new PropertiesClientConfig(
				turkPropertiesFile));
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		OntologyEvaluationTask testHIT = new OntologyEvaluationTask();

		// if (testHIT.hasEnoughFunds())
		testHIT.createOntologyEvaluationTask();
	}

	public void createOntologyEvaluationTask() throws Exception {
		int count = 0;

		HITProperties hitProps = new HITProperties(props);

		CategorizationQualificationRequirement qualReq = new CategorizationQualificationRequirement(
				service, props);

		QualificationRequirement[] qualReqs = qualReq
				.getQualificationRequirements();

		CategorizarionReviewPolicy reviewPolicy = new CategorizarionReviewPolicy(
				inputFile, hitProps, service, qualReqs);

		for (String nextQuestion = question.getNextQuestion (); nextQuestion != null; nextQuestion = question.getNextQuestion ()) {
			HIT hit = service.createHIT(reviewPolicy.getHitTypeId(), hitProps
					.getTitle(), hitProps.getDescription(),
					hitProps.getKeywords(),
					// question.getQuestion(inputMap),
					nextQuestion, new Double(hitProps.getRewardAmount()), new Long(
							hitProps.getAssignmentDuration()), new Long(
							hitProps.getAutoApprovalDelay()),
					new Long(hitProps.getLifetime()),
					new Integer(hitProps.getMaxAssignments()), hitProps
							.getAnnotation(), // requesterAnnotation
					qualReqs, null, // responseGroup
					null, // uniqueRequestToken
					reviewPolicy.getScoreKnownAnswersPolicy(), null);

			System.out.println("Created HIT: " + hit.getHITId());

			System.out.println("You may see your HIT with HITTypeId '"
					+ hit.getHITTypeId() + "' here: ");

			System.out.println(service.getWebsiteURL()
					+ "/mturk/preview?groupId=" + hit.getHITTypeId());
			count++;
		}
		System.out.println("Hits created: " + count);

		// hits.add(hit);
		// }
	}

	/**
	 * Check to see if there are sufficient funds.
	 * 
	 * @return true if there are sufficient funds. False otherwise.
	 */
	public boolean hasEnoughFunds() {
		double balance = service.getAccountBalance();
		System.out.println("Got account balance: "
				+ RequesterService.formatCurrency(balance));
		return balance > 0;
	}

	public static String getOntology() {
		return ontology;
	}

}
