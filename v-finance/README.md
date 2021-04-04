# v-finance
v-finance

# day kd strategy
```
1. k > 40 -> +1, k < 40 -> d /, >thres -> -1 or goto 2
                           d -, -1
                           d \, -1
2. k > 40 -> +1, k > 70 -> d /, k > 80 -> -1 or k < 70 -> -1 
                           d -, -1
                           d \, -1
3. k < 70 -> -1, k > 70 -> d /, +1 
                           d -, +1
                           d \, >thres -> +1 or goto 4
4. k < 70 -> -1, k < 40 -> d /, +1
                           d -, +1
                           d \, k < 30 -> +1 or k > 40 +1
```  

# day macd strategy
  

# hour kd strategy

# hour ma strategy
  * 穿越 ma -> +-1
  * 同方向漲跌 n 次 -> -+1
  * 反向穿越 ma -> -+1
  
* 多空頭排列
* https://tw.stock.yahoo.com/us/worldidx.php
* kd交錯
* 5ma 20ma交錯

# gradle build unix
```sh
./gradlew finance-core:build -x test
./gradlew finance-task:build -x test
./gradlew finance-service:build -x test
./gradlew finance-web:build -x test
```

# gradle build windows
```sh
gradlew.bat finance-core:build -x test
gradlew.bat finance-task:build -x test
gradlew.bat finance-service:build -x test
gradlew.bat finance-web:build -x test
```

# java run project
```sh
java -jar finance-task/build/libs/finance-task-0.0.1.jar
java -jar finance-service/build/libs/finance-service-0.0.1.jar
java -jar finance-web/build/libs/finance-web-0.0.1.jar
```
