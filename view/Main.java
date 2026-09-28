package view;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            try{
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());           
            }catch(Exception e){
                e.printStackTrace();
            }
            new MainFrame();
        });
    } 
}
