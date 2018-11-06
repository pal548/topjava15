package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceTestAbstract;

@ActiveProfiles(Profiles.JPA)
public class UserServiceJpaTest extends UserServiceTestAbstract {
}
