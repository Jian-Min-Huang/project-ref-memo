package org.yfr.finance.core.component.conn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Optional;

@Slf4j
@Component
public class TcpClient {

    @Value("${net.server.host}")
    private String host;

    @Value("${net.server.port}")
    private Integer port;

    private Socket client;

    private BufferedReader input;

    private BufferedOutputStream output;

    @PostConstruct
    void postConstruct() {
        client = new Socket();
        try {
            client.connect(new InetSocketAddress(host, port), 3000);

            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new BufferedOutputStream(client.getOutputStream());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public Optional<String> sendCommand(String command) {
        try {
            output.write(command.getBytes());
            output.flush();

            String readLine = input.readLine();
            return Optional.ofNullable(readLine);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);

            return Optional.empty();
        }
    }

}
