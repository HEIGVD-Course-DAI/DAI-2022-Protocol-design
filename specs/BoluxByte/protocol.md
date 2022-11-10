### Protocol objectives: what does the protocol do?
It allows a client to ask a server to perform a calculation and return the result. 

### Overall behavior:
#### What transport protocol do we use?
It uses TCP
#### How does the client find the server (addresses and ports)?
It is given to the client.
#### Who speaks first?
The client speaks first
#### Who closes the connection and when?
The client closes the connection when it doesn't want to communicate anymore.
### Messages:
#### What is the syntax of the messages?
WELCOME: The server sends this message to the client when the connection is established.
It contains the following information:
- The welcome message
- The list of supported operations

CALC: The client sends an operation containing the operand and two integers to the server, each seperated by a space.

RES: The server sends the result of the calculation to the client.

ERR: The servers gives an error message to the client if the CALC message is not valid.

CLOSE: The client closes the connection.

Example of an exchange:
```
SERVER: WELCOME: AVAILABLE OPERATIONS: ADD, SUB, MUL, DIV
CLIENT: CALC: ADD 2 3
SERVER: RES: 5
CLIENT: CLOSE
```
#### What is the sequence of messages exchanged by the client and the server? (flow)
1) Following the connection from the client to the server, the server sends a message
   with a list of the possible operations.

2) The client then sends a message with an operation.
3) Two possible outcomes:
   1) The server sends a message with the result of the operation.
   2) The server sends a message with an error.
4) The client closes the connection.

### Specific elements (if useful)
#### Supported operations
For now the supported operations are:
- ADD
- SUB
- MUL
- DIV






  