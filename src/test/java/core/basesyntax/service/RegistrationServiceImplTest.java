package core.basesyntax.service;

import static core.basesyntax.db.Storage.people;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.model.RegistrationException;
import core.basesyntax.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private static final int AGE_NEGATIVE = -1;
    private static final int AGE_ZERO = 0;
    private static final int AGE_BELOW_MINIMUM = 17;
    private static final int AGE_MINIMUM = 18;
    private static final int AGE_OVER_MINIMUM = 21;
    private static final int EXPECTED_COUNT_TWO = 2;
    private static final int EXPECTED_COUNT_FOUR = 4;
    private static final int INDEX_FIRST = 0;
    private static final String LOGIN_1 = "loginOne";
    private static final String LOGIN_2 = "loginTwo";
    private static final String LOGIN_3 = "loginThree";
    private static final String LOGIN_VALID = "qwerty";
    private static final String LOGIN_INVALID = "log";
    private static final String LOGIN_VALID_SIX_LETTERS = "login6";
    private static final String PASSWORD_VALID = "qwertyu";
    private static final String PASSWORD_VALID_SIX_LETTERS = "123456";
    private static final String PASSWORD_INVALID = "qwer";
    private static final String EMPTY_STRING = "";

    private static RegistrationService registrationService;

    @BeforeAll
    static void beforeAll() {
        registrationService = new RegistrationServiceImpl();
    }

    @AfterEach
    void tearDown() {
        people.clear();
    }

    @Test
    void register_nullLoginValue_NotOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(
                new User(null, PASSWORD_VALID, AGE_MINIMUM)));
    }

    @Test
    void register_nullPasswordValue_NotOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(
                new User(LOGIN_1, null, AGE_MINIMUM)));
    }

    @Test
    void register_nullAgeValue_NotOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(
                new User(LOGIN_1, PASSWORD_VALID, null)));
    }

    @Test
    void register_validAge_Ok() {
        registrationService.register(new User(LOGIN_1, PASSWORD_VALID, AGE_MINIMUM));
        registrationService.register(new User(LOGIN_2, PASSWORD_VALID, AGE_OVER_MINIMUM));
        assertTrue(people.contains(new User(LOGIN_1, PASSWORD_VALID, AGE_MINIMUM)));
        assertTrue(people.contains(new User(LOGIN_2, PASSWORD_VALID, AGE_OVER_MINIMUM)));
        assertEquals(EXPECTED_COUNT_TWO, people.size());
    }

    @Test
    void register_negativeAge_NotOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(
                new User(LOGIN_VALID, PASSWORD_VALID, AGE_NEGATIVE)));
    }

    @Test
    void register_zeroAge_NotOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(
                new User(LOGIN_VALID, PASSWORD_VALID, AGE_ZERO)));
    }

    @Test
    void register_belowMinimumAge_NotOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(
                new User(LOGIN_VALID, PASSWORD_VALID, AGE_BELOW_MINIMUM)));
    }

    @Test
    void register_validLogin_Ok() {
        registrationService.register(new User(LOGIN_1, PASSWORD_VALID, AGE_MINIMUM));
        registrationService.register(new User(LOGIN_2, PASSWORD_VALID, AGE_MINIMUM));
        registrationService.register(new User(LOGIN_3, PASSWORD_VALID, AGE_MINIMUM));
        registrationService.register(new User(
                LOGIN_VALID_SIX_LETTERS, PASSWORD_VALID, AGE_MINIMUM));
        assertTrue(people.contains(new User(LOGIN_1, PASSWORD_VALID, AGE_MINIMUM)));
        assertTrue(people.contains(new User(LOGIN_2, PASSWORD_VALID, AGE_MINIMUM)));
        assertTrue(people.contains(new User(LOGIN_3, PASSWORD_VALID, AGE_MINIMUM)));
        assertTrue(people.contains(new User(
                LOGIN_VALID_SIX_LETTERS, PASSWORD_VALID, AGE_MINIMUM)));
        assertEquals(EXPECTED_COUNT_FOUR, people.size());
    }

    @Test
    void register_validPassword_Ok() {
        registrationService.register(new User(LOGIN_1, PASSWORD_VALID, AGE_MINIMUM));
        registrationService.register(new User(
                LOGIN_2, PASSWORD_VALID_SIX_LETTERS, AGE_MINIMUM));
        assertTrue(people.contains(new User(LOGIN_1, PASSWORD_VALID, AGE_MINIMUM)));
        assertTrue(people.contains(new User(
                LOGIN_2, PASSWORD_VALID_SIX_LETTERS, AGE_MINIMUM)));
        assertEquals(EXPECTED_COUNT_TWO, people.size());
    }

    @Test
    void register_invalidPassword_NotOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(
                new User(LOGIN_VALID, PASSWORD_INVALID, AGE_MINIMUM)));
    }

    @Test
    void register_invalidPasswordEmptyString_NotOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(
                new User(LOGIN_VALID, EMPTY_STRING, AGE_MINIMUM)));
    }

    @Test
    void register_userAlreadyExists_NotOk() {
        people.add(new User(LOGIN_2, PASSWORD_VALID, AGE_MINIMUM));
        assertThrows(RegistrationException.class, () -> registrationService.register(
                new User(LOGIN_2, PASSWORD_VALID, AGE_MINIMUM)));
    }

    @Test
    void register_addValidUser_Ok() {
        User actual = registrationService.register(new User(
                LOGIN_1, PASSWORD_VALID_SIX_LETTERS, AGE_MINIMUM));
        User expected = people.get(INDEX_FIRST);
        assertEquals(actual, expected);
    }
}
