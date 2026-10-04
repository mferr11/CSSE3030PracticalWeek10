# JobsPortal: Week 10 practical (UI and performance testing)

Starter code for the Week 10 practical. Follow the worksheet for what to do.

## Get the code

```bash
git clone <REPO_URL> CSSE3030PracticalWeek10
cd CSSE3030PracticalWeek10
```

You work on the `main` branch. The `solution` branch holds the reference solution
(`git checkout solution`, and `git checkout main` to go back).

## Requirements

| Requirement | Used for | Where to get it |
|---|---|---|
| Java 17 or newer (`java -version`) | everything | https://adoptium.net |
| Google Chrome | Part 2 (Selenium) | https://www.google.com/chrome |
| Apache JMeter | Part 3 (load testing) | https://jmeter.apache.org/download_jmeter.cgi |

- **Gradle**: nothing to install. Use `./gradlew` (macOS/Linux) or `.\gradlew.bat` (Windows).
- **ChromeDriver**: downloaded automatically by Selenium on the first run, so that run needs network
  access. If the lab machines block it, download the ChromeDriver matching your Chrome version from
  https://googlechromelabs.github.io/chrome-for-testing/, put it on your `PATH`, and tell your tutor.
- **JMeter**: download the current stable **binary** release, unzip it, and add its `bin/` folder to
  your `PATH`. Check with `jmeter --version`.
