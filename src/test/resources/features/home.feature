Feature: Homepage
  As a Customer
  I want to access the Product Store homepage
  So that I can browse available products

  @smoke @homepage
  Scenario: Homepage loads and shows product list
    Given the user is on the homepage
    Then the homepage should display a list of products
    And each product should show a name, price and thumbnail

  @regression @navigation
  Scenario Outline: Navigate to product detail page
    Given the user is on the homepage
    When the user clicks the product "<product>"
    Then the product detail page should show the product name "<product>"
    And the product price should be visible
    And the "Add to Cart" button should be displayed

    Examples:
      | product            |
      | Samsung galaxy s6  |
      | Nokia lumia 1520   |
      | Sony vaio i5       |

  @regression @ui
  Scenario: Verify homepage navigation menu
    Given the user is on the homepage
    Then the navigation menu should display the following options:
      | Home   |
      | Contact|
      | Cart   |
      | Login  |

  @regression @ui
  Scenario: Verify homepage slider
    Given the user is on the homepage
    Then the homepage slider should display 3 images

  @regression @categories
  Scenario: Verify categories section
    Given the user is on the homepage
    Then the categories section should display:
      | Phones   |
      | Laptops  |
      | Monitors |
