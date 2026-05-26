Introduction to Software Testing

1. What are the differences between testing and debugging?
Testing is used to find failures or defects in the software. Debugging is the process of finding the 
cause of the defect and fixing it.

2. What is static testing and what is dynamic testing?
Static testing checks documents, requirements, or code without running the program. 
Dynamic testing checks the software by executing it and comparing the actual result with 
the expected result.

3. What is verification and what is validation?
Verification checks whether the product is built correctly according to the 
requirements and design. Validation checks whether the right product is built for the user’s needs.

4. What is regression testing?
Regression testing is testing existing functionality after changes have been made, 
to make sure that old features still work and no new defects were introduced.

5. Explain at least four general testing principles.
    1. Testing shows the presence of defects, not their absence. 
    2. Exhaustive testing is impossible, so tests must be prioritized. 
    3. Early testing saves time and money
    4. Defects cluster together

The V-model and testing in Agile

6. Describe the V-model
The V-model shows the relationship between development phases and test phases. On the left side, 
requirements and design are created. On the right side, tests are performed to check each level, 
from unit testing to acceptance testing.

7. According to the ISTQB certification (as expressed in the slides), what parts of the V-model correspond 
to verification and which ones to validation?
Verification is the left side of the V-model, where requirements, architecture, and design are reviewed. 
Validation is the right side, where the software is executed and tested through unit, integration, system, 
and acceptance testing.

8. Explain what types of tests fall normally under system testing
System testing includes performance tests and end-to-end tests. The goal is to test the complete 
system as a whole.

9. Explain the test pyramid and discuss its applicability
The test pyramid shows that a project should have many unit tests, 
fewer integration tests, and even fewer end-to-end tests. Unit tests are fast and cheap, 
while end-to-end tests are slower and more fragile. It is useful as a general guideline, 
but the exact balance depends on the project.

10. Explain the difficulties of performing acceptance testing in a Scrum setting
Acceptance testing can be difficult in Scrum because requirements may change during the sprint, 
and the customer or product owner may not always be available for feedback. It can also be hard to 
finish development and acceptance testing within the same sprint.

Test Management

11. What is test monitoring and control?
Test monitoring is tracking the progress and status of testing. 
Test control is taking action if testing is not going as planned, for example by changing priorities, 
resources, or the test schedule.

12. What is incident management?
Incident management is the process of reporting, tracking, analyzing, and handling unexpected events 
or problems found during testing. An incident may be caused by a defect, a test environment issue, 
or unclear requirements.

13. What is configuration management?
Configuration management is controlling and tracking versions of test items, testware, environments, 
and software. It ensures that the right versions are tested and that changes are documented.


White Box Test Design

14. Explain the pros and cons of coverage as a test progress measuring indicator
Coverage is useful because it shows how much of the code, statements, or decisions have 
been executed by tests. It can help find untested areas and measure test progress. 
The disadvantage is that high coverage does not prove that the tests are good or that the software is 
correct. It can also be misleading if the tests are not well designed or if they only cover trivial cases.

Unit Testing

15. Discuss whether private methods must be unit-tested or not
Private methods should normally not be tested directly. They are implementation details and should be 
tested through the public methods that use them. If a private method is very complex, it may be a sign 
that the code should be refactored into a separate class.

16. Discuss when a unit test can be considered an integration test
A unit test can be considered an integration test when it tests the database.

17. What are the advantages and disadvantages of the classical approach to unit testing 
(isolating the unit tests, using test doubles only for shared dependencies)?
The classical approach gives tests that are closer to real behavior because most 
real objects are used together. It often makes tests less fragile and easier to refactor. 
The disadvantage is that failures can be harder to locate because several classes may be involved.

18. What are the advantages and disadvantages of the London approach to unit testing  
(isolating the units of code, using test doubles for all but immutable dependencies)?
The London approach isolates each class more strictly by using many test doubles. 
This makes it easier to find where a failure happened and allows testing units independently. 
The disadvantage is that tests can become tightly coupled to implementation details and may break 
during refactoring.

19. Discuss whether mocking should be used for external (unmanaged) dependencies
Mocking is useful for external unmanaged dependencies such as payment services, email services, 
or third-party APIs. It makes tests faster, more stable, and independent of systems we do not control. 
However, some integration tests should still verify that the real dependency works as expected.

Test-Driven Development

20. What are the advantages and disadvantages of Test-Driven Development?
TDD can improve design, give fast feedback, and create good automated test coverage. 
It can also make developers think more clearly about requirements before writing the implementation. 
The disadvantage is that it can feel slower at first and can be difficult when requirements are unclear 
or changing a lot.

21. Explain the TDD cadence
The TDD cadence is red, green, refactor. First, write a failing test for the desired behavior. 
Then write the simplest code to make the test pass. Finally, improve the code while keeping all tests green.

Integration Testing

22. Discuss potential issues when writing tests that involve a database
Database tests can be slower and more fragile than unit tests. They may fail because of missing data, 
dirty test data, wrong schema, connection problems, or differences between local and online environments. 
Tests must also be isolated, so one test does not affect another.

23. List different possibilities when testing against a database
You can test against a real test database, an in-memory database, a database running in Docker, 
or a mocked repository. A real database gives the most realistic result, while mocks and in-memory 
databases are faster but less realistic.

Acceptance Testing

24. Explain what is an acceptance test
An acceptance test checks whether the system meets the user’s or customer’s requirements. 
It focuses on business value and whether the feature is ready to be accepted.

25. Explain how an acceptance test can be documented
An acceptance test can be documented as acceptance criteria, test cases, or user scenarios. 
A common format is Given-When-Then, which describes the precondition, the action, and the expected result.
