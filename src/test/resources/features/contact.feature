Feature: Contact form
  As a customer
  I want to use the Contact form
  So that I can send a message to the store

  Background:
    Given the customer is on the homepage
    And the customer opens the Contact form

  Scenario: Send a contact message successfully
    When the customer fills the Contact form with:
      | email   | test@example.com        |
      | name    | Kwame                   |
      | message | Hello, I need support   |
    And the customer clicks Send message
    Then an alert should appear with text "Thanks for the message!!"
    And the customer accepts the alert

  Scenario: Close the Contact form with X button
    When the customer clicks the Contact form close icon
    Then the Contact form should not be visible
