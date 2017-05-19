
public class Items {
    String loandate,objname,objtype;

    public Items(){

    }

    public Items(String loandate,String objname, String objtype){
        this.loandate=loandate;
        this.objname=objname;
        this.objtype=objtype;
    }

    public String getLoandate(){
        return loandate;
    }

    public String getObjname(){
        return objname;
    }

    public String getObjtype(){
        return objtype;
    }

    public void setLoandate(String loandate){
        this.loandate=loandate;
    }

    public void setObjname(String objname){
        this.objname=objname;
    }

    public void setObjtype(String objtype){
        this.objtype=objtype;
    }
}
