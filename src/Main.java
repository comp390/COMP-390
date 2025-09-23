public class Main {
    public static void main(String[] args){


        //  history implementayion
        HistoryDAO history = new HistoryDAO();

        // Manager class executes history
        // DatabaseManager dbManager = new DatabaseManager(history);

        // Create a new HISTORY obj and save it
        History h1 = new History("15-S", "AA6", "S8FW-86", "S5-D7F8ES2F2");
        History h2 = new History("kdoo", "5476", "f88-evd", "1227-568592");
        History h3 = new History("98oo", "ii76", "777-evd", "3d-96654448");

        history.save_H(h3);
        history.save_H(h1);
        history.save_H(h2);
        history.save_H(h3);
    }
}

