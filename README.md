#  DemoBlaze QA Automation Project

##  Overview
This project is a **full end-to-end QA automation framework** built for the [Demoblaze e-commerce site](https://www.demoblaze.com/).  
It covers **functional, performance, security, and BDD testing**, all integrated into a modern **CI/CD pipeline** with reporting and notifications.  

The project was designed to demonstrate best practices in **automation, DevOps integration, and test management** within a short timeframe (4 days).  

---

## Objectives
- Validate core website functionality through **UI automation**.  
- Ensure business requirements are met using **BDD scenarios**.  
- Measure system responsiveness under load with **performance testing**.  
- Identify vulnerabilities through **security testing**.  
- Deliver a **fully automated pipeline** that runs tests, generates reports, and shares them with the team.  

---

## Tools & Technologies
- **UI Automation**: [Selenide](https://selenide.org/) + Selenium WebDriver  
- **BDD Testing**: [Cucumber](https://cucumber.io/)  
- **Performance Testing**: [Apache JMeter](https://jmeter.apache.org/)  
- **Security Testing**: [OWASP ZAP](https://www.zaproxy.org/)  
- **Pipeline**: GitHub Actions + Docker  
- **Reporting**: Allure Report, Cucumber HTML Report, JMeter HTML Report, ZAP HTML Report  
- **Collaboration**: Slack (notifications), Jira (test case management)  
- **Build Tool**: Maven  

---

## Documentation
The project includes the following QA documentation:  
- **Test Plan** (overall testing approach)  
- **Performance Test Plan** (JMeter load testing strategy)  
- **Test Strategy Document** (methods, scope, techniques)  
- **Test Summary Report** (results and observations)  
- **Security Audit Report** (OWASP ZAP findings)  

---

## CI/CD Pipeline
The pipeline is powered by **GitHub Actions** and **Docker**:  
- Build and set up test environment inside Docker.  
- Run **Selenide + Cucumber** functional tests.  
- Run **JMeter** performance tests.  
- Run **OWASP ZAP** security scan.  
- Generate **Allure, Cucumber, JMeter, and ZAP reports**.  
- Deploy reports to **GitHub Pages**.  
- Send **Slack notification** with report links.  

---

##  Reports
After each run, the following reports are automatically generated:  
-  **Allure Report** (detailed test execution with screenshots & logs)  
-  **Cucumber HTML Report** (BDD scenario results)  
-  **JMeter Report** (performance graphs & metrics)  
-  **ZAP Report** (security vulnerabilities)  

Reports are deployed to **GitHub Pages** for easy access.  
