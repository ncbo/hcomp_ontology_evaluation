## Mechanical Turk Configuration Options

- <code>mturk.properties</code> contains AWS keys (insert yours here) and other access information

- <code>categorization.properties</code> properties for microtasks: the ontology to use; how many assignments to request; how long the HIT is active, etc.

The rest of the configs are in pilotConfigs:

- <code>categorization.input</code> the main input file that contains the child-parent pairs for all ontologies

- <code>categorization.header</code> the text that we show above the questions

- <code>categorization.question</code> velocity template for the verification question

- <code>qualification_WN.input</code> pairs of terms for simple qualification questions

- <code>qualification.input</code> pairs of terms for the original qualification questions

- <code>qualification.answer</code> velocity template for the answers to qualification questions

- <code>qualification.header</code> the text that we show above the qualification questions

- <code>qualification.question</code> velocity template for the qualification question 

- <code>qualification.properties</code> properties for qualification set up (including built in qualification requirements)



