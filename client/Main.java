package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    static class Args {
        @Parameter(names = "-t", description = "Type", required = true)
        String type;

        @Parameter(names = "-i", description = "Index")
        Integer index;

        @Parameter(names = "-m", description = "Optional message")
        String message;

        @Parameter(names = {"-h", "--help"}, help = true, description = "Show help")
        boolean help;
    }

    public static void main(String[] argm) throws IOException {
        Scanner sc = new Scanner(System.in);
        String address = "127.0.0.1";
        int port = 33333;

        System.out.println("Client started!");

        String line = sc.nextLine().trim();

        // Strip optional "java Main" or "java client.Main" prefix
        if (line.startsWith("java ")) {
            int secondSpace = line.indexOf(' ', line.indexOf(' ') + 1);
            line = (secondSpace >= 0) ? line.substring(secondSpace).trim() : "";
        }

        // Extract -m value as "rest of line" to preserve spaces between words
        String manualMessage = null;
        int mPos = line.indexOf(" -m ");
        if (mPos >= 0) {
            manualMessage = line.substring(mPos + 4);
            line = line.substring(0, mPos);
        }

        String[] argv = line.isEmpty() ? new String[0] : line.split("\\s+");

        Args args = new Args();
        JCommander jc = JCommander.newBuilder()
                .addObject(args)
                .programName("java Main")
                .build();

        try {
            jc.parse(argv);
            if (args.help) {
                jc.usage();
                return;
            }
            if (manualMessage != null) {
                args.message = manualMessage;
            }
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            jc.usage();
            System.exit(3);
        }

        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            String sent;
            if ("exit".equals(args.type)) {
                sent = "exit";
            } else if (args.message != null) {
                sent = args.type + " " + args.index + " " + args.message;
            } else {
                sent = args.type + " " + args.index;
            }

            output.writeUTF(sent);
            System.out.println("Sent: " + sent);
            String received = input.readUTF();
            System.out.println("Received: " + received);
        }
    }
}
