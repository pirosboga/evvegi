import java.util.ArrayList;
import java.util.List;

public class Jarat {
    protected String jaratSzam;
    protected String tipus;
    protected List<Megallo> megallok;
    protected List<String> indulasiIdok;
    protected int kesesPerc;

    public Jarat(String jaratSzam, String tipus) {
        this.jaratSzam = jaratSzam;
        this.tipus = tipus;
        this.megallok = new ArrayList<>();
        this.indulasiIdok = new ArrayList<>();
        this.kesesPerc = 0;
    }

    public void megalloHozzaad(Megallo megallo) {
        megallok.add(megallo);
    }

    public void idoHozzaad(String ido) {
        indulasiIdok.add(ido);
    }

    public String getJaratSzam() { return jaratSzam; }
    public String getTipus() { return tipus; }
    public List<Megallo> getMegallok() { return megallok; }
    public List<String> getIndulasiIdok() { return indulasiIdok; }
    public int getKesesPerc() { return kesesPerc; }

    public void setKesesPerc(int kesesPerc) {
        this.kesesPerc = kesesPerc;
    }

    public void info() {
        System.out.println(">>> " + tipus + " | Jarat: " + jaratSzam + " | Keses: " + kesesPerc + " perc");
        System.out.print(" Erintett megallohelyek: ");
        for (Megallo m : megallok) {
            System.out.print(m.getNev() + " -> ");
        }
        System.out.println("VEGE");
        System.out.println("Indulasok: " + indulasiIdok);
    }
}