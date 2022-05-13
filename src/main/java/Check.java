public class Check {
    private int checkNum;
    private String date;
    private String payer;
    private double amount;

    public Check(int checkNum, String date, String payer, double amount){
        this.checkNum = checkNum;
        this.date = date;
        this.payer = payer;
        this.amount = amount;
    }

    public Check() {
        this(0, "May 9", "John Doe", 0.0);
    }

    public int getCheckNum() {
        return checkNum;
    }

    public String getDate() {
        return date;
    }

    public String getPayer() {
        return payer;
    }

    public double getAmount() {
        return amount;
    }


    @Override
    public String toString() {
        return "Check Number: " + checkNum + " (Date: " + date + ", Payer: " + payer + ", Amount: " + amount + ")\n";
    }
}