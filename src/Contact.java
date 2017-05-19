
public class Contact {
    private String id,nom,prenom;
    private Items items;

    public Contact(){
        items=new Items();
    }

    public Contact(String id, String nom, String prenom, Items items){
        this.id=id;
        this.nom=nom;
        this.prenom=prenom;
        this.items=items;
    }

    public Contact( String nom, String prenom, Items items){
        this.nom=nom;
        this.prenom=prenom;
        this.items=items;
    }

    public String getID(){
        return id;
    }

    public String getNom(){
        return nom;
    }

    public String getPrenom(){
        return prenom;
    }

    public Items getItems(){
        return items;
    }

    public void setID(String id){
        this.id=id;
    }

    public void setNom(String nom){
        this.nom=nom;
    }

    public void setPrenom(String prenom){
        this.prenom=prenom;
    }

    public void setItems(Items items){
        this.items=items;
    }
}
