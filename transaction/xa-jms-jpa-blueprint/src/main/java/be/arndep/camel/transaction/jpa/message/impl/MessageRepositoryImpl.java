package be.arndep.camel.transaction.jpa.message.impl;

import be.arndep.camel.transaction.jpa.message.Message;
import be.arndep.camel.transaction.jpa.message.MessageRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * Created by arnaud on 03.01.15.
 */
public class MessageRepositoryImpl implements MessageRepository {
    
    private final EntityManager entityManager;

    public MessageRepositoryImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Message create(Message message) {
        entityManager.persist(message);
        return message;
    }

    @Override
    public Optional<Message> find(final Long id) {
        return Optional.ofNullable(entityManager.find(Message.class, id));
    }

    @Override
    public List<Message> findAll(final Optional<Integer> page, final Optional<Integer> limit) {
        return entityManager.createQuery("select m from Message m order by m.lastModifiedDate")
            .setMaxResults(limit.orElse(20))
            .setFirstResult(page.orElse(0))
            .getResultList();
    }

    @Override
    public Message update(final Message message) {
        return entityManager.merge(message);
    }

    @Override
    public boolean delete(final Message message) {
        if (message != null) {
            entityManager.remove(entityManager.merge(message));
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(final Long id) {
        return delete(entityManager.find(Message.class, id));
    }

    @Override
    public long count() {
        return entityManager.createQuery("select count(m.id) from Message m", Long.class).getSingleResult();
    }
}
