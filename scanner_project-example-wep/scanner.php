<?php
$host = 'localhost';
$port = 8080;

$server = stream_socket_server("tcp://$host:$port", $errno, $errorMessage);

if ($server === false) {
    throw new UnexpectedValueException("Could not bind to socket: $errorMessage");
}

for (;;) {
    $client = @stream_socket_accept($server);

    if ($client) {
        $request = fread($client, 1024);
        preg_match('/GET (.*) HTTP/', $request, $matches);
        $path = $matches[1];

        if ($path == '/scan') {
            $data = urldecode(file_get_contents("php://input"));
            file_put_contents('scan.txt', $data);
            fwrite($client, "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\nOK");
        } else {
            fwrite($client, "HTTP/1.1 404 Not Found\r\n\r\n");
        }
        fclose($client);
    }
}
