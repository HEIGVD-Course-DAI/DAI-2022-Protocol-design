* Protocol objectives: what does the protocol do?<br>
    This protocol will be used to realise operations (addition, multiplication, ...) between two numbers
* Overall behavior:
    * What transport protocol do we use?<br>
        TCP with UTF-8 encoding
    * How does the client find the server (addresses and ports)?<br>
        The client will be given the ip address and the port by the server admin
    * Who speaks first?<br>
        The client will speak to the server first
    * Who closes the connection and when?<br>
        The server will close the connexion but the client can also initialize the close through a message
* Messages:
    * What is the syntax of the messages?<br>
        For the operation: "❔ operator(emoji) number number"<br>
        For the list of operations: "📃 operation1(emoji), operation2(emoji), ..., operationN(emoji)"
        For the result: "🟰 number"<br>
        For the heartbeat (keep alive each 10sec / server closes the connexion after 3 missing heartbeat): "💓"<br>
        For the connexion close: "❌"<br>
        For the connexion start: "💡"<br>
        For the errors: "💥 message"<br>

        All messages are ended by "🔚"
    * What is the sequence of messages exchanged by the client and the server? (flow)<br>
        client -> server : connexion start<br>
        server -> client : list of operation<br>
        client -> server : operation<br>
        server -> client : result<br>
        ...<br>
        client -> server : connexion close<br>
    * What happens when a message is received from the other party? (semantics)<br>
        We check the first character of the message and handle the message based on this emoji and the message will be ended by a specific emoji
* Specific elements (if useful)
    * Supported operations<br>
        ➕, ✖️
    * Error handling<br>
        Send "error" message with a text explaining the error
    * Extensibility<br>
        Add other operations
* Examples: examples of some typical dialogs.<br>
client -> server : 💡🔚<br>
server -> client : 📃 ➕, ✖️🔚<br>
client -> server : ❔ ➕ 1 2🔚<br>
client -> server : 💓<br>
server -> client : 🟰 3🔚<br>
client -> server : ❌🔚<br>