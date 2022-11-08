Protocol objectives: what does the protocol do?

communication between client server, 1 client at time, client initiate conversation, when request is over close connection and wait for a new one.
Overall behavior:

What transport protocol do we use?

    TCP, port : 4000
How does the client find the server (addresses and ports)?

    arbitrary values

Who speaks first?

    server gives instruction available

Who closes the connection and when?

    server when client is done with calculs

Messages:

What is the syntax of the messages?

information sent by the server containing the operation available

    BONJOUR

information sent by client containing operation to do

    CALCUL <OPERAND> <OPERATOR> <OPERAND>
information sent by server containing the result in decimal of the CALCUL

    RESULTAT <RESULT>

information sent by the client to stop the conversation

    QUIT

information sent by the server when CALCUL values are incorrect

    ERREUR <MERROR MSG>

What is the sequence of messages exchanged by the client and the server? (flow)
- BONJOUR (serveur)
- CALCUL (client)
- RESULTAT (serveur)
- QUIT (client)


