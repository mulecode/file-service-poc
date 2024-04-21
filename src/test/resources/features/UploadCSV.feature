Feature: As a customer I would like to upload a CSV file, so that I can have it processed by the system

  Scenario: Upload CSV file
    Given A CSV file "data/entry_valid.txt"
    When I process the file from ip address "200.0.0.0"
    Then I should receive 200 status code
    And The response body should be valid with schema "./data/schema/OutcomeV1Response.json"
    And A record with idAddress "200.0.0.0" should be saved in the database

  Scenario: Upload Empty CSV file
    Given A CSV file "data/entry_invalid_empty.txt"
    When I process the file from ip address "200.0.0.1"
    Then I should receive 400 status code
    And The response body should be valid with schema "./data/schema/ErrorV1Response.json"
    And A record with idAddress "200.0.0.1" should be saved in the database

  Scenario: Upload Empty CSV from unauthorized IP
    Given A CSV file "data/entry_valid.txt"
    When I process the file from ip address "403.0.0.0"
    Then I should receive 403 status code
    And The response body should be valid with schema "./data/schema/ErrorV1Response.json"
    And A record with idAddress "403.0.0.0" should be saved in the database
