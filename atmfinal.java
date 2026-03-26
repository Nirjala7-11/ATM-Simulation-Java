import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Account
{
    private int accNo;
    private int pin;
    private long balance;
    private ArrayList<String> transactions;

    Account(int accNo, int pin, long balance)
    {
        this.accNo = accNo;
        this.pin = pin;
        this.balance = balance;
        transactions = new ArrayList<>();
    }

    public int getAccNo()
    {
        return accNo;
    }

    public int getPin()
    {
        return pin;
    }

    public long getBalance()
    {
        return balance;
    }

    public void setBalance(long balance)
    {
        this.balance = balance;
    }

    public void setPin(int newPin)
    {
        this.pin = newPin;
    }

    public void addTransaction(String type, int amount)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String time = LocalDateTime.now().format(formatter);

        String entry = type + " : ₹" + amount + " | " + time;
        transactions.add(entry);

        if(transactions.size() > 10)
        {
            transactions.remove(0);
        }
    }

    public ArrayList<String> getTransactions()
    {
        return transactions;
    }
}

class Bank
{
    private ArrayList<Account> accounts;
    private int nextAccNo = 1004;

    Bank()
    {
        accounts = new ArrayList<>();

        accounts.add(new Account(1001,1234,50000));
        accounts.add(new Account(1002,4567,120000));
        accounts.add(new Account(1003,7890,75000));
    }

    Account login(int accNo, int pin)
    {
        for(Account acc : accounts)
        {
            if(acc.getAccNo() == accNo && acc.getPin() == pin)
                return acc;
        }
        return null;
    }

    public boolean accExists(int accNo)
    {
        for(Account acc : accounts)
        {
            if(acc.getAccNo() == accNo)
                return true;
        }
        return false;
    }

    public void createAccount(int pin, long balance)
    {
        int accNo = nextAccNo++;

        accounts.add(new Account(accNo, pin, balance));
        System.out.println("Account created successfully!");
        System.out.println("Your Account Number: " + accNo);
    }
}

class ATM
{
    private final int notes[] = {100,50,20,10,5,2,1};

    void withdraw(Account acc,int amt)
    {
        if(amt == 0)
        {
            System.out.println("Invalid amount");
            return;
        }

        if(amt % 10 != 0)
        {
            System.out.println("Enter amount in multiples of 10");
            return;
        }

        if(amt > acc.getBalance())
        {
            System.out.println("Insufficient balance");
            return;
        }

        System.out.println("Processing...");

        acc.setBalance(acc.getBalance() - amt);

        int temp = amt;

        System.out.println("Dispensing:");

        for(int i=0;i<notes.length;i++)
        {
            int count = temp / notes[i];
            temp %= notes[i];

            if(count>0)
                System.out.println(notes[i] + " x " + count);
        }

        acc.addTransaction("Withdrawal", amt);
        System.out.println("Withdrawal successful!");
    }

    void deposit(Account acc,int amt)
    {
        if(amt<=0)
        {
            System.out.println("Invalid amount");
            return;
        }

        System.out.println("Processing...");

        acc.setBalance(acc.getBalance() + amt);

        int temp = amt;
        System.out.println("Depositing:");

        for(int i=0;i<notes.length;i++)
        {
            int count = temp / notes[i];
            temp %= notes[i];

            if(count>0)
                System.out.println(notes[i] + " x " + count);
        }

        acc.addTransaction("Deposit", amt);
        System.out.println("Deposit successful!");
    }

    void showBalance(Account acc)
    {
        System.out.println("Balance: ₹" + acc.getBalance());
    }

    void miniStatement(Account acc)
    {
        System.out.println("\n------ MINI STATEMENT ------");

        ArrayList<String> list = acc.getTransactions();

        if(list.size() == 0)
        {
            System.out.println("No transactions yet");
        }
        else
        {
            int start = Math.max(0, list.size() - 5);

            for(int i = start; i < list.size(); i++)
            {
                System.out.println(list.get(i));
            }
        }

        System.out.println("Current Balance: ₹" + acc.getBalance());
        System.out.println("-----------------------------");
    }
}

class Main
{
    public static void main(String args[])
    {
        Scanner input = new Scanner(System.in);

        Bank bank = new Bank();
        ATM atm = new ATM();
        Account user = null;

        while(true)
        {
            System.out.println("\n1] Login");
            System.out.println("2] Create Account");
            System.out.println("3] Exit");

            System.out.print("Enter choice: ");
            int option = input.nextInt();

            if(option == 1)
            {
                System.out.print("Enter Account Number: ");
                int accNo = input.nextInt();

                System.out.print("Enter PIN: ");
                int pin = input.nextInt();

                user = bank.login(accNo, pin);

                if(user != null)
                {
                    System.out.println("Login Successful!");
                    System.out.println("Welcome! Account NO: " + user.getAccNo());
                    break;
                }
                else
                {
                    System.out.println("Invalid Account Number or PIN!");
                }

            }

            else if(option == 2)
            {
                System.out.print("Set PIN: ");
                int newPin = input.nextInt();

                System.out.print("Enter initial balance: ");
                long newBal = input.nextLong();

                bank.createAccount(newPin, newBal);
            }

            else if(option == 3)
            {
                System.out.println("Thank you!");
                System.exit(0);
            }

            else
            {
                System.out.println("Invalid choice!");
            }
        }

        while(true)
        {
            System.out.println("\n============================");
            System.out.println("=== WELCOME TO ABC BANK! ===");
            System.out.println("Account No: " + user.getAccNo());

            System.out.println("\n1) Withdraw");
            System.out.println("2) Deposit");
            System.out.println("3) Balance");
            System.out.println("4) Mini Statement");
            System.out.println("5) Change PIN");
            System.out.println("6) Exit");

            System.out.print("\nEnter your Choice: ");
            int ch = input.nextInt();

            switch(ch)
            {
                case 1:
                    System.out.print("Enter Amount: ");
                    atm.withdraw(user,input.nextInt());
                    break;

                case 2:
                    System.out.print("Enter Amount: ");
                    atm.deposit(user,input.nextInt());
                    break;

                case 3:
                    atm.showBalance(user);
                    break;

                case 4:
                    atm.miniStatement(user);
                    break;

                case 5:
                    System.out.print("Enter current PIN: ");
                    int oldPin = input.nextInt();

                    if(user.getPin() != oldPin)
                    {
                        System.out.println("Incorrect PIN!");
                    }
                    else
                    {
                        System.out.print("Enter new PIN: ");
                        int newPin = input.nextInt();
                        user.setPin(newPin);
                        System.out.println("PIN changed successfully!");
                    }
                    break;

                case 6:
                    System.out.println("Thank you for using ABC Bank ATM!");
                    input.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}