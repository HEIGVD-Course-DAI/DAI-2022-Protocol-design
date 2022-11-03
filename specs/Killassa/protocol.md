# Objectif
Le protocol *Calculation* permet à des clients de réaliser des opérations arithmétiques, le serveur doit être capable d'effectuer des additions, soustractions, multiplication, division. Il peut fournir plus d'opérations. La liste des opérations disponible est fournie lors de de la connexion au serveur.

# Fonctionnement général
Utilisation du protocole TCP.
Port 3333 par défaut.
Lorsque le serveur a accepté la connexion, il propose la liste des opérations. Le client peut ensuite effectuer le nombre d'opérations désiré.

Le client envoie en message une opération (ADD, SUB, MULT, DIV) suivi de 2 opérandes. Si le message est correct le serveur envoie un message de résultat, sinon il envoit un message d'erreur

Si le client a terminé il peut envoyer un message (QUIT) afin de terminer la connexion avec le serveur.

# Syntaxe des messages
On a 5 types de messages, WELCOME, CALCULATE, RESULT, ERROR, QUIT et on les termine par un \n, cela permet d'indiquer la fin de ligne.

## WELCOME
Le client doit lire jusqu'à END lors de la connexion au serveur.

Exemple:
- Available operations:\n
- ADD n1 n2\n
- SUB n1 n2\n
- MULT n1 n2\n
- DIV n1 n2\n
- END\n

## CALCULATE
Ce message permet de soumettre une opération. On commence par le type d'opérations que l'on veut effectuer suivi des 2 opérandes.

Exemple:
- ADD 3 5\n
- SUB 3 5\n
- MULT 3 5\n
- DIV 3 5\n

## RESULT
Ce message indique le résultat de l'opération si le message est correct

Exemple:
- RESULT resultNumber\n

## ERROR
Ce message indique une erreur si le message est incorrect

Exemple:
- ERROR UNKNOWN OPERATION\n

## QUIT
Ce message indique la fin de session

Exemple:
- QUIT\n
