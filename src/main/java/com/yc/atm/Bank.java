package com.yc.atm;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<BankAccount> accounts = new ArrayList<>();


    public Bank(){
        //初始化一些账号
        for (int i = 0; i < 10; i++) {
            accounts.add(new BankAccount(i,10));
        }
    }


    //查询：无需加锁
    public BankAccount search(int id) throws Exception{
        for (BankAccount account : accounts) {
            if (account.getId()==id){
                return account;
            }
        }
        throw new Exception("没有找到该账号");
    }


    //存款：加锁
    public BankAccount deposit(int id,double money) throws Exception{
        BankAccount ba = search(id);
        synchronized (ba){
            ba.setBalance(ba.getBalance()+money);
            return ba;
        }
    }



    //取款
    public BankAccount withdraw(int id,double money) throws Exception{
        BankAccount ba = search(id);
        synchronized (ba){
            if (ba.getBalance()<money){
                throw new Exception("余额不足");
            }
            ba.setBalance(ba.getBalance()-money);
            return ba;
        }
    }
}
