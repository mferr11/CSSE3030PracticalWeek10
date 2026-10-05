# JobsPortal: Week 10 practical (UI and performance testing)

Starter code for the Week 10 practical. Follow the worksheet for what to do.

## Get the code

```bash
git clone https://github.com/mferr11/CSSE3030PracticalWeek10.git
cd CSSE3030PracticalWeek10
```

You work on the `main` branch. The `solution` branch is for tutors.

## Requirements

| Requirement | Used for | Where to get it |
|---|---|---|
| JDK 17 or newer (21 tested; `java -version`) | everything | https://adoptium.net |
| Git | cloning the repo | https://git-scm.com |
| Google Chrome | Part 2 (Selenium) | https://www.google.com/chrome |
| Apache JMeter 5.6.x, with `bin` on your `PATH` (`jmeter --version`) | Part 3 (load testing) | https://jmeter.apache.org/download_jmeter.cgi |

- **Gradle**: not needed. Use the wrapper: `./gradlew` (PowerShell, Linux, macOS) or `gradlew.bat` (Command Prompt).
- **ChromeDriver**: Selenium Manager downloads it on the first run, so that run needs internet access.
- **Linux/macOS**: if you get `Permission denied`, run `chmod +x gradlew`, and `chmod +x <jmeter>/bin/jmeter` for JMeter.

Check your setup:

```bash
./gradlew -q javaToolchains     # must list a JDK 17 or newer
./gradlew build -x test         # must end with BUILD SUCCESSFUL
```

## Part 2: Selenium tests

The search filter lives in `src/main/java/jobsportal/JobRepository.java`.

```bash
./gradlew testQ2                # run the tests
./gradlew cleanTestQ2 testQ2    # rerun (forces the tests to run again)
```

Do not run `./gradlew run` at the same time: both use port 8080, and the tests start the app themselves.

## Part 3: JMeter load tests

Start the app in one terminal and leave it running:

```bash
./gradlew run
```

GUI, from the repo root (the `WARN StatusConsoleListener` lines are harmless):

```bash
jmeter -t jmeter/search-load.jmx
```

Non-GUI runs, from the `jmeter` folder:

```bash
cd jmeter
jmeter -n -t search-load.jmx -Jthreads=20 -l results-20.jtl -e -o report-20
```

`-o` needs a new or empty folder, so delete the old report folder (and `.jtl` file) before re-running.
