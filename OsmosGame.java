// import java.awt.*;
// import java.awt.event.*;
// import java.io.BufferedReader;
// import java.io.File;
// import java.io.FileReader;
// import java.io.IOException;
// import javax.swing.*;
// import java.util.ArrayList;
// import java.util.Iterator;

// public class OsmosGame extends JFrame {
//     private DrawArea drawArea;
//     private Timer moveBallsTimer, checkCollisionsTimer;
//     private int pressX, pressY;

//     /*public static JPanel main(String saveFileName){
//         OsmosGame OG=new OsmosGame("save/"+saveFileName);
//         return OG.drawArea;
//     }*/

//     public DrawArea getDrawArea(){
//         return drawArea;
//     }

//     public OsmosGame(String fileName) {
//         drawArea = new DrawArea();
//         this.add(drawArea);
//         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         this.setSize(800, 800);
//         this.setVisible(true);

//         moveBallsTimer = new Timer(50, e -> drawArea.moveBalls());
//         checkCollisionsTimer = new Timer(30, e -> drawArea.checkCollisions());

//         checkCollisionsTimer.start();
//         moveBallsTimer.start();
//         openfile(fileName);
//     }

//     void openfile(String fileName){
//         File selectedFile = new File(fileName);
//         try {
//             BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
//             String line;
//             drawArea.mainBall = null;
//             drawArea.balls.clear();
//             while ((line = reader.readLine()) != null) {
//                 String[] parts = line.split(",");
//                 double x = Double.parseDouble(parts[0]);
//                 double y = Double.parseDouble(parts[1]);
//                 double area = Double.parseDouble(parts[2]);
//                 drawArea.balls.add(new Ball(Math.max(area-1000,0), x, y, 0, 0));
//             }
//             reader.close();
//             drawArea.mainBall = drawArea.balls.get(0);
//             drawArea.repaint();
//         } catch (IOException | NumberFormatException ex) {
//             ex.printStackTrace();
//         }
//     }

//     class DrawArea extends JPanel {
//         private int ballX = 100;
//         private int ballY = 100;
//         private ArrayList<Ball> balls;
//         private Ball mainBall = new Ball(1000, ballX, ballY, 0, 0);

//         public DrawArea() {
//             this.setBackground(Color.BLACK);
//             balls = new ArrayList<>();

//             this.addMouseListener(new MouseAdapter() {

//                 @Override
//                 public void mouseClicked(MouseEvent e) {
//                     if(mainBall.getArea()==0)return;
//                     pressX=e.getX();
//                     pressY=e.getY();
                    
                    
                    
//                     double directionX = pressX - mainBall.getX();
//                     double directionY = pressY - mainBall.getY();
//                     balls.add(mainBall.sub(directionX, directionY, 0.01, 3));
//                 }

//             });
//         }

//         private void drawBall(int x, int y, double area, Graphics2D g2d, Color color) {
//             g2d.setColor(color);
//             int R = (int) Math.sqrt(area / Math.PI);
//             g2d.fillOval(x - R, y - R, R * 2, R * 2);
            
//         }

//         public void moveBalls() {
//             Iterator<Ball> iterator = balls.iterator();
//             while (iterator.hasNext()) {
//                 Ball ball = iterator.next();
//                 ball.move();
//                 handleCollision(ball);
//                 repaint();
//             }
//         }

//         private void handleCollision(Ball ball) {

//             double enx=ball.getEnergyX();
//             double eny=ball.getEnergyY();
//             int xto=0, yto=0;
//             if (enx > 0) xto = 1;
//             else if (enx < 0) xto = -1;

//             if (eny > 0) yto = 1;
//             else if (eny < 0) yto = -1;


//             double nextX = ball.getX() + Math.sqrt(Math.abs(enx / ball.getArea())) * xto;
//             double nextY = ball.getY() +  Math.sqrt(Math.abs(eny / ball.getArea())) * yto;

