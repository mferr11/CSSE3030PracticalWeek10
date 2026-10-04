package jobsportal;

import java.util.List;
import java.util.Optional;

public class JobRepository {

    private static final String BNE = "Brisbane";
    private static final String SYD = "Sydney";
    private static final String MEL = "Melbourne";

    private final List<Job> jobs = List.of(
            job(1, "Data Engineer", BNE),
            job(2, "Data Analyst", BNE),
            job(3, "Software Engineer", BNE),
            job(4, "Java Developer", BNE),
            job(5, "Project Manager", BNE),
            job(6, "UX Designer", BNE),
            job(7, "QA Tester", BNE),
            job(8, "DevOps Engineer", BNE),
            job(9, "Business Analyst", BNE),
            job(10, "Data Engineer", SYD),
            job(11, "Data Analyst", SYD),
            job(12, "Software Engineer", SYD),
            job(13, "Frontend Developer", SYD),
            job(14, "Product Manager", SYD),
            job(15, "Graphic Designer", SYD),
            job(16, "Test Automation Engineer", SYD),
            job(17, "Machine Learning Engineer", SYD),
            job(18, "Data Scientist", SYD),
            job(19, "Data Engineer", MEL),
            job(20, "Data Analyst", MEL),
            job(21, "Software Engineer", MEL),
            job(22, "Backend Developer", MEL),
            job(23, "Engineering Manager", MEL),
            job(24, "Product Designer", MEL),
            job(25, "Performance Tester", MEL),
            job(26, "Cloud Engineer", MEL),
            job(27, "Security Analyst", MEL),
            job(28, "Mobile Developer", BNE),
            job(29, "Data Platform Manager", SYD),
            job(30, "Systems Analyst", BNE));

    private static Job job(int id, String title, String location) {
        return new Job(id, title, location,
                "We are hiring a " + title + " to join our team in " + location + ".");
    }

    public List<Job> all() {
        return jobs;
    }

    public Optional<Job> findById(int id) {
        return jobs.stream().filter(j -> j.id() == id).findFirst();
    }

    /**
     * Required behaviour: titles containing the keyword (case-insensitive) AND location equal
     * to the location; a blank keyword or location means "do not filter on that field".
     */
    public List<Job> search(String keyword, String location) {
        String k = keyword == null ? "" : keyword.trim().toLowerCase();
        String l = location == null ? "" : location.trim();
        boolean hasKeyword = !k.isEmpty();
        boolean hasLocation = !l.isEmpty();

        return jobs.stream()
                .filter(j -> !hasKeyword || j.title().toLowerCase().contains(k))
                .filter(j -> (hasKeyword && hasLocation) || !hasLocation || j.location().equalsIgnoreCase(l))
                .toList();
    }
}
