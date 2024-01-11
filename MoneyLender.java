import java.util.Scanner;

public class MoneyLender {
    private double loanAmount;
    private double interestRate;
    private double loanTerm;
    private String paymentFrequency;
    private double lumpSumPayment;
    private double additionalMonthlyPayment;

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void setLoanTerm(double loanTerm) {
        this.loanTerm = loanTerm;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public void setLumpSumPayment(double lumpSumPayment) {
        this.lumpSumPayment = lumpSumPayment;
    }

    public void setAdditionalMonthlyPayment(double additionalMonthlyPayment) {
        this.additionalMonthlyPayment = additionalMonthlyPayment;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getLoanTerm() {
        return loanTerm;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public double getLumpSumPayment() {
        return lumpSumPayment;
    }

    public double getAdditionalMonthlyPayment() {
        return additionalMonthlyPayment;
    }

    public void calculateLoan() {
        double moneyAvailable = loanAmount - lumpSumPayment;

        if (moneyAvailable <= 0) {
            System.out.println("You do not need a loan.");
            return;
        }

        double monthlyInterestRate = interestRate / 100 / 12;

        double monthlyPayment;
        if (paymentFrequency.equals("m")) {
            monthlyPayment = moneyAvailable * (monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -loanTerm * 12)));
        } else if (paymentFrequency.equals("w")) {
            monthlyPayment = moneyAvailable * (monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -loanTerm * 52 / 12)));
            monthlyPayment *= 4;
        } else {
            monthlyPayment = moneyAvailable * (monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -loanTerm * 26 / 12)));
            monthlyPayment *= 2;
        }

        if (lumpSumPayment > 0) {
            System.out.printf("Lump sum payment of $%.2f applied.%n", lumpSumPayment);
        }

        if (additionalMonthlyPayment > 0) {
            System.out.printf("Additional monthly payment of $%.2f applied.%n", additionalMonthlyPayment);
        }

        double newMonthlyPayment;
        if (paymentFrequency.equals("m")) {
            newMonthlyPayment = (loanAmount - lumpSumPayment) / loanTerm + additionalMonthlyPayment;
        } else if (paymentFrequency.equals("w")) {
            newMonthlyPayment = (loanAmount - lumpSumPayment) * (1 + interestRate / 100 / 52);
            newMonthlyPayment = newMonthlyPayment / 4 + additionalMonthlyPayment;
        } else {
            newMonthlyPayment = (loanAmount - lumpSumPayment) * (1 + interestRate / 100 / 26);
            newMonthlyPayment = newMonthlyPayment / 2 + additionalMonthlyPayment;
        }

        double newTotalInterest = loanAmount - moneyAvailable;

        System.out.printf("Loan amount: $%.2f%n", loanAmount);
        System.out.printf("Payment frequency: %%s%n", paymentFrequency.equals("m") ? "Monthly" : paymentFrequency.equals("w") ? "Weekly" : "Bi-weekly");
        System.out.printf("Loan term: %.2f years%n", loanTerm);
        System.out.printf("Interest rate: %.2f%%%n", interestRate);
        System.out.printf("Total interest paid: $%.2f%n", newTotalInterest);
        double totalPayment = 0;
        double totalInterestPaid = 0;
        double remainingBalance = loanAmount;
    
        System.out.println("\nAmortization Schedule:");
        System.out.println("-----------------------");
        System.out.println("Payment#\tPayment\t\tInterest\tPrincipal\tBalance");
    
        for (int i = 1; i <= loanTerm * 12; i++) {
            double monthlyInterest = remainingBalance * monthlyInterestRate;
            double monthlyPrincipal = monthlyPayment - monthlyInterest;
    
            if (i == loanTerm * 12 && remainingBalance > 0) {
                monthlyPayment += remainingBalance;
                monthlyPrincipal = remainingBalance;
            }
    
            if (i % (paymentFrequency.equals("w") ? 4 : paymentFrequency.equals("b") ? 2 : 1) == 0) {
                monthlyPayment += additionalMonthlyPayment;
            }
    
            if (monthlyPayment > remainingBalance + monthlyInterest) {
                monthlyPayment = remainingBalance + monthlyInterest;
                monthlyPrincipal = remainingBalance;
            }
    
            remainingBalance -= monthlyPrincipal;
            totalPayment += monthlyPayment;
            totalInterestPaid += monthlyInterest;
    
            System.out.printf("%d\t\t$%.2f\t\t$%.2f\t\t$%.2f\t\t$%.2f%n", i, monthlyPayment, monthlyInterest, monthlyPrincipal, remainingBalance);
    
            if (remainingBalance <= 0) {
                break;
            }
        }
    
        System.out.printf("%nTotal payment: $%.2f%n", totalPayment);
        System.out.printf("Total interest paid: $%.2f%n", totalInterestPaid);
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
    
        MoneyLender moneyLender = new MoneyLender();
    
        System.out.println("Welcome to the Money Lender Calculator!");
    
        System.out.print("Please enter the loan amount: ");
        double loanAmount = scanner.nextDouble();
        moneyLender.setLoanAmount(loanAmount);
    
        System.out.print("Please enter the interest rate (%): ");
        double interestRate = scanner.nextDouble();
        moneyLender.setInterestRate(interestRate);
    
        System.out.print("Please enter the loan term (years): ");
        double loanTerm = scanner.nextDouble();
        moneyLender.setLoanTerm(loanTerm);
    
        System.out.print("Please enter the payment frequency (m, w, or b): ");
        String paymentFrequency = scanner.next();
        moneyLender.setPaymentFrequency(paymentFrequency);
    
        System.out.print("Please enter any lump sum payment (if applicable): ");
        double lumpSumPayment = scanner.nextDouble();
        moneyLender.setLumpSumPayment(lumpSumPayment);
    
        System.out.print("Please enter any additional monthly payment (if applicable): ");
        double additionalMonthlyPayment = scanner.nextDouble();
        moneyLender.setAdditionalMonthlyPayment(additionalMonthlyPayment);
    
        System.out.println();
    
        moneyLender.calculateLoan();
    }
}    
