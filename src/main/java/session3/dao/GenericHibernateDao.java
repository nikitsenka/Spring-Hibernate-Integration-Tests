package session3.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

/**
 * The Generic Data Access Object.
 *
 * @param <T>
 *            entity type
 */
public class GenericHibernateDao<T>
{
    @Autowired
    protected SessionFactory sessionFactory;

    protected Class<T> clazz;

    /**
     * Creates a new GenericHibernateDao object.
     */
    public GenericHibernateDao(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    /**
     * Obtains the current session.
     *
     * @return the current session.
     */
    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Retrieves entity.
     *
     * @param id
     *            entity id
     *
     * @return entity.
     */
    @SuppressWarnings("unchecked")
    public T getById(final Serializable id)
    {
        Assert.notNull(id, "ID must not be null");

        return (T) getCurrentSession().get(clazz, id);
    }

    /**
     * Retrieves all entities.
     *
     * @return all entities.
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll()
    {
        return getCurrentSession().createCriteria(clazz).list();
    }

    /**
     * Updates entity.
     *
     * @param entity
     *            entity to update
     *
     * @return updated entity.
     */
    @SuppressWarnings("unchecked")
    public T update(final T entity)
    {
        Assert.notNull(entity, "Entity must not be null");

        return (T) getCurrentSession().merge(entity);
    }

    /**
     * Saves entity.
     *
     * @param entity
     *            entity to save
     */
    public void save(final T entity)
    {
        Assert.notNull(entity, "Entity must not be null");

        getCurrentSession().persist(entity);
    }

    /**
     * Saves or updates entity.
     *
     * @param entity
     *            entity to save or update
     */
    public void saveOrUpdate(final T entity)
    {
        Assert.notNull(entity, "Entity must not be null");

        getCurrentSession().saveOrUpdate(entity);
    }

    /**
     * Deletes entity.
     *
     * @param entity
     *            entity to delete
     */
    public void delete(final T entity)
    {
        Assert.notNull(entity, "Entity must not be null");

        getCurrentSession().delete(entity);
    }

    /**
     * Deletes entity by id.
     *
     * @param id
     *            entity id
     */
    public void delete(final Serializable id)
    {
        Assert.notNull(id, "ID must not be null");

        delete(getById(id));
    }

    public void setReadOnly(final T entity, boolean readOnly)
    {
        Assert.notNull(entity, "Entity must not be null");

        // Workaround for hibernate bug fixed in 3.5
        if (entity instanceof HibernateProxy)
            sessionFactory.getCurrentSession().setReadOnly(((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation(), readOnly);
        else
            sessionFactory.getCurrentSession().setReadOnly(entity, readOnly);
    }
}
