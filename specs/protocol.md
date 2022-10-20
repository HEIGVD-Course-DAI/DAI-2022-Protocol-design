## Protocol objectives
Specify a client-server protocol, which will allow a client to ask a server to compute a calculation and to return the result.

## Overall behavior
### What transport protocol do we use?
TCP
### How does the client find the server (addresses and ports)?
With a standard ipv4 address, and port 4433
### Who speaks first?
The client, because he is the only one to know the other
### Who closes the connection and when?
The server, as soon as he sends an answer


## Messages
### What is the syntax of the messages?
#### Request
The data is sent as a formatted String

The syntax is <1stOperand> <Operation> <2cdOperand>

The 1st and second Operand are composed of a potentially decimal number

The Operation is one of the following : + - * /

#### Answer
The data is sent as a formatted String

The String just contains a potentially decimal number

### What is the sequence of messages exchanged by the client and the server? (flow)
The client sends a Request, the server sends an Answer
![](.\communication flow.svg)

### What happens when a message is received from the other party? (semantics)
#### Server
Parses the message, does the calculation, returns an Answer with the result

#### Client
Parses the Answer

## Specific elements (if useful)
### Supported operations
* \+
* \-
* \*
* \/

### Error handling
The server drops the connection without sending an Answer in case of an error

The client should handle the error on it's own, and restart the procedure if appropriate


### Extensibility
It's easy to add operators

Adding parenthesis would require changing the syntax of sent messages

## Examples: examples of some typical dialogs.
Client : 1 + 2

Server : 3