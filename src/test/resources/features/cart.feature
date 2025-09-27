Feature: Shopping Cart
  As a customer
  I want to manage products in my cart
  So that I can review or remove items before purchase

  Background:
    Given the customer is on the homepage
    And the customer adds "Samsung galaxy s6" to the cart
    And the customer navigates to the Cart page

  Scenario: View products in cart
    Then the cart table should be visible
    And the total price should be visible
    And the Place Order button should be visible

  Scenario: Delete a product from cart
    When the customer deletes the first product
    Then the cart should be empty
