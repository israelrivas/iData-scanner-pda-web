<?php
set_time_limit(0);
ob_implicit_flush();

$address = '192.168.1.46';
$port = 8080;
$sock = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
socket_bind($sock, $address, $port) or die('Could not bind to address');
socket_listen($sock);

echo "Server started at ws://$address:$port\n";

$clients = array($sock);

while (true) {
    $changed = $clients;
    socket_select($changed, $null, $null, 0, 10);

    if (in_array($sock, $changed)) {
        $client = socket_accept($sock);
        $clients[] = $client;
        $header = socket_read($client, 1024);
        perform_handshaking($header, $client, $address, $port);
        echo "Client connected\n";
        unset($changed[array_search($sock, $changed)]);
    }

    foreach ($changed as $client) {
        while (@socket_recv($client, $buf, 1024, 0) >= 1) {
            $msg = unmask($buf);
            send_message($msg);
            echo "Message received: $msg\n";
            break 2;
        }

        $buf = @socket_read($client, 1024, PHP_NORMAL_READ);
        if ($buf === false) {
            unset($clients[array_search($client, $clients)]);
            socket_close($client);
            echo "Client disconnected\n";
        }
    }
}

socket_close($sock);

function send_message($msg) {
    global $clients;
    $msg = mask($msg);
    foreach ($clients as $client) {
        @socket_write($client, $msg, strlen($msg));
    }
    return true;
}

function perform_handshaking($received_header, $client_conn, $host, $port) {
    $headers = array();
    $lines = preg_split("/\r\n/", $received_header);
    foreach ($lines as $line) {
        $line = chop($line);
        if (preg_match('/\A(\S+): (.*)\z/', $line, $matches)) {
            $headers[$matches[1]] = $matches[2];
        }
    }

    $secKey = $headers['Sec-WebSocket-Key'];
    $secAccept = base64_encode(pack('H*', sha1($secKey . '258EAFA5-E914-47DA-95CA-C5AB0DC85B11')));
    $upgrade  = "HTTP/1.1 101 Web Socket Protocol Handshake\r\n" .
                "Upgrade: websocket\r\n" .
                "Connection: Upgrade\r\n" .
                "WebSocket-Origin: $host\r\n" .
                "WebSocket-Location: ws://$host:$port/websocket_server.php\r\n" .
                "Sec-WebSocket-Accept: $secAccept\r\n\r\n";
    socket_write($client_conn, $upgrade, strlen($upgrade));
}

function unmask($payload) {
    $length = ord($payload[1]) & 127;
    if ($length == 126) {
        $masks = substr($payload, 4, 4);
        $data = substr($payload, 8);
    } elseif ($length == 127) {
        $masks = substr($payload, 10, 4);
        $data = substr($payload, 14);
    } else {
        $masks = substr($payload, 2, 4);
        $data = substr($payload, 6);
    }

    $text = '';
    for ($i = 0; $i < strlen($data); ++$i) {
        $text .= $data[$i] ^ $masks[$i % 4];
    }

    return $text;
}

function mask($text) {
    $b1 = 0x81;
    $length = strlen($text);

    if ($length <= 125) {
        $header = pack('CC', $b1, $length);
    } elseif ($length > 125 && $length < 65536) {
        $header = pack('CCn', $b1, 126, $length);
    } else {
        $header = pack('CCNN', $b1, 127, ($length >> 32), ($length & 0xFFFFFFFF));
    }

    return $header . $text;
}
