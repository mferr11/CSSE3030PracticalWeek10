# JobsPortal: Week 10 practical (UI and performance testing)

A small Java 17 web app (Javalin, Gradle) for the Week 10 practical. You will write Selenium UI
tests (Part 2) and a JMeter load test (Part 3) against it.

## 1. Get the code

```bash
git clone <REPO_URL> CSSE3030PracticalWeek10
cd CSSE3030PracticalWeek10
git branch        # should show: * main
```

Work on `main`. The `solution` branch holds reference solutions; do not look at it until you have
had a proper attempt:

```bash
git checkout solution     # view the reference solution
git checkout main         # go back to the starter
```

If git refuses to switch branches because you have uncommitted work, save it first:
`git add -A && git commit -m "my work"`.

## 2. Requirements

- Java 17 or newer (`java -version`).
- Google Chrome installed (Part 2).
- Network access on the first Selenium run: Selenium Manager downloads the matching ChromeDriver
  automatically.
  - If the lab machines block that download, download the ChromeDriver that matches your Chrome
    version (https://googlechromelabs.github.io/chrome-for-testing/), put `chromedriver` on your
    `PATH`, and rerun. Tell your tutor if neither works.
- Apache JMeter (Part 3), see below.

Use `./gradlew` on macOS/Linux and `gradlew.bat` on Windows (PowerShell or cmd: `.\gradlew.bat`).

## 3. The app

```bash
./gradlew run
```

Then open http://localhost:8080. Stop it with Ctrl+C. Use this for manual browsing and for the
JMeter load test (Part 3). In this mode there is **no** artificial response delay.

Search API (used by the page's JavaScript):
`GET /api/search?q=<keyword>&location=<location>` returns JSON.

## 4. Part 2: Selenium tests

```bash
./gradlew testQ2
```

- The test classes start the app themselves on port 8080, so **do not** run `./gradlew run` at the
  same time (stop it first, or you will get "Address already in use").
- Tests run in headless Chrome. Test files: `src/test/java/jobsportal/JobSearchTest.java` and
  `FlakySearchTest.java`.
- Gradle skips a test task when nothing has changed. To force a rerun use:

```bash
./gradlew cleanTestQ2 testQ2
```

- To run one class: `./gradlew cleanTestQ2 testQ2 --tests '*FlakySearchTest*'`
  (on Windows PowerShell, keep the single quotes).
- The HTML report is at `build/reports/tests/testQ2/index.html`.
- Note: `keywordSearchShowsMatchingJobs` starts as a skeleton that deliberately fails
  (`fail("Not implemented yet")`) until you complete it. The other stubs are empty and pass until you
  fill them in.

## 5. Part 3: JMeter

Install JMeter (needs a Java runtime): download the current stable **binary** release from
https://jmeter.apache.org/download_jmeter.cgi, unzip it, and add its `bin/` folder to your `PATH`.
Check with `jmeter --version`.

Start the app in one terminal (`./gradlew run`) and leave it running. Then, in another terminal:

```bash
jmeter -t jmeter/search-load.jmx            # opens the GUI to build the test plan
```

`jmeter/search-load.jmx` contains only a Thread Group (threads from `${__P(threads,20)}`, 20 s
ramp-up, 60 s duration). You add the HTTP Request, CSV Data Set Config (`jmeter/keywords.csv`,
variable name `keyword`), Response Assertion and Uniform Random Timer.

Run it without the GUI (the results folder given to `-o` must not exist or must be empty, so use a
new folder for every run):

```bash
jmeter -n -t jmeter/search-load.jmx -Jthreads=20 -l results-20.jtl -e -o report-20
```

Open `report-20/index.html` for the dashboard.
