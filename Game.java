import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

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
    Image zero, um, dois, tres, quatro, cinco;

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

    //#region "Flags"

    boolean iniciaRodada = true;

    //#endregion

    //#region "Placar"

    int pontosPLayer1 = 0;
    int pontosPLayer2 = 0;

    //#endregion

    Timer tempor;

    Game(){
        super("Pong");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
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
                        player2State = Direcao.SOBE;
                        break;
					case KeyEvent.VK_DOWN:
                        player2State = Direcao.DESCE;
                        break;
				}
				repaint();
		   }
		});

        tempor = new Timer(25, new Temporizador());
        tempor.start();
    }

    void atualizaGame(){
        movimentaPlayer1();
        movimentaPlayer2();
        movimentaBola();
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

    void movimentaPlayer2(){
        if(player2State == Direcao.SOBE){
            posicaoYP2 -= 10;
        }

        if(player2State == Direcao.DESCE){
            posicaoYP2 += 10;
        }

        player2State = null;
    }

    void movimentaBola(){
        if(iniciaRodada){
            Random randomNumber = new Random();
            bolaState = DirecaoBola.values()[randomNumber.nextInt(4)];
        }

        if(bolaState == DirecaoBola.SOBE_ESQUERDA){
            posicaoXBola -= 5;
            posicaoYBola -= 5;
        }

        if(bolaState == DirecaoBola.SOBE_DIREITA){
            posicaoXBola += 5;
            posicaoYBola -= 5;
        }

        if(bolaState == DirecaoBola.DESCE_ESQUERDA){
            posicaoXBola -= 5;
            posicaoYBola += 5;
        }

        if(bolaState == DirecaoBola.DESCE_DIREITA){
            posicaoXBola += 5;
            posicaoYBola += 5;
        }

        if(posicaoYBola == 2 && bolaState == DirecaoBola.SOBE_DIREITA){
            bolaState = DirecaoBola.DESCE_DIREITA;
        }
        
        if(posicaoYBola == 2 && bolaState == DirecaoBola.SOBE_ESQUERDA){
            bolaState = DirecaoBola.DESCE_ESQUERDA;
        }

        if(posicaoYBola == 582 && bolaState == DirecaoBola.DESCE_DIREITA){
            bolaState = DirecaoBola.SOBE_DIREITA;
        }

        if(posicaoYBola == 582 && bolaState == DirecaoBola.DESCE_ESQUERDA){
            bolaState = DirecaoBola.SOBE_ESQUERDA;
        }

        // está no lado esquerdo pronto para bater com o player
        if(posicaoXBola <= 20){
            // caso esteja batendo no jogador
            if(posicaoYBola >= posicaoYP1 && posicaoYBola <= posicaoYP1 + 100){
                // esteja subindo
                if(bolaState == DirecaoBola.SOBE_ESQUERDA){
                    bolaState = DirecaoBola.SOBE_DIREITA;
                }

                // esteja descendo
                if(bolaState == DirecaoBola.DESCE_ESQUERDA){
                    bolaState = DirecaoBola.DESCE_DIREITA;
                }
            }
        }

        // ponto para o jogador 2
        if(posicaoXBola <= 0){
            pontosPLayer2++;
            iniciaRodada = true;
            posicaoXBola = 492;
            posicaoYBola = 292;
        }

        // está no lado direito pronto para bater com o player 2
        if(posicaoXBola >= 940 ){
            // caso esteja batendo no jogador
            if(posicaoYBola >= posicaoYP2 && posicaoYBola <= posicaoYP2 + 100){
                // esteja subindo
                if(bolaState == DirecaoBola.SOBE_DIREITA){
                    bolaState = DirecaoBola.SOBE_ESQUERDA;
                }

                // esteja descendo
                if(bolaState == DirecaoBola.DESCE_DIREITA){
                    bolaState = DirecaoBola.DESCE_ESQUERDA;
                }
            }
        }

        if(posicaoXBola >= 1000){
            pontosPLayer1++;
            iniciaRodada = true;
            posicaoXBola = 492;
            posicaoYBola = 292;
        }

        iniciaRodada = false;
        repaint();
    }

    class Visual extends JPanel{
        Visual(){
            setPreferredSize(new Dimension(1000, 600));
            try{
                fundo = ImageIO.read(new File("media/fundo.png"));
                player1 = ImageIO.read(new File("media/p1.png"));
                player2 = ImageIO.read(new File("media/p2.png"));
                bola = ImageIO.read(new File("media/bola.png"));
                zero = ImageIO.read(new File("media/0.png"));
                um = ImageIO.read(new File("media/1.png"));
                dois = ImageIO.read(new File("media/2.png"));
                tres = ImageIO.read(new File("media/3.png"));
                quatro = ImageIO.read(new File("media/4.png"));
                cinco = ImageIO.read(new File("media/5.png"));
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

            switch(pontosPLayer1){
                case 0:
                    g.drawImage(zero, 200, 100, 50, 50, this);
                    break;
                case 1:
                    g.drawImage(um, 200, 100, 50, 50, this);
                    break;
                case 2:
                    g.drawImage(dois, 200, 100, 50, 50, this);
                    break;
                case 3:
                    g.drawImage(tres, 200, 100, 50, 50, this);
                    break;
                case 4:
                    g.drawImage(quatro, 200, 100, 50, 50, this);
                    break;
            }

            switch(pontosPLayer2){
                case 0:
                    g.drawImage(zero, 700, 100, 50, 50, this);
                    break;
                case 1:
                    g.drawImage(um, 700, 100, 50, 50, this);
                    break;
                case 2:
                    g.drawImage(dois, 700, 100, 50, 50, this);
                    break;
                case 3:
                    g.drawImage(tres, 700, 100, 50, 50, this);
                    break;
                case 4:
                    g.drawImage(quatro, 700, 100, 50, 50, this);
                    break;
            }

            g.drawImage(player1, 5, posicaoYP1, 15, 100, this);
            g.drawImage(player2, 980, posicaoYP2, 15, 100, this);
            g.drawImage(bola, posicaoXBola, posicaoYBola, 16, 16, this);
            Toolkit.getDefaultToolkit().sync();

            if(pontosPLayer1 >= 5){
                tempor.stop();
                g.drawImage(cinco, 200, 100, 50, 50, this);
                super.paintComponent(g);
                JOptionPane.showMessageDialog(this, "O jogador 1 ganhou!!");
                System.exit(1);
            }

            if(pontosPLayer2 >= 5){
                tempor.stop();
                g.drawImage(cinco, 700, 100, 50, 50, this);
                super.paintComponent(g);
                JOptionPane.showMessageDialog(this, "O jogador 2 ganhou!!");
                System.exit(1);
            }
        }
    }

    class Temporizador implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
			atualizaGame();
		}
    }
}