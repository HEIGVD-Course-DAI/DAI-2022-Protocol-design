# Specification - Lab 03

## Overall behavior
Transport protocol : TCP
How does the client find the server  ? Global variable or config file describing server ip adresse and port

Who speaks first ?  Client, trying to connect to server.
Who closes the connection and when . Also the client when he finish computing what he needs.

### Sequence / flow

1) client -> server :  Open connexion and trying to establish connexion
2) server -> client : acknowledge connexion
3) client -> server : send data to perform an operation
4) server -> client : send back the result

## Messages

### Request format (client -> server)
The request is formatted in a single string as follow:
```
[<code> <data>]
```
The code and data field are separated by a space character (U+0020)

For the moment, only 3 codes are used int the protocol:

COMPUTE :    Data field contains the dijkstra formatted calculus to be resolved.

DISCONNECT :     Data field is ignored by the server.


### Response format (server -> client)
The response is formatted in a single string as follow:
```
[<status> <response_type> <data>]
```
#### Status codes :

OK : The request is valid and has been processed successfully

ERROR : The request is invalid

#### Response type for each response :

For status code OK:

INFO : 

RESULT :

For ststus code error:

DEFINITION_DOMAIN : For example, division by 0 or sqrt of a negative number

SYNTAX : The syntax of the request is ill-formed (for example a parenthesis is 
missing)

#### Data field
The data field contains the detail of the corresponding response_type field

### Syntax of requests
The request
([number a] [operation] [number b])

### Supported operations:
1) addition (*)
2) substraction (-)
3) multiplication (*)
4) division (/)
5) modulus (%)
6) power (^)
7) Sqare root (#)

### Supported numbers
Integer or floating point numbers

### Operator precedence
To ensure the operator recedence, the operations are delimited with parenthesis

### Exemples of client requests
(1+2)

(1+((2*8)/3))


### Semantics
1) open a TCP  connection on a specific ip / port
2) if the server seen an incoming connection, recieve it properly and response with a "successfully connected" message.
3) Client send a message containing all neded informations to perform an operation
4) the server send back a message containing the result of the operation and maybe a flag to tell us if the operation was successfull or not
## Specific element
