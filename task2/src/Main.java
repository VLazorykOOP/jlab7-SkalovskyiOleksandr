import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

interface Displayable {
    void display();
}

public class Main {
    private static BlockingQueue<Record> queue = new LinkedBlockingQueue<>();
    private static Object lock = new Object();
    private static JComboBox<ThreadPriority> bookPriorityComboBox;
    private static JComboBox<ThreadPriority> moviePriorityComboBox;
    private static JTextField titleTextField;
    private static JTextField authorTextField;
    private static JTextField yearTextField;
    private static JTextField directorTextField;
    private static JTextField durationTextField;

    public static void main(String[] args) {
        ArrayList<Record> records = new ArrayList<>();

        JFrame frame = new JFrame("AI System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 2));

        JPanel bookPanel = new JPanel();
        bookPanel.setLayout(new FlowLayout());
        JLabel bookTitleLabel = new JLabel("Назва книги:");
        JLabel bookAuthorLabel = new JLabel("Автор книги:");
        JLabel bookYearLabel = new JLabel("Рік видання:");
        titleTextField = new JTextField(15);
        authorTextField = new JTextField(15);
        yearTextField = new JTextField(15);
        JButton addBookButton = new JButton("Додати книгу");

        bookPanel.add(bookTitleLabel);
        bookPanel.add(titleTextField);
        bookPanel.add(bookAuthorLabel);
        bookPanel.add(authorTextField);
        bookPanel.add(bookYearLabel);
        bookPanel.add(yearTextField);
        bookPanel.add(addBookButton);

        JPanel moviePanel = new JPanel();
        moviePanel.setLayout(new FlowLayout());
        JLabel movieTitleLabel = new JLabel("Назва фільму:");
        JLabel movieAuthorLabel = new JLabel("Автор фільму:");
        JLabel movieDirectorLabel = new JLabel("Режисер:");
        JLabel movieDurationLabel = new JLabel("Тривалість:");
        titleTextField = new JTextField(15);
        authorTextField = new JTextField(15);
        directorTextField = new JTextField(15);
        durationTextField = new JTextField(15);
        JButton addMovieButton = new JButton("Додати фільм");

        moviePanel.add(movieTitleLabel);
        moviePanel.add(titleTextField);
        moviePanel.add(movieAuthorLabel);
        moviePanel.add(authorTextField);
        moviePanel.add(movieDirectorLabel);
        moviePanel.add(directorTextField);
        moviePanel.add(movieDurationLabel);
        moviePanel.add(durationTextField);
        moviePanel.add(addMovieButton);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        JButton stopButton = new JButton("Зупинити");
        JButton startButton = new JButton("Відновити");

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopAI(records);
            }
        });

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startAI(records);
            }
        });

        bookPriorityComboBox = new JComboBox<>(ThreadPriority.values());
        moviePriorityComboBox = new JComboBox<>(ThreadPriority.values());

        controlPanel.add(stopButton);
        controlPanel.add(startButton);
        controlPanel.add(new JLabel("Пріоритет книг:"));
        controlPanel.add(bookPriorityComboBox);
        controlPanel.add(new JLabel("Пріоритет фільмів:"));
        controlPanel.add(moviePriorityComboBox);

        addBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleTextField.getText();
                String author = authorTextField.getText();
                int year = Integer.parseInt(yearTextField.getText());
                Book book = new Book(title, author, year);
                records.add(book);
                titleTextField.setText("");
                authorTextField.setText("");
                yearTextField.setText("");
            }
        });

        addMovieButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleTextField.getText();
                String author = authorTextField.getText();
                String director = directorTextField.getText();
                int duration = Integer.parseInt(durationTextField.getText());
                Movie movie = new Movie(title, author, director, duration);
                records.add(movie);
                titleTextField.setText("");
                authorTextField.setText("");
                directorTextField.setText("");
                durationTextField.setText("");
            }
        });

        frame.add(bookPanel);
        frame.add(moviePanel);
        frame.add(controlPanel);
        frame.setVisible(true);
    }

    public static void stopAI(ArrayList<Record> records) {
        synchronized (lock) {
            for (Record record : records) {
                if (record instanceof Book) {
                    Book book = (Book) record;
                    book.stop();
                } else if (record instanceof Movie) {
                    Movie movie = (Movie) record;
                    movie.stop();
                }
            }
            lock.notifyAll();
        }
    }

    public static void startAI(ArrayList<Record> records) {
        synchronized (lock) {
            for (Record record : records) {
                if (record instanceof Book) {
                    Book book = (Book) record;
                    ThreadPriority priority = (ThreadPriority) bookPriorityComboBox.getSelectedItem();
                    BookAI bookAI = new BookAI(book, queue, lock, priority);
                    Thread thread = new Thread(bookAI);
                    thread.start();
                } else if (record instanceof Movie) {
                    Movie movie = (Movie) record;
                    ThreadPriority priority = (ThreadPriority) moviePriorityComboBox.getSelectedItem();
                    MovieAI movieAI = new MovieAI(movie, queue, lock, priority);
                    Thread thread = new Thread(movieAI);
                    thread.start();
                }
            }
        }
    }

    static class Record implements Displayable {
        protected String title;
        protected String author;

        public Record(String title, String author) {
            this.title = title;
            this.author = author;
        }

        public void display() {
            System.out.println("Назва: " + title + ", Автор: " + author);
        }
    }

    static class Book extends Record {
        private int year;
        private volatile boolean stopped = false;

        public Book(String title, String author, int year) {
            super(title, author);
            this.year = year;
        }

        public void stop() {
            stopped = true;
        }

        public boolean isStopped() {
            return stopped;
        }

        public void display() {
            super.display();
            System.out.println("Рік видання: " + year);
        }
    }

    static class Movie extends Record {
        private String director;
        private int duration;
        private volatile boolean stopped = false;

        public Movie(String title, String author, String director, int duration) {
            super(title, author);
            this.director = director;
            this.duration = duration;
        }

        public void stop() {
            stopped = true;
        }

        public boolean isStopped() {
            return stopped;
        }

        public void display() {
            super.display();
            System.out.println("Режисер: " + director);
            System.out.println("Тривалість: " + duration + " хв");
        }
    }

    static class BookAI implements Runnable {
        private Book book;
        private BlockingQueue<Record> queue;
        private Object lock;
        private ThreadPriority priority;

        public BookAI(Book book, BlockingQueue<Record> queue, Object lock, ThreadPriority priority) {
            this.book = book;
            this.queue = queue;
            this.lock = lock;
            this.priority = priority;
        }

        public void run() {
            while (!book.isStopped()) {
                try {
                    queue.put(book);
                    synchronized (lock) {
                        lock.notify();
                        lock.wait();
                    }
                    if (!book.isStopped()) {
                        book.display();
                    }
                    Thread.sleep(priority.getDelay());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class MovieAI implements Runnable {
        private Movie movie;
        private BlockingQueue<Record> queue;
        private Object lock;
        private ThreadPriority priority;

        public MovieAI(Movie movie, BlockingQueue<Record> queue, Object lock, ThreadPriority priority) {
            this.movie = movie;
            this.queue = queue;
            this.lock = lock;
            this.priority = priority;
        }

        public void run() {
            while (!movie.isStopped()) {
                try {
                    queue.put(movie);
                    synchronized (lock) {
                        lock.notify();
                        lock.wait();
                    }
                    if (!movie.isStopped()) {
                        movie.display();
                    }
                    Thread.sleep(priority.getDelay());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    enum ThreadPriority {
        LOW(2000),
        MEDIUM(1000),
        HIGH(500);

        private int delay;

        ThreadPriority(int delay) {
            this.delay = delay;
        }

        public int getDelay() {
            return delay;
        }
    }
}
