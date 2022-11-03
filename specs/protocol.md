# Protocol specification

## Protocol objectives

## Overall behavior
### Transport protocol
TCP protocol

### Adresses and ports
Adresses : **localhost**  
Port: 420

### Who speaks first?
Client sends a request to the server.

### Who closes the connection and when?
Client closes the connection when he is done.  
Server closes if idle timeout reached.

## Messages
### Message syntax
`polish operation number1 number2\n` (inverse polish notation)  
OR  
`basic number1 operation number2\n` (basic notation)

### Message sequence (flow)
- Server starts and wait for a request.
- Client connects to the server.
- Server sends a welcome message with indications.
- Client sends a request with the syntax above.
- Server computes the requested operation and sends the result (or error) as a response.
- Server closes the connection when client asks or if idle timeout reached.

### Message semantics
- `Error response` (ex : operation not supported, format not correct)
- `Result response` (request `=` response)

## Specific elements
### Supported operations
* Add (+)
* Multiply (*)

### Error handling
- `Operation not supported` if operation not listed above.
- `Format error` if request format does not match the syntax above.
- `Overflow` if sum/multiplication leads to an overflow.

### Extensibility
- Adding operations (-, /, %, **).
- Error handling for important operations (such as divide by 0).
- Error handling for new operations.
- Adding decimal numbers or complex numbers.

## Examples

Client : `+ 1 2`  
Server : `3`

Client : `- 1 2`  
Server : `Error: operation not supported`

Client : `4 + 5`  
Server : `9`

Client : `4 , e3`
Server : `Error: format error`