import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DataProcessor {
    private static final Map<String, Integer> map = new ConcurrentHashMap<>();
    private final AtomicInteger activeTasks = new AtomicInteger(0);
    private final ExecutorService service = Executors.newFixedThreadPool(10);
    private final List<Future<Integer>> futures = new ArrayList<>();
    private final List<CalculateSumTask> tasks = new ArrayList<>();

    public synchronized void submitTask(List<Integer> list) {
        CalculateSumTask task = new CalculateSumTask("task " + (tasks.size() + 1), list);
        tasks.add(task);
    }

    public void executeAll() throws InterruptedException {
        for (CalculateSumTask task : tasks) {
            activeTasks.incrementAndGet();

            Future<Integer> future = service.submit(() -> {
                try {
                    return task.call();
                } finally {
                    activeTasks.decrementAndGet();
                }
            });

            futures.add(future);
        }

        while (activeTasks.get() > 0) {
            System.out.println("Ожидание завершения задач. Активных задач: " + activeTasks.get());
            Thread.sleep(100);
        }

        for (int i = 0; i < futures.size(); i++) {
            try {
                int result = futures.get(i).get();
                synchronized (map) {
                    map.put(tasks.get(i).getTaskName(), result);
                }
            } catch (ExecutionException e) {
                System.err.println("Ошибка выполнения задачи: " + e.getMessage());
            }
        }
    }

    public void printResult() {
        synchronized (map) {
            System.out.println("Результаты сложения: ");
            if (map.isEmpty()) {
                System.out.println("Нет результатов");
            } else {
                map.forEach((key, value) -> System.out.println(key + " = " + value));
            }
        }
    }

    public Optional<Integer> resultByName(String value) {
        synchronized (map) {
            return Optional.ofNullable(map.get(value));
        }
    }

    public void shutdown() {
        service.shutdown();
        try {
            if (!service.awaitTermination(5, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
        }
    }
}