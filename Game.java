import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

class Game extends JFrame{
    Visual visual = new Visual();
    Image fundo;

    Game(){
        super("Pong");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(visual);
        pack();
        setVisible(true);

        addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e){
				switch(e.getKeyCode()){
					case KeyEvent.VK_W:
					case KeyEvent.VK_S:
					case KeyEvent.VK_UP:
					case KeyEvent.VK_DOWN:
				}
				repaint();
		   }
		});
    }

    class Visual extends JPanel{
        Visual(){
            setPreferredSize(new Dimension(1000, 600));
            try{
                fundo = ImageIO.read(new File("media/fundo.png"));
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
            Toolkit.getDefaultToolkit().sync();
        }
    }
}