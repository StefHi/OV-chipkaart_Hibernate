package P67;

import P67.domain.Adres;
import P67.domain.OVChipkaart;
import P67.domain.Reiziger;
import P67.interfaces.AdresDAO;
import P67.interfaces.OVChipkaartDAO;
import P67.persistancy.AdresDAOHibernate;
import P67.persistancy.OVChipkaartDAOHibernate;
import P67.persistancy.ReizigerDAOHibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.sql.SQLException;
import java.util.List;

/**
 * Testklasse - deze klasse test alle andere klassen in deze package.
 *
 * System.out.println() is alleen in deze klasse toegestaan (behalve voor exceptions).
 *
 * @author tijmen.muller@hu.nl
 */
public class Main {
    // Creëer een factory voor Hibernate sessions.
    private static final SessionFactory factory;

    static {
        try {
            // Create a Hibernate session factory
            Configuration cfg = new Configuration();
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Retouneer een Hibernate session.
     *
     * @return Hibernate session
     * @throws HibernateException
     */
    private static Session getSession() throws HibernateException {
        return factory.openSession();
    }

    private static void closeConnection() throws HibernateException {
        factory.close();
    }

    public static void main(String[] args) throws SQLException {
        testFetchAll();
        try {
            Session session = getSession();
            ReizigerDAOHibernate reizigerDAO = new ReizigerDAOHibernate(session);
            AdresDAO adresDAO = new AdresDAOHibernate(session, reizigerDAO);
            OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOHibernate(session, reizigerDAO);
            testReizigerDAO(reizigerDAO);
            testAdresDAO(adresDAO, reizigerDAO);
            testOVChipkaartDAO(ovChipkaartDAO, reizigerDAO);
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * P6. Haal alle (geannoteerde) entiteiten uit de database.
     */
    private static void testFetchAll() {
        Session session = getSession();
        try {
            Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                Query query = session.createQuery("from " + entityType.getName());

                System.out.println("[Test] Alle objecten van type " + entityType.getName() + " uit database:");
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
                System.out.println();
            }
        } finally {
            session.close();
        }
    }

    private static void testReizigerDAO(ReizigerDAOHibernate rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(6, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Update reiziger
        sietske.setAchternaam("Stads");
        System.out.print("[Test] Eerst was de achternaam van de reiziger " + rdao.findById(6).getAchternaam() + ", ");
        rdao.update(sietske);
        System.out.println("na ReizigerDAO.update() " + rdao.findById(6).getAchternaam() + "\n");

        // Delete reiziger
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers");
    }

    private static void testAdresDAO(AdresDAO adao, ReizigerDAOHibernate rdao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        // Haal alle adressen op uit de database
        List<Adres> adresList = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adresList) {
            System.out.println(a);
        }
        System.out.println();

        // Maak een nieuwe reiziger met adres aan em persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(6, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        rdao.save(sietske);
        System.out.print("[Test] Eerst " + adresList.size() + " reizigers, na ReizigerDAO.save() ");
        Adres adrSietske = new Adres(6, "1234AB", "1", "eenstraat", "utrecht", rdao.findById(77));
        adao.save(adrSietske);
        adresList = adao.findAll();
        System.out.println(adresList.size() + " adressen\n");

        // Update adres
        adrSietske.setHuisnummer("2");
        System.out.print("[Test] Eerst was het huisnummer van de reiziger " + adao.findByReiziger(sietske).getHuisnummer() +
                                 ", ");
        adao.update(adrSietske);
        System.out.println("na AdresDAO.update() " + adao.findByReiziger(sietske).getHuisnummer() + "\n");

        // Delete adres
        System.out.print("[Test] Eerst " + adresList.size() + " reizigers, na ReizigerDAO.delete() ");
        adao.delete(adrSietske);
        adresList = adao.findAll();
        System.out.println(adresList.size() + " adressen");
        rdao.delete(sietske);
    }

    private static void testOVChipkaartDAO(OVChipkaartDAO odao, ReizigerDAOHibernate rdao) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        // Haal alle reizigers op uit de database
        Reiziger reiziger = rdao.findById(2);
        List<OVChipkaart> ovChipkaarts = odao.findByReiziger(reiziger);
        System.out.println("[Test] OVChipkaartDAO.findByReiziger(reiziger) geeft de volgende OV-Chipkaarten:");
        for (OVChipkaart kaart : ovChipkaarts) {
            System.out.println(kaart);
        }
        System.out.println();

        // Maak een nieuwe ov-chiokaart aan en persisteer deze in de database
        OVChipkaart ovChipkaart = new OVChipkaart(18626, java.sql.Date.valueOf("2024-01-01"), 2, 24.02, reiziger);
        System.out.print("[Test] Eerst " + ovChipkaarts.size() + " OV-Chipkaarten voor B van Rijn, na OVChipkaartDAO" +
                                 ".save() ");
        odao.save(ovChipkaart);
        ovChipkaarts = odao.findByReiziger(reiziger);
        System.out.println(ovChipkaarts.size() + " OV-Chipkaarten\n");

        // update ov-chipkaart
        ovChipkaart.setSaldo(40.02);
        System.out.print("[Test] Eerst was het saldo van de reiziger " + odao.findByReiziger(reiziger).get(3).getSaldo() +
                                 ", ");
        odao.update(ovChipkaart);
        System.out.println("na OvChipkaartDAO.update() " + odao.findByReiziger(reiziger).get(3).getSaldo() + "\n");

        // delete ov-chipkaart
        System.out.print("[Test] Eerst " + ovChipkaarts.size() + " OV-Chipkaarten voor B van Rijn, na OVChipkaartDAO" +
                                 ".delete() ");
        odao.delete(ovChipkaart);
        ovChipkaarts = odao.findByReiziger(reiziger);
        System.out.println(ovChipkaarts.size() + " OV-Chipkaarten\n");
    }
}