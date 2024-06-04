package com.example.modultugas6;

import com.example.modultugas6.models.Admin;
import com.example.modultugas6.models.Book;
import com.example.modultugas6.models.Student;
import com.example.modultugas6.models.User;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class LibrarySystemApp extends Application {
    private ArrayList<Student> students = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library System");

        // Main Menu
        Label label = new Label("===== Library System =====");

        ImageView imageView = new ImageView(new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSzGGJCOSdL-Axy37uCrOk2QBIV3NQ_GRFNHw&s"));
        imageView.setFitHeight(150);
        imageView.setFitWidth(180);
        Button studentButton = new Button("Login sebagai Mahasiswa");
        Button adminButton = new Button("Login sebagai Admin");
        Button exitButton = new Button("Keluar");

        studentButton.setBackground(new Background(new BackgroundFill(Color.BLUE,new CornerRadii(5), null)));

        studentButton.setOnAction(e -> showStudentLogin(primaryStage));
        adminButton.setOnAction(e -> showAdminLogin(primaryStage));
        exitButton.setOnAction(e -> System.exit(0));

        VBox mainMenu = new VBox(10,imageView, label, studentButton, adminButton, exitButton);
        mainMenu.setAlignment(Pos.CENTER);
        Scene mainScene = new Scene(mainMenu, 380, 500);

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void showStudentLogin(Stage stage) {
        Label label = new Label("Masukkan NIM:");
        TextField nimField = new TextField();
        Button loginButton = new Button("Login");
        Button backButton = new Button("Kembali");

        loginButton.setOnAction(e -> {
            try {
                String nim = nimField.getText();
                if (nim.isEmpty()) {
                    throw new IllegalArgumentException("NIM tidak boleh kosong.");
                }
                if (nim.length() != 15) {
                    throw new IllegalArgumentException("NIM harus memiliki panjang 15 angka.");
                }
                for (Student student : students) {
                    if (student.getNim().equals(nim)) {
                        showStudentMenu(stage, student);
                        return;
                    }
                }
                throw new IllegalArgumentException("NIM tidak ditemukan.");
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.showAndWait();
            }
        });

        backButton.setOnAction(e -> start(stage));

        VBox loginMenu = new VBox(10, label, nimField, loginButton, backButton);
        loginMenu.setAlignment(Pos.BASELINE_LEFT);
        Scene loginScene = new Scene(loginMenu, 380, 500);
        stage.setScene(loginScene);
    }

    private void showAdminLogin(Stage stage) {
        Label userLabel = new Label("Masukkan Username:");
        TextField userField = new TextField();
        Label passLabel = new Label("Masukkan Password:");
        PasswordField passField = new PasswordField();
        Button loginButton = new Button("Login");
        Button backButton = new Button("Kembali");

        loginButton.setOnAction(e -> {
            try {
                String username = userField.getText();
                String password = passField.getText();
                if (username.isEmpty() || password.isEmpty()) {
                    throw new IllegalArgumentException("Username dan password tidak boleh kosong.");
                }
                Admin admin = new Admin();

                if (admin.login(username, password)) {
                    showAdminMenu(stage, admin);
                } else {
                    throw new IllegalArgumentException("Username atau password salah.");
                }
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.showAndWait();
            }
        });

        backButton.setOnAction(e -> start(stage));

        VBox loginMenu = new VBox(10, userLabel, userField, passLabel, passField, loginButton, backButton);
        loginMenu.setAlignment(Pos.CENTER);
        Scene loginScene = new Scene(loginMenu, 380, 500);
        stage.setScene(loginScene);
    }

    private void showStudentMenu(Stage stage, Student student) {
        Label label = new Label("===== Student Menu =====");
        Button displayBooksButton = new Button("Display Books");
        displayBooksButton.setFont(Font.font("Times New Roman",14));
        Button choiceBookButton = new Button("Choice Book");
        Button returnBookButton = new Button("Return Book");
        Button backButton = new Button("Back to Main Menu");



        displayBooksButton.setOnAction(e -> showBooks(stage, student));
        choiceBookButton.setOnAction(e -> showChoiceBook(stage, student));
        returnBookButton.setOnAction(e -> showReturnBook(stage, student));
        backButton.setOnAction(e -> start(stage));

        VBox studentMenu = new VBox(10, label, displayBooksButton, choiceBookButton, returnBookButton, backButton);
        studentMenu.setAlignment(Pos.CENTER);
        Scene studentScene = new Scene(studentMenu, 380, 500);
        stage.setScene(studentScene);
    }

    private void showReturnBook(Stage stage, Student student) {
        Label label = new Label("===== Return Book =====");
        ListView<String> borrowedBooksListView = new ListView<>();

        for (Book book : student.getBorrowedBooks()) {
            borrowedBooksListView.getItems().add(book.getIdBuku() + " - " + book.getJudul());
        }

        Button returnButton = new Button("Return");
        Button backButton = new Button("Back to Student Menu");

        returnButton.setOnAction(e -> {
            try {
                String selectedItem = borrowedBooksListView.getSelectionModel().getSelectedItem();
                if (selectedItem == null) {
                    throw new IllegalArgumentException("Pilih buku yang ingin dikembalikan.");
                }
                String selectedBookId = selectedItem.split(" - ")[0];
                Book selectedBook = Book.getBookById(selectedBookId);
                if (selectedBook == null) {
                    throw new IllegalArgumentException("Buku tidak ditemukan.");
                }
                selectedBook.returnBook();
                student.removeBorrowedBook(selectedBook);
                showStudentMenu(stage, student);
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.showAndWait();
            }
        });

        backButton.setOnAction(e -> showStudentMenu(stage, student));

        VBox returnBookLayout = new VBox(10, label, borrowedBooksListView, returnButton, backButton);
        returnBookLayout.setAlignment(Pos.CENTER);
        Scene returnBookScene = new Scene(returnBookLayout, 380, 500);
        stage.setScene(returnBookScene);
    }

    private void showAdminMenu(Stage stage, Admin admin) {
        Label label = new Label("===== Admin Menu =====");
        Button displayBooksButton = new Button("Display Books");
        Button displayStudentsButton = new Button("Display Students");
        Button addBookButton = new Button("Add Book");
        Button addStudentButton = new Button("Add Student");
        Button backButton = new Button("Kembali ke Menu Utama");

        displayBooksButton.setOnAction(e -> showBooks(stage, admin));
        displayStudentsButton.setOnAction(e -> showStudents(stage, admin));
        addBookButton.setOnAction(e -> showAddBookForm(stage, admin));
        addStudentButton.setOnAction(e -> showAddStudentForm(stage, admin));
        backButton.setOnAction(e -> start(stage));

        VBox adminMenu = new VBox(10, label, displayBooksButton, displayStudentsButton, addBookButton, addStudentButton, backButton);
        adminMenu.setAlignment(Pos.CENTER_LEFT);
        Scene adminScene = new Scene(adminMenu, 380, 500);
        adminMenu.setBackground(new Background(Color.YELLOW));
        stage.setScene(adminScene);
    }

    private void showAddBookForm(Stage stage, Admin admin) {
        Label label = new Label("=====Add Book =====");
        TextField idField = new TextField();
        idField.setPromptText("ID Buku");
        TextField titleField = new TextField();
        titleField.setPromptText("Judul");
        TextField stockField = new TextField();
        stockField.setPromptText("Stok");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Kategori");
        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        TextField durationField = new TextField();
        durationField.setPromptText("Durasi");

        Button addButton = new Button("Add");
        Button backButton = new Button("Kembali");

        addButton.setOnAction(e -> {
            try {
                String id = idField.getText();
                String title = titleField.getText();
                int stock = Integer.parseInt(stockField.getText());
                String category = categoryField.getText();
                String author = authorField.getText();
                int duration = Integer.parseInt(durationField.getText());

                if (id.isEmpty() || title.isEmpty() || category.isEmpty() || author.isEmpty()) {
                    throw new IllegalArgumentException("Semua field harus diisi.");
                }

                Book newBook = new Book(id, title, stock, category, author, duration);
                Book.addBook(newBook); // Ensure the book is added to the global list
                showAdminMenu(stage, admin);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Stok dan Durasi harus berupa angka.");
                alert.showAndWait();
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.showAndWait();
            }
        });

        backButton.setOnAction(e -> showAdminMenu(stage, admin));

        VBox addBookForm = new VBox(10, label, idField, titleField, stockField, categoryField, authorField, durationField, addButton, backButton);
        addBookForm.setAlignment(Pos.CENTER);
        Scene addBookScene = new Scene(addBookForm, 380, 500);
        stage.setScene(addBookScene);
    }

    private void showAddStudentForm(Stage stage, Admin admin) {
        Label label = new Label("===== Add Student =====");
        TextField nimField = new TextField();
        nimField.setPromptText("NIM");
        TextField nameField = new TextField();
        nameField.setPromptText("Nama");
        TextField facultyField = new TextField();
        facultyField.setPromptText("Fakultas");
        TextField studyProgramField = new TextField();
        studyProgramField.setPromptText("Program Studi");

        Button addButton = new Button("Add");
        Button backButton = new Button("Kembali");

        addButton.setOnAction(e -> {
            try {
                String nim = nimField.getText();
                String name = nameField.getText();
                String faculty = facultyField.getText();
                String studyProgram = studyProgramField.getText();

                if (nim.isEmpty() || name.isEmpty() || faculty.isEmpty() || studyProgram.isEmpty()) {
                    throw new IllegalArgumentException("Semua field harus diisi.");
                }

                if (nim.length() != 15) {
                    throw new IllegalArgumentException("NIM harus memiliki panjang 15 angka.");
                }

                Student newStudent = new Student(nim, name, faculty, studyProgram);
                students.add(newStudent); // Add student to the students list
                showAdminMenu(stage, admin);
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.showAndWait();
            }
        });

        backButton.setOnAction(e -> showAdminMenu(stage, admin));

        VBox addStudentForm = new VBox(10, label, nimField, nameField, facultyField, studyProgramField, addButton, backButton);
        addStudentForm.setAlignment(Pos.CENTER);
        Scene addStudentScene = new Scene(addStudentForm, 380, 500);
        stage.setScene(addStudentScene);
    }

    private void showBooks(Stage stage, User user) {
        Label label = new Label("===== Display Books =====");
        ListView<String> bookListView = new ListView<>();

        for (Book book : Book.getAllBooks()) {
            bookListView.getItems().add(book.getIdBuku() + " - " + book.getJudul() + " - " + book.getStok() + " - " + book.getCategory() + " - " + book.getAuthor() + " - " + book.getDuration());
        }

        Button backButton = new Button("Kembali");
        backButton.setOnAction(e -> {
            if (user instanceof Admin) {
                showAdminMenu(stage, (Admin) user);
            } else if (user instanceof Student) {
                showStudentMenu(stage, (Student) user);
            }
        });

        VBox displayBooksLayout = new VBox(10, label, bookListView, backButton);
        displayBooksLayout.setAlignment(Pos.CENTER);
        Scene displayBooksScene = new Scene(displayBooksLayout, 380, 500);
        stage.setScene(displayBooksScene);
    }

    private void showChoiceBook(Stage stage, Student student) {
        Label label = new Label("===== Choice Book =====");
        ListView<String> bookListView = new ListView<>();

        for (Book book : Book.getAllBooks()) {
            bookListView.getItems().add(book.getIdBuku() + " - " + book.getJudul() + " - " + book.getStok() + " - " + book.getCategory() + " - " + book.getAuthor() + " - " + book.getDuration());
        }

        Button borrowButton = new Button("Borrow");
        Button backButton = new Button("Kembali");

        borrowButton.setOnAction(e -> {
            try {
                String selectedItem = bookListView.getSelectionModel().getSelectedItem();
                if (selectedItem == null) {
                    throw new IllegalArgumentException("Pilih buku yang ingin dipinjam.");
                }
                String selectedBookId = selectedItem.split(" - ")[0];
                Book selectedBook = Book.getBookById(selectedBookId);
                if (selectedBook == null) {
                    throw new IllegalArgumentException("Buku tidak ditemukan.");
                }
                if (selectedBook.getStok() <= 0) {
                    throw new IllegalArgumentException("Buku tidak tersedia atau stok habis.");
                }
                selectedBook.borrowBook();
                student.addBorrowedBook(selectedBook);
                showStudentMenu(stage, student);
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.showAndWait();
            }
        });

        backButton.setOnAction(e -> showStudentMenu(stage, student));

        VBox choiceBookLayout = new VBox(10, label, bookListView, borrowButton, backButton);
        choiceBookLayout.setAlignment(Pos.CENTER);
        Scene choiceBookScene = new Scene(choiceBookLayout, 380, 500);
        stage.setScene(choiceBookScene);
    }

    private void showStudents(Stage stage, Admin admin) {
        Label label = new Label("===== Display Students =====");
        ListView<String> studentListView = new ListView<>();

        for (Student student : students) {
            studentListView.getItems().add(student.getNim() + " - " + student.getName() + " - " + student.getFaculty() + " - " + student.getStudyProgram());
        }

        Button backButton = new Button("Kembali");
        backButton.setOnAction(e -> showAdminMenu(stage, admin));

        VBox displayStudentsLayout = new VBox(10, label, studentListView, backButton);
        displayStudentsLayout.setAlignment(Pos.CENTER);
        Scene displayStudentsScene = new Scene(displayStudentsLayout, 380, 500);
        stage.setScene(displayStudentsScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
