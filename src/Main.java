import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {
    static AtomicInteger beautiful3 = new AtomicInteger(0); // Счетчик красивых никнеймов из 3 букв
    static AtomicInteger beautiful4 = new AtomicInteger(0); // Счетчик красивых никнеймов из 4 букв
    static AtomicInteger beautiful5 = new AtomicInteger(0); // Счетчик красивых никнеймов из 5 букв

    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];
        // Генерируем массив строк
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
//            System.out.println(texts[i]);
        }
        // Определяем пул потоков
        ExecutorService myThreads = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        // Поток поиска никнейма - Палиндрома
        Runnable findPalindrome = new Thread(() -> {
            for (String text : texts) {
                int j = 0;
                boolean find = true;
                while (j < text.length() - 1 - j) {
                    if (text.charAt(j) != text.charAt(text.length() - 1 - j)) {
                        find = false;
                        break;
                    }
                    j++;
                }
                if (find) {
                    countBeautiful(text);
//                    System.out.println("----\n" + text);
                }
            }
        });
        // Поток поиска никнейма состоящего из одинаковых букв
        Runnable findOneChar = new Thread(() -> {
            for (String text : texts) {
                int j = 0;
                boolean find = true;
                char char1 = text.charAt(0);
                while (j < text.length()) {
                    if (text.charAt(j) != char1) {
                        find = false;
                        break;
                    }
                    j++;
                }
                if (find) {
                    countBeautiful(text);
//                    System.out.println("----\n" + text);
                }
            }
        });
        // Поток поиска никнейма, в котором буквы в слове идут по возрастани
        Runnable letterUp = new Thread(() -> {
            for (String text : texts) {
                boolean find = true;
                for (int i = 0; i < text.length() - 1; i++) {
//                    System.out.println(text + " CharNum = " + (int) text.charAt(i));
                    if ((int) text.charAt(i) > (int) text.charAt(i + 1)) {
                        find = false;
                        break;
                    }
                }
                if (find) {
                    countBeautiful(text);
//                    System.out.println("----\n" + text);
                }
            }
        });
        // Запуск потоков
        myThreads.submit(findPalindrome);
        myThreads.submit(findOneChar);
        myThreads.submit(letterUp);
        // Вывод результата поиска в консоль
        System.out.println("Красивых слов с длиной 3: " + beautiful3 + " шт\nКрасивых слов с длиной 4: " +
                beautiful4 + " шт\nКрасивых слов с длиной 5: " + beautiful5 + " шт");
        // Завершение пула потоков
        myThreads.shutdown();
    }

    // Метод генерации текста
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    // Метод подсчета количества красивых никнеймов
    public static void countBeautiful(String str) {
        if (str.length() == 3) {
            beautiful3.getAndIncrement();
        } else if (str.length() == 4) {
            beautiful4.getAndIncrement();
        } else beautiful5.getAndIncrement();
    }
}