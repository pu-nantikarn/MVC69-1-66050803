import View.MainMenuView;
import javax.swing.SwingUtilities;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuView mainMenu = new MainMenuView();
            mainMenu.setVisible(true);
        });
    }
}