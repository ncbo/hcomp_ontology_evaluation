package edu.stanford.bmir.test.mturk;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.amazonaws.mturk.requester.QualificationRequirement;
import com.amazonaws.mturk.requester.QualificationType;
import com.amazonaws.mturk.service.axis.RequesterService;

public class CategorizationQualificationRequirement {
	protected static Logger log = Logger
			.getLogger(CategorizationQualificationRequirement.class);

	private QualificationRequirement[] qualReqs = null;
	private QualificationRequirement[] builtInQualReqs = null;

	private static final Integer DEFAULT_TEST_DURATION = 60 * 60;
	protected RequesterService service = null;

	public CategorizationQualificationRequirement(RequesterService service, Properties props)
						throws Exception {
		this.service = service;

		CategorizationQualificationProperties qualProps = new CategorizationQualificationProperties(props);

		String qualTypeId = createCategorizationQualificationType(qualProps);

		QualificationRequirement qualReq = new QualificationRequirement(
				qualTypeId, qualProps.getComparator(),
				qualProps.getIntegerValue(), null,
				qualProps.getRequiredToPreview());

		builtInQualReqs = qualProps.getBuiltInQualificationRequirements();

		qualReqs = new QualificationRequirement[builtInQualReqs == null ? 1
				: builtInQualReqs.length + 1];
		qualReqs[0] = qualReq;

		for (int i = 1; i < qualReqs.length; i++) {
			qualReqs[i] = builtInQualReqs[i - 1];
		}
	}

	public QualificationRequirement[] getQualificationRequirements() {
		return qualReqs;
	}

	public QualificationRequirement[] getBuiltInQualificationRequirements() {
		return builtInQualReqs;
	}

	private String createCategorizationQualificationType(
			CategorizationQualificationProperties qualProperties) {
		QualificationType qualType = getExistingQualificationType(qualProperties);

		if (qualType == null) {
			Long retryDelay = (qualProperties.getRetryDelayInSeconds() == null ? null
					: new Long(qualProperties.getRetryDelayInSeconds()));
			Long testDuration = (qualProperties.getTestDurationInSeconds() == null ? DEFAULT_TEST_DURATION
					: new Long(qualProperties.getTestDurationInSeconds()));
			Integer autoGrantedValue = (qualProperties.getAutoGrantedValue() == null ? null
					: new Integer(qualProperties.getAutoGrantedValue()));
			
			qualType = service.createQualificationType(
					qualProperties.getName(), qualProperties.getKeywords(),
					qualProperties.getDescription(),
					qualProperties.getStatus(), retryDelay,
					qualProperties.getQualificationTest(),
					qualProperties.getAnswerKey(), testDuration,
					qualProperties.getAutoGranted(), autoGrantedValue);
		}

		return qualType.getQualificationTypeId();
	}

	private QualificationType getExistingQualificationType(
			CategorizationQualificationProperties qualProperties) {
		QualificationType[] existingTypes = service.getAllQualificationTypes();
		QualificationType result = null;

		for (int i = 0; i < existingTypes.length; i++) {
			QualificationType next = existingTypes[i];
			if (next.getName().equals(qualProperties.getName()))
				return next;
		}

		return result;
	}

}
