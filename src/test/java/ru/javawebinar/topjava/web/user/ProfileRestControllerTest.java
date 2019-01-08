package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UserUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.web.user.ProfileRestController.REST_URL;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Test
    void testGet() throws Exception {
        TestUtil.print(
                mockMvc.perform(get(REST_URL)
                        .with(userHttpBasic(USER)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(getUserMatcher(USER))
        );
    }

    @Test
    void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isNoContent());
        assertMatch(userService.getAll(), ADMIN);
    }

    @Test
    void testRegister() throws Exception {
        UserTo createdTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 1500);

        ResultActions action = mockMvc.perform(post(REST_URL + "/register").contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(createdTo)))
                .andDo(print())
                .andExpect(status().isCreated());
        User returned = readFromJsonResultActions(action, User.class);

        User created = UserUtil.createNewFromTo(createdTo);
        created.setId(returned.getId());

        assertMatch(returned, created);
        assertMatch(userService.getByEmail("newemail@ya.ru"), created);
    }

    @Test
    void testUpdate() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 1500);

        mockMvc.perform(put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(userService.getByEmail("newemail@ya.ru"), UserUtil.updateFromTo(new User(USER), updatedTo));
    }

    @Test
    void testValidationFail() throws Exception {
        doTestValidationBoth("name", new UserTo(null, "1", "e@mail.com", "password", 2000));
        doTestValidationBoth("name", new UserTo(null, "qwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwrerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwrerqwerqwer", "e@mail.com", "password", 2000));
        doTestValidationBoth("email", new UserTo(null, "name", "", "password", 2000));
        doTestValidationBoth("email", new UserTo(null, "name", "qwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwrerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwrerqwe@mail.com", "password", 2000));
        doTestValidationBoth("password", new UserTo(null, "name", "e@mail.com", "pass", 2000));
        doTestValidationBoth("password", new UserTo(null, "name", "e@mail.com", "qwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwrerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwrerqwerqwer", 2000));
        doTestValidationBoth("caloriesPerDay", new UserTo(null, "name", "e@mail.com", "password", 9));
        doTestValidationBoth("caloriesPerDay", new UserTo(null, "name", "e@mail.com", "password", 10001));
    }

    private <T> void doTestValidationBoth(String field, T obj) throws Exception {
        doTestValidationOne(true, field, obj);
        doTestValidationOne(false, field, obj);
    }

    private <T> void doTestValidationOne(boolean register, String field, T obj) throws Exception {
        MockHttpServletRequestBuilder requestBuilder;
        // need to be Anonymous to register, logged in otherwise
        if (!register) {
            requestBuilder = put(REST_URL).with(userHttpBasic(ADMIN));
        } else {
            requestBuilder = post(REST_URL + "/register");
        }
        requestBuilder = requestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(obj));
        ResultActions action = mockMvc.perform(requestBuilder)
                .andExpect(status().isUnprocessableEntity());
        ErrorInfo errorInfo = readFromJsonResultActions(action, ErrorInfo.class);
        assertEquals(ErrorType.VALIDATION_ERROR, errorInfo.getType());
        assertEquals(field, errorInfo.getDetail().split(" ")[0]);
    }
}