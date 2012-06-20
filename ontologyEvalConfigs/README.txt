
mturk.properties: contains AWS keys (insert yours here) and other access information

categorization.properties: properties for microtasks: the ontology to use; how many assignments to request; how long the HIT is active, etc.

The rest of the configs are in pilotConfigs:

categorization.input: the main input file that contains the child-parent pairs for all ontologies

categorization.header: the text that we show above the questions

categorization.question: velocity template for the verification question

qualification_WN.input: pairs of terms for simple qualification questions

qualification.input: pairs of terms for the original qualification questions

qualification.answer: velocity template for the answers to qualification questions

qualification.header: the text that we show above the qualification questions

qualification.question: velocity template for the qualification question 

qualification.properties: properties for qualification set up (including built in qualification requirements)



