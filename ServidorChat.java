import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorChat {
    private static final int PORTA = 24000;
    private static final List<Socket> clientes = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(PORTA);
        System.out.println("Servidor escutando na porta " + PORTA);

        while (true) {
            Socket cliente = servidor.accept();
            clientes.add(cliente);
            System.out.println("Novo cliente conectado: " + cliente.getInetAddress());

            new Thread(() -> {
                try {
                    BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                    String mensagem;
                    while ((mensagem = entrada.readLine()) != null) {
                        System.out.println("Recebido: " + mensagem);
                        broadcast(mensagem, cliente);
                    }
                } catch (IOException e) {
                    System.out.println("Cliente desconectado.");
                } finally {
                    clientes.remove(cliente);
                    try { cliente.close(); } catch (IOException ignored) {}
                }
            }).start();
        }
    }

    private static void broadcast(String mensagem, Socket origem) {
        for (Socket cliente : clientes) {
            if (cliente != origem) {
                try {
                    PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);
                    saida.println(mensagem);
                } catch (IOException ignored) {}
            }
        }
    }
}
