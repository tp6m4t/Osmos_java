import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class OsmosMain extends JFrame {
    
    private Timer backTitleTimer;
    private ArrayList<JButton> buArrayList =new ArrayList<>();
    JScrollPane scrollPane;
    

    public OsmosMain() {
        setTitle("Osmos File Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        List<String> fileNames = getOsmFiles("save");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        

        for (String fileName : fileNames) {
            JButton button = new JButton(fileName.substring(0, fileName.length() - 4));
            button.setPreferredSize(new Dimension(50, 50));
            button.addActionListener(new ButtonClickListener(fileName));
            buttonPanel.add(button);
            buArrayList.add(button);
        }

        scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
    }

    private List<String> getOsmFiles(String directory) {
        List<String> fileNames = new ArrayList<>();
        File dir = new File(directory);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((dir1, name) -> name.endsWith(".osm"));
            if (files != null) {
                for (File file : files) {
                    fileNames.add(file.getName());
                }
            }
        }
        return fileNames;
    }

    private class ButtonClickListener implements ActionListener {
        private final String fileName;

        public ButtonClickListener(String fileName) {
            this.fileName = fileName;
        }
        ActionEvent clickb;
        @Override
        public void actionPerformed(ActionEvent e) {
            OsmosGame os = new OsmosGame("save/"+fileName);
            
            backTitleTimer =new Timer(50,a->backtitle(os.finish,os.winlose));
            
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 800);
            setResizable(false);
 

            JPanel gameDrawArea = os.getDrawArea();
            backTitleTimer.start();
            
            os.finish=0;
            os.winlose=0;
            getContentPane().removeAll();
            getContentPane().add(gameDrawArea);
            revalidate();
            repaint();
            playSound("music.wav");
            clickb=e; 
            } 
            public void backtitle(int finish,int winlose){
            if(finish==1){
                backTitleTimer.stop(); 
                if(winlose==1){
                    getContentPane().removeAll();
                    for(JButton click : buArrayList)if(click==clickb.getSource()) click.setBackground(Color.green);
                    getContentPane().add(scrollPane);
                    revalidate();
                    repaint(); 
                }
                else{
                    getContentPane().removeAll();
                    for(JButton click : buArrayList)if(click==clickb.getSource())click.setBackground(Color.red);
                    getContentPane().add(scrollPane);
                    revalidate();
                    repaint(); 
                }       
            }  
        
    } 
    private void playSound(String soundFileName) {
        try {
            File soundFile = new File(soundFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-30.0f); 
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }     
        }
   
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OsmosMain viewer = new OsmosMain();
            viewer.setVisible(true);
        });
    }
}