//             double R = ball.R();
//             if (nextX - R <= 0 || nextX + R >= getWidth()) {
//                 ball.setEnergyX(-ball.getEnergyX());
//             }

//             if (nextY - R <= 0 || nextY + R >= getHeight()) {
//                 ball.setEnergyY(-ball.getEnergyY());
//             }

//             double x=ball.getX();
//             double y=ball.getY();

//             double r=R;
//             if(x+r>=getWidth()){ball.setX(getWidth()-r-1);}
//             if(x-r<=0){ball.setX(r+1);}
//             if(y+r>=getHeight()){ball.setY(getHeight()-r-1);}
//             if(y-r<=0){ball.setY(r+1);}
            
//         }

//         protected void checkCollisions() {
//             for (int i = 0; i < balls.size(); i++) {
//                 for (int j = i + 1; j < balls.size(); j++) {
//                     Ball ball1 = balls.get(i);
//                     Ball ball2 = balls.get(j);
//                     if (isColliding(ball1, ball2)) {
//                         double contactArea = Math.PI * Math.pow(ball1.R() + ball2.R(), 2);
//                         double absorptionRate = contactArea * 0.001;
//                         if (ball1.getArea() > ball2.getArea()) {
//                             absorb(ball1, ball2, absorptionRate);
//                         } else if(ball2.getArea()>ball1.getArea()) {
//                             absorb(ball2, ball1, absorptionRate);
//                         }
//                     }
//                 }
//             }
//         }

//         private boolean isColliding(Ball ball1, Ball ball2) {
//             double dx = ball1.getX() - ball2.getX();
//             double dy = ball1.getY() - ball2.getY();
//             double distance = Math.sqrt(dx * dx + dy * dy);
//             return distance < (ball1.R() + ball2.R());
//         }

//         private void absorb(Ball largerBall, Ball smallerBall, double area) {
//             if(largerBall.getArea()==0||smallerBall.getArea()==0) return;
//             double absorptionRate = area;
//             double areaToTransfer = Math.min(absorptionRate, smallerBall.getArea());
//             double p = areaToTransfer / smallerBall.getArea();

//             double newArea = largerBall.getArea() + areaToTransfer;
//             largerBall.setArea(newArea);
//             largerBall.setEnergyX(largerBall.getEnergyX()+smallerBall.getEnergyX()*p);
            
//             largerBall.setEnergyY(largerBall.getEnergyY()+smallerBall.getEnergyY()*p);

//             smallerBall.setArea(smallerBall.getArea()-areaToTransfer);
//             smallerBall.setEnergyX(smallerBall.getEnergyX()-smallerBall.getEnergyX()*p);
//             smallerBall.setEnergyY(smallerBall.getEnergyY()-smallerBall.getEnergyY()*p);

//             if (smallerBall.getArea() <= 0) {
//                 if(smallerBall==mainBall)
//                 balls.remove(smallerBall);
//             }
//         }

//         @Override
//         protected void paintComponent(Graphics g) {
//             super.paintComponent(g);
//             Graphics2D g2d = (Graphics2D) g;

//             for (Ball ball : balls) {
//                 if(ball==null)System.out.println("error");
//                 Color color = Color.CYAN;
//                 if (ball.getArea() >= mainBall.getArea())
//                     color = Color.RED;
//                 else
//                     color = Color.GREEN;
//                 drawBall((int) ball.getX(), (int) ball.getY(), ball.getArea(), g2d, color);
//                 if(ball.getEnergyX()!=0|| ball.getEnergyY()!=0)
//                 g.drawLine((int)ball.getX(), (int)ball.getY(), (int)(ball.getX()+ball.getEnergyX()), (int)(ball.getY()+ball.getEnergyY()));
//             }
//             drawBall((int) mainBall.getX(), (int) mainBall.getY(), mainBall.getArea(), g2d, Color.BLUE);
//         }
//     }

    
// }


