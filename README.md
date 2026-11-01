# AI-Assisted Selenium Automation Framework

A Java-based web automation framework built using Selenium WebDriver, TestNG, Maven, and the Page Object Model (POM). Selenium MCP and AI-assisted workflows were used to explore application behavior and support test scenario generation.

## Tech Stack

- **Language:** Java
- **Automation:** Selenium WebDriver
- **Test Framework:** TestNG
- **Build Tool:** Maven
- **Design Pattern:** Page Object Model (POM)
- **Browser:** Google Chrome
- **AI Integration:** Selenium MCP with GitHub Copilot
- **CI:** GitHub Actions
- **Application Under Test:** SauceDemo

## Project Structure

```text
AI-Selenium-Automation/
├── .github/
│   └── workflows/
│       └── maven.yml
├── src/
│   ├── main/
│   │   └── java/
│   │       └── Main.java
│   └── test/
│       └── java/
│           ├── base/
│           │   └── BaseTest.java
│           ├── pages/
│           │   ├── LoginPage.java
│           │   ├── InventoryPage.java
│           │   ├── CartPage.java
│           │   └── CheckoutPage.java
│           └── tests/
│               └── SauceDemoTest.java
├── pom.xml
└── README.md
```

## Automated Test Scenarios

The framework currently covers nine scenarios:

| # | Test Scenario |
|---|---|
| 1 | Login with valid credentials |
| 2 | Login with invalid credentials |
| 3 | Login with empty fields |
| 4 | Verify inventory products |
| 5 | Add a product to cart |
| 6 | Verify shopping cart badge |
| 7 | Verify cart product details |
| 8 | Remove a product from cart |
| 9 | Navigate to checkout |

## Framework Design

- **BaseTest:** Manages WebDriver initialization and cleanup before and after each test.
- **Page Object Model:** Separates page locators and interactions from test logic.
- **TestNG:** Organizes test scenarios and assertions.
- **Maven:** Manages project dependencies and test execution.
- **Explicit Waits:** Synchronizes browser interactions with page elements.
- **Headless Chrome:** Supports browser execution without a visible UI, including CI environments.

## AI-Assisted Test Development

Selenium MCP was used with GitHub Copilot to:

1. Explore the SauceDemo application through browser automation.
2. Identify page elements, locators, and application workflows.
3. Discover relevant functional test scenarios.
4. Assist in generating Java Selenium test code.
5. Organize the generated tests using POM and TestNG.
6. Review, refine, and validate the implementation through test execution.

The generated code was reviewed and tested independently of the MCP exploration workflow.

## Running the Tests Locally

### Prerequisites

- JDK 17 or later
- Maven 3.10+
- Google Chrome

### Execute tests

Clone the repository:

```bash
git clone https://github.com/shiyashalmali/AI-Selenium-Automation.git
```

Navigate to the project directory:

```bash
cd AI-Selenium-Automation
```

Run the test suite:

```bash
mvn clean test
```

## Continuous Integration

GitHub Actions is configured to execute the Maven test suite automatically on:

- Pushes to the `master` branch
- Pull requests targeting the `master` branch
- Manual workflow dispatch

The workflow checks out the repository, configures Java 17, and executes:

```bash
mvn -B clean test
```

## Application Under Test

SauceDemo: https://www.saucedemo.com/

Public test credentials:

- Username: `standard_user`
- Password: `secret_sauce`

---

**Project Focus:** Java Selenium Automation | AI-Assisted Testing | MCP | TestNG | Maven | Page Object Model | GitHub Actions
