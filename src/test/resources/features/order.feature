Feature: Place Order
  As a customer
  I want to complete my purchase
  So that I can confirm the order successfully

  Background:
    Given the customer is on the Cart page
    And the customer clicks Place Order

  Scenario: Place a new order successfully
    When the customer enters order details
      | name    | Kwame                |
      | country | Ghana                |
      | city    | Kumasi               |
      | card    | 1234567812345678     |
      | month   | 12                   |
      | year    | 2026                 |
    And the customer clicks Purchase
    Then the success modal should be displayed
    And the success title should be "Thank you for your purchase!"
    And the success details should not contain the card details "1234567812345678"
    And the customer clicks OK
