Feature: User Sign in flow

  @RegressionWorking
  Scenario Outline: Login into the Application using invalid email and phone number.
    Given I open Browser
    And   I go to URL
    Then  I verify the Login Page - 'Cloud Carbon Footprint'
    And   I click on Login button
    Then  I verify user is navigated to 'Sign in' page
    When  I enter '<EmailOrPhone>' email address credentials in Sign in Page
    And   I click on Next Button in Sign in page
    Then  I verify the username incorrect validation message: "<ValidationMessage>"

    Examples:
      | EmailOrPhone | ValidationMessage                                           |
      | 1232         | 1232 isn't in our system. Make sure you typed it correctly. |
      | test         | Enter a valid email address or phone number.                |
      | 111          | Enter a valid email address or phone number.                |

  @RegressionWorking
  Scenario: Login into the Application with incorrect user name
    Given I open Browser
    And   I go to URL
    Then  I verify the Login Page - 'Cloud Carbon Footprint'
    And   I click on Login button
    Then  I verify user is navigated to 'Sign in' page
    When  I enter 'fatima.zahara1@appnetwise.com' email address credentials in Sign in Page
    And   I click on Next Button in Sign in page
    Then  I verify the username incorrect validation message: 'This username may be incorrect. Make sure you typed it correctly. Otherwise, contact your admin.'

  @RegressionWorking
  Scenario: Login into the Application with incorrect password
    Given I open Browser
    And   I go to URL
    Then  I verify the Login Page - 'Cloud Carbon Footprint'
    And   I click on Login button
    Then  I verify user is navigated to 'Sign in' page
    When  I enter 'fatima.zahara@appnetwise.com' email address credentials in Sign in Page
    And   I click on Next Button in Sign in page
    When  I enter 'Zebronic@1' password credentials in Sign in Page
    And   I click on Sign in button
    Then  I verify the password incorrect validation message: "Your account or password is incorrect. If you don't remember your password, reset it now."

  @RegressionWorking
  Scenario: Login into the Application using valid email and password
    Given I open Browser
    And   I go to URL
    Then  I verify the Login Page - 'Cloud Carbon Footprint'
    And   I click on Login button
    Then  I verify user is navigated to 'Sign in' page
    When  I enter '' email address credentials in Sign in Page
    And   I click on Next Button in Sign in page
    When  I enter '' password credentials in Sign in Page
    And   I click on Sign in button
    Then  I verify the 'Stay signed in?' message
    And   I select the Don't show this again checkbox option
    Then  I click on the No button