import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame{
static final class DrawComponent extends Component {
    private int[][] coordinates;
    public DrawComponent(int[][] c) {
        coordinates = c;
    }
        public void paint(Graphics g) {
            super.paint(g);
            // Получаем объект Graphics2D для отрисовки графики и включаем режим сглаживания для более качественной отрисовки
            Graphics2D g2d = (Graphics2D) g;
            g2d.clearRect(0, 0, 1200, 1200);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Устанавливаем толщину линии
            g2d.setStroke(new BasicStroke(3));
            // Устанавливаем цвет рисования
            g2d.setColor(Color.BLACK);
            // Проходим по массиву координат точек и рисуем линии между ними, а также точки
            for (int i = 0; i < coordinates.length - 1; i++) {
                int x1 = coordinates[i][0] * 5; // Увеличиваем координаты точек в 3 раза
                int y1 = coordinates[i][1] * 5;
                int x2 = coordinates[i + 1][0] * 5;
                int y2 = coordinates[i + 1][1] * 5;
                g2d.drawLine(x1, y1, x2, y2);
                g2d.fillOval(x1 - 4, y1 - 4, 8, 8); // Увеличиваем размер точек в 3 раза
                g2d.fillOval(x2 - 4, y2 - 4, 8, 8);
            }
            // Рисуем последнюю точку
            int[] lastCoord = coordinates[coordinates.length - 1];
            int[] firstCoord = coordinates[0];
            g2d.drawLine(lastCoord[0] * 5, lastCoord[1] * 5, firstCoord[0] * 5, firstCoord[1] * 5);
            g2d.fillOval(lastCoord[0] * 5 - 4, lastCoord[1] * 5 - 4, 8, 8);
        }

        public static void drawPoints(int[][] coordinates, JPanel panel) {
            // Получаем Graphics объект для панели
            Graphics g = panel.getGraphics();
            // Создаем объект типа DrawPoints
            DrawComponent drawPoints = new DrawComponent(coordinates);
            // Рисуем объект типа DrawPoints на панели
            drawPoints.paint(g);
        }
    }
    

    private JPanel panelMain;
    private JPanel panel2;
    private JButton buttonFindOtvetGen;
    private JPanel panelGenet;
    private JPanel panelAnt;
    private JPanel panelGenet111;
    private JPanel panelAnt111;
    private JTextField fieldMutationRate;
    private JTextField fieldPopulationSize;
    private JTextField fieldAntCount;
    private JTextField fieldAlpha;
    private JTextField fieldBeta;
    private JTextField fieldEvaporationRate;
    private JTextField fieldMaxIterations;
    private JTextField fieldGenerations;
    private JTextField fieldCrossoverRate;
    private JTextField cntPointsGen;
    private JTabbedPane tabbedPane1;
    private JButton buttonFindOtvetAnt;
    private JTextField cntPointsAnt;
    private JTextField fieldPathGen;
    private JTextField fieldDistGen;
    private JTextField fieldPathAnt;
    private JTextField fieldDistAnt;


    public MainFrame() throws IOException {
        this.setTitle("Алгоритм коммивояжёра");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.pack();

        buttonFindOtvetGen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (Objects.equals(fieldCrossoverRate.getText(), "-") && Objects.equals(fieldMutationRate.getText(), "-") &&
                            Objects.equals(fieldPopulationSize.getText(), "-") && Objects.equals(fieldGenerations.getText(), "-") &&
                            Integer.parseInt(cntPointsGen.getText()) >= 2){
                        double[][] distanceMatrix = Others.createAdjacencyMatrix(Others.generateRandomPoints(Integer.parseInt(cntPointsGen.getText())));
                        GeneticAlgorithm gen = new GeneticAlgorithm();
                        String strG = "";
                        int[] bestRoute = gen.getPath(distanceMatrix,Integer.parseInt(cntPointsAnt.getText()));
                        for (int i = 0; i < bestRoute.length; i++) {
                            strG += bestRoute[i] + " ";
                        }
                        fieldPathGen.setText(strG);
                        fieldDistGen.setText(String.valueOf(gen.getDistance()));

                        DrawComponent.drawPoints(Others.rearrangeSubArrays(gen.bestRouteN), panelGenet);
                    } else if (Double.parseDouble(fieldCrossoverRate.getText()) > 0 && Double.parseDouble(fieldMutationRate.getText()) > 0 &&
                            Integer.parseInt(fieldPopulationSize.getText()) > 0 && Integer.parseInt(fieldGenerations.getText()) > 0 &&
                            Integer.parseInt(cntPointsGen.getText()) >= 2){
                        double[][] distanceMatrix = Others.createAdjacencyMatrix(Others.generateRandomPoints(Integer.parseInt(cntPointsGen.getText())));
                        Map<String, String> paramsGen = new HashMap<>();
                        paramsGen.put("populationSize", fieldPopulationSize.getText());
                        paramsGen.put("mutationRate", fieldMutationRate.getText());
                        paramsGen.put("numGenerations", fieldGenerations.getText());
                        paramsGen.put("crossoverRate", fieldCrossoverRate.getText());
                        GeneticAlgorithm gen = new GeneticAlgorithm();
                        String strG = "";
                        int[] bestRoute = gen.getPath(distanceMatrix, paramsGen);
                        for (int i = 0; i < bestRoute.length; i++) {
                            strG += bestRoute[i] + " ";
                        }
                        fieldPathGen.setText(strG);
                        fieldDistGen.setText(String.valueOf(gen.getDistance()));

                        DrawComponent.drawPoints(Others.rearrangeSubArrays(gen.bestRouteN), panelGenet);
                    }
                } catch(Exception ex){
                    SwingUtils.showErrorMessageBox(ex);
                }
            }
        });
        buttonFindOtvetAnt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (Objects.equals(fieldAlpha.getText(), "-") && Objects.equals(fieldBeta.getText(), "-") &&
                            Objects.equals(fieldEvaporationRate.getText(), "-") && Objects.equals(fieldMaxIterations.getText(), "-") &&
                            Objects.equals(fieldAntCount.getText(), "-") && Integer.parseInt(cntPointsAnt.getText()) >= 2){
                        double[][] distanceMatrix = Others.createAdjacencyMatrix(Others.generateRandomPoints(Integer.parseInt(cntPointsAnt.getText())));
                        AntAlgorithm ant = new AntAlgorithm();
                        String strA = "";
                        int[] bestRoute = ant.getPath(distanceMatrix, Integer.parseInt(cntPointsAnt.getText()));
                        for (int i = 0; i < bestRoute.length; i++) {
                            strA += bestRoute[i] + " ";
                        }
                        fieldPathAnt.setText(strA);
                        fieldDistAnt.setText(String.valueOf(ant.getDistance()));

                        DrawComponent.drawPoints(Others.rearrangeSubArrays(AntAlgorithm.bestRouteN), panelAnt);
                    } else if (Double.parseDouble(fieldAlpha.getText()) > 0 && Double.parseDouble(fieldBeta.getText()) > 0 &&
                            Double.parseDouble(fieldEvaporationRate.getText()) > 0 && Integer.parseInt(fieldMaxIterations.getText()) > 0 &&
                            Integer.parseInt(fieldAntCount.getText()) > 0 && Integer.parseInt(cntPointsAnt.getText()) >= 2){
                        double[][] distanceMatrix = Others.createAdjacencyMatrix(Others.generateRandomPoints(Integer.parseInt(cntPointsAnt.getText())));
                        Map<String, String> paramsAnt = new HashMap<>();
                        paramsAnt.put("alpha", fieldAlpha.getText());
                        paramsAnt.put("beta", fieldBeta.getText());
                        paramsAnt.put("antCount", fieldAntCount.getText());
                        paramsAnt.put("maxIterations", fieldMaxIterations.getText());
                        paramsAnt.put("evaporationRate", fieldEvaporationRate.getText());
                        AntAlgorithm ant = new AntAlgorithm();
                        String strA = "";
                        int[] bestRoute = ant.getPath(distanceMatrix, paramsAnt);
                        for (int i = 0; i < bestRoute.length; i++) {
                            strA += bestRoute[i] + " ";
                        }
                        fieldPathAnt.setText(strA);
                        fieldDistAnt.setText(String.valueOf(ant.getDistance()));

                        DrawComponent.drawPoints(Others.rearrangeSubArrays(AntAlgorithm.bestRouteN), panelAnt);
                    }
                } catch(Exception ex){
                    SwingUtils.showErrorMessageBox(ex);
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
