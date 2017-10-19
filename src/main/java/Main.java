import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.scene.text.FontWeight;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IOException, IllegalAccessException, UnirestException {
        createForm();
        readAllCards();
    }
    /**Metodas grąžina kortos paveikslėlio URL adresą pagal perduotą pavadinimą.
     * @param name kortos pavadinimas.
     * @return gražina kortos url adresą.
     * Gaudomos klaidos {@link UnirestException} ir {@link JSONException}
     * */
    public static String getCardUrl(String name){
        if(name == null){name = "Ysera";}
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://omgvamp-hearthstone-v1.p.mashape.com/cards/"+name)
                    .header("X-Mashape-Key", "Q0ZvyGgPZfmshIFUmF1yWuxhPOPop1ON2nqjsnogXDUcMQ9Cdh")
                    .header("Accept", "application/json")
                    .asJson();
           // data = response.getBody().getArray().getJSONObject(0).getString("img");
            return response.getBody().getArray().getJSONObject(0).getString("img");
        } catch (JSONException | UnirestException e) {
            JOptionPane.showMessageDialog(new JButton("OKEY"), "Wrong card name !!!");
        }
        catch (Exception ee){
            JOptionPane.showMessageDialog(new JButton("OKEY"), "Wrong card name !!!");
        }
        return name;
    }
    public static List<String> readAllCards() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("https://omgvamp-hearthstone-v1.p.mashape.com/cards")
                .header("X-Mashape-Key", "Q0ZvyGgPZfmshIFUmF1yWuxhPOPop1ON2nqjsnogXDUcMQ9Cdh")
                .asJson();

       // System.out.println(length);
            List<String> list = new ArrayList<>();
            JSONArray basicCards = response.getBody().getObject().getJSONArray("Basic");
        for (int i = 0; i < basicCards.length(); i++) {
            list.add(basicCards.getJSONObject(i).getString("name"));
        }
        return list;

    }
    /** Funkcijos paskirtis JLabel nurodyti paveikslėlį.
     * @param name pavadinimas, pagal kurį iš web serviso gauname URL adresą.
     * @throws IOException jeigu adresas neegzistuoja įvyksta klaida.
    * */
    public static ImageIcon createImage(String name) throws IOException {
        return new ImageIcon(ImageIO.read(new URL(getCardUrl(name))));
    }

    public static void createForm() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException, UnirestException {
        // Langas
        JFrame frame = new JFrame();
        frame.setSize(400, 600);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Tekstinis laukelis
        JTextField textField = new JTextField("Ysera");
        textField.setPreferredSize(new Dimension(50,50));
        textField.setFont(new Font("Verdana", 1, 20));
        String name = textField.getText();
        JComboBox jComboBox = new JComboBox();
        jComboBox.setFont(new Font("Verdana", 1, 20));
        jComboBox.setPreferredSize(new Dimension(50, 50));

        for (String card: readAllCards()){
            jComboBox.addItem(card);
            jComboBox.addActionListener(e -> {
                textField.setText(jComboBox.getSelectedItem().toString());
            });
        }

        JLabel label = new JLabel(createImage(name));
        JButton jButton = new JButton("SEARCH");
        jButton.setPreferredSize(new Dimension(50, 50));

        textField.addActionListener((e) -> {
            try {
                label.setIcon(createImage(textField.getText()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        jButton.addActionListener(e -> {
            try {
                label.setIcon(createImage(textField.getText()));
            } catch (IOException e1) {
               e1.printStackTrace();
                // JOptionPane.showMessageDialog(null, "Neteisingas kortos pavadinimas");
            }
        });

        // Komponentu isdestymas
        frame.add(label, BorderLayout.SOUTH);
        frame.add(textField, BorderLayout.NORTH);
        frame.add(jButton, BorderLayout.EAST);
        frame.add(jComboBox, BorderLayout.CENTER);
        frame.pack();

        frame.setVisible(true);
    }

}
