import java.util.List;
import java.util.concurrent.Callable;

public class CalculateSumTask implements Callable<Integer> {
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
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int result = 0;

        for(Integer list : taskNumbers){
            result += list;
        }
        return result;
    }

    public String getTaskName(){
        return taskName;
    }
}