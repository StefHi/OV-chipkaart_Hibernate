package P67.persistancy;

import P67.domain.Adres;
import P67.domain.Reiziger;
import P67.interfaces.AdresDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;

public class AdresDAOHibernate implements AdresDAO {

    private Session session;
    private ReizigerDAOHibernate reizigerDAOHibernate;

    public AdresDAOHibernate(Session session, ReizigerDAOHibernate reizigerDAOHibernate) {
        this.session = session;
        this.reizigerDAOHibernate = reizigerDAOHibernate;
    }

    @Override
    public boolean save(Adres adres) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(adres);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return false;
        }


    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(adres);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(adres);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return false;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        return session.createQuery("from Adres where reiziger_id = :id", Adres.class)
                      .setParameter("id", reiziger.getId())
                      .getSingleResult();
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<Adres> adressen = session.createQuery("from Adres", Adres.class).list();
            transaction.commit();
            return adressen;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return null;
        }
    }
}
