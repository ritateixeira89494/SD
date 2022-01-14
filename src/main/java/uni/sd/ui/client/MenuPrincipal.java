package uni.sd.ui.client;

import uni.sd.ln.client.ILN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Menu principal do utilizador normal. Aqui estão todas os métodos
 * que o utilizador pode executar.
 * <p>
 *  TODO: Acabar de adicionar os métodos.
 */
public class MenuPrincipal extends JFrame {
    public MenuPrincipal(ILN ln) {
        // Criar os paineis
        JPanel pConta = new JPanel();
        JPanel pVoo = new JPanel();

        // Criar e atribuir os layouts para os paineis
        BoxLayout layoutConta = new BoxLayout(pConta, BoxLayout.Y_AXIS);
        BoxLayout layoutVoo = new BoxLayout(pVoo, BoxLayout.Y_AXIS);

        pConta.setLayout(layoutConta);
        pVoo.setLayout(layoutVoo);

        // Criar bordas dos paineis        
        pConta.setBorder(BorderFactory.createTitledBorder("Conta"));
        pVoo.setBorder(BorderFactory.createTitledBorder("Voo"));

        // Criar os butões
        JButton logout = new JButton("Log Out");
        JButton obterVoos = new JButton("Lista de Voos");

        // Adicionar as ações dos butões
        logout.addActionListener(new ActionListener() {
            /**
             *  Caso o utilizador carregue no butão de logout,
             *  criamos a janela de login e destruimos a janela
             *  do menú principal.
             *
             * @param event Este argumento tem de ser passado. 
             *              Nós aqui não o usamos.
             */
            public void actionPerformed(ActionEvent event) {
                new Login(ln);
                dispose();
            }
        });
        obterVoos.addActionListener(new ActionListener() {
            /**
             * Caso o utilizador carregue no butão de obter lista de voos,
             * buscar a lista de voos à camada de lógica de negócios e 
             * criar uma nova janela de listagem de voos.
             *
             * @param event Este argumento tem de ser passado. 
             *              Nós aqui não o usamos.
             */
            public void actionPerformed(ActionEvent event) {
                try {
                    new ListaVoos(ln.obterListaVoo());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Adicionar os butões aos respetivos paineis
        pConta.add(logout);
        pVoo.add(obterVoos);

        // Configurar a janela
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Menu Principal");

        // Adicionar os paineis à janela
        this.setLayout(new FlowLayout());
        this.add(pConta);
        this.add(pVoo);

        // Definir o tamanho da janela
        this.pack();
    }
}
