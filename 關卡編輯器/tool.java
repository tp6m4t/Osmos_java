

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

import java.util.ArrayList;

public class tool extends JFrame {
    private DrawArea drawArea;
    private int mouseX,mouseY;
    private int area=1000;
    public static void main(String[] argv){
        new tool();
    }

    public tool() {
        drawArea = new DrawArea();
        this.add(drawArea);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 800);
        this.setVisible(true);



        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        
        
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFromFile();
            }
        });
        
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToFile();
            }
        });
        
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
       
    }

    private void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                String line;
                drawArea.balls.clear();
                while ((line = reader.readLine()) != null) {
                    
                    String[] parts = line.split(",");
                    double x = Double.parseDouble(parts[0]);
                    double y = Double.parseDouble(parts[1]);
                    double area = Double.parseDouble(parts[2]);
                    drawArea.balls.add(new Ball(area, x, y));
                }
                reader.close();
                if(drawArea.balls.size()!=0)
                    drawArea.mainBall=drawArea.balls.get(0);
                drawArea.repaint();
            } catch (IOException | NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().endsWith(".osm")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".osm");
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));
                
                for (Ball ball : drawArea.balls) {
                    writer.write(ball.x + "," + ball.y + "," + ball.area);
                    writer.newLine();
                }
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }



    class DrawArea extends JPanel {
        
        private ArrayList<Ball> balls;
        private Ball mainBall=null;

        public DrawArea() {
            this.setBackground(Color.BLACK);
            balls = new ArrayList<>();
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int clickX = e.getX();
                    int clickY = e.getY();

                    for(int i=0;i!=balls.size();i++){
                        Ball ball=balls.get(i);
                        if(Math.pow(clickX-ball.x,2)+Math.pow(clickY-ball.y, 2)<Math.pow(ball.R(),2)){
                            balls.remove(i);
                            i--;
                        }
                    }

                    if(area>0){
                        if (SwingUtilities.isRightMouseButton(e)) {
                            if(mainBall==null){
                                mainBall=new Ball(area, mouseX, mouseY);
                                balls.add(mainBall);
                            }
                            else{
                                mainBall.x=mouseX;
                                mainBall.y=mouseY;
                                mainBall.area=area;
                            }
                        }
                        else{
                            balls.add(new Ball(area,mouseX,mouseY));
                            if(balls.size()==1)mainBall=balls.get(0);
                        }
                    }
                    
                    repaint();
                }

            });
            this.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {

                    int R = (int) Math.sqrt(area / Math.PI);
                    if(e.getX()-R>0 && e.getX()+R<getWidth())
                        mouseX = e.getX();
                    
                    if(e.getY()-R>0 &&e.getY()+R<getHeight())
                        mouseY = e.getY();
                    repaint();
                }
            });

            this.addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    int scroll = e.getWheelRotation();
                    area += scroll*100;
                    if(area<0)
                        area=0;
                    int R = (int) Math.sqrt(area / Math.PI);
                    if(mouseX-R<0) mouseX = 1+R;
                    if(mouseX+R>getWidth()) mouseX = getWidth()-1-R;
                    if(e.getY()-R<0) mouseY = 1+R;
                    if(e.getY()+R>getHeight()) mouseY = getHeight()-1-R;

                    repaint();
                }
            });
        }

        private void drawBall(int x, int y, double area, Graphics2D g2d,Color color) {
            g2d.setColor(color);
            int R = (int) Math.sqrt(area / Math.PI);
            g2d.fillOval(x - R, y - R, R * 2, R * 2);
        }







        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            for (Ball ball : balls) {
                Color color=Color.CYAN;
                if(mainBall==null||ball.area>=mainBall.area)
                    color=Color.RED;
                else
                    color=Color.GREEN;
                drawBall((int) ball.x, (int) ball.y, ball.area, g2d,color);
            }
            if(mainBall!=null)
                drawBall((int) mainBall.x, (int) mainBall.y, mainBall.area, g2d,Color.BLUE);


            g2d.setColor(Color.white);
            int R = (int) Math.sqrt(area / Math.PI);
            g2d.drawOval(mouseX - R, mouseY - R, R * 2, R * 2);
        }
    }

    class Ball {
        private double area;
        private double x;
        private double y;

        public Ball(double area, double x, double y) {
            this.area = area;
            this.x = x;
            this.y = y;
        }

        public double R() {
            return Math.sqrt(area / Math.PI);
        }
    }
}
