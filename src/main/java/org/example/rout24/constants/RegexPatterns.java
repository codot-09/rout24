package org.example.rout24.constants;

public interface RegexPatterns {
    String PHONE_UZB = "^(998)(9[0-9]{8})$";
    String EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    String PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
}
