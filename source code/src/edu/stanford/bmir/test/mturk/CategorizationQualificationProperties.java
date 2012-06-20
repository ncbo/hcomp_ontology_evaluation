package edu.stanford.bmir.test.mturk;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.amazonaws.mturk.requester.Comparator;
import com.amazonaws.mturk.requester.Locale;
import com.amazonaws.mturk.requester.QualificationRequirement;
import com.amazonaws.mturk.requester.QualificationTypeStatus;

public class CategorizationQualificationProperties {

	protected static Logger log = Logger
			.getLogger(CategorizationQualificationProperties.class);

	private final int MAX_NUM_QUALIFICATIONS_SUPPORTED = 5;

	private final String QUALIFICATION_TEST_HEADER = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
			+ "<QuestionForm xmlns=\"http://mechanicalturk.amazonaws.com/AWSMechanicalTurkDataSchemas/2005-10-01/QuestionForm.xsd\"> \n";
	
	private final String QUALIFICATION_TEST_FOOTER = "</QuestionForm>";
	
	private final String ANSWER_KEY_HEADER = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
			+ "<AnswerKey xmlns=\"http://mechanicalturk.amazonaws.com/AWSMechanicalTurkDataSchemas/2005-10-01/AnswerKey.xsd\"> \n";
	
	private final String ANSWER_KEY_FOOTER = "</AnswerKey>";
	// private Map inputMap = new HashMap();

	public final static String QUAL_FIELD = "qualification";
	public final static String QUAL_VALUE_FIELD = "qualification.value";
	public final static String QUAL_COMPARATOR_FIELD = "qualification.comparator";
	public final static String QUAL_LOCALE_FIELD = "qualification.locale";
	public final static String QUAL_PRIVATE_FIELD = "qualification.private";

	protected String name = null;
	protected String keywords = null;
	protected String description = null;
	protected QualificationTypeStatus status = null;
	protected String retryDelayInSeconds = null;
	protected String testDurationInSeconds = null;
	protected Boolean autoGranted = null;
	protected String autoGrantedValue = null;
	protected String qualificationTest = null;
	protected String answerKey = null;
	protected Integer integerValue = null;
	protected Boolean includeTest = false;

	protected String[] qualificationType = new String[MAX_NUM_QUALIFICATIONS_SUPPORTED];
	protected String[] qualificationComparator = new String[MAX_NUM_QUALIFICATIONS_SUPPORTED];
	protected String[] qualificationValue = new String[MAX_NUM_QUALIFICATIONS_SUPPORTED];
	protected String[] qualificationPrivate = new String[MAX_NUM_QUALIFICATIONS_SUPPORTED];
	protected String[] qualificationLocale = new String[MAX_NUM_QUALIFICATIONS_SUPPORTED];

	protected Boolean requiredToPreview = true;
	protected Comparator comparator = Comparator.GreaterThanOrEqualTo;

	protected String nameField = "name";
	protected String keywordsField = "keywords";
	protected String descriptionField = "description";
	protected String statusField = "status";
	protected String retryDelayInSecondsField = "retryDelayInSeconds";
	protected String testDurationInSecondsField = "testDurationInSeconds";
	protected String autoGrantedField = "autoGranted";
	protected String autoGrantedValueField = "autoGrantedValue";
	protected String comparatorField = "comparator";
	protected String integerValueField = "value";
	protected String includeTestValueField = "includeTest";
	protected String requiredToPreviewField = "private";
	
	protected String qualificaitonPropetiesFileField = "qualificationPropertiesFile";
	protected String qualificationTestFileField = "qualificationTestFile";
	protected String qualificationInputDataFileField = "qualificationInputDataFile";
	protected String qualificationAnswerKeyFileField = "qualificationAnswerKeyFile";
	protected String qualificationHeaderFileField = "qualificationHeaderFile";
	
	
	protected String qualificaitonPropetiesFile;
	protected String qualificationTestFile;
	protected String qualificationInputDataFile;
	protected String qualificationAnswerKeyFile;
	protected String qualificationHeaderFile;
	
	// -------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------

