package modul5.tugas.data;

import modul5.tugas.books.*;
import modul5.tugas.util.iMenu;
import java.util.ArrayList;
import java.util.Scanner;
import modul5.tugas.exception.custom.illegalAdminAccess;
import static modul5.tugas.com.main.LibrarySystem.*;

public class Admin extends User implements iMenu{

    Scanner scanner = new Scanner(System.in);

    public Admin() {
        super("admin");
    }

    public void login() {
        System.out.print("Masukkan Username (admin): ");
        String username = scanner.next();
        System.out.print("Masukkan Password (admin): ");
        String password = scanner.next();
        try {
            if (isAdmin(username, password)) {
                System.out.println("Login berhasil sebagai Admin");
                menu();
            }
        } catch (illegalAdminAccess e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        }
    }

    private boolean isAdmin(String username, String password) throws illegalAdminAccess {
        if (username.equals("admin") && password.equals("admin")) {
            return true;
        } else {
            throw new illegalAdminAccess("Invalid credentials");
        }
    }

    public void menu() {
        while (true) {
            try {
                System.out.println("Menu Admin");
                System.out.println("1. Tambah Mahasiswa");
                System.out.println("2. Tampilkan Mahasiswa");
                System.out.println("3. Input Buku");
                System.out.println("4. Tampilkan Daftar Buku");
                System.out.println("5. Logout");
                System.out.print("Pilih antara (1-5): ");
                if (!scanner.hasNextInt()) {
                    scanner.next();
                    throw new illegalAdminAccess("Invalid credentials");
                }
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        displayStudents();
                        break;
                    case 3:
                        inputBook();
                        break;
                    case 4:
                        displayBooks(new ArrayList<>(daftarBuku)); // Mengubah ArrayList menjadi array
                        break;
                    case 5:
                        System.out.println("Logout berhasil.");
                        return;
                    default:
                        throw new illegalAdminAccess("Invalid credentials");
                }
            } catch (illegalAdminAccess e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
            }
        }
    }

    @Override
    public void addStudent() {
        // Implementasi penambahan mahasiswa
        System.out.println("Menambahkan mahasiswa...");
        System.out.print("Masukkan Nama: ");
        scanner.nextLine(); // Menggunakan nextLine() untuk membaca nama dengan dua kata
        String name =scanner.nextLine();
        System.out.print("Masukkan NIM: ");
        String nim = scanner.next();
        scanner.nextLine();
        while(true){
            if (nim.length() != 15 ) {
                System.out.print("Nim Harus 15 Digit!!!\n");
                System.out.print("Masukkan NIM: ");
                nim = scanner.nextLine();
            } else if (checkNim(nim)){
                System.out.println("NIM sudah terdaftar");
                System.out.print("Masukkan NIM baru: ");
                nim = scanner.nextLine();
            } else {
                break;
            }
        }
        System.out.print("Masukkan Fakultas: ");
        String faculty = scanner.nextLine();
        System.out.print("Masukkan Program Studi: ");
        String studyProgram = scanner.nextLine();
        daftarSiswa.add(new Student(nim, name, faculty, studyProgram));
        System.out.println("Mahasiswa dengan NIM " + nim + " berhasil ditambahkan.");
    }


    // Admin.java
    private void inputBook() {
        // Implementasi input buku
        System.out.println("Memasukkan buku...");
        System.out.println("Pilih jenis buku:");
        System.out.println("1. History Book");
        System.out.println("2. Story Book");
        System.out.println("3. Text Book");
        System.out.print("Pilih jenis buku (1-3): ");
        int bookType = scanner.nextInt();
        scanner.nextLine();

        String idBuku, judul, author, category;
        int stok;
        System.out.print("Masukkan judul buku: ");
        judul = scanner.nextLine();
        System.out.print("Masukkan author buku: ");
        author = scanner.nextLine();
        System.out.print("Masukkan category buku: ");
        category = scanner.nextLine();
        System.out.print("Masukkan stok buku: ");
        stok = scanner.nextInt();
        scanner.nextLine();

        switch (bookType) {
            case 1:
                idBuku = generateId("HB");
                daftarBuku.add(new HistoryBook(idBuku, judul, stok, category, author));
                break;
            case 2:
                idBuku = generateId("SB");
                daftarBuku.add(new StoryBook(idBuku, judul, stok, category, author));
                break;
            case 3:
                idBuku = generateId("TB");
                daftarBuku.add(new TextBook(idBuku, judul, stok, category, author));
                break;
            default:
                System.out.println("Pilihan tidak valid!");
                return;
        }
        System.out.println("Buku berhasil ditambahkan.");
    }

    @Override
    public void displayBooks(ArrayList<Book> bookList) {
        // Implementasi menampilkan daftar buku
        System.out.println("Daftar Buku Tersedia:");
        System.out.println("================================================================");
        System.out.println("|| No. || Id Buku || Nama Buku || Author || Category || Stock ||");
        int index = 1;
        for (Book book : bookList) {
            if (book != null) {
                System.out.println("|| " + index + " || " + book.getIdBuku() + " || " + book.getJudul() + " || " + book.getAuthor() + " || " + book.getCategory() + "  || " + book.getStok() + " ||");
                index++;
            }
        }
        System.out.println("================================================================");
    }


    private void displayStudents() {
        System.out.println("Daftar Mahasiswa yang terdaftar:");
        for (Student student : daftarSiswa) {
            System.out.println("\nNama: " + student.getName());
            System.out.println("NIM: " + student.getNim());
            System.out.println("Fakultas: " + student.getFaculty());
            System.out.println("Program Studi: " + student.getStudyProgram());
            if (!student.getBorrowedBooks().isEmpty()) {
                System.out.println("  Meminjam Buku:");
                for (Book book : student.getBorrowedBooks()) {
                    System.out.println("    - " + book.getJudul());
                }
            }
        }
    }

    private String generateId(String prefix) {
        // Implementasi pembuatan ID unik
        // Contoh: HB001, SB002, TB003, dst.
        int nextId = i + 1;
        return prefix + String.format("%03d", nextId);
    }
}
