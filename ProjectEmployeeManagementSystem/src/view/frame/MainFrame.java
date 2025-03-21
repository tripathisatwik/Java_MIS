package view.frame;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import view.panel.BottomPanel;
import view.panel.MainPanel;
import view.panel.TopPanel;

public class MainFrame extends JFrame implements ActionListener {

    static MainPanel mp;
    BorderLayout bl;
    TopPanel tp;
    BottomPanel bp;

    public MainFrame() {
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Dashboard");
        bl = new BorderLayout();
        setLayout(bl);
        // adding main panel
        tp = new TopPanel();
        mp = new MainPanel();
        bp = new BottomPanel();
        add(tp, bl.NORTH); // tp is a JPanel
        add(mp, bl.CENTER); // mp is a JDesktopPane
        add(bp, bl.SOUTH); // bp is a JPanel
        /**
         * here 'tp' is a TopPanel's object it can access to all public
         * attributes of TopPanel so here we are accessing public JMenuItem
         * 'user_add'
         */
        tp.employee_add.addActionListener(this);
        tp.employee_list.addActionListener(this);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == tp.employee_list) {
            employeeListView();
        } else if (e.getSource() == tp.employee_add) {
            addEmployeeView();
        }
    }

    public static void employeeListView() {
        disposeAllFrame();
        getUiManager();
        EmployeeListView ul = new EmployeeListView(); // is a JInternalFrame
        ul.setVisible(true);
        try {
            ul.setSelected(true);
        } catch (PropertyVetoException e) {
            System.out.println("Error: " + e.getMessage());
        }
        mp.add(ul);
    }

    public static void addEmployeeView() {
        disposeAllFrame();
        getUiManager();
        AddEmployeeView au = new AddEmployeeView(); // is a JInternalFrame
        au.setVisible(true);
        try {
            au.setSelected(true);
        } catch (PropertyVetoException e) {
            System.out.println("Error: " + e.getMessage());
        }
        mp.add(au);
    }

    public static void loadEditEmployeeView(EditEmployeeView ev) {
        disposeAllFrame();
        getUiManager();
        ev.setVisible(true);
        try {
            ev.setSelected(true);
        } catch (PropertyVetoException e) {
            System.out.println("Error: " + e.getMessage());
        }
        mp.add(ev);
    }

    public static void getUiManager() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void disposeAllFrame() {
        JInternalFrame[] frames = mp.getAllFrames();
        for (JInternalFrame frame : frames) {
            if (frame.isSelected()) {
                frame.dispose();
            }
        }
    }
}