	public CategorizationQualificationProperties(Properties props) throws Exception {
		qualificaitonPropetiesFile = (MTurkUtil.getFileName(props, qualificaitonPropetiesFileField));
		qualificationTestFile = (MTurkUtil.getFileName(props, qualificationTestFileField));
		qualificationInputDataFile = (MTurkUtil.getFileName(props, qualificationInputDataFileField));
		qualificationAnswerKeyFile = (MTurkUtil.getFileName(props, qualificationAnswerKeyFileField));
		qualificationHeaderFile = MTurkUtil.getFileName(props, qualificationHeaderFileField);

		populateFields(MTurkUtil.loadPropertiesFile(qualificaitonPropetiesFile));
		createQualificationTest(qualificationTestFile, qualificationInputDataFile);
		createQualificationAnswerKey(qualificationAnswerKeyFile, qualificationInputDataFile);
	}

	private void createQualificationTest(String qualificationTestFile, String qualificationInputDataFile)
			throws Exception {
		
		qualificationTest = MTurkUtil.generateMultipleQuestionsFromInputMap 
					(qualificationTestFile, qualificationInputDataFile, QUALIFICATION_TEST_HEADER + MTurkUtil.getString(qualificationHeaderFile), QUALIFICATION_TEST_FOOTER, null);
		//		qualificationTest = (new FileUtil(qualificationTestFile)).getString();

	}

	private void createQualificationAnswerKey(String qualificationAnswerKeyFile, String answerKeyInputFile)
			throws Exception {
//		answerKey = (new FileUtil(qualificationAnswerKeyFile)).getString();

		answerKey = MTurkUtil.generateMultipleQuestionsFromInputMap 
				(qualificationAnswerKeyFile, answerKeyInputFile, ANSWER_KEY_HEADER, ANSWER_KEY_FOOTER, null);

	}

	public QualificationRequirement[] getBuiltInQualificationRequirements() {
		List<QualificationRequirement> quals = new ArrayList<QualificationRequirement>();
		for (int i = 0; i < MAX_NUM_QUALIFICATIONS_SUPPORTED; i++) {
			if (qualificationType[i] != null
					&& !qualificationType[i].equals("")) {
				// There's a qualification type here
				QualificationRequirement thisQual = new QualificationRequirement(
						getQualificationType(i), getQualificationComparator(i),
						getQualificationIntegerValue(i),
						getQualificationLocaleValue(i),
						getQualificationPrivate(i));
				quals.add(thisQual);
			}
		}
		// Return the array of qualifications
		return quals.toArray(new QualificationRequirement[quals.size()]);

	}

	protected void populateFields(Properties props) {

		setName(props.getProperty(nameField));
		setDescription(props.getProperty(descriptionField));
		setKeywords(props.getProperty(keywordsField));
		setStatus(QualificationTypeStatus.Active);
		setRetryDelayInSeconds(props.getProperty(retryDelayInSecondsField));
		setTestDurationInSeconds(props.getProperty(testDurationInSecondsField));
		setAutoGranted(Boolean.valueOf(props.getProperty(autoGrantedField)));
		setAutoGrantedValue(props.getProperty(autoGrantedValueField));
		setIntegerValue(new Integer(props.getProperty(integerValueField)));
		setIncludeTestValue(new Boolean(props.getProperty(includeTestValueField)));

		for (int i = 1; i <= MAX_NUM_QUALIFICATIONS_SUPPORTED; i++) {
			setQualificationType(i - 1, props.getProperty(QUAL_FIELD + "." + i));
			setQualificationValue(i - 1,
					props.getProperty(QUAL_VALUE_FIELD + "." + i));
			setQualificationComparator(i - 1,
					props.getProperty(QUAL_COMPARATOR_FIELD + "." + i));
			setQualificationLocale(i - 1,
					props.getProperty(QUAL_LOCALE_FIELD + "." + i));
			setQualificationPrivate(i - 1,
					props.getProperty(QUAL_PRIVATE_FIELD + "." + i));
		}

	}

