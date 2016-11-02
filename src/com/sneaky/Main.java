package com.sneaky;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Otp opt = new Otp("Good Luck :p", "tsauzeau@gmail.com");
        while (true) {
            System.out.println("otp:" + opt.getCode() + " time: " + opt.getSecondsLeft() + " account: " + opt.getAccount());
            Thread.sleep(1000);
        }
    }
}
