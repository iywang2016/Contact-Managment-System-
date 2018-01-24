/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package contactmanager;

import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author krishna
 */
public class Database {
    
    ResultSet executeQuery(String query, Component obj)
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url="jdbc:mysql://localhost:3306/contactmanager";
            Connection con = DriverManager.getConnection(url, "root", "bunny007");
            Statement stmt = con.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            return rs;
        }
        catch(ClassNotFoundException | SQLException e){ 
            JOptionPane.showMessageDialog(obj, e);
            return null;
        }
    }

    int executeUpdate(String query, Component obj)
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url="jdbc:mysql://localhost:3306/contactmanager";
            Connection con = DriverManager.getConnection(url, "root", "bunny007");
            Statement stmt = con.createStatement();
            return stmt.executeUpdate(query);
        }
        catch(ClassNotFoundException | SQLException e){ 
            JOptionPane.showMessageDialog(obj, e);
            return 0;
        }
    }
}