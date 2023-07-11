package connectionDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class koneksi {
    private Connection koneksi;
    private Statement stm; 
    public Connection connect(){
        try {
            String url ="jdbc:mysql://localhost/SPK_PemilihanPekerjaanME";
            String user="root";
            String pass="";
            Class.forName("com.mysql.jdbc.Driver");
            koneksi = DriverManager.getConnection(url,user,pass);
            stm = koneksi.createStatement();
            System.out.println("Koneksi Berhasil;");
        } catch (Exception e) {
            System.err.println("Koneksi Gagal" +e.getMessage());
        }
        return koneksi;
    }
}