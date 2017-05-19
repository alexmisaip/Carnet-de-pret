
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LoanBook extends JFrame {

    JPanel mainFrame;
    InfoContainer nom, prenom;
    ItemsContainer items;
    JTable list;
    DefaultTableModel Model;
    //
    private XMLTraitement xmltraitement;

    public LoanBook(XMLTraitement xmlp) {
        super("Sharing Items by Alexandre M");
        xmltraitement = xmlp;

        mainFrame = new JPanel();

        mainFrame.setLayout(new GridLayout(1, 1));
        mainFrame.add(displayHome());
        //add(displayHome());
        add(mainFrame);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (d.getWidth() / 2) - 250, (int) (d.getHeight() / 2) - 250);
        setLayout(new GridLayout(1, 1));
        setSize(300, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //display home page
    private JPanel displayHome() {
        JPanel p = new JPanel();
        JButton add = new JButton("Add a contact");
        JButton view = new JButton("View a contact");
        JPanel contactListPane = new JPanel();
        //JList contactlist;

        Contact[] contacts = xmltraitement.getAllContacts();
        //contactlist = new JList(contacts);
        Vector titleVec = new Vector();
        String title[] = {"ID", "Nom", "Prenom"};

        Vector contactlist = new Vector();

        for (int i = 0; i < contacts.length; i++) {
            Vector contact = new Vector();
            Contact c = contacts[i];
            contact.add(c.getID());
            contact.add(c.getNom());
            contact.add(c.getPrenom());
            contactlist.add(contact);
        }
        titleVec.addAll(Arrays.asList(title));
        Model = new DefaultTableModel(contactlist, titleVec);
        list = new JTable();
        list.setModel(Model);
        list.setDragEnabled(false);
        JScrollPane js = new JScrollPane(list);
        js.setBorder(BorderFactory.createTitledBorder("Liste des contacts"));
        js.setBackground(Color.gray);
        //contactlist.addListSelectionListener(this);
        contactListPane.setLayout(new GridLayout(1, 1));
        contactListPane.setBounds(20, 80, 250, 270);
        contactListPane.add(js);

        add.addActionListener(new ActionListener() {  // add a contact listener

            public void actionPerformed(ActionEvent e) {
                mainFrame.removeAll();
                mainFrame.add(addContactDisplay(new Contact(), true));
                mainFrame.validate();
                mainFrame.repaint();
            }
        });
        add.setBounds(20, 30, 120, 25);

        view.addActionListener(new ActionListener() {  //view listener

            public void actionPerformed(ActionEvent e) {
                try {
                    int x = list.getSelectedRow();
                    int y = 0;
                    String s = String.valueOf(list.getModel().getValueAt(x, y));
                    mainFrame.removeAll();
                    mainFrame.add(addContactDisplay(xmltraitement.getContact(s), false));
                    mainFrame.validate();
                    mainFrame.repaint();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Selectionner un contact de la liste");
                }
            }
        });
        view.setBounds(150, 30, 120, 25);

        p.setLayout(null);
        p.add(add);
        p.add(view);
        p.add(contactListPane);
        p.setPreferredSize(new Dimension(400, 400));

        return p;
    }

    private JPanel addContactDisplay(Contact _contact, final Boolean b) {
        final Contact contact = _contact;
        JPanel p = new JPanel();
        JPanel content = new JPanel();
        nom = new InfoContainer("Nom", contact.getNom());
        prenom = new InfoContainer("Prenom", contact.getPrenom());
        items = new ItemsContainer(contact.getItems());

        content.setLayout(new GridLayout(2, 1, 10, 0));
        content.setBounds(20, 20, 250, 80);
        content.setBorder(BorderFactory.createTitledBorder("Informations"));
        content.add(nom);
        content.add(prenom);

        items.setBounds(20, 100, 250, 140);
        items.setBorder(BorderFactory.createTitledBorder("Items"));

        JButton save = new JButton("Save");
        save.setBounds(20, 250, 100, 30);
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //save contact
                contact.setNom(nom.getValue());
                contact.setPrenom(prenom.getValue());
                contact.setItems(items.getValue());
                if (b) {
                    xmltraitement.addContact(contact);
                    JOptionPane.showMessageDialog(null, "Contact enregistré!");
                    mainFrame.removeAll();
                    mainFrame.add(addContactDisplay(contact, false));
                    mainFrame.validate();
                    mainFrame.repaint();
                } else {
                    xmltraitement.saveEdit(contact);
                    JOptionPane.showMessageDialog(null, "Changement effectué!");
                }

            }
        });
        JButton cancel = new JButton("Cancel");
        if (!b) {
            cancel.setText("Back home");
        }
        cancel.setBounds(160, 250, 100, 30);
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mainFrame.removeAll();
                mainFrame.add(displayHome());
                mainFrame.validate();
                mainFrame.repaint();
            }
        });

        JButton delete = new JButton("Delete");
        delete.setBounds(95, 280, 100, 30);
        delete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                xmltraitement.deleteContact(contact);
                mainFrame.removeAll();
                mainFrame.add(displayHome());
                mainFrame.validate();
                mainFrame.repaint();
            }
        });

        p.add(content);
        p.add(items);
        p.add(save);
        p.add(cancel);
        if (!b) {
            p.add(delete);
        }
        p.setLayout(null);
        p.setPreferredSize(new Dimension(400, 400));

        return p;
    }

    public static void main(String args[]) {
        try {
            String filename = "LoanBookData.xml";
            File sourceXML = new File(filename);
            if (sourceXML.createNewFile()) {
                FileWriter fw = new FileWriter(sourceXML);
                fw.write("<?xml version='1.0' encoding='UTF-8'?>" + "<contactlist nextid='1'></contactlist>");
                fw.close();
            }
            //File sourceXML = new File(filename);
            XMLTraitement xmlp = new XMLTraitement(sourceXML);
            LoanBook ab = new LoanBook(xmlp);
        } catch (IOException ex) {
            Logger.getLogger(LoanBook.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class InfoContainer extends JPanel {

        private String label = "";
        private String value = "";
        private JTextField textfield;

        public InfoContainer(String _label, String _value) {
            label = _label;
            value = _value;
            setLayout(new GridLayout(1, 2));
            add(new JLabel(label));
            add(textfield = new JTextField(value));
        }

        public void setEditable(boolean b) {
            textfield.setEditable(b);
            revalidate();
        }

        public void setValue(String _value) {
            textfield.setText(_value);
        }

        public String getValue() {
            return textfield.getText();
        }
    }

    class ItemsContainer extends JPanel {

        private InfoContainer loandate, objname, objtype;

        public ItemsContainer(Items items) {
            this(items.getLoandate(), items.getObjname(), items.getObjtype());
        }

        public ItemsContainer(String _loandate, String _objname, String _objtype) {
            this.loandate = new InfoContainer("Date de pret", _loandate);
            this.objname = new InfoContainer("Nom objet", _objname);
            this.objtype = new InfoContainer("Type objet", _objtype);

            setLayout(new GridLayout(3, 1, 10, 0));

            add(loandate);
            add(objname);
            add(objtype);
        }

        public Items getValue() {
            Items items = new Items(loandate.getValue(), objname.getValue(), objtype.getValue());
            return items;
        }

        public void setValue(Items items) {
            loandate.setValue(items.getLoandate());
            objname.setValue(items.getObjname());
            objtype.setValue(items.getObjtype());

        }

        public void setEditable(boolean b) {
            loandate.setEditable(b);
            objname.setEditable(b);
            objtype.setEditable(b);
            revalidate();
        }
    }
}
