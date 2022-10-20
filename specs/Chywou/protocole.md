Protocol objectives: what does the protocol do?
permettre les opérations d'addition, de soustraction, de multiplication et de division
Overall behavior:

What transport protocol do we use?
TCP
How does the client find the server (addresses and ports)?
* Via son adresse et son port (port xxxx)
Who speaks first?
* Le serveur donne les indications supportée au client
Who closes the connection and when?
* Le client indique qu'il n'a plus besoin de faire de calcul.

Messages:
What is the syntax of the messages?
BONJOUR
* Information sur les opération et leur syntaxe envoyé par le serveur
CALCUL
* Le calcul envoyé par le client
RESULTAT
* Le résultat par rapport au calcul du clien
ERREUR
* En cas d'erreur lors de la demande de calcul
FIN
* Fin demandée par le client

What is the sequence of messages exchanged by the client and the server? (flow)
* s: BONJOUR
* c: CALCUL
* s: RESULTAT
* c: CALCUL (supposition : calcul faux)
* s: ERREUR
* c: FIN
CALCUl est reappelable autant de fois que le client le souhaite jusqu'à l'utilisation de FIN

What happens when a message is received from the other party? (semantics)
Il détecte le type de messages et y répond en fonction
Specific elements (if useful)

Supported operations
Error handling
Extensibility

Examples: examples of some typical dialogs.