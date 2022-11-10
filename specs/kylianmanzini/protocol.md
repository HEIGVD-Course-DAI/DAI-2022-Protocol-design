Protocol objectives: what does the protocol do?

The MEGAMATH protocole communicate between a client and a server. Basicly the client sent a string with operations and the server send back the response. This protocole MUST work with +- and COULD work with */ operation.

Overall behavior:
What transport protocol do we use?
    TCP
How does the client find the server (addresses and ports)?
    Ip address and the port 12345
Who speaks first?
    Server
Who closes the connection and when?
    Server, when sending result or error

Messages:
What is the syntax of the messages?
    S : OP + - * / END\n
    C : CALC "Int1OpSymbolInt2" END\n
    S : RESULT num END\n
    S : ERROR errorCode errorTxt END\n
    S : QUIT END\n

What is the sequence of messages exchanged by the client and the server? (flow)
    OP 
    CALC
    RESULT / ERROR
    QUIT

What happens when a message is received from the other party? (semantics)
    result or error if server receive from client

Specific elements (if useful)
Supported operations
    + - * /
Error handling

    S : ERROR errorCode errorTxt END

    ERROR LIST :
    400 - WSYNTAX
    401 - UKNOWNOP
    402 - EXPECTCALC
    601 - DIV0
    602 - INPUT2BIG

Extensibility

Examples: examples of some typical dialogs.

    S : OP + - * END
    C : CALC "3+5" END
    S : RESULT 8 END
    S : QUIT END

    S : OP + - * END
    C : CALC "3/3" END
    S : ERROR 401 UKNOWNOP END
    S : QUIT END

    S : OP + - * / END
    C : CALC "5-8" END
    S : RESULT -3 END
    S : QUIT END

    S : OP + - * / END
    C : RTVDF >retGFDGD
    S : ERROR 400 WSYNTAX END
    S : QUIT END
    
    S : OP + - * / END
    C : CALC "5-8*3" END
    S : ERROR 400 WSYNTAX END
    S : QUIT END