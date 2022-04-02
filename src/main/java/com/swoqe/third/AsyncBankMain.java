package com.swoqe.third;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

/**
 author Cay Horstmann
 */
public class AsyncBankMain {
    public static final int NACCOUNTS = 10;
    public static final int INITIAL_BALANCE = 10000;

    public static void main(String[] args) {
        Bank b = new Bank(NACCOUNTS, INITIAL_BALANCE);
        for (int i = 0; i < NACCOUNTS; i++) {
            TransferThread t = new TransferThread(b, i, INITIAL_BALANCE);
            t.setPriority(Thread.NORM_PRIORITY + i % 2);
            t.start() ;
        }
    }
}

class Bank {

    public static final int NTEST = 100000;
    private final Account[] accounts;
//    private final AtomicIntegerArray accounts;
    private long ntransacts = 0;

    public Bank(int n, int initialBalance) {
        accounts = new Account[n];
        Arrays.fill(accounts, new Account(initialBalance));
    }

    public void transfer(int from, int to, int amount) {
        accounts[from].reentrantLock.lock();
        accounts[to].reentrantLock.lock();

        accounts[from].setAccount(accounts[from].getAccount() - amount);
        accounts[to].setAccount(accounts[to].getAccount() + amount);
        ntransacts++;
        if (ntransacts % NTEST == 0) test();

        accounts[from].reentrantLock.unlock();
        accounts[to].reentrantLock.unlock();
    }

    public void test() {
        int sum = 0;
        for (Account account : accounts)
            sum += account.getAccount();
        System.out.println("Transactions:" + ntransacts + " Sum: " + sum);
    }

    public int size() {
        return accounts.length;
    }

    static class Account {
        private long account;
        private final ReentrantLock reentrantLock = new ReentrantLock(true);

        public Account(long account) {
            this.account = account;
        }

        public void setAccount(long account) {
            this.account = account;
        }

        public long getAccount() {
            return account;
        }

    }
}

class TransferThread extends Thread {
    private static final int REPS = 1000;
    private final Bank bank;
    private final int fromAccount;
    private final int maxAmount;

    public TransferThread(Bank b, int from, int max) {
        bank = b;
        fromAccount = from;
        maxAmount = max;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < REPS; i++) {
                int toAccount = (int) (bank.size() * Math.random());
                int amount = (int) (maxAmount * Math.random() / REPS);
                bank.transfer(fromAccount, toAccount, amount);
            }
        }
    }
}