package simple;

public class Main {
    public static void main(String[] args) {
        try (final SimpleProviderLib.SimpleProviderImpl lib = new SimpleProviderLib.SimpleProviderImpl()) {
            long simpleNum = lib.getSimpleNum();
            System.out.println("getSimpleNum: " + simpleNum);
        } catch (Exception e) {
            System.out.println("caught: " + e.getMessage());
            e.printStackTrace();
            System.exit(1); 
        }
    }
}
