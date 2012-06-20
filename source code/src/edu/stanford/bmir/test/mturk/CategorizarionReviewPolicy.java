package edu.stanford.bmir.test.mturk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.mturk.addon.HITDataCSVReader;
import com.amazonaws.mturk.addon.HITDataInput;
import com.amazonaws.mturk.addon.HITProperties;
import com.amazonaws.mturk.requester.ParameterMapEntry;
import com.amazonaws.mturk.requester.PolicyParameter;
import com.amazonaws.mturk.requester.QualificationRequirement;
import com.amazonaws.mturk.requester.ReviewPolicy;
import com.amazonaws.mturk.service.axis.RequesterService;

public class CategorizarionReviewPolicy {
	protected static Logger log = Logger
			.getLogger(OntologyEvaluationTask.class);

	protected String hitTypeId = null;
	protected ReviewPolicy scoreKnownAnswersPolicy = null;

	public CategorizarionReviewPolicy(String inputFile, HITProperties hitProps,
			RequesterService service, QualificationRequirement[] qualReqs) throws IOException {
		// Create a HIT type for our HIT
		String hitTypeId = service.registerHITType(
				RequesterService.DEFAULT_AUTO_APPROVAL_DELAY_IN_SECONDS,
				RequesterService.DEFAULT_ASSIGNMENT_DURATION_IN_SECONDS,
				hitProps.getRewardAmount(), hitProps.getTitle(),
				hitProps.getKeywords(), // keywords
				hitProps.getDescription(), qualReqs); // qualRequirements

		// Set up a ScoreKnownAnswers policy

		HITDataInput input = new HITDataCSVReader(inputFile);
		List<ParameterMapEntry> answerKey = new ArrayList<ParameterMapEntry>();

		for (int i = 0, j = 0; i < input.getNumRows() - 1; i++) {
			Map<String, String> inputMap = input.getRowAsMap(i + 1);
			if ((OntologyEvaluationTask.getOntology() == null)
					|| inputMap.get("ontology").equals(
							OntologyEvaluationTask.getOntology())) {
				answerKey.add(new ParameterMapEntry(inputMap.get("id"),
						new String[] { inputMap.get("value") }));
			}
		}

		ParameterMapEntry[] answerKeyArray = answerKey
				.toArray(new ParameterMapEntry[answerKey.size()]);
		for (int j = 0; j < answerKeyArray.length; j++) {
			ParameterMapEntry next = answerKeyArray[j];
		}
		PolicyParameter[] skaParams = {
				new PolicyParameter("ApproveIfKnownAnswerScoreIsAtLeast",
						new String[] { "0" }, null),
				new PolicyParameter("ApproveReason", (String[]) Arrays.asList(
						"Thank you").toArray(), null),
//				new PolicyParameter("RejectIfKnownAnswerScoreIsLessThan",
//						new String[] { "2" }, null),
//				new PolicyParameter("RejectReason", (String[]) Arrays.asList(
//						"Fewer than 30% correct answers").toArray(), null),
				new PolicyParameter(
						"AnswerKey",
						null,
						(ParameterMapEntry[]) answerKey
								.toArray(new ParameterMapEntry[answerKey.size()])) };

		this.scoreKnownAnswersPolicy = new ReviewPolicy(
				"ScoreMyKnownAnswers/2011-09-01", skaParams);
		// log.info("name = " + scoreKnownAnswersPolicy.getPolicyName()
		// + "parameter "
		// + scoreKnownAnswersPolicy.getParameter(55).getKey());

		this.hitTypeId = hitTypeId;

	}

	public String getHitTypeId() {
		return hitTypeId;
	}

	public ReviewPolicy getScoreKnownAnswersPolicy() {
		return scoreKnownAnswersPolicy;
	}

}
