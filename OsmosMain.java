import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OsmosMain extends JFrame {
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
        }

        JScrollPane scrollPane = new JScrollPane(buttonPanel);
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

        @Override
        public void actionPerformed(ActionEvent e) {
            OsmosGame os = new OsmosGame("save/"+fileName);


            
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OsmosMain viewer = new OsmosMain();
            viewer.setVisible(true);
        });
    }
}
