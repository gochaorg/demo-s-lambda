package org.example;

import org.example.api.OSInfo;
import xyz.cofe.trambda.sec.SecurityFilters;
import xyz.cofe.trambda.tcp.TcpServer;
import xyz.cofe.trambda.tcp.TrEvent;
import xyz.cofe.trambda.tcp.TrListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.regex.Pattern;

public class ServerApp {
    public static void main(String[] args0){
        ServerApp app = new ServerApp();
        List<String> args = new ArrayList<String>(Arrays.asList(args0));
        String state = "init";
        boolean autoStart = true;
        boolean interactive = false;
        while (!args.isEmpty()){
            String arg = args.remove(0);
            if( arg==null )continue;
            switch (state) {
                case "init":
                    switch (arg) {
                        case "-port":
                            state = "-port";
                            break;
                        case "-soTimeout":
                            state = "-soTimeout";
                            break;
                        case "-daemon":
                            state = "-daemon";
                            break;
                        case "-interactive":
                            state = "-interactive";
                            break;
                        case "-autoStart":
                            state = "-autoStart";
                            break;
                    }
                    break;
                case "-port":
                    state = "init";
                    app.port = Integer.parseInt(arg);
                    break;
                case "-soTimeout":
                    state = "init";
                    app.soTimeout = Integer.parseInt(arg);
                    break;
                case "-daemon":
                    state = "init";
                    app.daemon = Boolean.parseBoolean(arg);
                    break;
                case "-interactive":
                    state = "init";
                    interactive = Boolean.parseBoolean(arg);
                    break;
                case "-autoStart":
                    state = "init";
                    autoStart = Boolean.parseBoolean(arg);
                    break;
                default:
                    break;
            }
        }

        if( autoStart )app.start();
        if( interactive )app.interactive();
    }

    private int port = 12345;
    private int soTimeout = 1000*5;
    private boolean daemon = false;
    private boolean autoClose = true;
    private TcpServer<OSInfo> server;

    private Pattern setParams = Pattern.compile("(?is)set +(?<key>port|soTimeout|daemon) +(?<value>.+)");

    private void interactive(){
        Runnable prompt = () -> System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        while (true){
            try {
                prompt.run();
                String line = scanner.nextLine();
                if( line.trim().toLowerCase().equals("exit") ){
                    break;
                }else {
                    var mKeyValue = setParams.matcher(line);
                    if( mKeyValue.matches() ){
                        var key = mKeyValue.group("key");
                        switch ( key.toLowerCase() ){
                            case "port":
                                port = Integer.parseInt(mKeyValue.group("value"));
                                continue;
                            case "sotimeout":
                                soTimeout = Integer.parseInt(mKeyValue.group("value"));
                                continue;
                            case "daemon":
                                daemon = Boolean.parseBoolean(mKeyValue.group("value"));
                                continue;
                        }
                    }else {
                        switch (line.trim().toLowerCase()){
                            case "start":
                                start();
                                break;
                            default:
                                break;
                        }
                    }
                }
            } catch ( NoSuchElementException | IllegalStateException ex ){
                break;
            }
        }

        if( autoClose && server!=null ){
            System.out.println("shutdown server");
            server.shutdown();
        }
    }

    private void start(){
        System.out.println("starting server with service OSInfo");

        try {
            System.out.println("  on port "+port);
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("  socket so timeout "+soTimeout);
            serverSocket.setSoTimeout(soTimeout);

            server = new TcpServer<OSInfo>(serverSocket, ses -> new OSInfo(), SecurityFilters.create( s -> {
                // Разрешаем вызовы строго - определенных методов
                s.allow( a -> {

                    // Публикуемый API нашего сервиса
                    a.invoke("demo api", c->
                        c.getOwner().matches(
                                "org\\.example\\.api\\..+"
                        ));

                    // общий API
                    a.invoke("common api", c->
                        c.getOwner().matches(
                                "xyz\\.cofe\\.ecolls\\..+"
                        ));

                    // Работа с коллекциями
                    a.invoke("java collections api", c->c.getOwner().matches(
                        "java\\.util\\.([\\w]*List|[\\w]*Map|[\\w]*Set|Arrays|)|java\\.util\\.stream\\.([\\w\\d]+)"));

                    // Работа с Java строками
                    a.invoke("java lang api", c->
                        c.getOwner().matches("java\\.lang\\.(String|Character|Short|Byte|Integer|Float|Double|Long|Boolean)"));

                    // Методы которые использует компилятор Java
                    a.invoke("java compiler", c->
                        c.getOwner().matches(
                            "java\\.lang\\.invoke\\.(LambdaMetafactory|StringConcatFactory)"));
                });

                // Все остальное запрещаем
                s.deny().any("by default");
            }));

            server.addListener(new TrListener() {
                @Override
                public void trEvent(TrEvent trEvent) {
                }
            });

            System.out.println(" daemon is "+daemon);
            server.setDaemon(daemon);

            System.out.println("start server thread");
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
