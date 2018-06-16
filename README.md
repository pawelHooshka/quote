# quote
Simple loan quoting program

In order to run this program you only have to run the jar file like so: java -jar quote.jar market.csv 1000
where quote.jar is the name of the jar, "market.csv" is the path to the csv file containing lenders data and the last argument (1000, in this case) is the requested amount of the loan.

The compiled jar and the example market.csv file can be found under directory "archive" of this project. In order to be able to see correctly displayed currency sign on the command line, please run the program in the following manner: java -Dfile.encoding=IBM850 -jar quote.jar market.csv 1000 if the result of the chcp command on you console is Active code page: 850.

The project was written, compiled and should be run with JDK-1.8.
