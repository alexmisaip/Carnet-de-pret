
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

public class XMLTraitement {

    private Document doc;
    private File file;

    public XMLTraitement(File file) {
        open(file);
    }

    public void addContact(Contact c) {
        try {
            //find last id
            List nodes = XPath.selectNodes(doc.getRootElement(), "//contact[@id]");
            Iterator it = nodes.iterator();
            int lastid = 0;

            while (it.hasNext()) {
                Element e = (Element) it.next();
                String id = e.getAttributeValue("id");
                lastid = Integer.parseInt(id);
            }

            int newid = ++lastid;

            Element contact = new Element("contact");
            contact.setAttribute("id", String.valueOf(newid));
            doc.getRootElement().addContent(contact);
            addNode(contact, "nom", c.getNom());
            addNode(contact, "prenom", c.getPrenom());
            addItemsNode(contact, c.getItems());

        } catch (JDOMException ex) {
            Logger.getLogger(XMLTraitement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addItemsNode(Element e, Items items) {
        addNode(e, "items", "");
        Element n = e.getChild("items");
        addNode(n, "loandate", items.getLoandate());
        addNode(n, "objname", items.getObjname());
        addNode(n, "objtype", items.getObjtype());
    }

    private void addNode(Element parent, String child, String text) {
        Element node = new Element(child);
        node.setText(text);
        parent.addContent(node);
        save();
    }

    public void saveEdit(Contact c) {
        try {
            List node = XPath.selectNodes(doc.getRootElement(), "//contact[@id='" + c.getID() + "']");
            Iterator i = node.iterator();
            Element e = (Element) i.next();
            e.getChild("nom").setText(c.getNom());
            e.getChild("prenom").setText(c.getPrenom());

            List itemsNode = XPath.selectNodes(e, "//contact[@id='" + c.getID() + "']/items']");
            Iterator pi = itemsNode.iterator();
            Element pe = (Element) pi.next();
            pe.getChild("loandate").setText(c.getItems().getLoandate());
            pe.getChild("objname").setText(c.getItems().getObjname());
            pe.getChild("objtype").setText(c.getItems().getObjtype());

            save();
        } catch (JDOMException ex) {
        }
    }

    public void deleteContact(Contact c) {
        try {
            List node = XPath.selectNodes(doc.getRootElement(), "//contact[@id='" + c.getID() + "']");
            Iterator i = node.iterator();
            Element e = (Element) i.next();
            Element parent = e.getParentElement();
            parent.removeContent(e);
            save();
            JOptionPane.showMessageDialog(null, c.getNom() + " " + c.getPrenom() + " a été supprimé");
        } catch (JDOMException ex) {
            JOptionPane.showMessageDialog(null, "Contact non supprimé");
        }
    }

    public void deleteContact(String id) {
    }

    public Contact[] getAllContacts() {
        Vector contacts = new Vector();
        try {

            List nodes = XPath.selectNodes(doc.getRootElement(), "//contact");
            Iterator it = nodes.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                //getting values
                String id = e.getAttributeValue("id");
                String nom = e.getChildText("nom");
                String prenom = e.getChildText("prenom");;
                //Items
                String loandate = e.getChild("items").getChildText("loandate");
                String objname = e.getChild("items").getChildText("objname");
                String objtype = e.getChild("items").getChildText("objtype");

                Items items = new Items(loandate, objname, objtype);
                Contact c = new Contact(id, nom, prenom, items);

                contacts.add(c);
            }

        } catch (JDOMException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        //loading to contact array
        Contact contactlist[] = new Contact[contacts.size()];
        for (int i = 0; i < contacts.size(); i++) {
            contactlist[i] = (Contact) contacts.elementAt(i);
        }

        return contactlist;
    }

    public Contact getContact(String cid) {
        try {
            Contact c;
            List nodes = XPath.selectNodes(doc.getRootElement(), "//contact[@id='" + cid + "']");
            Iterator it = nodes.iterator();
            if (it.hasNext()) {
                Element e = (Element) it.next();
                //getting values
                String id = e.getAttributeValue("id");
                String nom = e.getChildText("nom");
                String prenom = e.getChildText("prenom");
                //items
                String loandate = e.getChild("items").getChildText("loandate");
                String objname = e.getChild("items").getChildText("objname");
                String objtype = e.getChild("items").getChildText("objtype");

                Items items = new Items(loandate, objname, objtype);
                c = new Contact(id, nom, prenom, items);

                return c;

            } else {
                JOptionPane.showMessageDialog(null, "Contact non trouvé");
            }

        } catch (JDOMException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }

    public String getText(Object o) {

        return "";
    }

    public void open(File _file) {
        file = _file;
        try {
            SAXBuilder builder = new SAXBuilder();
            doc = builder.build(file);
            System.out.println("Fichier XML ouvert: " + _file.getName());
        } catch (JDOMException jde) {
            jde.printStackTrace();
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
    }

        public Contact[] findContacts(String search) {
        Vector contacts = new Vector();
        try {

            List nodes = XPath.selectNodes(doc.getRootElement(), "//contact[prenom='"+search+"' or nom='"+search+"']");
            Iterator it = nodes.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                //getting values
                String id = e.getAttributeValue("id");
                String nom = e.getChildText("nom");
                String prenom = e.getChildText("prenom");
                //Items
                String loandate = e.getChild("items").getChildText("loandate");
                String objname = e.getChild("items").getChildText("objname");
                String objtype = e.getChild("items").getChildText("objtype");

                Items items = new Items(loandate, objname, objtype);
                Contact c = new Contact(id, nom, prenom, items);

                contacts.add(c);
            }

        } catch (JDOMException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        //loading to contact array
        Contact contactlist[] = new Contact[contacts.size()];
        for (int i = 0; i < contacts.size(); i++) {
            contactlist[i] = (Contact) contacts.elementAt(i);
        }

        return contactlist;
    }

    public void save() {
        try {
            XMLOutputter xmlo = new XMLOutputter();
            xmlo.output(doc, new FileOutputStream(file));
        } catch (java.io.FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