// class Ball {
//     private double area;
//     private double x;
//     private double y;
//     private double energyX;
//     private double energyY;

//     public Ball(double area, double x, double y, double energyX, double energyY) {
//         this.area = area;
//         this.x = x;
//         this.y = y;
//         this.energyX = energyX;
//         this.energyY = energyY;
//     }

//     public Ball sub(double toX, double toY, double percent, double speed) {
//         double magnitude = Math.sqrt(toX * toX + toY * toY);
//         double unitX = toX / magnitude;
//         double unitY = toY / magnitude;

//         toX=0;
//         toY=0;
//         if(unitX>0)toX=1;
//         else if(unitX<0)toX=-1;
//         if(unitY>0)toY=1;
//         else if(unitY<0)toY=-1;

//         double newArea = this.area * percent;
//         double energyX = Math.pow(unitX * speed, 2) * newArea*toX;
//         double energyY = Math.pow(unitY * speed, 2) * newArea*toY;

//         double remainingArea = this.area - newArea;

//         double r = this.R();
//         Ball ball = new Ball(newArea, x + unitX * r * 1.1, y + unitY * r * 1.1, energyX, energyY);

//         this.energyX -= energyX;
//         this.energyY -= energyY;
//         this.area = remainingArea;



//         return ball;
//     }

//     public double R() {
//         return Math.sqrt(area / Math.PI);
//     }

//     public void move() {
//         int xto=0;
//         int yto=0;
//         if (energyX > 0) xto = 1;
//         else if (energyX < 0) xto = -1;

//         if (energyY > 0) yto = 1;
//         else if (energyY < 0) yto = -1;

//         x +=  Math.sqrt(Math.abs(energyX / area)) * xto;
//         y += Math.sqrt(Math.abs(energyY / area)) * yto;

        

        

//         energyX *= 0.99;
//         energyY *= 0.99;
//     }

//     public double getX() {
//         return x;
//     }

//     public double getY() {
//         return y;
//     }

//     public void setX(double x) {
//         this.x=x;
//     }

//     public void setY(double y) {
//         this.y= y;
//     }

//     public double getEnergyX() {
//         return energyX;
//     }

//     public double getEnergyY() {
//         return energyY;
//     }

//     public void setEnergyX(double energyX) {
//         this.energyX = energyX;
//     }

//     public void setEnergyY(double energyY) {
//         this.energyY = energyY;
//     }

//     public double getArea() {
//         return area;
//     }

//     public void setArea(double area) {
//         this.area = area;
//     }
// }

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

public class OsmosGame {
    private DrawArea drawArea;
    private Timer moveBallsTimer, checkCollisionsTimer,chackGameWinOrOver;
    private int pressX, pressY;

    /*public static JPanel main(String saveFileName){
        OsmosGame OG=new OsmosGame("save/"+saveFileName);
        return OG.drawArea;
    }*/

    public DrawArea getDrawArea(){
        return drawArea;
    }

    public OsmosGame(String fileName) {
        drawArea = new DrawArea();

        moveBallsTimer = new Timer(50, e -> drawArea.moveBalls());
        checkCollisionsTimer = new Timer(30, e -> drawArea.checkCollisions());
        chackGameWinOrOver=new Timer(1000,e -> drawArea.chackGameWinOrOver());
        checkCollisionsTimer.start();
        moveBallsTimer.start();
        chackGameWinOrOver.start();
        openfile(fileName);
    }

