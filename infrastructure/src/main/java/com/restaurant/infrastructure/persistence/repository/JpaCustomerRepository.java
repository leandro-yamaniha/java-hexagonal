package com.restaurant.infrastructure.persistence.repository;

import com.restaurant.application.port.out.CustomerRepository;
import com.restaurant.domain.entity.Customer;
import com.restaurant.infrastructure.persistence.entity.CustomerEntity;
import com.restaurant.infrastructure.persistence.mapper.CustomerMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA implementation of CustomerRepository
 */
public class JpaCustomerRepository implements CustomerRepository {
    
    private final EntityManager entityManager;

    public JpaCustomerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @Override
    @Transactional
    public Customer save(Customer customer) {
        CustomerEntity entity = entityManager.find(CustomerEntity.class, customer.getId());
        
        if (entity == null) {
            // Create new entity
            entity = CustomerMapper.toEntity(customer);
            entityManager.persist(entity);
        } else {
            // Update existing entity
            CustomerMapper.updateEntity(entity, customer);
            entity = entityManager.merge(entity);
        }
        
        return CustomerMapper.toDomain(entity);
    }
    
    @Override
    public Optional<Customer> findById(UUID customerId) {
        CustomerEntity entity = entityManager.find(CustomerEntity.class, customerId);
        return Optional.ofNullable(CustomerMapper.toDomain(entity));
    }
    
    @Override
    public Optional<Customer> findByEmail(String email) {
        try {
            TypedQuery<CustomerEntity> query = entityManager.createQuery(
                "SELECT c FROM CustomerEntity c WHERE c.email = :email", CustomerEntity.class);
            query.setParameter("email", email);
            CustomerEntity entity = query.getSingleResult();
            return Optional.of(CustomerMapper.toDomain(entity));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Customer> findAll() {
        TypedQuery<CustomerEntity> query = entityManager.createQuery(
            "SELECT c FROM CustomerEntity c ORDER BY c.name", CustomerEntity.class);
        return query.getResultList().stream()
            .map(CustomerMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Customer> findAllActive() {
        TypedQuery<CustomerEntity> query = entityManager.createQuery(
            "SELECT c FROM CustomerEntity c WHERE c.active = true ORDER BY c.name", CustomerEntity.class);
        return query.getResultList().stream()
            .map(CustomerMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Customer> searchByName(String name) {
        TypedQuery<CustomerEntity> query = entityManager.createQuery(
            "SELECT c FROM CustomerEntity c WHERE LOWER(c.name) LIKE LOWER(:name) ORDER BY c.name", 
            CustomerEntity.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList().stream()
            .map(CustomerMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsByEmail(String email) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(c) FROM CustomerEntity c WHERE c.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }
    
    @Override
    @Transactional
    public void deleteById(UUID customerId) {
        CustomerEntity entity = entityManager.find(CustomerEntity.class, customerId);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }
    
    @Override
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(c) FROM CustomerEntity c", Long.class);
        return query.getSingleResult();
    }
    
    @Override
    public long countActive() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(c) FROM CustomerEntity c WHERE c.active = true", Long.class);
        return query.getSingleResult();
    }
}
