public class Megallo {
    private String nev;

    public Megallo(String nev) {
        this.nev = nev;
    }

    public String getNev() {
        return nev;
    }

    @Override
    public String toString() {
        return nev;
    }
}