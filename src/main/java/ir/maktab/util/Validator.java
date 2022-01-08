package ir.maktab.util;

import ir.maktab.exceptions.DataValidatorException;

public class Validator {
    public static void checkEmail(String email) {
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            throw new DataValidatorException("You Entered Email In Wrong Format.");
    }

    public static void checkPassword(String password) {
        if (!password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"))
            throw new DataValidatorException("The Password You Entered Is Weak.");
    }
}
