package controller;

import model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {

    @Test
    void createNewUser() {
        assertEquals(1,LoginController.createNewUser("u","pap","naserkazemi"));
        assertEquals(2,LoginController.createNewUser("uz","pap","naserkazemi"));
        assertEquals(3,LoginController.createNewUser("uz","pap","kazemNaseri"));
        assertEquals(3,LoginController.createNewUser("uz","m6BBnA3kiz","kazemNaseri"));
        assertEquals(3,LoginController.createNewUser("uz","AAAA6%BBA3","kazemNaseri"));
        assertEquals(3,LoginController.createNewUser("uz","m6%bbna3kiz","kazemNaseri"));
        assertEquals(0,LoginController.createNewUser("uz","m6%BBnA3kiz","kazemNaseri"));
        User.deleteUser(User.findUser("uz",false));
    }

    @Test
    void loginUser() {
        assertEquals(1,LoginController.loginUser("buz","wakeUp"));
        assertEquals(2,LoginController.loginUser("u","papa"));
        assertEquals(0,LoginController.loginUser("u","pap"));
        assertEquals(LoginController.getLoggedUser(),User.findUser("u",false));
    }

    @Test
    void changeNickname() {
        assertEquals(0,LoginController.createNewUser("uz","m6%BBnA3kiz","kazemNaseri"));
        LoginController.loginUser("uz","m6%BBnA3kiz");
        assertEquals(1, LoginController.changeNickname("naserkazemi"));
        assertEquals(0, LoginController.changeNickname("walterBlack"));
        User.deleteUser(User.findUser("uz",false));
    }

    @Test
    void changePassword() {
        assertEquals(0,LoginController.createNewUser("uz","m6%BBnA3kiz","kazemNaseri"));
        LoginController.loginUser("uz","m6%BBnA3kiz");
        assertEquals(1, LoginController.changePassword("baaaa","booo"));
        assertEquals(2, LoginController.changePassword("m6%BBnA3kiz","m6%BBnA3kiz"));
        assertEquals(3, LoginController.changePassword("m6%BBnA3kiz","mZ"));
        assertEquals(0, LoginController.changePassword("m6%BBnA3kiz","m6%BNBnA3kiz"));
        User.deleteUser(User.findUser("uz",false));
    }

    @Test
    void getLoggedUser() {
    }
}