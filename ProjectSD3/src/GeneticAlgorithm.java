import java.util.*;

class GeneticAlgorithm extends Algorithm {
    private double[][] distanceMatrix;
    private int populationSize;
    private int numGenerations;
    private double crossoverRate;
    private double mutationRate;
    int[] bestRouteN;
    double shortestDistance = Double.MAX_VALUE;

    public GeneticAlgorithm(){}


    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("populationSize", String.valueOf(populationSize));
        params.put("mutationRate", String.valueOf(mutationRate));
        params.put("numGenerations", String.valueOf(numGenerations));
        params.put("crossoverRate", String.valueOf(crossoverRate));
        return params;
    }


    public int[] getPath(double[][] matrix, Map<String, String> params) {
//        // Получаем значения параметров
        populationSize = Integer.parseInt(params.get("populationSize"));
        mutationRate = Double.parseDouble(params.get("mutationRate"));
        numGenerations = Integer.parseInt(params.get("numGenerations"));
        crossoverRate = Double.parseDouble(params.get("crossoverRate"));
        distanceMatrix = matrix;
        bestRouteN = new int[distanceMatrix.length];


        List<List<Integer>> population = generatePopulation(); // создаём случайную начальную популяцию из генов
        List<Integer> bestRoute = null;

        for (int generation = 0; generation < numGenerations; generation++) {
            population = selection(population); // выборка лучших генов
            List<List<Integer>> newPopulation = new ArrayList<>();

            for (int i = 0; i < populationSize / 2; i++) {
                List<Integer> parent1 = getRandomElement(population); // берём два случайных гена
                List<Integer> parent2 = getRandomElement(population);

                List<Integer> child1, child2;
                if (Math.random() < crossoverRate) {
                    child1 = crossover(parent1, parent2);  // скрещиваем
                    child2 = crossover(parent2, parent1);
                } else {
                    child1 = new ArrayList<>(parent1); // копируем ген родителя
                    child2 = new ArrayList<>(parent2);
                }

                mutate(child1);  // смотрим мутацию ребёнка
                mutate(child2);

                newPopulation.add(child1);
                newPopulation.add(child2);
            }

            population = newPopulation; // обновляем общую популяцию, добавляя новые гены

            if (generation % 10 == 0) {
                bestRoute = getBestRoute(population);
                double distance = calculateDistance(bestRoute);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                }
            }
        }

        bestRoute = getBestRoute(population); // обновляем значения для вывода
        shortestDistance = calculateDistance(bestRoute);
        for (int i = 0; i < bestRoute.size(); i++) {
            bestRouteN[i] = bestRoute.get(i) - 1;
        }

        return bestRouteN;
    }
    public int[] getPath(double[][] matrix, int cntCity) {
        if ((2 < cntCity) && (cntCity <= 20)){
            populationSize = 200;
            mutationRate = 0.2;
            numGenerations = 2000;
            crossoverRate = 0.3;
        }else if ((20 < cntCity) && (cntCity <= 35)){
            populationSize = 600;
            mutationRate = 0.2;
            numGenerations = 4000;
            crossoverRate = 0.3;
        }else if ((35 < cntCity) && (cntCity <= 60)){
            populationSize = 5000;
            mutationRate = 0.2;
            numGenerations = 2000;
            crossoverRate = 0.3;
        }else if (cntCity > 60){
            populationSize = 10000;
            mutationRate = 0.2;
            numGenerations = 1000;
            crossoverRate = 0.3;
        }
        distanceMatrix = matrix;
        bestRouteN = new int[distanceMatrix.length];


        List<List<Integer>> population = generatePopulation(); // создаём случайную начальную популяцию из генов
        List<Integer> bestRoute = null;

        for (int generation = 0; generation < numGenerations; generation++) {
            population = selection(population); // выборка лучших генов
            List<List<Integer>> newPopulation = new ArrayList<>();

            for (int i = 0; i < populationSize / 2; i++) {
                List<Integer> parent1 = getRandomElement(population); // берём два случайных гена
                List<Integer> parent2 = getRandomElement(population);

                List<Integer> child1, child2;
                if (Math.random() < crossoverRate) {
                    child1 = crossover(parent1, parent2);  // скрещиваем
                    child2 = crossover(parent2, parent1);
                } else {
                    child1 = new ArrayList<>(parent1); // копируем ген родителя
                    child2 = new ArrayList<>(parent2);
                }

                mutate(child1);  // смотрим мутацию ребёнка
                mutate(child2);

                newPopulation.add(child1);
                newPopulation.add(child2);
            }

            population = newPopulation; // обновляем общую популяцию, добавляя новые гены

            if (generation % 10 == 0) {
                bestRoute = getBestRoute(population);
                double distance = calculateDistance(bestRoute);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                }
            }
        }

        bestRoute = getBestRoute(population); // обновляем значения для вывода
        shortestDistance = calculateDistance(bestRoute);
        for (int i = 0; i < bestRoute.size(); i++) {
            bestRouteN[i] = bestRoute.get(i) - 1;
        }

        return bestRouteN;
    }
    public double getDistance(){
        return shortestDistance;
    }
    private List<List<Integer>> generatePopulation() { // создаём случайную начальную популяцию
        List<List<Integer>> population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            List<Integer> route = new ArrayList<>(); // создаём случайный начальный ген
            for (int j = 1; j <= distanceMatrix.length; j++) {
                route.add(j);
            }
            Collections.shuffle(route); // перетасовка случайного гена
            population.add(route);
        }

        return population;
    }

    private List<List<Integer>> selection(List<List<Integer>> population) {// выбор того, кто останется в живых, селекция
        List<List<Integer>> selected = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            List<List<Integer>> tournament = getRandomElements(population, 2);
            List<Integer> winner = getBestRoute(tournament);
            selected.add(winner);
        }

        return selected;
    }

    private List<Integer> crossover(List<Integer> parent1, List<Integer> parent2) { // создание нового гена, скрещивание
        int size = parent1.size();
        int start = new Random().nextInt(size - 1);
        int end = new Random().nextInt(size - start) + start;

        List<Integer> child = new ArrayList<>(parent1.subList(start, end + 1));
        for (int gene : parent2) {
            if (!child.contains(gene)) {
                child.add(gene);
            }
        }

        return child;
    }

    private void mutate(List<Integer> route) {// внутренние изменение гена в случае мутации
        if (Math.random() < mutationRate) {
            int index1 = new Random().nextInt(route.size());
            int index2 = new Random().nextInt(route.size());
            Collections.swap(route, index1, index2);
        }
    }
    private List<Integer> getBestRoute(List<List<Integer>> population) { // вычисляем лучший маршрут из всей популяции
        return Collections.min(population, Comparator.comparingDouble(this::calculateDistance));
    }

    private double calculateDistance(List<Integer> route) { // высчет расстояния точек
        double distance = 0;
        int size = route.size();

        for (int i = 0; i < size; i++) {
            int city1 = route.get(i) - 1;
            int city2 = route.get((i + 1) % size) - 1;
            distance += distanceMatrix[city1][city2];
        }

        return distance;
    }

    private <T> T getRandomElement(List<T> list) {
        int index = new Random().nextInt(list.size());
        return list.get(index);
    }

    private <T> List<T> getRandomElements(List<T> list, int count) {
        List<T> randomElements = new ArrayList<>();

        while (randomElements.size() < count) {
            T element = getRandomElement(list);
            if (!randomElements.contains(element)) {
                randomElements.add(element);
            }
        }

        return randomElements;
    }
}

