import java.util.Random;

public class Others {
    public static int [][] mainMassiv;

    public static int[][] generateRandomPoints(int numPoints) {

        int[][] points = new int[numPoints][2]; // Создаем массив точек заданного размера
        Random random = new Random(); // Создаем объект для генерации случайных чисел

        for (int i = 0; i < numPoints; i++) {
            int x = random.nextInt(101); // Генерируем случайную координату x в диапазоне 0-100
            int y = random.nextInt(101); // Генерируем случайную координату y в диапазоне 0-100
            points[i] = new int[]{x, y}; // Создаем массив с двумя элементами для каждой точки с заданными координатами и добавляем его в массив
        }
        mainMassiv = points;
        return points; // Возвращаем массив точек
    }

    public static double[][] createAdjacencyMatrix(int[][] points) {
        int n = points.length; // Получаем количество точек в массиве
        double[][] adjacencyMatrix = new double[n][n]; // Создаем матрицу смежности заданной размерности
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                double distance = Math.sqrt(Math.pow(points[i][0] - points[j][0], 2) + Math.pow(points[i][1] - points[j][1], 2)); // Вычисляем расстояние между точками i и j
                adjacencyMatrix[i][j] = adjacencyMatrix[j][i] = distance; // Записываем расстояние в соответствующие элементы матрицы смежности
            }
        }

        return adjacencyMatrix; // Возвращаем матрицу смежности
    }
    public static int[][] rearrangeSubArrays(int[] order) {
        int[][] rearrangedMainMassiv = new int[mainMassiv.length][2]; // Создаем новый двумерный массив, в который будут записаны переставленные подмассивы
        for (int i = 0; i < order.length; i++) { // Проходим по массиву order с индексами в нужном порядке
            rearrangedMainMassiv[i] = mainMassiv[order[i]]; // Присваиваем элементам из переставляемого массива значения, определенные порядком в массиве order
        }

        return rearrangedMainMassiv; // Возвращаем переставленный двумерный массив
    }

}


