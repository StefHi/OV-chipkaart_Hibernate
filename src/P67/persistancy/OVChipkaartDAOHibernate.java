package P67.persistancy;

import P67.domain.Adres;
import P67.domain.OVChipkaart;
import P67.domain.Reiziger;
import P67.interfaces.OVChipkaartDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;

public class OVChipkaartDAOHibernate implements OVChipkaartDAO {

    private Session session;
    private ReizigerDAOHibernate reizigerDAOHibernate;
    private ProductDAOHibernate productDAOHibernate;

    public OVChipkaartDAOHibernate(Session session, ReizigerDAOHibernate reizigerDAOHibernate) {
        this.session = session;
        this.reizigerDAOHibernate = reizigerDAOHibernate;
        this.productDAOHibernate = new ProductDAOHibernate(session, this);
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(ovChipkaart);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(ovChipkaart);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(ovChipkaart);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return false;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<OVChipkaart> ovChipkaarten = session.createQuery("from OVChipkaart where reiziger_id = :id", OVChipkaart.class)
                                                     .setParameter("id", reiziger.getId())
                                                     .getResultList();
            transaction.commit();
            return ovChipkaarten;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<OVChipkaart> ovChipkaarten = session.createQuery("from OVChipkaart ", OVChipkaart.class).getResultList();
            transaction.commit();
            return ovChipkaarten;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return null;
        }
    }
}
