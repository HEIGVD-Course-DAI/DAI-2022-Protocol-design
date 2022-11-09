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
The server, when an error occurs
The client, when he is done


TODO
Finished here because an indivual spec would take hours, I don't see how we are supposed
to complete it in 20 minutes.

Started the common spec

## Messages
### What is the syntax of the messages?
### What is the sequence of messages exchanged by the client and the server? (flow)
### What happens when a message is received from the other party? (semantics)

## Specific elements (if useful)
### Supported operations
### Error handling
### Extensibility

## Examples: examples of some typical dialogs.