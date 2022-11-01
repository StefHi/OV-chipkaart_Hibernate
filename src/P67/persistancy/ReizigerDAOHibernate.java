package P67.persistancy;

import P67.domain.Reiziger;
import P67.interfaces.ReizigerDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class ReizigerDAOHibernate implements ReizigerDAO {

    private Session session;
    private AdresDAOHibernate adresDAOHibernate;
    private OVChipkaartDAOHibernate ovChipkaartDAOHibernate;

    public ReizigerDAOHibernate(Session session) {
        this.session = session;
        this.adresDAOHibernate = new AdresDAOHibernate(session, this);
        this.ovChipkaartDAOHibernate = new OVChipkaartDAOHibernate(session, this);
    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(reiziger);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(reiziger);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(reiziger);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return false;
        }
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Reiziger reiziger = session.get(Reiziger.class, id);
            transaction.commit();
            return reiziger;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) throws SQLException {
        Date sqlDate = Date.valueOf(datum);
        return session.createQuery("from Reiziger where geboortedatum = :date", Reiziger.class)
                      .setParameter("date", sqlDate)
                      .getResultList();
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<Reiziger> reizigers = session.createQuery("from Reiziger").list();
            transaction.commit();
            return reizigers;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return null;
        }
    }
}
