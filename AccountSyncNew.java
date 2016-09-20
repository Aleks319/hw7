package lesson7.hw;
/*
Решить задачу про банк с помощью чего-нибудь
из java.util.concurrent.* Не через Atomic
 */
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Account {
    private int money;

    public Account(int money) {
        this.money = money;
    }

    public int get() {
        return money;
    }

    public void set(int money) {
        this.money = money;
    }
}

class Transaction extends Thread {
    private Account account;
    private int withdraw;
    private Lock lock;

    public Transaction(Account account, int withdraw) {
        this.account = account;
        this.withdraw = withdraw;
        this.lock = new ReentrantLock();
    }

    public void run() {
        try {
            Thread.sleep(System.currentTimeMillis() % 50);
        } catch (InterruptedException e) {}

        try {
                lock.lock();
                int total = account.get();
                if (total >= withdraw)
                    account.set(total - withdraw);

        } finally {
            lock.unlock();
        }
    }
}

public class AccountSyncNew {
    public static void main(String[] args) {
        Account acc = new Account(1000);
        System.out.println("Total: " + acc.get());
        Transaction[] transactions = {
                new Transaction(acc, 100),
                new Transaction(acc, 500),
                new Transaction(acc, 200),
                new Transaction(acc, 50),
                new Transaction(acc, 150)
        };

        for (Transaction t : transactions)
            t.start();

        for (Transaction t : transactions) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Total: " + acc.get());
    }
}