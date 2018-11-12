# Team21

## Deliverables

The deliverables for this project are contained within the *Deliverables* directory.
* **Deliverable 1** - Team Agreement and team member biographies
* **Deliverable 2** - Product Backlog(includes personas and user stories)
* **Deliverable 3** - Project Planning and Execution (Sprint 1 and Sprint 2)
* **Deliverable 4** - Verification and Code Review (Sprint 3 and Sprint 4)

## Source Code

The source code is contained within the *immigration-analytics* directory. Maven commands must be executed from within this directory.

### Requirements

* Maven
* <a href='https://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase10-4425482.html'>Java JDK 10</a>

### Running the Application

Please ensure you are in the *immigration-analytics* directory before running the following commands:

First, compile the code with:

`mvn compile`

Then, you can run the application directly (without packaging) with the following commands:
* On Linux/Mac: `mvn exec:java`
* On Windows: `mvn exec:java`

To package and install the application, run:

`mvn package`

Then, you can run the packaged version with the following command (replacing &lt;version&gt; with the application version):
`java -cp "target/immigration-analytics-<version>-SNAPSHOT-jar-with-dependencies.jar" "com.nineplusten.app.App"`
