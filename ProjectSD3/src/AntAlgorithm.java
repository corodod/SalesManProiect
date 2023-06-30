import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class AntAlgorithm extends Algorithm {
    static int[] bestRouteN = null;
    double[][] distances;
    int antCount;
    int maxIterations;
    double alpha;
    double beta;
    double evaporationRate;
    double bestDistance = Double.MAX_VALUE;

    public AntAlgorithm() {}

    @Override
    public Map<String, String> getParams() {
        // Возвращает параметры алгоритма в виде словаря
        Map<String, String> params = new HashMap<>();
        params.put("alpha", String.valueOf(alpha));
        params.put("beta", String.valueOf(beta));
        params.put("antCount", String.valueOf(antCount));
        params.put("maxIterations", String.valueOf(maxIterations));
        params.put("evaporationRate", String.valueOf(evaporationRate));
        return params;
    }

    @Override
    public int[] getPath(double[][] matrix, Map<String, String> params) {
        // Получает матрицу расстояний и параметры алгоритма, возвращает лучший маршрут
        alpha = Double.parseDouble(params.get("alpha"));
        beta = Double.parseDouble(params.get("beta"));
        evaporationRate = Double.parseDouble(params.get("evaporationRate"));
        antCount = Integer.parseInt(params.get("antCount"));
        maxIterations = Integer.parseInt(params.get("maxIterations"));

        distances = matrix;
        int cityCount = matrix.length;
        double[][] pheromones = initializePheromones(cityCount);

        // Исследовательские итерации
        int exploratoryIterations = (int) (maxIterations * 0.1);
        for (int i = 0; i < exploratoryIterations; i++) {
            int[][] antTours = constructRandomTours(cityCount);
            updateBest(antTours);
        }

        // Основные итерации
        for (int i = exploratoryIterations; i < maxIterations; i++) {
            int[][] antTours = constructTours(pheromones);
            updateBest(antTours);
            updatePheromones(pheromones, antTours);
        }

        return bestRouteN;
    }
    public int[] getPath(double[][] matrix, int cntCity) {
        if ((2 < cntCity) && (cntCity <= 20)){
            alpha = 1.0;
            beta = 2.0;
            evaporationRate = 0.5;
            antCount = 25;
            maxIterations = 3000;
        }else if ((20 < cntCity) && (cntCity <= 35)){
            alpha = 1.0;
            beta = 2.0;
            evaporationRate = 0.2;
            antCount = 60;
            maxIterations = 4000;
        }else if ((35 < cntCity) && (cntCity <= 60)){
            alpha = 1.2;
            beta = 2.0;
            evaporationRate = 0.4;
            antCount = 100;
            maxIterations = 5000;
        }else if(cntCity > 60){
            alpha = 1.2;
            beta = 2.0;
            evaporationRate = 0.4;
            antCount = 120;
            maxIterations = 6000;
        }
        distances = matrix;
        int cityCount = matrix.length;
        double[][] pheromones = initializePheromones(cityCount);

        // Исследовательские итерации
        int exploratoryIterations = (int) (maxIterations * 0.1);
        for (int i = 0; i < exploratoryIterations; i++) {
            int[][] antTours = constructRandomTours(cityCount);
            updateBest(antTours);
        }

        // Основные итерации
        for (int i = exploratoryIterations; i < maxIterations; i++) {
            int[][] antTours = constructTours(pheromones);
            updateBest(antTours);
            updatePheromones(pheromones, antTours);
        }

        return bestRouteN;
    }

    public double getDistance() {
        return bestDistance;
    }

    private double[][] initializePheromones(int cityCount) {
        // Инициализирует феромоны равномерно
        double[][] pheromones = new double[cityCount][cityCount];
        double initialPheromone = 1.0 / cityCount;

        for (int i = 0; i < cityCount; i++) {
            Arrays.fill(pheromones[i], initialPheromone);
        }

        return pheromones;
    }

    private int[][] constructTours(double[][] pheromones) {
        // Строит маршруты муравьев с учетом феромонов
        int cityCount = pheromones.length;
        int[][] antTours = new int[antCount][cityCount];

        Random random = new Random();

        for (int k = 0; k < antCount; k++) {
            boolean[] visited = new boolean[cityCount];
            int[] tour = new int[cityCount];
            int startCity = random.nextInt(cityCount);

            tour[0] = startCity;
            visited[startCity] = true;

            for (int i = 1; i < cityCount; i++) {
                int currentCity = tour[i - 1];
                int nextCity = chooseNextCity(currentCity, visited, pheromones);

                tour[i] = nextCity;
                visited[nextCity] = true;
            }

            antTours[k] = tour;
        }

        return antTours;
    }

    private int[][] constructRandomTours(int cityCount) {
        // Строит случайные маршруты муравьев без учета феромонов
        int[][] antTours = new int[antCount][cityCount];

        Random random = new Random();

        for (int k = 0; k < antCount; k++) {
            boolean[] visited = new boolean[cityCount];
            int[] tour = new int[cityCount];
            int startCity = random.nextInt(cityCount);

            tour[0] = startCity;
            visited[startCity] = true;

            for (int i = 1; i < cityCount; i++) {
                int nextCity = chooseRandomNextCity(visited);
                tour[i] = nextCity;
                visited[nextCity] = true;
            }

            antTours[k] = tour;
        }

        return antTours;
    }

    private int chooseNextCity(int currentCity, boolean[] visited, double[][] pheromones) {
        // Выбирает следующий город для посещения муравьем с учетом феромонов
        int cityCount = pheromones.length;
        double[] probabilities = new double[cityCount];
        double total = 0.0;

        for (int i = 0; i < cityCount; i++) {
            if (!visited[i]) {
                probabilities[i] = Math.pow(pheromones[currentCity][i], alpha) *
                        Math.pow(1.0 / distances[currentCity][i], beta);
                total += probabilities[i];
            }
        }

        Random random = new Random();
        double randomValue = random.nextDouble() * total;
        double cumulative = 0.0;

        for (int i = 0; i < cityCount; i++) {
            if (!visited[i]) {
                cumulative += probabilities[i];
                if (cumulative >= randomValue) {
                    return i;
                }
            }
        }

        throw new RuntimeException("Нет непосещенных городов");
    }

    private int chooseRandomNextCity(boolean[] visited) {
        // Выбирает случайный следующий город для посещения муравьем
        int cityCount = visited.length;
        Random random = new Random();
        int nextCity = random.nextInt(cityCount);

        while (visited[nextCity]) {
            nextCity = random.nextInt(cityCount);
        }

        return nextCity;
    }

    private void updatePheromones(double[][] pheromones, int[][] antTours) {
        // Обновляет значения феромонов после прохода муравьев
        int cityCount = pheromones.length;

        for (int i = 0; i < cityCount; i++) {
            for (int j = 0; j < cityCount; j++) {
                pheromones[i][j] *= (1.0 - evaporationRate);
            }
        }

        for (int k = 0; k < antCount; k++) {
            int[] tour = antTours[k];
            double tourFitness = getFitness(tour);

            for (int i = 0; i < cityCount - 1; i++) {
                int cityA = tour[i];
                int cityB = tour[i + 1];

                pheromones[cityA][cityB] += evaporationRate * tourFitness;
                pheromones[cityB][cityA] += evaporationRate * tourFitness;
            }
        }
    }

    private double getFitness(int[] tour) {
        // Вычисляет приспособленность маршрута (расстояние)
        double fitness = 0.0;
        int cityCount = tour.length;

        for (int i = 0; i < cityCount - 1; i++) {
            int cityA = tour[i];
            int cityB = tour[i + 1];
            fitness += distances[cityA][cityB];
        }

        return fitness;
    }

    private void updateBest(int[][] antTours) {
        // Обновляет лучший маршрут и его расстояние
        for (int[] tour : antTours) {
            double tourDistance = getFitness(tour);
            if (tourDistance < bestDistance) {
                bestDistance = tourDistance;
                bestRouteN = tour.clone();
            }
        }
    }
}
/*
getPath(): Основной метод, который выполняет алгоритм муравьиной колонии. Он принимает матрицу расстояний и
параметры алгоритма и возвращает лучший найденный маршрут. Метод начинает с инициализации переменных и
матрицы феромонов. Затем выполняются исследовательские и основные итерации. Исследовательские итерации
выполняются вначале и не учитывают феромоны при выборе следующего города для посещения. Основные итерации
выполняются после и обновляют феромоны на основе маршрутов муравьев.
Лучший маршрут и его расстояние обновляются во время выполнения алгоритма.

initializePheromones(): Метод инициализирует матрицу феромонов равномерно.

constructTours(): Метод строит маршруты для муравьев с учетом феромонов.
Он выбирает следующий город для посещения с учетом значений феромонов и
эвристической информации (в данном случае, расстояний).
Метод возвращает массив маршрутов для всех муравьев.

constructRandomTours(): Метод строит случайные маршруты для муравьев без учета феромонов. Он выбирает следующий
город для посещения случайным образом из непосещенных городов. Метод возвращает массив случайных маршрутов для всех муравьев.

chooseNextCity(): Метод выбирает следующий город для посещения муравьем с учетом
значений феромонов и эвристической информации. Он вычисляет вероятности для каждого
непосещенного города и выбирает следующий город на основе этих вероятностей.

chooseRandomNextCity(): Метод выбирает случайный следующий город для посещения муравьем без учета значений феромонов.

updatePheromones(): Метод обновляет значения феромонов после прохода муравьев. Он уменьшает значения
феромонов на основе испарения и увеличивает значения феромонов на основе маршрутов муравьев.

getFitness(): Метод вычисляет приспособленность маршрута (расстояние).

updateBest(): Метод обновляет лучший маршрут и его расстояние, если текущий маршрут имеет меньшее расстояние.

Весь код представлен в классе AntAlgorithm. Чтобы использовать этот класс, вы можете создать экземпляр
AntAlgorithm и вызвать метод getPath() с матрицей расстояний и параметрами алгоритма.
 */