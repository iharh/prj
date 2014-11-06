package fe;

public class CLI {
    public static void main(String [] args) {
        if (args.length < 1) {
            System.err.println("not enough args");

        } else {
            String fileName = args[0];
            System.out.println("file: " + fileName);
            java.io.File f = new java.io.File(fileName);
            System.out.println("exists: " + (f.exists() ? "true" : "false"));
        }
    }
}
