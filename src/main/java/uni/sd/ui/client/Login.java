package uni.sd.ui.client;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import uni.sd.ln.server.Iln;
import uni.sd.ln.server.ssutilizadores.exceptions.CredenciaisErradasException;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {
    /**
     *  Este construtor cria a janela e todas as caixas de texto/labels/botão
     *  necessários. Para além de os criar também trata dos tamanhos e posicionamento destes
     */
    public Login(Iln ln) {
        // Criar o painel, botão de login e todas as caixas de texto e labels
        JPanel p = new JPanel(new GridBagLayout());

        JTextField usernameBox = new JTextField(20);
        JTextField passwordBox = new JTextField(20);
    
        JButton loginButton = new JButton("Login");

        JLabel usernameLabel = new JLabel("Nome de utilizador");
        JLabel passwordLabel = new JLabel("Palavra-passe");
        JLabel statusLabel = new JLabel();

        // Criar as ações dos butões
        loginButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // Obter o texto das caixas de texto
                String username = usernameBox.getText();
                String password = passwordBox.getText();

                try {
                    // Se utilizador existir criar o menú principal e destruir esta janela
                    if(ln.autenticar(username, password) != -1) {
                        new MenuPrincipal(ln);
                        dispose();
                    }
                } catch (CredenciaisErradasException e) {
                    /** 
                     *  Caso as credenciais do utilizador estejam erradas,
                     *  informamo-lo e atualizamos o tamanho da janela.
                     */
                    statusLabel.setText("Nome de utilizador ou palavra-passe incorreta.");
                    statusLabel.setVisible(true);
                    pack();
                }
            }
        });

        // Definir o posicionamento dos butões, caixas de texto e labels
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(10, 10, 10, 10);

        c.gridx = 0;
        c.gridy = 0;
        p.add(usernameLabel,c);

        c.gridx = 1;
        p.add(usernameBox,c);

        c.gridx = 0;
        c.gridy = 1;
        p.add(passwordLabel, c);

        c.gridx = 1;
        p.add(passwordBox, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        p.add(loginButton,c);

        c.gridy = 3;
        p.add(statusLabel,c);

        // Configurar a janela
        this.setSize(400,200);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Login");
        
        // Adicionar o painel à janela
        this.add(p);
        // Definir o tamanho da janela
        this.pack();
    }
}
