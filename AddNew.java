/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contactmanager;

import java.awt.Component;
import java.awt.Container;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import sun.awt.windows.ThemeReader;

/**
 *
 * @author Krishna
 */
public class AddNew extends javax.swing.JFrame {

    /**
     * Creates new form CManager
     */
    
    Database db = null;
//    List<Component> list = null;
    Date dt = null;
    ResultSet personrs = null;
    ResultSet emailrs = null;
    ResultSet phoners = null;
    ResultSet groupsrs = null;
    ResultSet meetingsrs = null;
    ResultSet addressrs = null;
    int editedpid = 0;

    public AddNew() {
        initComponents();
//        list =  getAllComponents(this);
        db = new Database();
    }
    
    public void loadData(int pid){
        String query = "select * from %s where pid = "+Integer.toString(pid);
        
        String personquery = String.format(query, "person");
        String emailquery = String.format(query, "email");
        String phonequery = String.format(query, "phonenumber");
        String groupsquery = String.format(query, "groups");
        String meetingsquery = String.format(query, "meetings");
        String addressquery = String.format(query,"address");
        
        boolean isadded = false;
        try{
            //Get Results
            personrs = db.executeQuery(personquery,this);
            emailrs = db.executeQuery(emailquery,this);
            phoners = db.executeQuery(phonequery,this);
            groupsrs = db.executeQuery(groupsquery,this);
            meetingsrs = db.executeQuery(meetingsquery,this);
            addressrs = db.executeQuery(addressquery,this);
            
            //Person Details
            while(personrs.next()){
                editedpid = Integer.parseInt(personrs.getString("pid"));
                this.firstName.setText(personrs.getString("fname"));
                this.middleInitial.setText(personrs.getString("minit"));
                this.lastName.setText(personrs.getString("lname"));
                this.persondob.setText(personrs.getDate("dob").toString());                
            }
                        
            while(emailrs.next()){
                mailid.setText(emailrs.getString("mail_id"));
                mailtype.setText(emailrs.getString("mail_type"));
            }

            while(phoners.next()){
                    areaCode.setText(phoners.getString("ph_areacode"));
                    phno.setText(phoners.getString("ph_number"));
                    phtype.setText(phoners.getString("ph_type"));
            }
            
            while(meetingsrs.next()){

                meetingdate.setText(meetingsrs.getDate("meeting_date").toString());
                meetingtime.setText(meetingsrs.getTime("meeting_time").toString());
                meetingplace.setText(meetingsrs.getString("place"));
                notes.setText(meetingsrs.getString("notes"));
            }
            
            while(addressrs.next()){
                strtno.setText(addressrs.getString("streetno"));
                strtname.setText(addressrs.getString("streetname"));
                aptno.setText(addressrs.getString("aptno"));
                city.setText(addressrs.getString("city"));
                zipcode.setText(addressrs.getString("zipcode"));
                state.setText(addressrs.getString("state"));
                addresstype.setText(addressrs.getString("address_type"));
                
            }
            
            while(groupsrs.next()){
                this.groups.setText(groupsrs.getString("group_name"));
            }
            

        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }
    
    public static List<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container)
                compList.addAll(getAllComponents((Container) comp));
        }
        return compList;
    }
    
    public void setEditable(boolean bool){
        List<Component> components = getAllComponents(this);
        for(Component c:components){
            if(c instanceof JTextField){
                ((JTextField) c).setEditable(bool);
            }
            if(c instanceof JTextArea){
                ((JTextArea) c).setEditable(bool);
            }
        }
    }
        
    public void showDialog(String title, boolean areControlsEnabled){
        changeTitle(title);
//        makeEditable(areControlsEnabled);
        this.setVisible(true);
    }
    
    private void changeTitle(String str){
        this.title.setText(str);
    }
    
