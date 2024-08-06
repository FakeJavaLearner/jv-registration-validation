package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.model.RegistrationException;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MINIMUM_AGE = 18;
    private final StorageDao storageDao = new StorageDaoImpl();

    @Override
    public User register(User user) {
        if (storageDao.get(user.getLogin()) != null) {
            throw new RegistrationException("A user with the login \""
                    + user.getLogin() + "\" already exists.");
        }

        if (user.getPassword() == null) {
            throw new RegistrationException("Password cannot be null.");
        }

        if (user.getLogin() == null) {
            throw new RegistrationException("Login cannot be null.");
        }

        if (user.getAge() == null) {
            throw new RegistrationException("Age cannot be null.");
        }

        if (user.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new RegistrationException("Invalid password: " + user.getPassword()
                    + ". Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        }

        if (user.getAge() < MINIMUM_AGE) {
            throw new RegistrationException("Invalid age: "
                    + user.getAge() + ". Age must be at least " + MINIMUM_AGE + " years.");
        }

        storageDao.add(user);
        return user;
    }
}
