package uni.sd.ui.client;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.FlowLayout;

import uni.sd.ln.ssvoos.voos.Voo;

/**
 *  Esta classe cria uma janela para mostrar os voos.
 *  Como esta classe não faz mais nada para além disto, não
 *  precisamos de lhe passar a interface de lógica de negócio
 *  como fazemos com outras janelas.
 */
public class ListaVoos extends JFrame {
    public ListaVoos(List<Voo> voos) {
        // Criar os paineis
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();

        // Criar e atribuir os layouts
        BoxLayout layout1 = new BoxLayout(p1, BoxLayout.Y_AXIS);
        p1.setLayout(layout1);
        p1.setBorder(BorderFactory.createEtchedBorder());

        BoxLayout layout2 = new BoxLayout(p2, BoxLayout.Y_AXIS);
        p2.setLayout(layout2);
        p2.setBorder(BorderFactory.createEtchedBorder());

        BoxLayout layout3 = new BoxLayout(p3, BoxLayout.Y_AXIS);
        p3.setLayout(layout3);
        p3.setBorder(BorderFactory.createEtchedBorder());

        // Se a lista estiver vazia não temos voos para imprimir
        if(voos.isEmpty()) {
            JLabel empty = new JLabel("Não há nenhum voo. HAHAHA!!!");
            Dimension d = empty.getPreferredSize();
            empty.setSize(d);
            empty.setVisible(true);
            p1.add(empty);
        } else {
            /** Imprime todos os voos nos seguintes layouts:
                Partida: Painel 1
                Destino: Painel 2
                Capacidade: Painel 3
            */
            for(Voo voo: voos) {
                JLabel partidaLabel = new JLabel("Partida:" + voo.getPartida());
                JLabel destLabel = new JLabel("Destino:" + voo.getDestino());
                JLabel capacidadeLabel = new JLabel("Capacidade: " + voo.getCapacidade());

                // Definir o alinhamento das labels
                partidaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                destLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                capacidadeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                // Adicionar as labels aos respetivos paineis
                p1.add(partidaLabel);
                p2.add(destLabel);
                p3.add(capacidadeLabel);

                // Adicionar um pading vertical
                p1.add(Box.createRigidArea(new Dimension(0,10)));
                p2.add(Box.createRigidArea(new Dimension(0,10)));
                p3.add(Box.createRigidArea(new Dimension(0,10)));
            }
        }

        // Configurar a janela
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Lista de Voos");

        // Adicionar os paineis à janela
        this.setLayout(new FlowLayout());
        this.add(p1);
        this.add(p2);
        this.add(p3);
        
        // Definir o tamanho da janela
        this.pack();
    }
}
