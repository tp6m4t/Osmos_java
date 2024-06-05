import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class OsmosMain extends JFrame {
    private OsmosGame os;
    private Timer OnGameOver;
    private JScrollPane scrollPane;
    private Map<String,Integer> GameTypes;


    


    public OsmosMain() {
        setTitle("Osmos File Viewer");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        

        List<String> fileNames = getOsmFiles();
        GameTypes= new HashMap<>();
        getGameTypes();
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        for (String fileName : fileNames) {
            JButton button = new JButton(fileName.substring(0, fileName.length() - 4));
            button.setPreferredSize(new Dimension(50, 50));
            button.addActionListener(new ButtonClickListener(fileName));
            if(GameTypes.get(fileName)!=null){
                button.setBackground(new Color(GameTypes.get(fileName)));
            }
            buttonPanel.add(button);
        }

        scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
    }

    private List<String> getOsmFiles() {
        List<String> fileNames = new ArrayList<>();
        File dir = new File("save");
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

    private void getGameTypes() {

        File selectedFile = new File("save/GameTypes.ost");
        if(selectedFile.exists()==false)return;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                GameTypes.put(parts[0], Integer.parseInt(parts[1]));
            }
            reader.close();
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }

    }

    private class ButtonClickListener implements ActionListener {
        private final String fileName;

        public ButtonClickListener(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            os = new OsmosGame("save/"+fileName);

            JButton button = (JButton) e.getSource();
            OnGameOver=new Timer(100,E->GameOver(os,button) );
            OnGameOver.start();

            
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 800);
            setResizable(false);


            JPanel gameDrawArea = os.getDrawArea();
            getContentPane().removeAll();
            getContentPane().add(gameDrawArea);
            revalidate();
            repaint();
        }
    }


    private void GameOver(OsmosGame os,JButton Button){
        if(os.GameType!=0){

            Color color=os.GameType==1?Color.GREEN:Color.RED;
            Button.setBackground(color);
            OnGameOver.stop();
            getContentPane().removeAll();
            getContentPane().add(scrollPane);
            repaint();


            
            GameTypes.put(Button.getText()+".osm",(os.GameType==1?Color.GREEN.getRGB():Color.RED.getRGB()));

            saveGameTypes();
        }
        
    }

    private void saveGameTypes(){
        
        File selectedFile = new File("save/GameTypes.ost");
            
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));
                
            for (var type :GameTypes.entrySet()) {
                writer.write(type.getKey() + "," + type.getValue());
                writer.newLine();
            }
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OsmosMain viewer = new OsmosMain();
            viewer.setVisible(true);
        });
    }
}
