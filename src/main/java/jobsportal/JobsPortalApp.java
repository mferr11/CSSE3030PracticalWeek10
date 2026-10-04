package jobsportal;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * JobsPortal: a small job search web app used for the Week 10 UI and performance practical.
 *
 * System properties (all optional):
 *   port        HTTP port (default 8080)
 *   demo.delay  true = add a uniform random 100-1500 ms total response time for every /api/search (default false)
 *   workers     search worker threads (default 8)
 *   queue       waiting-request queue size (default 50)
 *   service.ms  simulated work per search in ms (default 100)
 */
public class JobsPortalApp {

    private final JobRepository repository = new JobRepository();
    private final boolean demoDelay;
    private final long serviceMs;
    private final ThreadPoolExecutor workers;
    private Javalin javalin;
    private volatile boolean warmingUp;

    public JobsPortalApp(boolean demoDelay, int workerCount, int queueSize, long serviceMs) {
        this.demoDelay = demoDelay;
        this.serviceMs = serviceMs;
        // Bounded pool + bounded queue: when both are full the task is rejected and we answer 503.
        this.workers = new ThreadPoolExecutor(workerCount, workerCount, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize), new ThreadPoolExecutor.AbortPolicy());
        this.workers.prestartAllCoreThreads();
    }

    public static JobsPortalApp fromSystemProperties() {
        return new JobsPortalApp(
                Boolean.getBoolean("demo.delay"),
                Integer.getInteger("workers", 8),
                Integer.getInteger("queue", 50),
                Long.getLong("service.ms", 100L));
    }

    public JobsPortalApp start(int port) {
        javalin = Javalin.create(config -> {
            config.showJavalinBanner = false;
            config.staticFiles.add("/public");
        });
        javalin.get("/", ctx -> ctx.contentType("text/html; charset=utf-8").result(resource("/public/index.html")));
        javalin.get("/api/search", this::search);
        javalin.get("/jobs/{id}", this::jobDetails);
        javalin.start(port);
        warmUp(port);
        return this;
    }

    /** One throwaway request so the first real search does not pay JVM class-loading cost. */
    private void warmUp(int port) {
        warmingUp = true;
        try {
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            for (String path : List.of("/api/search?q=Data", "/", "/jobs/1")) {
                client.send(java.net.http.HttpRequest.newBuilder(
                                java.net.URI.create("http://localhost:" + port + path))
                                .timeout(java.time.Duration.ofSeconds(5)).build(),
                        java.net.http.HttpResponse.BodyHandlers.discarding());
            }
        } catch (IOException | InterruptedException e) {
            // warm-up is best effort only
        } finally {
            warmingUp = false;
        }
    }

    public void stop() {
        if (javalin != null) {
            javalin.stop();
        }
        workers.shutdownNow();
    }

    private void search(Context ctx) {
        String q = ctx.queryParam("q");
        String location = ctx.queryParam("location");
        if (isBlank(q) && isBlank(location)) {
            ctx.status(HttpStatus.BAD_REQUEST).json(List.of());
            return;
        }

        CompletableFuture<List<Job>> work = new CompletableFuture<>();
        try {
            workers.execute(() -> {
                try {
                    Thread.sleep(serviceMs); // simulated work, not CPU bound
                    work.complete(repository.search(q, location));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    work.completeExceptionally(e);
                }
            });
        } catch (RejectedExecutionException full) {
            ctx.status(HttpStatus.SERVICE_UNAVAILABLE).result("Service Unavailable");
            return;
        }

        CompletableFuture<List<Job>> response = work;
        if (demoDelay && !warmingUp) {
            // TRAP (Part 2, Q2): the total response time is uniform random 100-1500 ms. The simulated
            // work above already took serviceMs, so only the remainder is added here.
            long delayMs = Math.max(0, ThreadLocalRandom.current().nextLong(100, 1501) - serviceMs);
            response = work.thenApplyAsync(jobs -> jobs,
                    CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS));
        }
        CompletableFuture<List<Job>> finalResponse = response;
        ctx.future(() -> finalResponse.thenAccept(ctx::json));
    }

    private void jobDetails(Context ctx) {
        Integer id = null;
        try {
            id = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException ignored) {
            // falls through to 404
        }
        Optional<Job> job = id == null ? Optional.empty() : repository.findById(id);
        if (job.isEmpty()) {
            ctx.status(HttpStatus.NOT_FOUND).contentType("text/html; charset=utf-8")
                    .result("<h1>Job not found</h1><a href=\"/\">Back to search</a>");
            return;
        }
        // TRAP (Part 2, Q4): the Apply button id gets a new random suffix on every page load.
        String applyId = "apply-" + Long.toString(ThreadLocalRandom.current().nextLong(0x10000000L, 0xFFFFFFFFL), 16);
        String html = resource("/public/job.html")
                .replace("{{title}}", escape(job.get().title()))
                .replace("{{location}}", escape(job.get().location()))
                .replace("{{description}}", escape(job.get().description()))
                .replace("{{applyId}}", applyId);
        ctx.contentType("text/html; charset=utf-8").result(html);
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    private static String resource(String path) {
        try (InputStream in = JobsPortalApp.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException("Missing resource " + path);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args) {
        int port = Integer.getInteger("port", 8080);
        JobsPortalApp app = fromSystemProperties().start(port);
        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
        System.out.println("JobsPortal running at http://localhost:" + port
                + " (demo.delay=" + app.demoDelay + ")");
    }
}
