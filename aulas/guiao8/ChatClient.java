import java.io.*;
import java.net.Socket;


public class ChatClient {
    public static void main(String[] args) {
        if (args.length <2) {
            System.err.println("Usage: java ChatClient <ip> <porta>");
            System.exit(1);
        }

        String ip = args[0];
        int porta = Integer.parseInt(args[1]);

        try {
            Socket socket = new Socket(ip, porta);
            System.out.println("Connected to the server.");

            //dados lidos no servidor atraves do socket
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //enviar dados para servidor e permitir a escritar
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
            // le do terminal o que o cliente escreveu
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            
             // Loop para enviar mensagens digitadas pelo usuário para o servidor
            Thread receiveThread = new Thread(
                () -> {
                try {
                    String message;
                    while ((message = inputReader.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (Exception ignorException) {
                }
            });
            receiveThread.start();

            
            String userInput;
            while ((userInput = consoleReader.readLine()) != null) {
                outputWriter.println(userInput);
                outputWriter.flush();
            }

            // Fechar o socket
            socket.close();
        } catch (Exception ignorException) {
        }
    }

}