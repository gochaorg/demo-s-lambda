package org.example;

import org.example.api.OSInfo;
import org.junit.jupiter.api.Test;
import xyz.cofe.trambda.tcp.TcpQuery;

import java.util.ArrayList;

public class TestClient {
    @Test
    public void test01(){
        var tcpQuery = TcpQuery
            // Какой API мы будем использовать
            .create(OSInfo.class)
            // Указываем адрес сервера
            .host("localhost").port(12345)
            // Получаем Query<IEnv>
            .build();

        var processes = tcpQuery.apply( osInfo -> new ArrayList<>(osInfo.processes()) );

        processes.forEach( proc -> {
            System.out.println(proc.getPid()+" "+proc.getName()+" "+proc.getCommandLine());
        });
    }
}
