import java.util.List;
import java.util.concurrent.Callable;

public class CalculateSumTask implements Callable<Integer> {
    private static final int SIMULATION_DELAY_MS = 200; // Константа вместо магического числа
    private final String taskName;
    private final List<Integer> taskNumbers;

    public CalculateSumTask(String taskName, List<Integer> taskNumbers){
        this.taskName = taskName;
        this.taskNumbers = taskNumbers;
    }

    @Override
    public Integer call() {
        System.out.println("Номер задачи: " + taskName + " | Номер потока: " + Thread.currentThread().getName());

        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Задача была прервана", e);
        }

        return taskNumbers.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public String getTaskName(){
        return taskName;
    }
}