# Swag Labs Mobile Test Automation Framework

Appium-based mobile test automation framework for the **Swag Labs** Android app using **Java**, **TestNG**, and the **Page Object Model** design pattern.

## Tech Stack

- **Language:** Java 17
- **Build Tool:** Maven
- **Test Framework:** TestNG 7.9
- **Automation:** Appium Java Client 9.x (UiAutomator2)
- **Design Pattern:** Page Object Model (POM)
- **Reporting:** Allure Reports
- **CI/CD:** GitHub Actions
- **Cloud Platforms:** LambdaTest / BrowserStack

## Project Structure

```
swag-labs-mobile-automation/
├── pom.xml                                    # Maven build configuration
├── .github/workflows/mobile-tests.yml         # CI/CD pipeline
├── app/                                       # APK file
├── src/main/java/com/swaglabs/
│   ├── config/ConfigReader.java               # Configuration loader
│   ├── driver/DriverManager.java              # ThreadLocal driver management
│   ├── pages/                                 # Page Object classes
│   │   ├── BasePage.java                      # Common interactions
│   │   ├── LoginPage.java                     # Login screen
│   │   ├── ProductsPage.java                  # Product listing
│   │   ├── CartPage.java                      # Shopping cart
│   │   ├── CheckoutInfoPage.java              # Checkout info form
│   │   ├── CheckoutOverviewPage.java          # Order review
│   │   ├── CheckoutCompletePage.java          # Order confirmation
│   │   └── MenuPage.java                      # Side menu
│   ├── utils/                                 # Utility classes
│   │   ├── WaitHelper.java                    # Explicit waits
│   │   ├── ScreenshotUtil.java                # Screenshot capture
│   │   └── OrientationHelper.java             # Portrait/Landscape
│   └── listeners/                             # TestNG listeners
│       ├── TestListener.java                  # Test lifecycle events
│       └── RetryAnalyzer.java                 # Auto-retry failed tests
├── src/test/java/com/swaglabs/tests/
│   ├── BaseTest.java                          # Test setup/teardown
│   ├── LoginTest.java                         # Login test scenarios
│   ├── ProductBrowseAndCartTest.java          # Browse & cart tests
│   ├── CheckoutFlowTest.java                  # Checkout E2E tests
│   └── MenuValidationTest.java               # Menu validation tests
└── src/test/resources/
    ├── config.properties                      # Local config
    ├── config-lambdatest.properties           # LambdaTest config
    ├── config-browserstack.properties         # BrowserStack config
    ├── testng-portrait.xml                    # Portrait test suite
    └── testng-landscape.xml                   # Landscape test suite
```

## Test Scenarios

| Test Class | Scenario | ID |
|---|---|---|
| `LoginTest` | App launch & login page verification | TC_LOGIN_001 |
| `LoginTest` | Login with valid credentials (standard_user) | TC_LOGIN_002 |
| `LoginTest` | Login with invalid credentials | TC_LOGIN_003 |
| `LoginTest` | Login with empty username | TC_LOGIN_004 |
| `LoginTest` | Login with empty password | TC_LOGIN_005 |
| `ProductBrowseAndCartTest` | Products page displays products | TC_PRODUCT_001 |
| `ProductBrowseAndCartTest` | Browse and verify product titles | TC_PRODUCT_002 |
| `ProductBrowseAndCartTest` | Add single product to cart | TC_PRODUCT_003 |
| `ProductBrowseAndCartTest` | Add multiple products to cart | TC_PRODUCT_004 |
| `ProductBrowseAndCartTest` | Verify cart items match selection | TC_PRODUCT_005 |
| `ProductBrowseAndCartTest` | Proceed to checkout from cart | TC_PRODUCT_006 |
| `CheckoutFlowTest` | Fill checkout info and proceed | TC_CHECKOUT_001 |
| `CheckoutFlowTest` | Verify checkout overview details | TC_CHECKOUT_002 |
| `CheckoutFlowTest` | Complete E2E checkout & validate order | TC_CHECKOUT_003 |
| `CheckoutFlowTest` | Navigate back to home after order | TC_CHECKOUT_004 |
| `CheckoutFlowTest` | Checkout with empty info shows error | TC_CHECKOUT_005 |
| `MenuValidationTest` | Validate all menu options displayed | TC_MENU_001 |
| `MenuValidationTest` | Validate menu options collectively | TC_MENU_002 |
| `MenuValidationTest` | Close menu returns to products | TC_MENU_003 |
| `MenuValidationTest` | All Items navigates to products | TC_MENU_004 |
| `MenuValidationTest` | Logout returns to login page | TC_MENU_005 |
| `MenuValidationTest` | Menu validation after E2E flow | TC_MENU_006 |

