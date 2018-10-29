# Team21

## Deliverables

The deliverables for this project are contained within the *Deliverables* directory.
* **Deliverable 1** - Team Agreement and team member biographies
* **Deliverable 2** - Product Backlog(includes personas and user stories)
* **Deliverable 3** - PRoject Planning and Execution

## Source Code

The source code is contained within the *immigration-analytics* directory. Maven commands must be executed from within this directory.

### Requirements

* Maven
* <a href='https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html'>Java JDK 8</a>

### Running the Application

Please ensure you are in the *immigration-analytics* directory before running the following commands:

First, compile the code with:

`mvn compile`

Then, you can run the application with the following commands:
* On Linux/Mac: `mvn exec:java -Dexec.mainClass="com.nineplusten.app.App"`
* On Windows: `mvn exec:java -D"exec.mainClass"="com.nineplusten.app.App"`