	public String getName() {
		if (name == null)
			throw new IllegalStateException(nameField + " is not set");
		else
			return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		if (description == null)
			throw new IllegalStateException(descriptionField + " is not set");
		else
			return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public QualificationTypeStatus getStatus() {
		return status;
	}

	public void setStatus(QualificationTypeStatus status) {
		this.status = status;
	}

	public String getRetryDelayInSeconds() {
		return retryDelayInSeconds;
	}

	public void setRetryDelayInSeconds(String retryDelayInSeconds) {
		this.retryDelayInSeconds = retryDelayInSeconds;
	}

	public String getTestDurationInSeconds() {
		return testDurationInSeconds;
	}

	public void setTestDurationInSeconds(String testDurationInSeconds) {
		this.testDurationInSeconds = testDurationInSeconds;
	}

	public Boolean getAutoGranted() {
		return autoGranted;
	}

	public void setAutoGranted(Boolean autoGranted) {
		this.autoGranted = autoGranted;
	}

	public String getAutoGrantedValue() {
		return autoGrantedValue;
	}

	public void setAutoGrantedValue(String autoGrantedValue) {
		this.autoGrantedValue = autoGrantedValue;
	}

	public String getQualificationTest() {
		return qualificationTest;
	}

	public void setQualificationTest(String qualificationTest) {
		this.qualificationTest = qualificationTest;
	}

	public String getAnswerKey() {
		return answerKey;
	}

	public void setAnswerKey(String answerKey) {
		this.answerKey = answerKey;
	}

	public Comparator getComparator() {
		return comparator;
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}

	public Integer getIntegerValue() {
		return integerValue;
	}

	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}

	public Boolean getIncludeTestValue() {
		return includeTest;
	}

	public void setIncludeTestValue(Boolean includeTestValue) {
		this.includeTest = includeTestValue;
	}

	public Boolean getRequiredToPreview() {
		return requiredToPreview;
	}

	public void setRequiredToPreview(Boolean requiredToPreview) {
		this.requiredToPreview = requiredToPreview;
	}

	public void setQualificationType(int qualNum, String qualType) {
		qualificationType[qualNum] = qualType;
	}

	public void setQualificationValue(int qualNum, String qualValue) {
		qualificationValue[qualNum] = qualValue;
	}

	public void setQualificationComparator(int qualNum, String qualComparator) {
		qualificationComparator[qualNum] = qualComparator;
	}

	public void setQualificationLocale(int qualNum, String qualLocale) {
		qualificationLocale[qualNum] = qualLocale;
	}

	public void setQualificationPrivate(int qualNum, String qualPrivate) {
		qualificationPrivate[qualNum] = qualPrivate;
	}

	private String getQualificationType(int qualNum) {
		return safeTrim(qualificationType[qualNum]);
	}

	private Integer getQualificationIntegerValue(int qualNum) {
		if (qualificationValue[qualNum] == null
				|| qualificationValue[qualNum].equals(""))
			return null;

		return new Integer(qualificationValue[qualNum]);
	}

	private Comparator getQualificationComparator(int qualNum) {
		String qualComparator = qualificationComparator[qualNum];

		if (qualComparator != null) {
			qualComparator = qualComparator.trim().toLowerCase();

			if ("lessthan".equals(qualComparator))
				return Comparator.LessThan;
			else if ("lessthanorequalto".equals(qualComparator))
				return Comparator.LessThanOrEqualTo;
			else if ("greaterthan".equals(qualComparator))
				return Comparator.GreaterThan;
			else if ("greaterthanorequalto".equals(qualComparator))
				return Comparator.GreaterThanOrEqualTo;
			else if ("equalto".equals(qualComparator))
				return Comparator.EqualTo;
			else if ("notequalto".equals(qualComparator))
				return Comparator.NotEqualTo;
			else if ("exists".equals(qualComparator))
				return Comparator.Exists;
			else {
				log.info("Your configuration file provided an unrecognized comparator: "
						+ qualComparator);
				return null;
			}
		} else {
			return null;
		}
	}

	private Locale getQualificationLocaleValue(int qualNum) {
		if (qualificationLocale[qualNum] == null
				|| qualificationLocale[qualNum].equals(""))
			return null;

		String qualLocale = safeTrim(qualificationLocale[qualNum]);
		return new Locale(qualLocale);
	}

	private boolean getQualificationPrivate(int qualNum) {
		String isPrivate = safeTrim(qualificationPrivate[qualNum]);
		if (isPrivate != null)
			return "true".equals(isPrivate.toLowerCase());
		else
			return false;
	}

	private String safeTrim(String strToTrim) {
		if (strToTrim == null)
			return null;
		else
			return strToTrim.trim();
	}

}
