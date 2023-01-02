package budget;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Main {

    static final char COIN = '$';
    static final String[] CATP = {"Food", "Clothes", "Entertainment", "Other"};
    static final String[] CATLIST = {"Food", "Clothes", "Entertainment", "Other", "All"};
    static final String[] CATANALYZE = {"Sort all purchases", "Sort by type", "Sort certain type"};
    static DecimalFormat DF = new DecimalFormat();

    static class Bank {
        float balance;
        Map<String, List<Products>> products;

        public Bank () {
            this.balance = 0f;
            this.products = new HashMap<>();
        }

        public void addBalance(float balance) {
            this.balance += balance;
        }

        public void addProduct(String cat, String productName, float price) {
            if (!this.products.containsKey(cat)) {
                this.products.put(cat, new ArrayList<>());
            }
            if (!this.products.containsKey("All")) {
                this.products.put("All", new ArrayList<>());
            }
            this.products.get(cat).add(new Products(productName, price));

            if (!cat.equals("All"))
                this.products.get("All").add(new Products(productName, price));

            this.balance -= price;
        }

        public void addProductLoad(String cat, String productName, float price) {
            if (!this.products.containsKey(cat)) {
                this.products.put(cat, new ArrayList<>());
            }
            if (!this.products.containsKey("All")) {
                this.products.put("All", new ArrayList<>());
            }
            this.products.get(cat).add(new Products(productName, price));

            if (!cat.equals("All"))
                this.products.get("All").add(new Products(productName, price));
        }

        public Map<String, List<Products>> getPurchases() {
            return products;
        }

        public List<Products> getPurchases(String key) {
            return products.get(key);
        }

        public float getBalance() {
            return balance;
        }

        public void setBalance(float balance) {
            this.balance = balance;
        }
    }

    static class Products {
        String name;
        float price;

        public Products(String name, float price) {
            this.name = name;
            this.price = price;
        }
    }

    public static void ProductsList() {
        System.out.println("Choose the type of purchase");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");
        System.out.println("5) All");
        System.out.println("6) Back");
    }

    public static void Products() {
        System.out.println("Choose the type of purchase");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");
        System.out.println("5) Back");
    }

    public static void Sort() {
        System.out.println("How do you want to sort?");
        System.out.println("1) Sort all purchases");
        System.out.println("2) Sort by type");
        System.out.println("3) Sort certain type");
        System.out.println("4) Back");
    }

    public static void Menu() {
        System.out.println("Choose your action:");
        System.out.println("1) Add income");
        System.out.println("2) Add purchase");
        System.out.println("3) Show list of purchases");
        System.out.println("4) Balance");
        System.out.println("5) Save");
        System.out.println("6) Load");
        System.out.println("7) Analyze (Sort)");
        System.out.println("0) Exit");
    }

    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    /**
     *
     * Options
     *
     */

    public static void Option(int op, Bank bank, Scanner scanner) {
        switch (op) {
            case 1:
                AddIncome(bank, scanner);
                break;
            case 2:
                ListMenu(CATP, bank, scanner, "AddPurchase");
                break;
            case 3:
                ListMenu(CATLIST, bank, scanner, "ShowPurchaseList");
                break;
            case 4:
                DisplayBalance(bank);
                break;
            case 5:
                SaveFile(bank);
                break;
            case 6:
                LoadFile(bank);
                break;
            case 7:
                ListMenu(CATANALYZE, bank, scanner, "Analyze");
                break;
            case 0:
                System.out.println("Bye!");
                break;
        }
    }

    /**
     *
     * Options Metods
     *
     */

    private static void AddIncome(Bank bank, Scanner scanner) {
        System.out.println("Enter income:");

        String amount = "";

        while (!isFloat(amount)) {
            amount = scanner.nextLine();
        }

        bank.addBalance(Float.parseFloat(amount));
        System.out.println("Income was added!");
        System.out.println();
    }

    private static void AddPurchase(Bank bank, Scanner scanner, int catint) {
        System.out.println("Enter purchase name:");
        String product = "";
        product = scanner.nextLine();

        System.out.println("Enter its price:");
        float price = scanner.nextFloat();

        bank.addProduct(CATP[catint], product, price);
        System.out.println("Purchase was added!");
        System.out.println();
        //scanner.close();
        scanner = new Scanner(System.in);
    }

    private static void ShowPurchaseList(Bank bank, Scanner scanner, int catint) {
        if(bank.getPurchases(CATLIST[catint]) != null) {
            System.out.println(CATLIST[catint] + ":");
            float total = 0f;
            for (Products line : bank.getPurchases(CATLIST[catint])) {
                System.out.print(line.name + " " + COIN);
                if ((line.price + "").split("\\.")[1].length() < 2) {
                    System.out.println(line.price + "0");
                } else {
                    System.out.println(line.price + "");
                }
                total += line.price;
            }
            System.out.println("Total sum: " + COIN + total);

        } else {
            System.out.println(CATLIST[catint] + ":");
            System.out.println("The purchase list is empty");
        }
        System.out.println();
        //scanner.close();
        scanner = new Scanner(System.in);
    }

    private static void AnalysePurchaseList(Bank bank, Scanner scanner, int catint) {
        if(bank.getPurchases(CATP[catint]) != null) {
            System.out.println(CATP[catint] + ":");
            float total = 0f;
            List<Products> catList = SortbyPrice(bank.getPurchases(CATP[catint]));
            for (Products line : catList) {
                System.out.print(line.name + " " + COIN);
                if ((line.price + "").split("\\.")[1].length() < 2) {
                    System.out.println(line.price + "0");
                } else {
                    System.out.println(line.price + "");
                }
                total += line.price;
            }
            System.out.println("Total sum: " + COIN + total);

        } else {
            System.out.println(CATP[catint] + ":");
            System.out.println("The purchase list is empty");
        }
        System.out.println();
        //scanner.close();
        scanner = new Scanner(System.in);
    }

    private static void DisplayBalance(Bank bank) {
        System.out.printf("Balance: %s%.2f", COIN, bank.getBalance());
        System.out.println();
        System.out.println();
    }

    private static void SaveFile(Bank bank) {
        File file = new File("purchases.txt");

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write( bank.getBalance() + "\n");
            Set<String> keys = bank.getPurchases().keySet();

            for (int i = 0; i < keys.size(); i++) {
                if (!keys.stream().toList().get(i).equals("All"))
                    for (Products b : bank.getPurchases(keys.stream().toList().get(i))) {
                        writer.write(keys.stream().toList().get(i) + "=" + b.name + "=");

                        if ((b.price + "").split("\\.")[1].length() < 2) {
                            writer.write(b.price + "0");
                        } else {
                            writer.write(b.price + "");
                        }

                        writer.write("\n");
                    }
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Purchases were saved!\n");
    }

    private static void LoadFile(Bank bank) {
        File file = new File("purchases.txt");

        try {
            Scanner scannerFile = new Scanner(file);
            while (scannerFile.hasNextLine()) {
                String[] in = scannerFile.nextLine().split("=");

                if (in.length > 1){
                    bank.addProductLoad(in[0], in[1], Float.parseFloat(in[2]));
                } else if(in.length == 1) {
                    bank.setBalance(Float.parseFloat(in[0]));
                }

            }

            scannerFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Purchases were loaded!\n");
    }

    private static List<Products> SortbyPrice(List<Products> productsList) {
        List<Products> sortedList = productsList;
        for (int i = 0; i < sortedList.size() - 1; i++) {
            for (int j = 0; j < sortedList.size() - i - 1; j++) {
                if (sortedList.get(j).price > sortedList.get(j + 1).price) {
                    Products temp = sortedList.get(j);
                    sortedList.set(j, sortedList.get(j + 1));
                    sortedList.set(j + 1, temp);
                } else if (sortedList.get(j).price == sortedList.get(j + 1).price) {
                    if (sortedList.get(j).name.compareTo(sortedList.get(j + 1).name) > 0) {
                        Products temp = sortedList.get(j);
                        sortedList.set(j, sortedList.get(j + 1));
                        sortedList.set(j + 1, temp);
                    }
                }
            }
        }
        Collections.reverse(sortedList);
        return sortedList;
    }

    private static List<String> SortbyPriceString(List<String> productsList) {
        List<String> sortedList = productsList;
        for (int i = 0; i < sortedList.size() - 1; i++) {
            for (int j = 0; j < sortedList.size() - i - 1; j++) {
                if (Float.parseFloat(sortedList.get(j).split(" ")[1]) > Float.parseFloat(sortedList.get(j + 1).split(" ")[1])) {
                    String temp = sortedList.get(j);
                    sortedList.set(j, sortedList.get(j + 1));
                    sortedList.set(j + 1, temp);
                } else if (Float.parseFloat(sortedList.get(j).split(" ")[1]) == Float.parseFloat(sortedList.get(j + 1).split(" ")[1])) {
                    if (sortedList.get(j).split(" ")[0].compareTo(sortedList.get(j + 1).split(" ")[0]) > 0) {
                        String temp = sortedList.get(j);
                        sortedList.set(j, sortedList.get(j + 1));
                        sortedList.set(j + 1, temp);
                    }
                }
            }
        }
        Collections.reverse(sortedList);
        return sortedList;
    }

    private static void Analyze(Bank bank, Scanner scanner, int catint) {
        switch (catint) {
            case 0:
                if(bank.getPurchases("All") != null) {
                    List<Products> tempList = SortbyPrice(bank.getPurchases("All"));
                    System.out.println("All" + ":");
                    float total = 0f;
                    for (Products line : tempList) {
                        System.out.print(line.name + " " + COIN);
                        if ((line.price + "").split("\\.")[1].length() < 2) {
                            System.out.println(line.price + "0");
                        } else {
                            System.out.println(line.price + "");
                        }
                        total += line.price;
                    }
                    System.out.println("Total: " + COIN + DF.format(total));
                    System.out.println();
                } else {
                    System.out.println("The purchase list is empty");
                    System.out.println();
                }
                break;
            case 1:
                float total = 0.00f;
                float sum;
                System.out.println("Types:");

                List<String> tempListTipes = new ArrayList<>();
                for (String cat : CATP) {
                    sum = 0.00f;
                    if (bank.getPurchases(cat) != null) {
                        for (Products product : bank.getPurchases(cat)) {
                            sum += product.price;
                        }
                        tempListTipes.add(cat + " " + sum);
                    } else {
                        sum = 0.00f;
                    }
                    tempListTipes = SortbyPriceString(tempListTipes);


                }

                for (String res : tempListTipes) {
                    System.out.println(res.split(" ")[0] + " - " + COIN + DF.format(Float.parseFloat(res.split(" ")[1])));
                    total += Float.parseFloat(res.split(" ")[1]);
                }

                System.out.println("Total sum: " + COIN + DF.format(total));
                System.out.println();
                break;
            case 2:
                ListMenu(CATP, bank, scanner, "AnalysePurchaseList");
                break;
        }
    }

    private static void ListMenu(String[] cats, Bank bank, Scanner scanner, String metodName) {
        int catint = -1;
        do {
            do {
                String cat = "";
                while (!isInt(cat)) {
                    switch (metodName) {
                        case "AddPurchase":
                            Products();
                            break;
                        case "ShowPurchaseList":
                            ProductsList();
                            break;
                        case "AnalysePurchaseList":
                            Products();
                            break;
                        case "Analyze":
                            Sort();
                            break;
                    }
                    cat = scanner.nextLine();
                    System.out.println();
                }

                catint = Integer.parseInt(cat);
                catint--;
            } while (catint >= (cats.length + 1) || catint < 0);

            if (catint != cats.length) {
                switch (metodName) {
                    case "AddPurchase":
                        AddPurchase(bank, scanner, catint);
                        break;
                    case "ShowPurchaseList":
                        ShowPurchaseList(bank, scanner, catint);
                        break;
                    case "AnalysePurchaseList":
                        AnalysePurchaseList(bank, scanner, catint);
                        catint = cats.length;
                        break;
                    case "Analyze":
                        Analyze(bank, scanner, catint);
                        break;
                }
            }
        } while (catint != cats.length);
    }

    /**
     *
     * Main
     */

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        DF.setMaximumFractionDigits(2);

        Bank bank = new Bank();
        String op;

        do {
            int opint = -1;
            Menu();
            do {
                op = "";
                while (!isInt(op)) {
                    scanner = new Scanner(System.in);
                    op = scanner.nextLine();
                    System.out.println();
                }

                opint = Integer.parseInt(op);
            } while (opint >= 8 || opint < -1);

            Option(opint, bank, scanner);
        } while (Integer.parseInt(op) != 0);
    }
}