    void openfile(String fileName){
        File selectedFile = new File(fileName);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
            String line;
            drawArea.mainBall = null;
            drawArea.balls.clear();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                double area = Double.parseDouble(parts[2]);
                drawArea.balls.add(new Ball(Math.max(area-1000,0), x, y, 0, 0));
            }
            reader.close();
            drawArea.mainBall = drawArea.balls.get(0);
            drawArea.repaint();
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    class DrawArea extends JPanel {
        private int ballX = 100;
        private int ballY = 100;
        private ArrayList<Ball> balls;
        private Ball mainBall = new Ball(1000, ballX, ballY, 0, 0);

        public DrawArea() {
            this.setBackground(Color.BLACK);
            balls = new ArrayList<>();

            this.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if(mainBall.getArea()==0)return;
                    pressX=e.getX();
                    pressY=e.getY();
                    
                    
                    
                    double directionX = pressX - mainBall.getX();
                    double directionY = pressY - mainBall.getY();
                    balls.add(mainBall.sub(directionX, directionY, 0.01, 3));
                }

            });
        }

        private void drawBall(int x, int y, double area, Graphics2D g2d, Color color) {
            g2d.setColor(color);
            int R = (int) Math.sqrt(area / Math.PI);
            g2d.fillOval(x - R, y - R, R * 2, R * 2);
            
        }

        public void moveBalls() {
            Iterator<Ball> iterator = balls.iterator();
            while (iterator.hasNext()) {
                Ball ball = iterator.next();
                ball.move();
                handleCollision(ball);
                repaint();
            }
        }

        private void handleCollision(Ball ball) {

            double enx=ball.getEnergyX();
            double eny=ball.getEnergyY();
            int xto=0, yto=0;
            if (enx > 0) xto = 1;
            else if (enx < 0) xto = -1;

            if (eny > 0) yto = 1;
            else if (eny < 0) yto = -1;


            double nextX = ball.getX() + Math.sqrt(Math.abs(enx / ball.getArea())) * xto;
            double nextY = ball.getY() +  Math.sqrt(Math.abs(eny / ball.getArea())) * yto;

            double R = ball.R();
            if (nextX - R <= 0 || nextX + R >= getWidth()) {
                ball.setEnergyX(-ball.getEnergyX());
            }

            if (nextY - R <= 0 || nextY + R >= getHeight()) {
                ball.setEnergyY(-ball.getEnergyY());
            }

            double x=ball.getX();
            double y=ball.getY();

            double r=R;
            if(x+r>=getWidth()){ball.setX(getWidth()-r-1);}
            if(x-r<=0){ball.setX(r+1);}
            if(y+r>=getHeight()){ball.setY(getHeight()-r-1);}
            if(y-r<=0){ball.setY(r+1);}
            
        }

        protected void checkCollisions() {
            for (int i = 0; i < balls.size(); i++) {
                for (int j = i + 1; j < balls.size(); j++) {
                    Ball ball1 = balls.get(i);
                    Ball ball2 = balls.get(j);
                    if (isColliding(ball1, ball2)) {
                        double contactArea = Math.PI * Math.pow(ball1.R() + ball2.R(), 2);
                        double absorptionRate = contactArea * 0.001;
                        if (ball1.getArea() > ball2.getArea()) {
                            absorb(ball1, ball2, Math.max(absorptionRate,10));
                        } else if(ball2.getArea()>ball1.getArea()) {
                            absorb(ball2, ball1, Math.max(absorptionRate,10));
                        }
                    }
                }
            }
        }

        private boolean isColliding(Ball ball1, Ball ball2) {
            double dx = ball1.getX() - ball2.getX();
            double dy = ball1.getY() - ball2.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            return distance < (ball1.R() + ball2.R());
        }

        private void absorb(Ball largerBall, Ball smallerBall, double area) {
            if(largerBall.getArea()==0||smallerBall.getArea()==0) return;
            double absorptionRate = area;
            double areaToTransfer = Math.min(absorptionRate, smallerBall.getArea());
            double p = areaToTransfer / smallerBall.getArea();

            double newArea = largerBall.getArea() + areaToTransfer;
            largerBall.setArea(newArea);
            largerBall.setEnergyX(largerBall.getEnergyX()+smallerBall.getEnergyX()*p);
            
            largerBall.setEnergyY(largerBall.getEnergyY()+smallerBall.getEnergyY()*p);

            smallerBall.setArea(smallerBall.getArea()-areaToTransfer);
            smallerBall.setEnergyX(smallerBall.getEnergyX()-smallerBall.getEnergyX()*p);
            smallerBall.setEnergyY(smallerBall.getEnergyY()-smallerBall.getEnergyY()*p);

            if (smallerBall.getArea() <= 0) {
                if(smallerBall==mainBall)
                balls.remove(smallerBall);
            }
        }

        protected void chackGameWinOrOver(){
            if(mainBall.getArea()<1){
                moveBallsTimer.stop();
                checkCollisionsTimer.stop();
                chackGameWinOrOver.stop();
                JOptionPane.showMessageDialog(null,"Game Over");
            }
            else if(mainBall.getArea()>=TotalArea()){
                moveBallsTimer.stop();
                checkCollisionsTimer.stop();
                chackGameWinOrOver.stop();
                JOptionPane.showMessageDialog(null,"Winner");
            }
        }

        protected double TotalArea(){
            double total=0;
            for(Ball b:balls)
                total+=b.getArea();
            return total;
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            for (Ball ball : balls) {
                if(ball==null)System.out.println("error");
                Color color = Color.CYAN;
                if (ball.getArea() >= mainBall.getArea())
                    color = Color.RED;
                else
                    color = Color.GREEN;
                drawBall((int) ball.getX(), (int) ball.getY(), ball.getArea(), g2d, color);
                if(ball.getEnergyX()!=0|| ball.getEnergyY()!=0)
                g.drawLine((int)ball.getX(), (int)ball.getY(), (int)(ball.getX()+ball.getEnergyX()), (int)(ball.getY()+ball.getEnergyY()));
            }
            drawBall((int) mainBall.getX(), (int) mainBall.getY(), mainBall.getArea(), g2d, Color.BLUE);
        }
    }

    
}


