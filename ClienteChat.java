import java.io.*;
import java.net.*;

public class ClienteChat {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("192.168.0.4", 24000); // IP do servidor
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);

        System.out.print("Digite seu nome: ");
        String nome = teclado.readLine();

        new Thread(() -> {
            try {
                String resposta;
                while ((resposta = entrada.readLine()) != null) {
                    System.out.println(resposta);
                }
            } catch (IOException e) {
                System.out.println("Conexão encerrada.");
            }
        }).start();

        String mensagem;
        while ((mensagem = teclado.readLine()) != null) {
            saida.println(nome + ": " + mensagem);
        }

        socket.close();
    }
}
