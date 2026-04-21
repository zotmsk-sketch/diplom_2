package utils;

import com.github.javafaker.Faker;
import models.User;

public class UserGenerator {
    private static final Faker faker = new Faker();

    public static User generateRandomUser() {
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.internet().password(6, 12));
        user.setName(faker.name().firstName());
        return user;
    }

    public static User generateUserWithMissingEmail() {
        User user = new User();
        user.setEmail(null);
        user.setPassword(faker.internet().password(6, 12));
        user.setName(faker.name().firstName());
        return user;
    }

    public static User generateUserWithMissingPassword() {
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(null);
        user.setName(faker.name().firstName());
        return user;
    }

    public static User generateUserWithMissingName() {
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.internet().password(6, 12));
        user.setName(null);
        return user;
    }
}