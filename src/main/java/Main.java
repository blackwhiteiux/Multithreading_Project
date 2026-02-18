import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {
        DataProcessor dataProcessor = new DataProcessor();

        for(int i = 0; i < 10; i++){
            List<Integer> list = new ArrayList<>();
            Random random = new Random();
            int randomNumber = random.nextInt(10);

            for (int j = 0; j < randomNumber; j++){
                list.add(j);
            }

            dataProcessor.submitTask(list);
        }

        dataProcessor.executeAll();
        dataProcessor.printResult();
        dataProcessor.shutdown();
    }
}
