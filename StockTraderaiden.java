import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StockTraderaiden {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("zigzag.txt"));
        String symbol = in.next();
        int n = in.nextInt();

        int[] years = new int[n];
        int[] months = new int[n];
        double[] prices = new double[n];
        loadData(in, years, months, prices);
        printData(years, months, prices);
        
        System.out.println(symbol + " lowest: $" + prices[indexOfLowest(prices)] + " at " + monthName(months[indexOfLowest(prices)]) + " " + years[indexOfLowest(prices)]);
        System.out.println(symbol + " Highest: $" + prices[indexOfHighest(prices)] + " at " + monthName(months[indexOfHighest(prices)]) + " " + years[indexOfHighest(prices)]);

        double result = simulate(years, months, prices);
        System.out.printf("Final value: $%,.2f%n", result);
    }

    public static void loadData(Scanner in, int[] years, int[] months, double[] prices) {
        for (int i = 0; i < years.length; i++) {
            years[i] = in.nextInt();
            months[i] = in.nextInt();
            prices[i] = in.nextDouble();
        }
    }

    public static String monthName(int month) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return months[month - 1];
    }

    public static void printData(int[] years, int[] months, double[] prices) {
        for (int i = 0; i < years.length; i++) {
            System.out.printf("%s,%s: $%.2f", years[i], monthName(months[i]), prices[i]);
            System.out.println("");
        }
    }

    public static int indexOfLowest(double[] prices) {
        double lowestprice = 10000000;
        int dof = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < lowestprice) {
                lowestprice = prices[i];
                dof = i;
            }
        }
        return dof;
    }

    public static int indexOfHighest(double[] prices) {
        double highestprice = 0;
        int dof = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] > highestprice) {
                highestprice = prices[i];
                dof = i;
            }
        }
        return dof;
    }

    public static final double START_CASH = 10000.0;
    public static final double FEE = 10.0;

    public static double simulate(int[] years, int[] months, double[] prices) {
        double cash = START_CASH;
        int shares = 0;
        double boughtAt = 0.0;

        for (int i = 0; i < prices.length; i++) {
            double[] history = Arrays.copyOf(prices, i + 1);
            String action = decide(history, shares > 0, boughtAt);

            if (action.equals("BUY") && shares == 0) {
                int canBuy = (int) ((cash - FEE) / prices[i]);
                if (canBuy > 0) {
                    shares = canBuy;
                    cash = cash - shares * prices[i] - FEE;
                    boughtAt = prices[i];
                    System.out.printf("%s %d: BUY %d shares at $%.2f%n",
                            monthName(months[i]), years[i], shares, prices[i]);
                }
            } else if (action.equals("SELL") && shares > 0) {
                cash = cash + shares * prices[i] - FEE;
                System.out.printf("%s %d: SELL %d shares at $%.2f%n",
                        monthName(months[i]), years[i], shares, prices[i]);
                shares = 0;
                boughtAt = 0.0;
            }
        }
        return cash + shares * prices[prices.length - 1];
    }

    public static String buyAndHold(double[] history, boolean holding, double boughtAt) {
        return "BUY";
    }

    public static String dipBuyer(double[] history, boolean holding, double boughtAt) {
        if (history.length<3){
            return "HOLD";
        }
        
        if(!holding){
            int last=history.length-1;
            if (history[last-2] > history[last-1]&&history[last - 1]>history[last]) {
                return "BUY";
            }
        }
        if (holding==true){
			for (int i=0; i<history.length; i++){
				if (boughtAt<=(history[i]*90)/100){
					return "SELL";
				}
			}
		}
        return "HOLD";
    }
    
    public static String movingAverage(double[] history, boolean holding, double boughtAt){
		if (history.length<12){
            return "HOLD";
        }
		     

        for (int i=history.length-12; i<history.length; i++){
			double average=0;
			average=(average+history[i])/i;
			if (!holding){
				if (history[i]>average){
					return "BUY";
				}
			}
			if (holding==true){
				if (history[i]<average){
					return "SELL";
				}
			}
}
return "HOLD";
}


 public static String myStrategy(double[] history, boolean holding, double boughtAt) {

    if (history.length==1){
        return "BUY";
    }

    double average=0;
    for (int i=0; i<history.length; i++) {
        average=average+history[i];
}
    
 average=average/history.length;
    
    double currentPrice=history[history.length-1];
    if (!holding) {
        if (currentPrice<average) {
            return "BUY";
        }
}
    
    if (holding) {
        if (currentPrice>average) {
            return "SELL";
        }
    }

    return "HOLD";
}





    public static String decide(double[] history, boolean holding, double boughtAt) {
        return myStrategy(history, holding, boughtAt);
    }

}


