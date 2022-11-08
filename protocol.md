## costi
TCP 
PORT 6969
client speaks first asking for a connection while server awaits
client ends connection when he is done

<commande> <n1> <OPERATION> <n2>

clients asks for a computation server responds with result or error message

when message recived it analyse if its formed correctly(syntax and operator)

+ - *

bad syntax ask again

not supported OPERATION ask again

add mor operation / % sqrt pow

<commande> 2 ADD 3
RESULT 5

<commande> 2 nevinv 3
UNSUPPORTED OP, SUPORTED OP ARE <list Op>

nfjeailjfela
BAD SYNTAX,syntax is of form <command> <n1> <OP> <n2>

## cedric