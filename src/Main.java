import java.io.*;
import java.util.*;

public class Main {
    private static List<Jarat> jaratok = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String FAJL_NEV = "jaratok.txt";

    public static void main(String[] args) {
        beolvasFajlbol();

        if (jaratok.isEmpty()) {
            Busz b3900 = new Busz("3900");
            b3900.megalloHozzaad(new Megallo("Satoraljaujhely"));
            b3900.megalloHozzaad(new Megallo("Szephalom"));
            b3900.megalloHozzaad(new Megallo("Mikohaza"));
            b3900.megalloHozzaad(new Megallo("Palhaza"));
            b3900.idoHozzaad("06:15");
            b3900.idoHozzaad("14:30");
            jaratok.add(b3900);
            mentesFajlba();
        }

        while (true) {
            System.out.println("\nTOMEGKOZLEKEDESI MENETREND");
            System.out.println("1. Menetrend megtekintese (Minden jarat)");
            System.out.println("2. Utvonaltervezes (A-bol B-be)");
            System.out.println("3. Adminisztracio: Keses beirasa");
            System.out.println("4. Kilepes es Mentes");
            System.out.print("Valassz egy opciót: ");

            String opcio = scanner.nextLine();

            switch (opcio) {
                case "1":
                    menetrendListazas();
                    break;
                case "2":
                    utvonalTervezes();
                    break;
                case "3":
                    kesesBeallitas();
                    break;
                case "4":
                    mentesFajlba();
                    System.out.println("Adatok elmentve. Viszlat!");
                    System.exit(0);
                default:
                    System.out.println("Ervenytelen valasz, probald ujra!");
            }
        }
    }

    private static void menetrendListazas() {
        if (jaratok.isEmpty()) {
            System.out.println("Nincsenek jaratok a rendszerben.");
            return;
        }
        for (Jarat j : jaratok) {
            j.info();
            System.out.println("--------------------");
        }
    }

    private static void kesesBeallitas() {
        System.out.print("Melyik jarat kesik?: ");
        String szam = scanner.nextLine();

        Jarat talalt = null;
        for (Jarat j : jaratok) {
            if (j.getJaratSzam().equals(szam)) {
                talalt = j;
                break;
            }
        }

        if (talalt != null) {
            System.out.print("Hany percet kesik?: ");
            try {
                int perc = Integer.parseInt(scanner.nextLine());
                talalt.setKesesPerc(perc);
                mentesFajlba();
                System.out.println("Keses sikeresen regisztralva!");
            } catch (NumberFormatException e) {
                System.out.println("Hibas formatum, szamot adj meg!");
            }
        } else {
            System.out.println("Nem talalhato ilyen jarat.");
        }
    }

    private static void utvonalTervezes() {
        System.out.print("Indulasi megallo: ");
        String indulas = scanner.nextLine();
        System.out.print("Cel megallo: ");
        String cel = scanner.nextLine();

        System.out.println("\nKereses atszallas nelkul...");
        boolean vanTalalat = false;

        for (Jarat j : jaratok) {
            int indulasIndex = -1;
            int celIndex = -1;

            for (int i = 0; i < j.getMegallok().size(); i++) {
                if (j.getMegallok().get(i).getNev().equalsIgnoreCase(indulas)) indulasIndex = i;
                if (j.getMegallok().get(i).getNev().equalsIgnoreCase(cel)) celIndex = i;
            }

            if (indulasIndex != -1 && celIndex != -1 && indulasIndex < celIndex) {
                System.out.println("-> Megfelelo jarat: " + j.getTipus() + " " + j.getJaratSzam());
                System.out.println("   Eredeti indulasok errol a vegallomasrol: " + j.getIndulasiIdok());
                if (j.getKesesPerc() > 0) {
                    System.out.println("   Figyelem! A jarat jelenleg " + j.getKesesPerc() + " percet kesis!");
                }
                vanTalalat = true;
            }
        }

        if (!vanTalalat) {
            System.out.println("Sajnos kozvetlen jarat nem talalhato.");
        }
    }

    private static void mentesFajlba() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FAJL_NEV))) {
            for (Jarat j : jaratok) {
                StringBuilder megallokStr = new StringBuilder();
                for (Megallo m : j.getMegallok()) {
                    megallokStr.append(m.getNev()).append(",");
                }
                if (megallokStr.length() > 0) megallokStr.setLength(megallokStr.length() - 1);

                StringBuilder idokStr = new StringBuilder();
                for (String ido : j.getIndulasiIdok()) {
                    idokStr.append(ido).append(",");
                }
                if (idokStr.length() > 0) idokStr.setLength(idokStr.length() - 1);

                writer.println(j.getJaratSzam() + ";" + j.getTipus() + ";" + j.getKesesPerc() + ";" + megallokStr + ";" + idokStr);
            }
        } catch (IOException e) {
            System.out.println("Hiba a mentes soran: " + e.getMessage());
        }
    }

    private static void beolvasFajlbol() {
        File fajl = new File(FAJL_NEV);
        if (!fajl.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(fajl))) {
            String sor;
            while ((sor = reader.readLine()) != null) {
                if (sor.trim().isEmpty()) continue;
                String[] darabok = sor.split(";");

                String szam = darabok[0];
                String tipus = darabok[1];
                int keses = Integer.parseInt(darabok[2]);
                String[] megallokTomb = darabok[3].split(",");
                String[] idokTomb = darabok[4].split(",");

                Jarat j;
                if (tipus.equalsIgnoreCase("Busz")) {
                    j = new Busz(szam);
                } else {
                    j = new Villamos(szam);
                }

                j.setKesesPerc(keses);

                for (String mNev : megallokTomb) {
                    j.megalloHozzaad(new Megallo(mNev));
                }
                for (String ido : idokTomb) {
                    j.idoHozzaad(ido);
                }

                jaratok.add(j);
            }
        } catch (Exception e) {
            System.out.println("Hiba a beolvasasokor: " + e.getMessage());
        }
    }
}