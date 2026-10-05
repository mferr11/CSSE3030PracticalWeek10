# CSSE3030 Week 10 Practical: UI and Performance Testing

Selenium performs UI tests and JMeter performs load tests, using the `JobsPortal` app as the subject.

The search filter contains a deliberate bug (`src/main/java/jobsportal/JobRepository.java`).
The point of the exercise is to write tests that catch it yourself.

## Requirements

- JDK 17 or newer
- Google Chrome (internet on your first Selenium run, to fetch the driver)
- JMeter 5.6.3 with `bin` on your `PATH` (`jmeter --version`)


## Running

| Command | Runs |
|---|---|
| `./gradlew build -x test` | Check your setup (must end with `BUILD SUCCESSFUL`) |
| `./gradlew testQ2` | Part 2: Selenium tests (rerun with `./gradlew cleanTestQ2 testQ2`) |
| `./gradlew run` | Starts the app on port 8080 for Part 3 |
| `jmeter -t jmeter/search-load.jmx` | Part 3: open the load test plan (run from the repo root) |

Do not run `./gradlew run` while running `testQ2`: both use port 8080.

```bash
git clone https://github.com/mferr11/CSSE3030PracticalWeek10.git
```
