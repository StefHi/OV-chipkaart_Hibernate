package P6.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Product {
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Id
    @Column(name = "product_nummer")
    private int nummer;
    private String naam;
    private String beschrijving;
    private double prijs;

    @ManyToMany
    @JoinTable (
            name = "ov_chipkaart_product",
            joinColumns = @JoinColumn(name = "product_nummer"),
            inverseJoinColumns = @JoinColumn(name = "kaart_nummer")
    )
    private List<OVChipkaart> ovChipkaarts;

    public Product() {
    }

    public Product(int nummer, String naam, String beschrijving, double prijs) {
        this.nummer = nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
        ovChipkaarts = new ArrayList<>();
    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public List<OVChipkaart> getOvChipkaarts() {
        return ovChipkaarts;
    }

    public void setOvChipkaarts(List<OVChipkaart> ovChipkaarts) {
        this.ovChipkaarts = ovChipkaarts;
    }

    public boolean addKaartnummer (OVChipkaart kaartnummer) {
        if (!ovChipkaarts.contains(kaartnummer)) {
            return ovChipkaarts.add(kaartnummer);
        }
        return false;
    }

    public boolean removekaartnummer(OVChipkaart kaartnummer) {
        if (ovChipkaarts.contains(kaartnummer)) {
            return ovChipkaarts.remove(kaartnummer);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Product #%d: %s %s €%s", nummer, naam, beschrijving, prijs);
    }
}
