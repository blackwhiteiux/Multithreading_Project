import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DataProcessor {
    private final AtomicInteger counterTasks = new AtomicInteger(0);
    private final AtomicInteger activeTasks = new AtomicInteger(0);
    private final ExecutorService service = Executors.newFixedThreadPool(10);
    private static final Map<String, Integer> map = new HashMap<>();
    private final List<CalculateSumTask> tasks = new ArrayList<>();

    public void submitTask(List<Integer> list){
        CalculateSumTask task = new CalculateSumTask("task " + counterTasks.incrementAndGet(), list);
        tasks.add(task);
    }

    public void executeAll() throws InterruptedException, ExecutionException {
        activeTasks.set(tasks.size());

        List<Future<Integer>> futures = service.invokeAll(tasks);

        for (int i = 0; i < futures.size(); i++){
            activeTasks.decrementAndGet();
            System.out.println("Количество активных задач: " + activeTasks.get());

            int result = futures.get(i).get();
            synchronized (map){
                map.put(tasks.get(i).getTaskName(), result);
            }

            Optional<Integer> resultByName = resultByName(tasks.get(i).getTaskName());
            System.out.println("Результат задачи: " + tasks.get(i).getTaskName() + " : " + resultByName);
        }
    }

    public void printResult(){
        synchronized (map){
            System.out.println("Результаты сложения: ");
            map.forEach((key, value) -> System.out.println(key + " = " + value));
        }
    }

    public static Optional<Integer> resultByName(String value){
        return Optional.ofNullable(map.get(value));
    }

    public void shutdown(){
        service.shutdown();
    }
}