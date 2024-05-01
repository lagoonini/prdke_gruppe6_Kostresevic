package com.routing_app.backend.help_functions;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class CustomIdentifierGenerator implements IdentifierGenerator {

    private final Random random = new Random();

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        long id = 10000 + random.nextInt(90000); // Generate a number between 10000 and 99999
        return id;
    }
}
