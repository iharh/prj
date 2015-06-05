package rxjdbc;

public class PSCred {
    private String usr;
    private String pwd;

    public PSCred(String usr, String pwd) {
        this.usr = usr;
        this.pwd = pwd;
    }

    public String getUsr() {
        return usr;
    }

    public String getPwd() {
        return pwd;
    }

    @Override
    public String toString() {
        return "usr: " + usr + " pwd: " + pwd;
    }
};

