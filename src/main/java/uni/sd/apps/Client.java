package uni.sd.apps;

import uni.sd.ln.LN;
import uni.sd.ui.client.Login;

import java.sql.SQLException;

public class Client {
    public static void main(String[] args) throws SQLException {
        new Login(new LN());
    }
}
