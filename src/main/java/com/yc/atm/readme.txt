银行ATM系统协议设计：

客户端：
DEPOSIT 1 100
WITHDRAW 2 100
BALANCE 1
EXIT
==先读命令... 在读账号  在读金额


响应数据：json对象
jsonModel对象{
    BankAccount对象
}

