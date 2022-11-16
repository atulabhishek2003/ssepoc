@TEST_ONLY
Feature: Create an order for product with payment term 45, generate an Invoice and check arrival to Finance system. Product is set up in vIPER 

@TEST_ONLY
Scenario: TEST_ONLY 
  Then I login to Salesforce as a "System Admin"
  Then I logout