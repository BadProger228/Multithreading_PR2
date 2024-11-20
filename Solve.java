import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Solve {
    private int min = 10; 
    private int max = 50; 
    private int[] array;
    private int numberOfThreads;
    
    public Solve(int numberOfThreads){
        this.numberOfThreads = numberOfThreads;
        int arraySize = new Random().nextInt(21) + 40;
        array = generateArray(arraySize, min, max);
        System.out.print("First array: "); printArray(array);
    }

    private void printArray(int[] array){
        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            if(i == array.length - 1)
                System.out.print(array[i]);
            else
                System.out.print(array[i] + ", ");
        }
        System.out.println("]");
    }
    
    private static int[] generateArray(int size, int min, int max) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(max - min + 1) + min;
        }
        return array;
    }

    public void Start(){
        List<Future<int[]>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads); // Створюється пул потоків, які ждуть задачі

        int chunkSize = (int) Math.ceil(array.length / (double) numberOfThreads);
        long startTime = System.currentTimeMillis(); // Початок заміру часу

        for (int i = 0; i < array.length; i += chunkSize) {
            int start = i;
            int end = Math.min(i + chunkSize, array.length);
            int step = (end - start) / 2;

            Callable<int[]> task = () -> {
                int []result = new int[step];
                int index = 0;
                for (int j = start; j < end - 1; j += 2) {
                    result[index++] = array[j] * array[j + 1];
                }
                System.out.println("Processed chunk from index " + start + " to " + (end - 1));
                return result;
            };

            futures.add(executor.submit(task));
        }

        
        List<Integer> finalResults = new ArrayList<>();
        for (Future<int[]> future : futures) {
            try {
                if (!future.isCancelled()) {
                    while (!future.isDone()) {
                        System.out.println("Waiting for task completion...\n");
                        Thread.sleep(50); // Симуляція перевірки стану
                    }
                    for (int num : future.get()) {
                        finalResults.add(num);
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error occurred: " + e.getMessage());
            }
        }

        executor.shutdown();

        System.out.println("\nPairwise products:" + finalResults);
       

        long endTime = System.currentTimeMillis();
        System.out.println("\n\nExecution time: " + (endTime - startTime) + " ms");
    }

}
