public class Main {
    public static void main(String[] args) {
        System.out.println(" code started running ");
        try {
            Database.connectToDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
