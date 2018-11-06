package ru.javawebinar.topjava.service.dataJpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceTestAbstract;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collections;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceDataJpaTest extends UserServiceTestAbstract {

    @Test
    public void getWithMeals() {
        var user = service.getWithMeals(USER_ID);
        assertMatch(user.getMeals(), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void getWithMealNotFound() {
        service.getWithMeals(666);
    }

    @Test
    public void getWithoutMeals() {
        var user = service.getWithMeals(USER_ID + 10);
        assertMatch(user.getMeals(), Collections.EMPTY_LIST);
    }

}
