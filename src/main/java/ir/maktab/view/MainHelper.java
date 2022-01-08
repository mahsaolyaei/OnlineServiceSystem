package ir.maktab.view;

import ir.maktab.dto.UserDto;
import ir.maktab.exceptions.*;
import ir.maktab.model.*;
import ir.maktab.model.enums.*;
import ir.maktab.service.*;
import ir.maktab.util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainHelper {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final UserService USER_SERVICE = new UserService();
    private static final ManagerService MANAGER_SERVICE = new ManagerService();
    private static final CustomerService CUSTOMER_SERVICE = new CustomerService();
    private static final ExpertService EXPERT_SERVICE = new ExpertService();

    public static void showMenu() {
        do {
            try {
                System.out.println("**** Welcome To Online Service System ****" +
                        "\n1- Show Manager Menu" +
                        "\n2- Show Customer Menu" +
                        "\n3- Show Expert Menu" +
                        "\n9- Exit");
                String option = SCANNER.next();
                if ("1".equals(option))
                    showManagerMenu();
                else if ("2".equals(option))
                    showCustomerMenu();
                else if ("3".equals(option))
                    showExpertMenu();
                else if ("9".equals(option))
                    break;
                else
                    System.out.println("You Entered Wrong Number.");
            } catch (UserLoginException ex) {
                System.out.println(ex.getMessage());
            }
        } while (true);
    }

    private static void showExpertMenu() {
        UserDto userDto = showLoginMenu(UserType.EXPERT);
        System.out.println("Welcome " + userDto.getName());
        do {
            System.out.println("*** Expert Menu ***" +
                    "\n1- Add Offer" +
                    "\n2- Change Password" +
                    "\n9- Back");
            String option = SCANNER.next();
            if ("1".equals(option))
                showAddOfferMenu(userDto);
            else if ("2".equals(option))
                showChangePasswordMenu(userDto);
            else if ("9".equals(option))
                break;
            else
                System.out.println("You Entered Wrong Number.");
        } while (true);
    }

    private static void showAddOfferMenu(UserDto userDto) {
        List<Order> orderList = EXPERT_SERVICE.getOpenOrders(userDto);
        if (orderList.size() == 0) {
            System.out.println("There Is No Order To Show.");
        }
        else {
            System.out.println("Select An Order To Add Offer: ");
            showElementsByIndex(orderList);
            try {
                int index = SCANNER.nextInt();
                Order order = orderList.get(index - 1);
                showAddOfferDetailMenu(userDto, order);
            } catch (InputMismatchException | IndexOutOfBoundsException ex) {
                System.out.println("You Entered Invalid Number.");
            }
        }
    }

    private static void showAddOfferDetailMenu(UserDto userDto, Order order) {
        try {
            System.out.println("Please Fill Your Offer Data: ");
            System.out.println("Price: ");
            long price = SCANNER.nextLong();
            System.out.println("Hour: ");
            int hour = SCANNER.nextInt();
            System.out.println("Duration Hours: ");
            int duration = SCANNER.nextInt();
            Date date = order.getRequestedDate();
            date.setHours(hour);
            Offer offer = EXPERT_SERVICE.addOffer(userDto, order, price, date, duration);
            System.out.println("Offer Added Successfully.");
        } catch (InputMismatchException ex) {
            System.out.println("You Entered Invalid Number.");
        }
    }

    private static void showCustomerMenu() {
        UserDto userDto = showLoginMenu(UserType.CUSTOMER);
        System.out.println("Welcome " + userDto.getName());
        do {
            System.out.println("*** Customer Menu ***" +
                    "\n1- Add Order" +
                    "\n2- Check Offers" +
                    "\n3- Show Done Orders" +
                    "\n4- Show Paid Orders" +
                    "\n5- Change Password" +
                    "\n9- Back");
            String option = SCANNER.next();
            if ("1".equals(option))
                showAddOrderMenu(userDto);
            else if ("2".equals(option))
                showCustomerWaitingOrdersMenu(userDto);
            else if ("3".equals(option))
                showCustomerDoneOrdersMenu(userDto);
            else if ("4".equals(option))
                showCustomerPaidOrdersMenu(userDto);
            else if ("5".equals(option))
                showChangePasswordMenu(userDto);
            else if ("9".equals(option))
                break;
            else
                System.out.println("You Entered Wrong Number.");
        } while (true);
    }

    private static void showCustomerWaitingOrdersMenu(UserDto userDto) {
        List<Order> orderList = CUSTOMER_SERVICE.getUserOrdersByType(userDto, OrderStatus.WAITING_FOR_SELECT_EXPERT);
        if (orderList.size() == 0) {
            System.out.println("There Is No Offer To Show.");
        }
        else {
            System.out.println("Select An Order To Check Offers");
            showElementsByIndex(orderList);
            try {
                int index = SCANNER.nextInt();
                Order order = orderList.get(index - 1);
                showAcceptOfferMenu(order);
            } catch (InputMismatchException | IndexOutOfBoundsException ex) {
                System.out.println("You Entered Invalid Number.");
            }
        }
    }

    private static void showAcceptOfferMenu(Order order) {
        List<Offer> offerList = CUSTOMER_SERVICE.getOffers(order);
        if (offerList.size() == 0) {
            System.out.println("There Is No Offer To Show.");
        }
        else {
            System.out.println("Select An Offer To Accept");
            showElementsByIndex(offerList);
            try {
                int index = SCANNER.nextInt();
                Offer offer = offerList.get(index - 1);
                CUSTOMER_SERVICE.acceptOffer(offer);
                System.out.println("Offer Accepted.");
            } catch (InputMismatchException | IndexOutOfBoundsException ex) {
                System.out.println("You Entered Invalid Number.");
            }
        }
    }

    private static void showCustomerPaidOrdersMenu(UserDto userDto) {
        List<Order> orderList = CUSTOMER_SERVICE.getUserOrdersByType(userDto, OrderStatus.PAID);
        if (orderList.size() == 0) {
            System.out.println("There Is No Order To Show.");
        }
        else {
            System.out.println("Select An Order To Add Comment.");
            showElementsByIndex(orderList);
            try {
                int index = SCANNER.nextInt();
                Order order = orderList.get(index - 1);
                showAddCommentMenu(order);
            } catch (InputMismatchException | IndexOutOfBoundsException ex) {
                System.out.println("You Entered Invalid Number.");
            }
        }
    }

    private static void showCustomerDoneOrdersMenu(UserDto userDto) {
        List<Order> orderList = CUSTOMER_SERVICE.getUserOrdersByType(userDto, OrderStatus.DONE);
        if (orderList.size() == 0)
            System.out.println("There Is No Order To Show.");
        else {
            System.out.println("Select An Order To Pay.");
            showElementsByIndex(orderList);
            try {
                int index = SCANNER.nextInt();
                Order order = orderList.get(index - 1);
                showPayMenu(order);
            } catch (InputMismatchException | IndexOutOfBoundsException ex) {
                System.out.println("You Entered Invalid Number.");
            }
        }
    }

    private static void showPayMenu(Order order) {
        do {
            System.out.println(order);
            System.out.println(
                    "\n1- Pay" +
                            "\n9- Back");
            String option = SCANNER.next();
            if ("1".equals(option)) {
                CUSTOMER_SERVICE.payOrder(order);
                System.out.println("The Order Marked As Paid.");
            }
            else if ("9".equals(option))
                break;
            else
                System.out.println("You Entered Wrong Number.");
        } while (true);
    }

    private static void showAddCommentMenu(Order order) {
        try {
            System.out.println("Score: ");
            int score = SCANNER.nextInt();
            System.out.println("Comment: ");
            String comment = SCANNER.next();
            CUSTOMER_SERVICE.addComment(order, score, comment);
            System.out.println("The Comment Added Successfully.");
        } catch (InputMismatchException ex) {
            System.out.println("You Entered Invalid Number.");
        }
    }

    private static UserDto showLoginMenu(UserType userType) {
        System.out.println("Email: ");
        String email = SCANNER.next();
        System.out.println("Password: ");
        String password = SCANNER.next();
        UserDto userDto = null;
        if (UserType.CUSTOMER.equals(userType))
            userDto = CUSTOMER_SERVICE.login(email, password);
        else if (UserType.EXPERT.equals(userType))
            userDto = EXPERT_SERVICE.login(email, password);
        try {
            Validator.checkPassword(password);
        } catch (DataValidatorException ex) {
            System.out.println("You Must Change Your Password.");
            showChangePasswordMenu(userDto);
        }
        return userDto;
    }

    private static void showChangePasswordMenu(UserDto userDto) {
        do {
            try {
                System.out.println("New Password: ");
                String password1 = SCANNER.next();
                System.out.println("Repeat New Password: ");
                String password2 = SCANNER.next();
                if (password1.equals(password2)) {
                    Validator.checkPassword(password1);
                    USER_SERVICE.changeUserPassword(userDto, password1);
                    break;
                }
                System.out.println("Password Does Not Match To Its Repeat!");
            } catch (DataValidatorException ex) {
                System.out.println(ex.getMessage());
            }
        } while (true);
    }

    private static void showAddOrderMenu(UserDto userDto) {
        List<Service> serviceList = MANAGER_SERVICE.getAllServices();
        if (serviceList.size() == 0) {
            System.out.println("There Is No Service To Show.");
        }
        else {
            System.out.println("Select A Service To Show Its Sub-Services");
            showElementsByIndex(serviceList);
            try {
                int index = SCANNER.nextInt();
                Service service = serviceList.get(index - 1);
                List<SubService> subServiceList = MANAGER_SERVICE.getSubServices(service);
                showElementsByIndex(subServiceList);
                index = SCANNER.nextInt();
                SubService subService = subServiceList.get(index - 1);
                showAddOrderDetailMenu(userDto, subService);
            } catch (SimilarDataException ex) {
                System.out.println(ex.getMessage());
            } catch (InputMismatchException | IndexOutOfBoundsException ex) {
                System.out.println("You Entered Invalid Number.");
            }
        }
    }

    private static void showAddOrderDetailMenu(UserDto userDto, SubService subService) {
        try {
            SCANNER.nextLine();
            System.out.println("Please Fill Your Order Data: ");
            System.out.println("Address: ");
            String address = SCANNER.nextLine();
            System.out.println("Description: ");
            String description = SCANNER.nextLine();
            System.out.println("Price: ");
            long price = SCANNER.nextLong();
            System.out.println("Date: ");
            String dateStr = SCANNER.next();
            System.out.println("Hour: ");
            int hour = SCANNER.nextInt();
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse(dateStr);
            date.setHours(hour);
            Order order = CUSTOMER_SERVICE.addOrder(userDto, subService, address, description, price, date);
            System.out.println("Order Added Successfully.");
        } catch (InputMismatchException ex) {
            System.out.println("You Entered Invalid Number.");
        } catch (ParseException e) {
            System.out.println("You Entered Invalid Date.");
        }
    }

    private static void showManagerMenu() {
        do {
            System.out.println("*** Manager Menu ***" +
                    "\n1- Add Customer" +
                    "\n2- Add Expert" +
                    "\n3- Add Service" +
                    "\n4- Add Sub-Service" +
                    "\n5- Add Expert To Service" +
                    "\n6- Remove Expert From Service" +
                    "\n9- Back");
            String option = SCANNER.next();
            if ("1".equals(option))
                showAddUserMenu(UserType.CUSTOMER);
            else if ("2".equals(option))
                showAddUserMenu(UserType.EXPERT);
            else if ("3".equals(option))
                showAddServiceMenu();
            else if ("4".equals(option))
                showServiceActionsMenu("Add", SubService.class);
            else if ("5".equals(option))
                showServiceActionsMenu("Add", User.class);
            else if ("6".equals(option))
                showServiceActionsMenu("Remove", User.class);
            else if ("9".equals(option))
                break;
            else
                System.out.println("You Entered Wrong Number.");
        } while (true);
    }

    private static void showServiceActionsMenu(String action, Class actionClass) {
        List<Service> serviceList = MANAGER_SERVICE.getAllServices();
        if (serviceList.size() == 0) {
            System.out.println("There Is No Service To Show.");
        }
        else {
            System.out.printf("Please Select A Service To %s %s : \n", action, actionClass.getSimpleName());
            showElementsByIndex(serviceList);
            try {
                int index = SCANNER.nextInt();
                Service service = serviceList.get(index - 1);
                if (actionClass.equals(SubService.class))
                    showAddSubServiceMenu(service);
                else if (actionClass.equals(User.class)) {
                    if ("add".equalsIgnoreCase(action))
                        showAddUserToServiceMenu(service);
                    else if ("remove".equalsIgnoreCase(action))
                        showRemoveUserFromServiceMenu(service);
                }
            } catch (SimilarDataException ex) {
                System.out.println(ex.getMessage());
            } catch (InputMismatchException | IndexOutOfBoundsException ex) {
                System.out.println("You Entered Invalid Number.");
            }
        }
    }

    private static void showRemoveUserFromServiceMenu(Service service) {
        List<User> users = new ArrayList<>(service.getExperts());
        if (users.size() == 0)
            System.out.printf("The Service [%s] Has No Expert \n", service.getName());
        else {
            System.out.printf("Which Expert You Want To Remove From The Service [%s]: \n", service.getName());
            showElementsByIndex(users);
            int index = SCANNER.nextInt();
            User user = users.get(index - 1);
            service.getExperts().remove(user);
            MANAGER_SERVICE.updateService(service);
            System.out.printf("User [%s] Removed From Service [%s].\n", user.getName(), service.getName());
        }
    }

    private static void showAddUserToServiceMenu(Service service) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("userType", UserType.EXPERT);
        int pageSize = 10;
        int pageNum = 1;
        while (true) {
            int usersSize = MANAGER_SERVICE.getUsersSize(filterMap);
            List<UserDto> userList = MANAGER_SERVICE.getUsers(pageSize, pageNum, filterMap, new ArrayList<>());
            showPagingList(pageSize, pageNum, usersSize, userList);
            if (pageSize * (pageNum - 1) > 0)
                System.out.println("1) Previous Page");
            if (userList.size() == pageSize && pageSize * pageNum < usersSize)
                System.out.println("2) Next Page");
            System.out.println("3) Add Or Remove Filters\n" +
                    "4) Select An User\n" +
                    "9) Exit");
            int action = SCANNER.nextInt();
            if (pageSize * (pageNum - 1) > 0 && action == 1)
                --pageNum;
            else if (userList.size() == pageSize && pageSize * pageNum < usersSize && action == 2)
                ++pageNum;
            else if (action == 3) {
                showFiltersMenu(filterMap);
                pageNum = 1;
            } else if (action == 4) {
                showPagingList(pageSize, pageNum, usersSize, userList);
                System.out.println("\n\nWhich User Do You Want To Add? ");
                int index = SCANNER.nextInt();
                if (index >= 1 && index <= pageSize) {
                    UserDto userDto = userList.get(index - 1);
                    MANAGER_SERVICE.addUserToService(service, userDto);
                    System.out.printf("User [%s] Added To Service [%s].\n", userDto.getName(), service.getName());
                    break;
                }
            } else if (action == 9)
                break;
            else
                System.out.println("You Entered Invalid Number.");
        }
    }

    private static void showFiltersMenu(Map<String, Object> filterMap) {
        String name = (String) filterMap.get("name");
        String family = (String) filterMap.get("family");
        String email = (String) filterMap.get("email");
        String serviceName = (String) filterMap.get("serviceName");
        while (true) {
            System.out.println("Current Conditions Are:" +
                    "\nUser Name: " + (name != null ? name : "-") +
                    "\nUser Family: " + (family != null ? family : "-") +
                    "\nUser Email: " + (email != null ? email : "-") +
                    "\nService Name: " + (serviceName != null ? serviceName : "-") +
                    "\n\nWhat Do You Want To Do? " +
                    "\n1) Change User Name" +
                    "\n2) Change User Family" +
                    "\n3) Change User Email" +
                    "\n4) Change Service Name" +
                    "\n9) Submit");
            int action = SCANNER.nextInt();
            if (action >= 1 && action <= 6) {
                System.out.println("Enter New Value Or Enter - To Remove Old Value");
                String value = SCANNER.next();
                if (value.equals("-"))
                    value = null;
                if (action == 1)
                    name = value;
                else if (action == 2)
                    family = value;
                else if (action == 3)
                    email = value;
                else if (action == 4)
                    serviceName = value;
            } else
                break;

        }
        filterMap.put("name", name);
        filterMap.put("family", family);
        filterMap.put("email", email);
        filterMap.put("serviceName", serviceName);
    }


    private static void showPagingList(int pageSize, int pageNum, int searchSize, Collection collection) {
        System.out.printf("\nResults %d to %d From %d: \n", Math.min(pageSize * (pageNum - 1) + 1, searchSize), Math.min(pageSize * pageNum, searchSize), searchSize);
        int i = 1;
        for (Object object : collection)
            System.out.println(i++ + " - " + object);
    }

    private static void showAddSubServiceMenu(Service service) {
        System.out.println("Please Enter New Sub-Service Info:");
        System.out.println("Name: ");
        String name = SCANNER.next();
        System.out.println("Base Price: ");
        long basePrice = SCANNER.nextLong();
        System.out.println("Description: ");
        SCANNER.nextLine();
        String description = SCANNER.nextLine();
        SubService subService = MANAGER_SERVICE.addSubService(service, name, basePrice, description);
        System.out.printf("Sub-Service [%s] Added Successfully To Service [%s].\n", name, service.getName());
    }

    private static void showElementsByIndex(Collection collection) {
        int i = 1;
        for (Object object : collection) {
            System.out.println(i++ + " - " + object);
        }
    }

    private static void showAddServiceMenu() {
        System.out.println("Please Enter New Service Name: ");
        String name = SCANNER.next();
        try {
            Service service = MANAGER_SERVICE.addService(name);
            System.out.printf("Service [%s] Added Successfully.\n", name);
        } catch (SimilarDataException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void showAddUserMenu(UserType userType) {
        System.out.printf("Please Enter New %s Info: \n", userType.toString());
        System.out.println("Name: ");
        String name = SCANNER.next();
        System.out.println("Family: ");
        String family = SCANNER.next();
        System.out.println("Email: ");
        String email = SCANNER.next();
        System.out.println("Password: ");
        String password = SCANNER.next();
        System.out.println("Image Address: ");
        String imageAddress = SCANNER.next();
        try {
            Validator.checkEmail(email);
            MANAGER_SERVICE.addUser(userType, name, family, email, password, imageAddress);
            System.out.println("User Added Successfully.");
        } catch (SimilarDataException | DataValidatorException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
