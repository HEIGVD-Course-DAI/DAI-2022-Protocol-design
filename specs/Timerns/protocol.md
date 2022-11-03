* Overall behavior:
  * What transport protocol do we use?

    TCP
  * How does the client find the server (addresses and ports)?
    
    By ginving it a fix ip address and port.
  * Who speaks first?

    The client
  * Who closes the connection and when?

    the client when he close the application or the server if the client does not send any more massages.
* Messages:
  * What is the syntax of the messages?
    The mesasage are send in utf8 little-endian.
    
  * What is the sequence of messages exchanged by the client and the server? (flow)

    client send: a hello package "HELLO" to start the connection

    

    client send: a opperation package "operation code , op1, op2"

    
  * What happens when a message is received from the other party? (semantics)

    server send: a responce "HELLO BACK" to send that the connection is initiated

    server send: the serponce package "client package, res"
* Specific elements (if useful)
  * Supported operations

   "+ *"
  * Error handling

    connection init error, server send "BAD REQUEST"

    operation error, server send "UNKNOWN OPERATION"

    operation error, server send "BAD FORMAT"

    operation error, server send "BAD PARAMETER"
  * Extensibility
* Examples: examples of some typical dialogs.