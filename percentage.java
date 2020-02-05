import java.util.Scanner;
import java.util.StringJoiner;

public class Main {
    public static void main(String args[]) {

        int[] percentages = new int[] {90, 80, 70, 55, 50} ;
        String msg = printPercentage(percentages);
        boolean isContinue = true;
        while(isContinue) {

            System.out.println("-------------------------");
            System.out.println(msg);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter previous percentage:");
            int previous = scanner.nextInt();

            System.out.println("Enter current percentage:");
            int current = scanner.nextInt();

            int percentageAlert = 0;
            for (int percentage : percentages) {
                if(current >= percentage && percentage >= previous) {
                    percentageAlert = percentage;
                    break;
                }
            }
            System.out.println(percentageAlert != 0 ? "Alert " + percentageAlert : "No Alert");
        }
    }

    private static String printPercentage(int[] percentages) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (int percentage : percentages) {
            joiner.add(percentage + "");
        }
        return "Percentage Alerts in " + joiner.toString();
    }
}

