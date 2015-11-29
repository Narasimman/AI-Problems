To Compile: go to AI-Prog1 folder
javac -cp .:lib/* src/PS1/*.java

To Run:
 java -cp ./src/:lib/* PS1/MainDriver 5 100 
 
 or
 
 java -cp ./src/:lib/* PS1/MainDriver 5 100 verbose
 
 
 Program takes three arguments
 N - number of tasks
 E - number of experiments
 verbose - to print verbose output for each serach process
 
Verbose output sample:
Result 96
Search Output: [5764] 20 10
Is search successful: true
Total Number of states in the tree: 8969
Total Number of frontier states during searching: 24
------------------------
------------------------
Result 97
Search Output: [67] 14 4
Is search successful: true
Total Number of states in the tree: 109601
Total Number of frontier states during searching: 14


Statistics output sample:
Total Number of Successful searches: 96
Total Number of searches: 100
Fraction of successful searches(%): 96.0
Min | Max | Avg number of states in State Space Tree: 413 | 109601 | 52340.0
Min | Max | Avg number of FRONTIER states in search process: 7 | 156 | 26.14

 
 
 