package com.github.argajuvi;

import com.github.argajuvi.models.order.Order;
import com.github.argajuvi.models.product.BookProduct;
import com.github.argajuvi.models.product.ClothingProduct;
import com.github.argajuvi.models.product.FoodProduct;
import com.github.argajuvi.models.product.Product;
import com.github.argajuvi.models.receipt.Receipt;
import com.github.argajuvi.models.user.User;
import com.github.argajuvi.utils.Utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final List<Product> productList;
    private final List<User> userList;

    private User currentUser = null;

    public Main() {
        this.productList = new ArrayList<>();
        this.userList = new ArrayList<>();

        // PRE-REGISTER DATA ADMIN
        userList.add(registerAdmin());

        // (temp) Seed product data
        productList.add(new BookProduct("Java OOP Done Right", 473_426, 2021, " Alan Mellor"));
        productList.add(new BookProduct(
                "Tate no Yusha no Nariagari Vol. 1", 317_102, 2013, "Aneko Yusagi"));
        productList.add(new FoodProduct(
                "Lay's, 1 Ounce (Pack of 104)",
                831_476,
                LocalDate.now().plus(1, ChronoUnit.MONTHS)));
        productList.add(new ClothingProduct(
                "Shingeki no Kyojin Zip Hoodie",
                387_792, 'L'));

        while (true) {
            Utils.clearScreen();

            System.out.println(
                    " █████╗ ██╗   ██╗ ██████╗ ██████╗  █████╗     ███████╗██╗  ██╗ ██████╗ ██████╗ \n" +
                    "██╔══██╗╚██╗ ██╔╝██╔═══██╗██╔══██╗██╔══██╗    ██╔════╝██║  ██║██╔═══██╗██╔══██╗\n" +
                    "███████║ ╚████╔╝ ██║   ██║██████╔╝███████║    ███████╗███████║██║   ██║██████╔╝\n" +
                    "██╔══██║  ╚██╔╝  ██║   ██║██╔═══╝ ██╔══██║    ╚════██║██╔══██║██║   ██║██╔═══╝ \n" +
                    "██║  ██║   ██║   ╚██████╔╝██║     ██║  ██║    ███████║██║  ██║╚██████╔╝██║     \n" +
                    "╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝     ╚═╝  ╚═╝    ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝     \n");

            System.out.println(
                    "1. Login\n" +
                    "2. Register\n" +
                    "0. Exit\n");

            int choice = Utils.scanAbsoluteInt(">> ");
            switch (choice) {
                case 1:
                    loginUser(userList);
                    break;
                case 2:
                    userList.add(registerUser(userList));
                    break;
                case 0:
                    System.exit(0);
                    return;
                default:
                    System.out.println("Option is not available!");
                    Utils.waitForEnter();
                    break;
            }
        }
    }

    //	FUNCTION UNTUK LOGIN USER
    public void loginUser(List<User> users) {
        Scanner scanner = Utils.SCANNER;

        String username;
        String password;

        System.out.print("Input Username: ");
        username = scanner.nextLine();
        System.out.print("Input Password: ");
        password = scanner.nextLine();

        // VALIDASI DATA LOGIN
        try {
            // LOOPING UNTUK MENCARI USERNAME DALAM VECTOR
            for (int i = 0; i <= users.size(); i++) {
                User user = users.get(i);

                if (username.equals(user.getUsername())) {
                    if (password.equals(user.getPassword())) {
                        currentUser = user;

                        // VALIDASI APAKAH LOGIN UNTUK ADMIN ATAU USER
                        if (username.equals("admin")) {
                            // SHOW MENU ADMIN KALAU YANG LOGIN ADMIN
                            this.showAdminMenu();
                            break;
                        } else {
                            // SHOW MENU ADMIN KALAU YANG LOGIN USER BIASA
                            this.showUserMenu();
                            break;
                        }
                    } else {
                        System.out.println("Incorrect password! Make sure to enter the correct password next time!");
                        Utils.waitForEnter();
                        break;
                    }
                }
            }
            // VALIDASI JIKA TIDAK DITEMUKAN DATA LOGIN PADA ARRAYLIST (DENGAN ARRAY OUT OF RANGE)
        } catch (Exception e) {
            System.out.println("Username doesn't exists! Make sure the account is registered!");
            Utils.waitForEnter();
        }
    }

    //	FUNCTION REGISTER DATA USER BARU
    public User registerUser(List<User> users) {
        Scanner scanner = Utils.SCANNER;
        boolean registering = true;

        String username;
        String password;
        // LOOPING SAMPAI REGISTRASI BERHASIL
        do {
            // LOOPING SAMPAI KRITERIA USERNAME DAN PASSWORD TERPENUHI
            do {
                System.out.print("Input new username [> 5 characters]: ");
                username = scanner.nextLine();
                System.out.print("Input new password [> 5 characters]: ");
                password = scanner.nextLine();
            } while (username.length() < 5 || password.length() < 5);

            try {
                for (int i = 0; i <= users.size(); i++) {
                    if (username.equals(users.get(i).getUsername())) {
                        System.out.println("User has already been registered before, maybe try login with it?");
                        Utils.waitForEnter();
                        break;
                    }
                }
            } catch (Exception e) {
                registering = false;
            }
        } while (registering);

        System.out.println("User is successfully registered!");
        Utils.waitForEnter();

        return new User(username, password);
    }

    //	PRE REGISTER DATA ADMIN
    public User registerAdmin() {
        return new User("admin", "admin123");
    }

    private void showProductsView() {
        if (productList.isEmpty()) {
            System.out.println("No products found.");
        } else {
            String rowFormat = "| %3s | %-40s | %-20s | %-12s |\n";
            String line = "----------------------------------------------------------------------------------------\n";
            int count = 0;

            System.out.print(line);
            System.out.printf(rowFormat, "No.", "Product Name", "Product Price", "Product Type");
            System.out.print(line);

            for (Product product : productList) {
                count++;

                System.out.printf(
                        rowFormat,
                        count + "", product.getName(), Utils.formatPriceIDR(product.getPrice()), product.getTypeName()
                );
            }

            System.out.print(line);
        }
    }

    private void showCartView() {
        List<Order> cart = currentUser.getCart();

        if (cart.isEmpty()) {
            System.out.println("You haven't ordered anything.");
        } else {
            String rowFormat = "| %3s | %-40s | %-20s | %-12s | %8s | %-20s |\n";
            String line = "--------------------------------------------------------------------------------------------------------------------------\n";
            int count = 0;

            System.out.print(line);
            System.out.printf(
                    rowFormat,
                    "No.",
                    "Product Name", "Product Price", "Product Type",
                    "Quantity", "Total Price"
            );
            System.out.print(line);

            int totalOfTotalPrice = 0;
            for (Order order : cart) {
                count++;

                Product product = order.getProduct();
                totalOfTotalPrice += order.getTotalPrice();

                System.out.printf(
                        rowFormat,
                        count + "",
                        product.getName(), Utils.formatPriceIDR(product.getPrice()), product.getTypeName(),
                        order.getQuantity() + "",
                        Utils.formatPriceIDR(order.getTotalPrice())
                );
            }

            System.out.print(line);
            System.out.printf("| %-46s | %-69s |\n" ,"Total Price", Utils.formatPriceIDR(totalOfTotalPrice));
            System.out.print(line);
        }
    }

    private void showOrderProductMenu() {
        Utils.clearScreen();
        this.showProductsView();

        if (productList.isEmpty()) {
            Utils.waitForEnter();
            return;
        }

        int choice;
        while (true) {
            choice = Utils.scanAbsoluteInt("Product to buy ['0' to go back]: ");

            if (choice == 0) {
                return;
            }
            if (choice < 1 || choice > productList.size()) {
                System.out.println("Cannot find product");
                continue;
            }

            break;
        }

        Product chosenProduct = productList.get(choice - 1);
        int quantity;

        while (true) {
            quantity = Utils.scanAbsoluteInt("Product quantity: ");
            if (quantity < 1) {
                System.out.println("Product quantity must be greater than 1");
                continue;
            }

            break;
        }

        Order order = new Order(chosenProduct, quantity);
        currentUser.getCart().add(order);

        System.out.println("Product is added to the cart!");
        Utils.waitForEnter();
    }

    private void showCartProducts() {
        Utils.clearScreen();

        List<Order> cart = currentUser.getCart();
        this.showCartView();

        if (cart.isEmpty()) {
            Utils.waitForEnter();
            return;
        }

        int choice;
        System.out.println();

        while (true) {
            choice = Utils.scanAbsoluteInt("Remove order ['0' to go back]: ");

            if (choice == 0) {
                return;
            }
            if (choice < 1 || choice > cart.size()) {
                System.out.println("Cannot find order");
                continue;
            }

            break;
        }

        cart.remove(choice - 1);
        System.out.println("Successfully removed order from the cart");

        Utils.waitForEnter();
    }

    private void showCheckout() {
        Utils.clearScreen();

        List<Order> cart = currentUser.getCart();
        this.showCartView();

        if (cart.isEmpty()) {
            Utils.waitForEnter();
            return;
        }

        boolean isConfirmed = Utils.scanAbsoluteConfirmation("Do you want to purchase those products? [y/n]: ");
        if (!isConfirmed) {
            return;
        }

        List<Order> orderList = new ArrayList<>(cart);
        int totalOfTotalPrice = 0;

        for (Order order : orderList) {
            totalOfTotalPrice += order.getTotalPrice();
        }

        Receipt receipt = new Receipt(orderList, LocalDate.now(), totalOfTotalPrice);
        currentUser.getReceiptList().add(receipt);
        cart.clear();

        System.out.println("Successfully purchase products!");
        Utils.waitForEnter();
    }
    
    private void showReceiptsView() {
        List<Receipt> receiptList = currentUser.getReceiptList();

        if (receiptList.isEmpty()) {
            System.out.println("You haven't purchase anything from our shop.");
        } else {
            String rowFormat = "| %-15s | %-13s | %-20s |\n";
            String line = "-----------------------------------------------------------\n";

            System.out.print(line);
            System.out.printf(rowFormat, "Receipt ID", "Purchase Date", "Total Price");
            System.out.print(line);

            for (Receipt receipt : receiptList) {
                System.out.printf(
                        rowFormat,
                        receipt.getId() + "",
                        Utils.DATE_FORMATTER.format(receipt.getPurchaseDate()),
                        Utils.formatPriceIDR(receipt.getTotalPrice())
                );
            }

            System.out.print(line);
        }
    }

    private void showBuyProductsMenu() {
        while (true) {
            Utils.clearScreen();

            System.out.println(
                    "1. Order products\n" +
                    "2. Check my cart\n" +
                    "3. Checkout\n" +
                    "0. Back\n");

            int choice = Utils.scanAbsoluteInt(">> ");
            switch (choice) {
                case 1:
                    this.showOrderProductMenu();
                    break;
                case 2:
                    this.showCartProducts();
                    break;
                case 3:
                    this.showCheckout();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Option is not available!");
                    Utils.waitForEnter();
                    break;
            }
        }
    }

    private void showReceiptMenu() {
        while (true) {
            Utils.clearScreen();

            List<Receipt> receiptList = currentUser.getReceiptList();
            this.showReceiptsView();

            if (receiptList.isEmpty()) {
                Utils.waitForEnter();
                return;
            }

            int receiptId;
            Receipt foundReceipt = null;

            while (true) {
                receiptId = Utils.scanAbsoluteInt("Choose receipt ID to see detail ['0' to go back]: ");
                String notFoundMessage = "Cannot find receipt";

                if (receiptId == 0) {
                    return;
                }
                if (receiptId < 1) {
                    System.out.println(notFoundMessage);
                    continue;
                }


                for (Receipt receipt : receiptList) {
                    if (receipt.getId() == receiptId) {
                        foundReceipt = receipt;
                        break;
                    }
                }

                if (foundReceipt == null) {
                    System.out.println(notFoundMessage);
                    continue;
                }

                break;
            }

            System.out.println();
            System.out.println("---------------------------------------");
            System.out.println("Receipt ID   : " + foundReceipt.getId());
            System.out.println("Purchase Date: " + Utils.DATE_FORMATTER.format(foundReceipt.getPurchaseDate()));
            System.out.println("Total Price  : " + Utils.formatPriceIDR(foundReceipt.getTotalPrice()));
            System.out.println();

            List<Order> orderList = foundReceipt.getOrderList();
            int count = 0;

            for (Order order : orderList) {
                Product product = order.getProduct();

                count++;
                System.out.println(count + ". [" + product.getTypeName() + "] " + product.getName());
                System.out.println("   - Price       : " + Utils.formatPriceIDR(product.getPrice()));

                if (product instanceof BookProduct) {
                    BookProduct book = (BookProduct) product;

                    System.out.println("   - Author      : " + book.getAuthor());
                    System.out.println("   - Publish Year: " + book.getPublishYear());
                } else if (product instanceof ClothingProduct) {
                    ClothingProduct cloth = (ClothingProduct) product;

                    System.out.println("   - Size: " + cloth.getSize());
                } else if (product instanceof FoodProduct) {
                    FoodProduct food = (FoodProduct) product;

                    System.out.println("   - Expire Date : " + Utils.DATE_FORMATTER.format(food.getExpireDate()));
                }
            }

            System.out.println("---------------------------------------");

            Utils.waitForEnter();
        }
    }

    private void showUserMenu() {
        while (true) {
            Utils.clearScreen();

            System.out.println(
                    "Welcome, " + currentUser.getUsername() + " to AyopaShop\n" +
                    "-------------------------\n" +
                    "1. Check purchase history\n" +
                    "2. Buy products\n" +
                    "3. Logout\n" +
                    "0. Exit\n");

            int choice = Utils.scanAbsoluteInt(">> ");
            switch (choice) {
                case 1:
                    this.showReceiptMenu();
                    break;
                case 2:
                    this.showBuyProductsMenu();
                    break;
                case 3:
                    currentUser = null;
                    return;
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Option is not available!");
                    Utils.waitForEnter();
                    break;
            }
        }
    }

    private void showAdminMenu() {
        throw new UnsupportedOperationException("Admin menu has yet been build");
    }

    public static void main(String[] args) {
        new Main();
    }

}
