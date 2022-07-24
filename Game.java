import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

class Game extends JFrame{
    //#region "Enumeradores de controle"

    enum Direcao{
        SOBE,
        DESCE
    }

    enum DirecaoBola{
        SOBE_ESQUERDA,
        SOBE_DIREITA,
        DESCE_ESQUERDA,
        DESCE_DIREITA
    }

    //#endregion 

    //#region "Variáveis de instância"

    Visual Visual = new Visual();

    //#endregion

    //#region "Variáveis de imagem"

    Image fundo;
    Image player1;
    Image player2;
    Image bola;

    //#endregion

    //#region "Variáveis de direção"

    Direcao player1State;
    Direcao player2State;
    DirecaoBola bolaState;

    //#endregion

    //#region "Variáveis de posição"

    int posicaoYP1 = 250;
    int posicaoYP2 = 250;
    int posicaoXBola = 492;
    int posicaoYBola = 292;

    //#endregion

    Game(){
        super("Pong");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(Visual);
        pack();
        setVisible(true);

        addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e){
				switch(e.getKeyCode()){
					case KeyEvent.VK_W:
                        player1State = Direcao.SOBE;
                        break;
					case KeyEvent.VK_S:
                        player1State = Direcao.DESCE;
                        break;
					case KeyEvent.VK_UP:
					case KeyEvent.VK_DOWN:
				}
				repaint();
		   }
		});

        Timer t = new Timer(100, new Temporizador());
        t.start();
    }

    void atualizaGame(){
        movimentaPlayer1();
    }

    void movimentaPlayer1(){
        if(player1State == Direcao.SOBE){
            posicaoYP1 -= 10;
        }

        if(player1State == Direcao.DESCE){
            posicaoYP1 += 10;
        }

        player1State = null;
    }

    class Visual extends JPanel{
        Visual(){
            setPreferredSize(new Dimension(1000, 600));
            try{
                fundo = ImageIO.read(new File("media/fundo.png"));
                player1 = ImageIO.read(new File("media/p1.png"));
                player2 = ImageIO.read(new File("media/p2.png"));
                bola = ImageIO.read(new File("media/bola.png"));
            }
            catch(IOException ex){
                JOptionPane.showMessageDialog(this, "A imagem não pode ser carregada!\n" + ex, "Erro", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(fundo, 0, 0, getSize().width, getSize().height, this);
            g.drawImage(player1, 5, posicaoYP1, 15, 100, this);
            g.drawImage(player2, 980, posicaoYP2, 15, 100, this);
            g.drawImage(bola, 492, 292, 16, 16, this);
            Toolkit.getDefaultToolkit().sync();
        }
    }

    class Temporizador implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
			atualizaGame();
		}
    }
}