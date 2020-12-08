import java.awt.*;
import javax.swing.*;
/**
 *
 * @author Liutuan
 */
public class ChatFrame extends JFrame{
    
    public JTextArea textArea_1;
    public JTextArea textArea_2;
    public JButton button_2;
    
    public ChatFrame(){
        super();
        setTitle("Super chat system");
        setBounds(100, 100, 558, 576);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5,5));
        getContentPane().add(panel, BorderLayout.NORTH);

        final JLabel label = new JLabel(new ImageIcon("src/image/profile.png"));
        panel.add(label, BorderLayout.WEST);
        label.setPreferredSize(new Dimension(74,74));

        final JPanel panel_1 = new JPanel();
        panel_1.setLayout(new BorderLayout());
        panel.add(panel_1, BorderLayout.CENTER);

        final JLabel info = new JLabel();
        info.setText("Welcome to the super distributed chat room!");
        panel_1.add(info, BorderLayout.CENTER);
        
        final JLabel author = new JLabel();
        author.setText("                                                                         -- Taoyang LIU");
        panel_1.add(author, BorderLayout.SOUTH);

        final JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(300);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        final JPanel panel_2 = new JPanel();
        panel_2.setLayout(new BorderLayout());
        splitPane.setRightComponent(panel_2);

        final JPanel panel_3 = new JPanel();
        final FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        panel_3.setLayout(flowLayout);
        panel_2.add(panel_3, BorderLayout.NORTH);

        final JPanel panel_4 = new JPanel();
        final FlowLayout flowLayout_1 = new FlowLayout();
        flowLayout_1.setAlignment(FlowLayout.RIGHT);
        panel_4.setLayout(flowLayout_1);
        panel_2.add(panel_4, BorderLayout.SOUTH);


        button_2 = new JButton();
        button_2.setText("Send");
        panel_4.add(button_2);

        final JScrollPane scrollPane = new JScrollPane();
        panel_2.add(scrollPane, BorderLayout.CENTER);

        textArea_1 = new JTextArea();
        scrollPane.setViewportView(textArea_1);

        final JScrollPane scrollPane_1 = new JScrollPane();
        splitPane.setLeftComponent(scrollPane_1);

        textArea_2 = new JTextArea();
        textArea_2.setEditable(false);
        scrollPane_1.setViewportView(textArea_2);
        
    }
}