**Total: 22 test cases** covering all 8 assignment scenarios.

## Prerequisites

1. **Java 17+**: `java -version`
2. **Maven 3.8+**: `mvn -version`
3. **Node.js 18+**: `node -v`
4. **Appium 2.x**: `npm install -g appium`
5. **UiAutomator2 Driver**: `appium driver install uiautomator2`
6. **Android SDK** with an emulator configured
7. **Swag Labs APK**: Place in the `app/` directory

## Quick Start

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/swag-labs-mobile-automation.git
cd swag-labs-mobile-automation
```

### 2. Download the Swag Labs APK

```bash
curl -L -o app/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk \
  https://github.com/saucelabs/sample-app-mobile/releases/download/2.7.1/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk
```

### 3. Start Android Emulator

```bash
emulator -avd YOUR_AVD_NAME
```

### 4. Start Appium Server

```bash
appium --address 127.0.0.1 --port 4723 --relaxed-security
```

### 5. Run Tests

```bash
# Portrait mode (default)
mvn clean test

# Landscape mode
mvn clean test -Plandscape

# Both modes
mvn clean test -Pportrait && mvn test -Plandscape
```

## Running on Cloud Platforms

### LambdaTest

```bash
# Upload APK first, then run with LambdaTest profile
mvn clean test -Pportrait,lambdatest \
  -Dlt.username=YOUR_USERNAME \
  -Dlt.access.key=YOUR_ACCESS_KEY \
  -Dapp.url=lt://YOUR_APP_ID
```

### BrowserStack

```bash
# Upload APK first, then run with BrowserStack profile
mvn clean test -Pportrait,browserstack \
  -Dbs.username=YOUR_USERNAME \
  -Dbs.access.key=YOUR_ACCESS_KEY \
  -Dapp.url=bs://YOUR_APP_ID
```

## Allure Reports

```bash
# Generate and open Allure report
mvn allure:serve

# Or generate only
mvn allure:report
# Report at: target/site/allure-maven-plugin/index.html
```

## CI/CD Pipeline

The GitHub Actions pipeline (`.github/workflows/mobile-tests.yml`) is configured to:

1. **Trigger** automatically on every push to `main`/`develop` or on pull requests
2. **Run** the full test suite in both **portrait** and **landscape** modes on an Android emulator
3. **Generate** Allure test report and deploy to GitHub Pages
4. **Send email** with:
   - Total tests executed
   - Number of passed and failed tests
   - Links to screenshots/logs for failed tests
   - Summary of retries or flaky tests

### Required GitHub Secrets for Email

| Secret | Description |
|---|---|
| `SMTP_SERVER` | SMTP server address (e.g., `smtp.gmail.com`) |
| `SMTP_PORT` | SMTP port (e.g., `587`) |
| `SMTP_USERNAME` | SMTP username/email |
| `SMTP_PASSWORD` | SMTP password/app password |
| `REPORT_EMAIL_TO` | Recipient email address |

### Manual Trigger

You can manually trigger the workflow via GitHub Actions UI with custom parameters:
- **orientation**: `portrait`, `landscape`, or `both`
- **platform**: `local`, `lambdatest`, or `browserstack`

## Key Design Decisions

- **Page Object Model**: Each screen is a separate class extending `BasePage`
- **ThreadLocal Driver**: Supports parallel test execution
- **Fluent API**: Page methods return `this` for method chaining
- **RetryAnalyzer**: Auto-retries failed tests up to 2 times to handle flakiness
- **Allure @Step**: Each page method is annotated for detailed step-by-step reporting
- **Orientation Support**: Two TestNG XML suites + Maven profiles for portrait/landscape
- **Config Abstraction**: Properties-based config with system property overrides

## Credentials

| User | Password |
|---|---|
| `standard_user` | `secret_sauce` |

## Author

Swag Labs Mobile Automation Framework
