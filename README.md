<a href="https://opensource.newrelic.com/oss-category/#new-relic-experimental"><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/newrelic/opensource-website/raw/main/src/images/categories/dark/Experimental.png"><source media="(prefers-color-scheme: light)" srcset="https://github.com/newrelic/opensource-website/raw/main/src/images/categories/Experimental.png"><img alt="New Relic Open Source experimental project banner." src="https://github.com/newrelic/opensource-website/raw/main/src/images/categories/Experimental.png"></picture></a>

# New Relic Java Instrumentation for Tibco Core (JMS & Rendezvous)

Provides instrumentation for Tibco core elements of JMS/EMS and Rendezvous.

## Installation

To install the instrumentation:
1. Download the latest release.    
2. Also download the latest release of Tibco Core instrumentation to provide support for JMS and Rendevous [Tibco-Core-Instrumentation](https://github.com/newrelic-experimental/newrelic-java-tibco-core)
3. In the New Relic Java Agent directory, create a directory named extensions if it does not already exist.
4. Copy the downloaded jar files into the extensions directory
5. Follow the instructions to disable the product JMS instrumentation [Disable JMS Instrumentation](https://github.com/newrelic-experimental/newrelic-java-tibco-core/blob/main/Disable_JMS_Instrumentation.md)
6. Restart the Tibco Instance
## Getting Started
  
## Building

If you make changes to the instrumentation code and need to build the instrumentation jars, follow these steps
1. Set environment variable NEW_RELIC_EXTENSIONS_DIR.  Its value should be the directory where you want to build the jars (i.e. the extensions directory of the Java Agent).   
2. Build one or all of the jars.   
  a. To build one jar, run the command:  gradlew _moduleName_:clean  _moduleName_:install    
  b. To build all jars, run the command: gradlew clean install

## Support

New Relic hosts and moderates an online forum where customers can interact with New Relic employees as well as other customers to get help and share best practices. Like all official New Relic open source projects, there's a related Community topic in the New Relic Explorers Hub. You can find this project's topic/threads here:

## Contributing
New Relic has open-sourced this project. This project is provided AS-IS WITHOUT WARRANTY OR DEDICATED SUPPORT. Issues and contributions should be reported to the project here on GitHub.

We encourage you to bring your experiences and questions to the [Explorers Hub](https://discuss.newrelic.com) where our community members collaborate on solutions and new ideas.

**A note about vulnerabilities**

As noted in our [security policy](../../security/policy), New Relic is committed to the privacy and security of our customers and their data. We believe that providing coordinated disclosure by security researchers and engaging with the security community are important means to achieve our security goals.

If you believe you have found a security vulnerability in this project or any of New Relic's products or websites, we welcome and greatly appreciate you reporting it to New Relic through [HackerOne](https://hackerone.com/newrelic).

## License
New Relic Java Instrumentation for Tibco Core is licensed under the [Apache 2.0](http://apache.org/licenses/LICENSE-2.0.txt) License.

