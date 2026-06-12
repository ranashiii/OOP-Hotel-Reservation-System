package MainAndGUIReceptionist;

import Manager.SessionManager;
import OtherGUILoginRegistration.LoginFrame;
public class MainSystem {
    
    public static void main(String[] args) {
        
        SessionManager.init();
        new LoginFrame().setVisible(true);
    }
}
