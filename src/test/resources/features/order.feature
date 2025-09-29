Feature: Place Order (Checkout)
  As a Customer
  I want to place an order
  So that I receive a confirmation of my purchase

  @regression @checkout
  Scenario Outline: Place order with valid details
    Given the customer has 1 item in the cart
    When the customer opens the order form
    And the customer enters name "<name>" and credit card "<cc>"
    And the customer submits the order
    Then the order confirmation should be displayed

    Examples:
      | name          | cc                |
      | Kofi Peter    | 1234567890123456  |
      | Yaa Amankwaa  | 1020304050607080  |

  @regression @checkout
  Scenario: Try to place order with missing credit card
    Given the customer has 1 item in the cart
    When the customer opens the order form
    And the customer enters name "Test User" and credit card ""
    And the customer submits the order
    Then an error message should be shown
