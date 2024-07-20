/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Siswa;

import Autentifikasi.login;
import Dashboard.MenuSiswa;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;

/**
 *
 * @author mosyq
 */
public class PilihUniversitas extends javax.swing.JFrame {

    private Connection conn = new Koneksi().connect();
    private DefaultTableModel tabmode;
    private int id_user_login = new login().getIdUserLogin();
    private boolean sudah_memilih = false;
    /**
     * Creates new form PilihUniversitas
     */
    public PilihUniversitas() {
        initComponents();
        setComboBox("select * from universitas", "setIndex");
        cekPilihanSiswa();
    }
    
    //cek pilihan siswa
    private void cekPilihanSiswa(){
        String sql_pilihan = "select * from pilihan_universitas where id_siswa="+id_user_login;
//        String sql_pilihan = "select * from pilihan_universitas where id="+1;
        
        try{
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql_pilihan);
            String opsi ="";
            if(hasil.next()){
//                String sql = "select * from universitas where id_siswa="+id_user_login;
                opsi = "setIndex";
                sudah_memilih = true;
                JOptionPane.showMessageDialog(null, sudah_memilih);
                String sql = "select * from universitas";
//                setComboBox(sql, opsi);
            }
            
        }catch(Exception e){
            System.out.println("Gagal mengambil data pilihan universitas "+e);
        }
    }
    //./ cek pilihan siswa
    
    //set data form
    private void setComboBox(String sql, String opsi){
        if(opsi.equals("setIndex")){
            // query universitas
            try{
                java.sql.Statement stat_universitas = conn.createStatement();
                ResultSet hasil_universitas = stat_universitas.executeQuery(sql);
                
                while(hasil_universitas.next()){
                      universitas1.addItem(hasil_universitas.getString("nama"));
                      universitas2.addItem(hasil_universitas.getString("nama"));
                }
            }catch(Exception e){
                System.out.println("Gagal mengambil data universitas 1 "+e);
            }
            // ./ query universitas
            
            //set items
            String sql_pilihan_univ = "select * from pilihan_universitas join universitas "
                    + "on pilihan_universitas.id_universitas=universitas.id "
                    + "join jurusan "
                    + "on pilihan_universitas.id_jurusan=jurusan.id "
                    + "where id_siswa="+ id_user_login;
//                    + "where id_siswa="+ 1;
            try{
                java.sql.Statement stat_pilihan = conn.createStatement();
                ResultSet hasil_universitas = stat_pilihan.executeQuery(sql_pilihan_univ);
                
                String[] allUniversitas = new String[2];
                String[] allJurusan = new String[2];
                
                int no = 0;
                while(hasil_universitas.next()){
                      allUniversitas[no] = hasil_universitas.getString("universitas.nama");
                      allJurusan[no] = hasil_universitas.getString("jurusan.jurusan");
                      no++;
                }
                
                if(hasil_universitas.next()){
                    universitas1.setSelectedItem(allUniversitas[0]);
                    universitas2.setSelectedItem(allUniversitas[1]);
                    jurusan1.setSelectedItem(allJurusan[0]);
                    jurusan2.setSelectedItem(allJurusan[1]);
                }
                
            }catch(Exception e){
                System.out.println("Gagal mengambil data jurusan set combobox "+e);
            }
            //./ set items
        }
        
        if(opsi.equals("jurusan")){
            // query jurusan
            try{
                java.sql.Statement stat_jurusan = conn.createStatement();
                ResultSet hasil_jurusan = stat_jurusan.executeQuery(sql);

                jurusan1.removeAllItems();
                jurusan2.removeAllItems();
                while(hasil_jurusan.next()){
                    jurusan1.addItem(hasil_jurusan.getString("jurusan"));
                    jurusan2.addItem(hasil_jurusan.getString("jurusan"));

                }
            }catch(Exception e){
                System.out.println("Gagal mengambil data jurusan "+e);
            }
            // ./ query jurusan
        }
    
    }
    
    //ambil data jurusan
    
    private void queryJurusan(String opsi){
    
        if(opsi.equals("universitas1")){
            String pilihan = universitas1.getSelectedItem().toString();
            String sql_univ = "select * from universitas where nama like '%"+pilihan+"%'";
            
            int id_universitas = 0;
            try{
                java.sql.Statement stat = conn.createStatement();
                ResultSet hasil = stat.executeQuery(sql_univ);
                while(hasil.next()){
                    id_universitas = hasil.getInt("id");

                }
            }catch(Exception e){
                JOptionPane.showConfirmDialog(null, "data gagal mengambil data jurusan "+e);
            }
            
            String sql_jurusan = "select * from maping_universitas_jurusan join jurusan on "
                    + "maping_universitas_jurusan.jurusan_id=jurusan.id "
                    + "where universitas_id="+id_universitas;
            
            try{
                java.sql.Statement stat_jurusan = conn.createStatement();
                ResultSet hasil_jurusan = stat_jurusan.executeQuery(sql_jurusan);
                while(hasil_jurusan.next()){
                    jurusan1.addItem(hasil_jurusan.getString("jurusan"));
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "data gagal mengambil data jurusan "+e);
            }
        }
        
        if(opsi.equals("universitas2")){
            String pilihan = universitas2.getSelectedItem().toString();
            String sql_univ = "select * from universitas where nama like '%"+pilihan+"%'";
            
            int id_universitas = 0;
            try{
                java.sql.Statement stat = conn.createStatement();
                ResultSet hasil = stat.executeQuery(sql_univ);
                while(hasil.next()){
                    id_universitas = hasil.getInt("id");

                }
            }catch(Exception e){
                JOptionPane.showConfirmDialog(null, "data gagal mengambil data jurusan "+e);
            }
            
            String sql_jurusan = "select * from maping_universitas_jurusan join jurusan on "
                    + "maping_universitas_jurusan.jurusan_id=jurusan.id "
                    + "where universitas_id="+id_universitas;
            
            try{
                java.sql.Statement stat_jurusan = conn.createStatement();
                ResultSet hasil_jurusan = stat_jurusan.executeQuery(sql_jurusan);
                while(hasil_jurusan.next()){
                    jurusan2.addItem(hasil_jurusan.getString("jurusan"));
                }
            }catch(Exception e){
                JOptionPane.showConfirmDialog(null, "data gagal mengambil data jurusan "+e);
                System.out.println(e);
            }
        }
    
    }
    
    // ./ ambil data jurusan
    
    //simpan    
    
    private void simpan(){
    
        String univPilihan1 = universitas1.getSelectedItem().toString();
        String univPilihan2 = universitas2.getSelectedItem().toString();
        String jurusanPilihan1 = jurusan1.getSelectedItem().toString();
        String jurusanPilihan2 = jurusan2.getSelectedItem().toString();
        
        int idUnivPilihan1 = 0;
        int idUnivPilihan2 = 0;
        int idJurusanPilihan1 = 0;
        int idJurusanPilihan2 = 0;
        
        //get universitas
        
        String sql_univ = "select * from universitas where nama like '%"+univPilihan1+"%' "
                + "or nama like '%"+univPilihan2+"%'";

        try{
            java.sql.Statement stat_univ = conn.createStatement();
            ResultSet hasil_univ = stat_univ.executeQuery(sql_univ);
            
            int[] univs = new int[2];
            int noUniv = 0;
            while(hasil_univ.next()){
                univs[noUniv] = hasil_univ.getInt("id");
                noUniv++;
            }
            idUnivPilihan1=univs[0];
            idUnivPilihan2=univs[1];
        }catch(Exception e){
            JOptionPane.showConfirmDialog(null, "data gagal mengambil data universitas "+e);
        }
        
        //get ./ universitas
        
        //get jurusan
        
        String sql_jurusan = "select * from jurusan where jurusan.jurusan like '%"+jurusanPilihan1+"%' "
                + "or jurusan.jurusan like '%"+jurusanPilihan2+"%'";

        try{
            java.sql.Statement stat_jurusan = conn.createStatement();
            ResultSet hasil_jurusan = stat_jurusan.executeQuery(sql_jurusan);
            
            int[] allJurusan = new int[2];
            int noJurusan = 0;
            while(hasil_jurusan.next()){
                allJurusan[noJurusan] = hasil_jurusan.getInt("id");
                noJurusan++;
            }
            idJurusanPilihan1=allJurusan[0];
            idJurusanPilihan2=allJurusan[1];
        }catch(Exception e){
            JOptionPane.showConfirmDialog(null, "data gagal mengambil data jurusan "+e);
        }
        
        //./ get jurusan
        
        
        //simpan ke database
        if(!sudah_memilih){
        
            //univ 1
            String sql_insert = "insert into pilihan_universitas(id_siswa, id_universitas, id_jurusan) "
                    + "values(?,?,?)";

            try{
                PreparedStatement stat_insert = conn.prepareStatement(sql_insert);
                stat_insert.setInt(1, id_user_login);
                stat_insert.setInt(2, idUnivPilihan1);
                stat_insert.setInt(3, idJurusanPilihan1);
                System.out.println(idUnivPilihan1);
                System.out.println("sudah");

                stat_insert.executeUpdate();

            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Data univ 1 jurusan 1 gagal Disimpan");
            }

            //univ2
            try{
                PreparedStatement stat_insert = conn.prepareStatement(sql_insert);
                stat_insert.setInt(1, id_user_login);
                stat_insert.setInt(2, idUnivPilihan2);
                stat_insert.setInt(3, idJurusanPilihan2);
                System.out.println(idUnivPilihan2);

                stat_insert.executeUpdate();
    //            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Data univ 2 jurusan 2 gagal Disimpan");
            }


            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
            
        }else{
            
            //univ 1
            String sql_insert_1 = "update pilihan_universitas set id_universitas=?, id_jurusan=?"
                    + " where id_siswa="+id_user_login;

            try{
                PreparedStatement stat_insert = conn.prepareStatement(sql_insert_1);
                stat_insert.setInt(1, idUnivPilihan1);
                stat_insert.setInt(2, idJurusanPilihan1);
                System.out.println(idUnivPilihan1);
                System.out.println("belom");

                stat_insert.executeUpdate();

            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Data univ 1 jurusan 1 gagal Disimpan");
            }

            //univ2
            String sql_insert_2 = "update pilihan_universitas set id_universitas=?, id_jurusan=?"
                    + " where id_siswa="+id_user_login;
            try{
                PreparedStatement stat_insert = conn.prepareStatement(sql_insert_2);
                stat_insert.setInt(1, idUnivPilihan2);
                stat_insert.setInt(2, idJurusanPilihan2);
                System.out.println(idUnivPilihan2);
                System.out.println(idJurusanPilihan2);
                

                stat_insert.executeUpdate();
    //            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Data univ 2 jurusan 2 gagal Disimpan");
            }


            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
            
        }
        
        
        //./ simpan ke database
        
        
    
    }
    
    //./ simpan    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        universitas1 = new javax.swing.JComboBox<>();
        jurusan1 = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        universitas2 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jurusan2 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLabel5.setText("Universitas");

        jLabel6.setText("Jurusan 1");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(48, 48, 48)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Menu Siswa");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        jLabel1.setText("Pilihan Universitas");

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));

        jLabel2.setText("Universitas");

        jLabel3.setText("Jurusan");

        universitas1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Universitas" }));
        universitas1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                universitas1ActionPerformed(evt);
            }
        });

        jurusan1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Jurusan" }));
        jurusan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jurusan1ActionPerformed(evt);
            }
        });

        jLabel9.setText("Pilihan Universitas 1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(48, 48, 48)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(universitas1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jurusan1, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(universitas1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jurusan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));

        jLabel4.setText("Universitas");

        jLabel11.setText("Jurusan");

        universitas2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Universitas" }));
        universitas2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                universitas2ActionPerformed(evt);
            }
        });

        jLabel12.setText("Pilihan Universitas 2");

        jurusan2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Jurusan" }));
        jurusan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jurusan2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel11))
                .addGap(48, 48, 48)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(universitas2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jurusan2, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(universitas2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jurusan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        jButton2.setText("Simpan");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        MenuSiswa menuSiswa = new MenuSiswa();
        menuSiswa.setVisible(true);
        this.dispose();
        return;
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jurusan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jurusan1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jurusan1ActionPerformed

    private void jurusan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jurusan2ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jurusan2ActionPerformed

    private void universitas1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_universitas1ActionPerformed
        jurusan1.removeAllItems();
        queryJurusan("universitas1");
    }//GEN-LAST:event_universitas1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        simpan();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void universitas2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_universitas2ActionPerformed
        // TODO add your handling code here:
        queryJurusan("universitas2");
    }//GEN-LAST:event_universitas2ActionPerformed

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
            java.util.logging.Logger.getLogger(PilihUniversitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PilihUniversitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PilihUniversitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PilihUniversitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PilihUniversitas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JComboBox<String> jurusan1;
    private javax.swing.JComboBox<String> jurusan2;
    private javax.swing.JComboBox<String> universitas1;
    private javax.swing.JComboBox<String> universitas2;
    // End of variables declaration//GEN-END:variables
}
