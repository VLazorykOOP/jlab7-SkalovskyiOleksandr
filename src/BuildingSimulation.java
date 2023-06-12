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
        private static final int MAX_STEPS = 30;
        private static final int ROTATION_AREA_WIDTH = WIDTH / 4; // Ширина площини ротації
        private static final int ROTATION_AREA_HEIGHT = HEIGHT / 4; // Висота площини ротації

        @Override
        public void run() {
            Random random = new Random();
            int steps = 0;

            while (steps < MAX_STEPS) {
                int startX = random.nextInt(ROTATION_AREA_WIDTH);
                int startY = random.nextInt(ROTATION_AREA_HEIGHT);
                int endX = random.nextInt(ROTATION_AREA_WIDTH);
                int endY = random.nextInt(ROTATION_AREA_HEIGHT);

                moveCapitalBuilding(startX, startY, endX, endY);

                try {
                    Thread.sleep(1000); // Пауза між генераціями будинків
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                steps++;
            }
        }
    }

    static class WoodenBuildingRunnable implements Runnable {
        private static final int MAX_STEPS = 30;
        private static final int ROTATION_AREA_WIDTH = WIDTH / 4; // Ширина площини ротації
        private static final int ROTATION_AREA_HEIGHT = HEIGHT / 4; // Висота площини ротації

        @Override
        public void run() {
            Random random = new Random();
            int steps = 0;

            while (steps < MAX_STEPS) {
                int startX = WIDTH / 2 + random.nextInt(ROTATION_AREA_WIDTH);
                int startY = HEIGHT / 2 + random.nextInt(ROTATION_AREA_HEIGHT);
                int endX = WIDTH / 2 + random.nextInt(ROTATION_AREA_WIDTH);
                int endY = HEIGHT / 2 + random.nextInt(ROTATION_AREA_HEIGHT);

                moveWoodenBuilding(startX, startY, endX, endY);

                try {
                    Thread.sleep(1000); // Пауза між генераціями будинків
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                steps++;
            }
        }
    }

    private static void moveCapitalBuilding(int startX, int startY, int endX, int endY) {
        Point currentPos = new Point(startX, startY);

        while (currentPos.x != endX || currentPos.y != endY) {
            if (currentPos.x > endX) {
                currentPos.x -= V;
            } else if (currentPos.x < endX) {
                currentPos.x += V;
            }

            if (currentPos.y > endY) {
                currentPos.y -= V;
            } else if (currentPos.y < endY) {
                currentPos.y += V;
            }

            System.out.println("Капітальний будинок рухається до точки: " + currentPos);

            try {
                Thread.sleep(100); // Пауза між кроками руху будинка
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Капітальний будинок досяг кінцевої точки: " + currentPos);
    }

    private static void moveWoodenBuilding(int startX, int startY, int endX, int endY) {
        Point currentPos = new Point(startX, startY);

        while (currentPos.x != endX || currentPos.y != endY) {
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

            System.out.println("Дерев'яний будинок рухається до точки: " + currentPos);

            try {
                Thread.sleep(100); // Пауза між кроками руху будинка
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Дерев'яний будинок досяг кінцевої точки: " + currentPos);
    }
}
