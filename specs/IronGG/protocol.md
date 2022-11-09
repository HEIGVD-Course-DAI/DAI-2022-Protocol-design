##Overall behaviour :
-TCP -> easier to keep à connection and be sure it works

-Client asks for connection
-address -> only 1 server address (how to do multiple connections? Is it even a problem)
-port -> X

Server sends an answer and asks to close connection

What is need in a connection is -> 
1) Client asks for connection
2) Server agrees
3) Client sends info
4) Server answers 

if there's an error -> interrupt connection and redo.
Every connection has to be short

##Messages:
string with the operations?


Specific elements ?
+, -, *, /, (, ), ^x, sqrt(x)