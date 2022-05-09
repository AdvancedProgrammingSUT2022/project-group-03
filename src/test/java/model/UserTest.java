package model;

import controller.LoginController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTest {
    User user = new User("aa","12","nick");

    @Test
    void staticTest() {

    }

    @Test
    void findUser() {
        Assertions.assertEquals(user.getNickname() , User.findUser("aa",false).getNickname());
        Assertions.assertNull(User.findUser("aa",true));
        Assertions.assertNull( User.findUser("nick",false));
        Assertions.assertEquals(user.getNickname() , User.findUser("nick",true).getNickname());
    }

    @Test
    void isPasswordCorrect() {

        Assertions.assertTrue(user.isPasswordCorrect("12"));
        Assertions.assertFalse(user.isPasswordCorrect("13"));
    }

    @Test
    void changeNickname() {
        user.changeNickname("changed");
        Assertions.assertSame("changed",user.getNickname());
    }

    @Test
    void changePassword() {
        user.changePassword("123");
        Assertions.assertTrue(user.isPasswordCorrect("123"));
    }


}