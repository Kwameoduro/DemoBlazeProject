package com.ecommerce.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;




import static io.cucumber.junit.platform.engine.Constants.*;

// JUnit 5 Suite annotation
@Suite
@IncludeEngines("cucumber") // specify the Cucumber engine
@SelectClasspathResource("features") // path to your feature files
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.ecommerce.steps,com.ecommerce.hooks")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, json:target/cucumber.json, junit:target/cucumber.xml, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")

public class CucumberTestRunner {
    // Empty class — configuration is handled via annotations
}
