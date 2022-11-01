package P67.persistancy;

import P67.domain.OVChipkaart;
import P67.domain.Product;
import P67.interfaces.ProductDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;

public class ProductDAOHibernate implements ProductDAO {

    private Session session;
    private OVChipkaartDAOHibernate ovChipkaartDAOHibernate;

    public ProductDAOHibernate(Session session, OVChipkaartDAOHibernate ovChipkaartDAOHibernate) {
        this.session = session;
        this.ovChipkaartDAOHibernate = ovChipkaartDAOHibernate;
    }

    @Override
    public boolean save(Product product) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(product);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return false;
        }
    }

    @Override
    public boolean update(Product product) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(product);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(product);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return false;
        }
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<Product> products = session.createQuery("from Product where product_id in (select product_id from " +
                                                                 "ov_chipkaart_product where kaart_nummer = " +
                                                                 ":kaart_nummer)", Product.class).setParameter(
                    "kaart_nummer", ovChipkaart.getKaartnummer()).list();
            transaction.commit();
            return products;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return null;
        }
    }

    @Override
    public List<Product> findall() throws SQLException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<Product> products = session.createQuery("from Product", Product.class).list();
            transaction.commit();
            return products;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return null;
        }
    }
}
