# Non-Standard Calculator Protocol
## Overhall behavior
This protocol serves to transmit calculation requests and results between client and server.
It operates via **TCP/IP** on **port 1111**.
Client-side has to specifiy the server's hostname.
### Communication
A client can send a calculus request without asking any permission.
The server then either refuses the request or treats the request and sends a result.
### Connection
Since the client sends requests, it opens the connection and the server closes it since there is no other communication between the two.
## Messages
Messages uses UTF-8.
A client can send a request with the following syntax :
- "REQUEST:*calculus*"

**calculus** content :
- Floating points must be '.' and not ','
- Can contain any number of spaces or tabulations which will be ignored.
- Can contain any number of operations
- Can't contain more than one calculus
- Must follow order of operations as specified here : https://en.wikipedia.org/wiki/Order_of_operations

The server then can send multiple outcomes :
- "REFUSED" meaning he doesn't/can't accept the client's request.
- "UNSUPPORTED:*unsupported operations*\nSUPPORTED:*supported operations*" : meaning the *calculus* contains an operation that the server doesn't implement.
    - Here, the server specifies all unsupported operations found in the request to the client as well as all operations that the server supports.
    - All supported and unsupported operations are split by a ';' character and can be commented between parentheses.
    - Multiple characters shall be considered as one operation.
    - If *characters* are followed by a parenthese, it shall be considered as a function and marked as "*characters*()" in the unsupported operations.
    - *i* and/or *j* characters can be considered as sqrt(-1) in case a server supports operation on complexes.
- "RESULT:*calculs*=*value*" : meaning the server was able to compute all operations and return a result value.
- "ERROR:*problem*" : meaning the server encountered a problem while computing the result.
    - *problem* is a message specifying the encountered problem.
    - An example of *problem* can be : "Can't divide by 0".