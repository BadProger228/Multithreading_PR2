import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter how many thread you need: ");
        Solve solve = new Solve(s.nextInt());
        solve.Start();
        s.close();
    }
}