//    private void makeEditable(boolean bool){
//        for(Component c:list){
//            if (!(c instanceof JLabel || c instanceof JButton)){
//                if(c instanceof JTextField)
//                    ((JTextField) c).setEditable(bool);
//                if(c instanceof JTable)
//                    ((JTable) c).setEnabled(bool);
//                if(c instanceof JComboBox)
//                    ((JComboBox) c).setEnabled(bool);
//            }
//        }
//    }  
    
    public void insert(){
        String fname = firstName.getText();
        String mi = middleInitial.getText();
        String lname = lastName.getText();
        String date = persondob.getText();
        Character gender = this.maleGender.isSelected()?'M':'F';
        int newpid =0;
        try{
            //Person Table
            String query = "INSERT INTO PERSON(dob,gender,fname,minit,lname) VALUES(\"%s\",'%c',\"%s\",\"%s\",\"%s\")";
            query = String.format(query, date, gender,fname,mi,lname);
            db.executeUpdate(query,this);

            query = "SELECT pid FROM person order by pid desc limit 1";
            ResultSet rs = db.executeQuery(query,this);
            rs.next();
            newpid = rs.getInt(1);

            //Phone Number Table
            query = "insert into phonenumber values("+Integer.toString(newpid)+","+Integer.parseInt(areaCode.getText())+",";
            query+=Integer.parseInt(phno.getText())+",\""+phtype.getText()+"\")";
            db.executeUpdate(query,this);
            
            //Email Address Table
            query = "insert into email values("+Integer.toString(newpid)+",\""+mailid.getText()+"\",\"";
            query+=mailtype.getText()+"\")";
            db.executeUpdate(query,this);
            
            //Meetings Table
            query = "insert into meetings values("+Integer.toString(newpid)+",\""+meetingdate.getText()+"\",\""+meetingtime.getText()+"\",\"";
            query += meetingplace.getText()+"\",\""+notes.getText()+"\")";
            db.executeUpdate(query,this);
            
            //Address Table            
            query = "insert into address values("+Integer.toString(newpid)+",\""+strtno.getText()+"\",\""+strtname.getText()+"\",\"";
            query += aptno.getText()+"\",\""+city.getText()+"\",\""+zipcode.getText()+"\",\""+state.getText()+"\",\""+addresstype.getText()+"\")";
            db.executeUpdate(query,this);
                        
            //Groups Table
            query = "insert into groups values("+Integer.toString(newpid)+",\""+groups.getText()+"\")";
            db.executeUpdate(query,this);
            
            JOptionPane.showMessageDialog(this, "Successfully Inserted");
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        finally{
            this.setVisible(false);
            this.dispose();
        }
    }
    
    public void update(){
        
        try{
//            if(personrs!=null)
//                while(personrs.previous()){}
//            if(meetingsrs!=null)
//                while(meetingsrs.previous()){}
//            if(groupsrs!=null)
//                while(groupsrs.previous()){}
//            if(addressrs!=null)
//                while(addressrs.previous()){}
//            if(emailrs!=null)
//                while(emailrs.previous()){}
//            if(phoners!=null)
//                while(phoners.previous()){}
            
            boolean ischanged = false;
            
            String query = "update %s set ";
            String whereclause = "where pid = "+Integer.toString(editedpid);
            
            String personquery = String.format(query,"person");
            personquery+= "fname = \""+firstName.getText()+"\",";
            personquery+= "lname = \""+lastName.getText()+"\",";
            personquery+= "minit = \""+middleInitial.getText()+"\",";
            personquery+= "dob = \""+persondob.getText()+"\",";
            personquery+= "gender = '"+(maleGender.isSelected()?"M":"F")+"'";
            db.executeUpdate(personquery+" "+whereclause, this);
            
            String groupsquery = String.format(query,"groups");
            groupsquery += "group_name=\""+groups.getText()+"\"";
            db.executeUpdate(groupsquery+" "+whereclause, this);
            
            String meetingsquery = String.format(query,"meetings");
            meetingsquery += "meeting_date=\""+meetingdate.getText()+"\",";
            meetingsquery += "meeting_time=\""+meetingtime.getText()+"\",";
            meetingsquery += "place=\""+meetingplace.getText()+"\",";
            meetingsquery += "notes=\""+notes.getText()+"\"";
            db.executeUpdate(meetingsquery+" "+whereclause, this);
            
            String addressquery = String.format(query,"address");
            addressquery += "streetname=\""+strtname.getText()+"\",";
            addressquery += "streetno=\""+strtno.getText()+"\",";
            addressquery += "aptno=\""+aptno.getText()+"\",";
            addressquery += "city=\""+city.getText()+"\",";
            addressquery += "state=\""+state.getText()+"\",";
            addressquery += "zipcode=\""+zipcode.getText()+"\",";
            addressquery += "address_type=\""+addresstype.getText()+"\"";
            db.executeUpdate(addressquery+" "+whereclause, this);
            
            String emailquery = String.format(query,"email");
            emailquery += "mail_type=\""+mailid.getText()+"\",";
            emailquery += "mail_id=\""+mailtype.getText()+"\"";
            db.executeUpdate(emailquery+" "+whereclause, this);
            
            
            
            
//            
//            
//            
//            
//            
//            
//            
//            while(personrs.next()){
////                String personquery = String.format(query,"person");
//                if(!firstName.getText().equalsIgnoreCase(personrs.getString("fname"))){
//                    ischanged = true;
//                    personquery+= "fname = \""+firstName.getText()+"\"";
//                }
//                if(!lastName.getText().equalsIgnoreCase(personrs.getString("lname"))){
//                    if(ischanged){
//                        personquery+=",";
//                    }
//                    personquery+= "lname = \""+lastName.getText()+'\"';
//                    ischanged = true;
//                }
//                if(!middleInitial.getText().equalsIgnoreCase(personrs.getString("minit"))){
//                    if(ischanged){
//                        personquery+=",";
//                    }
//                    personquery+= "minit = \""+middleInitial.getText()+'\"';
//                    ischanged = true;
//                }
//                String dateStr = persondob.getText();
//                if(!dateStr.equalsIgnoreCase(personrs.getString("dob"))){
//                    if(ischanged){
//                        personquery+=",";
//                    }
//                    personquery+= "dob = \""+dateStr+'\"';
//                    ischanged = true;
//                }
//                boolean isGenderChanged = true;
//                if(isGenderChanged){
//                    if(ischanged){
//                        personquery+=",";
//                    }
//                    personquery+= "gender = '"+(maleGender.isSelected()?"M":"F")+"'";
//                    ischanged = true;
//                }
//                if(ischanged){
//                    ischanged = false;
//                    db.executeUpdate(personquery+" "+whereclause, this);
//                }
//            }
            
//            String[] grps = groups.getText().split("-");
//            String groupsquery = String.format(query,"groups");
//            while(groupsrs.next()){
//                
//            }
            
            
        }
        catch(Exception e){
            
        }
        finally{
            this.setVisible(false);
            this.dispose();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        firstName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        middleInitial = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        lastName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        title = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        maleGender = new javax.swing.JRadioButton();
        femaleGender = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        groups = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        save = new javax.swing.JButton();
        Update = new javax.swing.JButton();
        close = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        areaCode = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        phno = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        phtype = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        mailid = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        mailtype = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        strtno = new javax.swing.JTextField();
        strtname = new javax.swing.JTextField();
        aptno = new javax.swing.JTextField();
        city = new javax.swing.JTextField();
        zipcode = new javax.swing.JTextField();
        state = new javax.swing.JTextField();
        addresstype = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        meetingtime = new javax.swing.JFormattedTextField();
        jLabel27 = new javax.swing.JLabel();
        meetingplace = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        notes = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        persondob = new javax.swing.JTextField();
        meetingdate = new javax.swing.JTextField();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setAlwaysOnTop(true);
        setResizable(false);

        jLabel7.setText("First Name*");

        jLabel1.setText("Middle Name");

        jLabel2.setText("Last Name");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Email");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Phone Number");

        title.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("Add New Contact Details");

        jLabel8.setText("DOB*");

        jLabel9.setText("Gender");

        maleGender.setSelected(true);
        maleGender.setText("Male");
        maleGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maleGenderActionPerformed(evt);
            }
        });

        femaleGender.setText("Female");
        femaleGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                femaleGenderActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Relation");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Meeting");

        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        Update.setText("Update");
        Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateActionPerformed(evt);
            }
        });

        close.setText("Close");
        close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeActionPerformed(evt);
            }
        });

        jLabel6.setText("Area Code:");

        jLabel13.setText("Phone No");

        jLabel14.setText("Type");

        jLabel15.setText("Email ID");

        jLabel16.setText("Type");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("Address");

        jLabel18.setText("Street Number");

        jLabel19.setText("StreetName");

        jLabel20.setText("Apt Num");

        jLabel21.setText("City");

        jLabel22.setText("Zipcode");

        jLabel23.setText("State");

        jLabel24.setText("Type");

        jLabel25.setText("Date");

        jLabel26.setText("Time");

        meetingtime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("hh:mm:ss"))));
        meetingtime.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                meetingtimeFocusLost(evt);
            }
        });

        jLabel27.setText("Place");

        jLabel28.setText("Notes");

        notes.setColumns(20);
        notes.setRows(5);
        jScrollPane2.setViewportView(notes);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("People");

        jLabel12.setText("Relation Name");

        persondob.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                persondobFocusLost(evt);
            }
        });

        meetingdate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                meetingdateFocusLost(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(save)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Update)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(close)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(groups, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel25)
                                        .addGap(18, 18, 18)
                                        .addComponent(meetingdate, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(55, 55, 55)
                                        .addComponent(jLabel26)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(meetingtime, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel11))
                                .addGap(29, 29, 29)
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(meetingplace))
                            .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(40, 40, 40)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(firstName, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(middleInitial, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(15, 15, 15))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(persondob)
                                        .addGap(217, 217, 217)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(maleGender)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(femaleGender))
                                    .addComponent(lastName, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(areaCode, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(phno, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(phtype))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(strtno, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel23)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(state, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(strtname, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(city, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel24)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(addresstype))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel17)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel15)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(mailid, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel16)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(mailtype, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel4))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabel22)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(zipcode, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(293, 293, 293))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabel20)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                            .addComponent(aptno, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(14, 14, 14))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addGap(5, 5, 5)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(firstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(middleInitial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(lastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(maleGender)
                    .addComponent(femaleGender)
                    .addComponent(persondob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(areaCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(phno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(phtype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(mailid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(mailtype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel17)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(strtno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(strtname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(aptno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(city, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(zipcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24)
                    .addComponent(state, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addresstype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(groups, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(meetingtime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(meetingplace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(meetingdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel28)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(save)
                    .addComponent(Update)
                    .addComponent(close))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        // TODO add your handling code here:
               
        if(firstName.getText()== null || firstName.getText()==""){
            JOptionPane.showMessageDialog(this, "First Name is must");
            return;
        }
        else if(phno.getText()=="" && areaCode.getText()=="" && phtype.getText()==""){
            JOptionPane.showMessageDialog(this, "Phone number and type must be entered");
            return;
        }
        insert();
    }//GEN-LAST:event_saveActionPerformed
       
    private void maleGenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maleGenderActionPerformed
        // TODO add your handling code here:
        this.maleGender.setSelected(true);
        this.femaleGender.setSelected(false);
//        isGenderChanged = true;
    }//GEN-LAST:event_maleGenderActionPerformed

    private void femaleGenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_femaleGenderActionPerformed
        // TODO add your handling code here:
        this.maleGender.setSelected(false);
        this.femaleGender.setSelected(true);
//        isGenderChanged = true;
    }//GEN-LAST:event_femaleGenderActionPerformed

    private void closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_closeActionPerformed

    private void UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_UpdateActionPerformed

    private void persondobFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_persondobFocusLost
        // TODO add your handling code here:
        String regex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
        if(!persondob.getText().matches(regex)){
            JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format");
        }
    }//GEN-LAST:event_persondobFocusLost

    private void meetingdateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_meetingdateFocusLost
        // TODO add your handling code here:
        String regex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
        if(!meetingdate.getText().matches(regex)){
            JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format");
        }
    }//GEN-LAST:event_meetingdateFocusLost

    private void meetingtimeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_meetingtimeFocusLost
        // TODO add your handling code here:
        String regex= "[0-9]{2}:[0-9]{2}:[0-9]{2}";
        if(!meetingtime.getText().matches(regex))
             JOptionPane.showMessageDialog(this, "Time must be in hh:mm:ss format");
    }//GEN-LAST:event_meetingtimeFocusLost

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddNew().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Update;
    private javax.swing.JTextField addresstype;
    private javax.swing.JTextField aptno;
    private javax.swing.JTextField areaCode;
    private javax.swing.JTextField city;
    private javax.swing.JButton close;
    private javax.swing.JRadioButton femaleGender;
    private javax.swing.JTextField firstName;
    private javax.swing.JTextField groups;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField lastName;
    private javax.swing.JTextField mailid;
    private javax.swing.JTextField mailtype;
    private javax.swing.JRadioButton maleGender;
    private javax.swing.JTextField meetingdate;
    private javax.swing.JTextField meetingplace;
    private javax.swing.JFormattedTextField meetingtime;
    private javax.swing.JTextField middleInitial;
    private javax.swing.JTextArea notes;
    private javax.swing.JTextField persondob;
    private javax.swing.JTextField phno;
    private javax.swing.JTextField phtype;
    private javax.swing.JButton save;
    private javax.swing.JTextField state;
    private javax.swing.JTextField strtname;
    private javax.swing.JTextField strtno;
    private javax.swing.JLabel title;
    private javax.swing.JTextField zipcode;
    // End of variables declaration//GEN-END:variables
}
