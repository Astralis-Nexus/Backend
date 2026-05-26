1. What criteria did you use to assign roles for your specification review?
- Reviewere: Benjamin, Jeppe, Laith
- Moderator: Ahmad
- Referant: Jeppe
Ahmad is a great moderator because he is good at keeping discussions on track and ensuring that everyone 
has a chance to contribute. 

Benjamin, Jeppe, and Laith were chosen as reviewers because they have a 
strong understanding of the project and can provide valuable feedback on the specification. 

Jeppe was also chosen as the referant because he is detail-oriented and can help ensure that 
all important points are documented accurately.

2. How did you apply the boundary values technique to the design of test cases?
The boundary values technique involves testing the values at the edges of the input domain. This gives us an 
easier way of identifying our test cases, as we know when is "enough".

3. Why did you create/not create a decision table?
We chose not to create decision table as we felt that the table was not necessary for our test cases. As
it is used to represent complex decision logic, and our test cases were relatively straightforward. 

4. Track your unit tests to the test cases found in the black-box design phase or vice versa
-

5. Explain how you organised your risk table
-

6. Explain the follow-up to a specific risk
-

7. Did your coverage tool calculate statement coverage or decision coverage?
Our coverage tool calculates both.

8. Explain what approach to unit testing did you use
We approached our unit tests by Given-When-Then, which is a common approach to writing test cases. 

9. Discuss your use of the AAA pattern for your unit tests
We chose to use something similar to the AAA pattern, which stands for Arrange-Act-Assert. We chose
given-when-then as it is more descriptive and easier to understand for us.

10. Show your use of parameterised tests/data providers
-

11. Show how you used test doubles or, if you did not, explain why
we chose not to mock any dependencies as we felt that we'd rather test the actual implementation of our code. 

12. Explain how you tested the database
We tested the database by writing integration tests that interact with the database. 
We used a test database to ensure that our tests did not affect the production data. 

13. Explain how you tested the external API
We tested the external API by writing api tests that interact with the API. 
We used a mock server to simulate the API responses, which allowed us to test our code without relying 
on the actual API.

14. Showcase negative API tests
-

15. Explain the design of your internal API tests
-

16. Explain the design of your end-to-end tests
-

17. Explain the design of the stress performance tests
-