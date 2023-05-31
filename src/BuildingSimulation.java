import java.awt.Point;
import java.util.Random;

public class BuildingSimulation {
    private static final int WIDTH = 800; // Ширина області симуляції
    private static final int HEIGHT = 600; // Висота області симуляції
    private static final int V = 5; // Швидкість руху будинків

    public static void main(String[] args) {
        Thread capitalThread = new Thread(new CapitalBuildingRunnable());
        Thread woodenThread = new Thread(new WoodenBuildingRunnable());

        capitalThread.start();
        woodenThread.start();
    }

    static class CapitalBuildingRunnable implements Runnable {
        @Override
        public void run() {
            Random random = new Random();

            while (true) {
                // Генеруємо початкову точку будинку в лівій верхній чверті області
                int startX = random.nextInt(WIDTH / 2);
                int startY = random.nextInt(HEIGHT / 2);

                // Генеруємо кінцеву точку будинку в межах області
                int endX = random.nextInt(WIDTH / 2);
                int endY = random.nextInt(HEIGHT / 2);

                moveBuilding(startX, startY, endX, endY);

                try {
                    Thread.sleep(1000); // Пауза між генераціями будинків
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class WoodenBuildingRunnable implements Runnable {
        @Override
        public void run() {
            Random random = new Random();

            while (true) {
                // Генеруємо початкову точку будинку в нижній правій чверті області
                int startX = WIDTH / 2 + random.nextInt(WIDTH / 2);
                int startY = HEIGHT / 2 + random.nextInt(HEIGHT / 2);

                // Генеруємо кінцеву точку будинку в межах області
                int endX = WIDTH / 2 + random.nextInt(WIDTH / 2);
                int endY = HEIGHT / 2 + random.nextInt(HEIGHT / 2);

                moveBuilding(startX, startY, endX, endY);

                try {
                    Thread.sleep(1000); // Пауза між генераціями будинків
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void moveBuilding(int startX, int startY, int endX, int endY) {
        Point currentPos = new Point(startX, startY);

        while (currentPos.x != endX || currentPos.y != endY) {
            // Рухаємо будинок в напрямку кінцевої точки зі швидкістю V
            if (currentPos.x < endX) {
                currentPos.x += V;
            } else if (currentPos.x > endX) {
                currentPos.x -= V;
            }

            if (currentPos.y < endY) {
                currentPos.y += V;
            } else if (currentPos.y > endY) {
                currentPos.y -= V;
            }

            System.out.println("Будинок рухається до точки: " + currentPos);

            try {
                Thread.sleep(100); // Пауза між кроками руху будинка
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Будинок досяг кінцевої точки: " + currentPos);
    }
}
