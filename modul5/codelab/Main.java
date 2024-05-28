    package modul5.codelab;

    import java.util.ArrayList;
    import java.util.Scanner;

    public class Main {
        public static void main(String[] args) {
            ArrayList<String> daftarMahasiswa = new ArrayList<>();
            Scanner scanner = new Scanner(System.in);
    
            int i = 1;
            while (true) {
                try {
                    System.out.print("Masukkan nama ke-" + i + ": ");
                    String nama = scanner.nextLine();

                    if (nama.isEmpty()) {
                        throw new IllegalArgumentException("Nama tidak boleh kosong!");
                    }

                    if (nama.equals("selesai")) {
                        break;
                    }

                    daftarMahasiswa.add(nama);
                    i++;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            System.out.println("Daftar mahasiswa yang diinputkan:");
            for (String nama : daftarMahasiswa) {
                System.out.println("- " + nama);
            }
        }
    }