class Ball {
    private double area;
    private double x;
    private double y;
    private double energyX;
    private double energyY;

    public Ball(double area, double x, double y, double energyX, double energyY) {
        this.area = area;
        this.x = x;
        this.y = y;
        this.energyX = energyX;
        this.energyY = energyY;
    }

    public Ball sub(double toX, double toY, double percent, double speed) {
        double magnitude = Math.sqrt(toX * toX + toY * toY);
        double unitX = toX / magnitude;
        double unitY = toY / magnitude;

        toX=0;
        toY=0;
        if(unitX>0)toX=1;
        else if(unitX<0)toX=-1;
        if(unitY>0)toY=1;
        else if(unitY<0)toY=-1;

        double newArea =Math.max(10, this.area * percent);
        double energyX = Math.pow(unitX * speed, 2) * newArea*toX;
        double energyY = Math.pow(unitY * speed, 2) * newArea*toY;

        double remainingArea = this.area - newArea;

        double r = this.R();
        Ball ball = new Ball(newArea, x + unitX * r * 1.1, y + unitY * r * 1.1, energyX, energyY);

        this.energyX -= energyX;
        this.energyY -= energyY;
        this.area = remainingArea;



        return ball;
    }

    public double R() {
        return Math.sqrt(area / Math.PI);
    }

    public void move() {
        int xto=0;
        int yto=0;
        if (energyX > 0) xto = 1;
        else if (energyX < 0) xto = -1;

        if (energyY > 0) yto = 1;
        else if (energyY < 0) yto = -1;

        x +=  Math.sqrt(Math.abs(energyX / area)) * xto;
        y += Math.sqrt(Math.abs(energyY / area)) * yto;

        

        

        energyX *= 0.99;
        energyY *= 0.99;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x=x;
    }

    public void setY(double y) {
        this.y= y;
    }

    public double getEnergyX() {
        return energyX;
    }

    public double getEnergyY() {
        return energyY;
    }

    public void setEnergyX(double energyX) {
        this.energyX = energyX;
    }

    public void setEnergyY(double energyY) {
        this.energyY = energyY;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
}