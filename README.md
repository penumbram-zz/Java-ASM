# Java-ASM
Repository containing modules that make use of the <a href="http://asm.ow2.org/ ">ASM</a> Tree API for white box code manipulation tests. <a href="http://junit.org/junit4/">JUnit4</a> is used for unit testing purposes.
- ControlFlowGraph is a mini tool that can convert a Java class to convert its functions to .dot representation and applies Live Variable Analysis (Worklist algorithm)to show the live variables at all the lines.
- BranchLineCoverage is a mini tool that takes a test suite and a class file as input and calculates the branch/line coverage ratios similar to the way popular SDKs and tools like JaCoCo and EMMA do.
- MutationTesting contains a class file that is mutation tested with its arithmetic operators, conditional operators and relational operators throughout the project.

###More info on ASM can be found <a href="https://en.wikipedia.org/wiki/ObjectWeb_ASM ">here</a>
