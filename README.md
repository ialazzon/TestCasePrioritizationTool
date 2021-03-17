**Test Case Prioritization Tool for Model Transformations**

This is a Gradle Java project containing the program that applies the test case prioritization approach described in the paper &quot;Test Case Prioritization for Model Transformations&quot; by the authors Dr. Saqib Iqbal and Dr. Issam Al-Azzoni.

The Java file which can be executed is _TestCasePrioritizer_ in the package _tcp\_tool_.

The tool requires two inputs from the user:

1. The list of rule names in the transformation. The String array _rule\_names_ in the _TestCasePrioritizer_ class contains the rule names.
2. The trace models of the previous runs of the test cases. These are in the folder _test\_cases_. These trace models can be automatically obtained from the transformation executions as described in the paper.

The tool utilizes the trace models to build a rule coverage information model which is exploited later by the different techniques discussed in the paper.

As an output, the tool displays the test case orderings returned by Greedy, GA, and ACO in addition to the APRC value corresponding to each ordering.

This is a Gradle Java project. The _TestCasePrioritizer.java_ program can be run by executing Gradle&#39;s build followed by run tasks. Dependencies are handled by Gradle.

Note that the _TestCasePrioritizer_ class is used for the _BibTex2DocBook_ case study. We created another class _TestCasePrioritizer2_ for the _Ecore2Maude_ case study. Details on the different cases are provided in the paper.
