# costi proposal
### Overall behavior

#### Transpor protocol
TCP 

#### Addresse and Port
PORT 420

#### First responder
client speaks first asking for a connection while server awaits

#### Last responder
client ends connection when he is done

## Message

#### Syntax
"commande" "n1" "OPERATION" "n2"

#### Flow
clients asks for a computation server responds with result or error message

#### Error handling
when message recived it analyse if its formed correctly(syntax and operator)

## Sepcifications

#### Suported Operations
ADD MULT SUB

#### Error Handling flow
bad syntax ask again.
not supported OPERATION ask again.

## Extensibility
add mor operation / % sqrt pow

#### Examples
<commande> 2 ADD 3
RESULT 5

<commande> 2 nevinv 3
UNSUPPORTED OP, SUPORTED OP ARE <list Op>

nfjeailjfela
BAD SYNTAX,syntax is of form <command> <n1> <OP> <n2>
