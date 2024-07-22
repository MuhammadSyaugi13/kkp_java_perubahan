/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Walikelas;

import Autentifikasi.login;
import Dashboard.*;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;
import Dashboard.MenuWalikelas;

/**
 *
 * @author ThinkPad X390
 */
public class PilihMapelSiswa extends javax.swing.JFrame {
    
    private Connection conn = new Koneksi().connect();
    private DefaultTableModel tabmode;
    private int id_user_login = new login().getIdUserLogin();
//    private String kelas = "10 A";
    private String kelas;
    private String[] daftarMataPelajaran = {"Matematika Lanjutan", "Biologi", "Kimia", "Fisika", "Sosiologi", "Ekonomi", "Geografi"};
    

    /**
     * Creates new form PilihMapelSiswa
     */
    public PilihMapelSiswa() {
        initComponents();
        setDefault();
    }
    
    private void setDefault(){
        getKelas();
        setTabel();
        
        // set daftar item mata pelajaran
        
        for (int i = 0; i < daftarMataPelajaran.length; i++){
            cb1.addItem(daftarMataPelajaran[i]);
            cb2.addItem(daftarMataPelajaran[i]);
            cb3.addItem(daftarMataPelajaran[i]);
            cb4.addItem(daftarMataPelajaran[i]);
        }
        
        // ./ set daftar item mata pelajaran
    }
    
    private void getKelas(){
    
        JOptionPane.showMessageDialog(null, id_user_login);
        String sqlKelas = "select * from walikelas where user_id="+id_user_login;
        
        try{
            java.sql.Statement statKelas = conn.createStatement();
            ResultSet hasil = statKelas.executeQuery(sqlKelas);
            
            if(!hasil.next()){
                JOptionPane.showMessageDialog(null, "Belum ada data kelas !");
                return;
            }else{
                kelas = hasil.getString("kelas").toString();
            }
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "gagal mengambil data kelas : "+e);
            System.out.println("gagal mengambil data kelas : "+e);
        }
    }
    
    private void setTabel() {
        Object[] Baris = {"Id", "No", "NIS", "Nama", "mapel1", "mapel2", "mapel3", "mapel4"};
        tabmode = new DefaultTableModel(null, Baris);
        tabelMapel.setModel(tabmode);
        
        //hapus kolom id agar tidak tampil tetapi bisa mengambil data id ketika barik di klik
        tabelMapel.removeColumn(tabelMapel.getColumnModel().getColumn(0));

        String sqlSiswa = "select siswa.user_id as user_id_siswa, siswa.nama, siswa.nis, mata_pelajaran_siswa.* from siswa "
                + "left join mata_pelajaran_siswa "
                + "on mata_pelajaran_siswa.user_id=siswa.user_id "
                + "where siswa.kelas='"+kelas+"' order by user_id_siswa";
        try {
            java.sql.Statement statSiswa = conn.createStatement();
            ResultSet hasilSiswa = statSiswa.executeQuery(sqlSiswa);
            Integer no = 1;
            while (hasilSiswa.next()) {
                   System.out.println(hasilSiswa.getString("user_id_siswa"));
                   System.out.println(hasilSiswa.getString("mapel1"));
                   System.out.println((hasilSiswa.getString("mapel1") == null));
                   
                   String user_id_siswa = hasilSiswa.getString("user_id_siswa");
                   String nis = hasilSiswa.getString("nis");
                   String nama = hasilSiswa.getString("nama");
                   String mapel1 = (hasilSiswa.getString("mapel1") == null) ? "-" : hasilSiswa.getString("mapel1");
                   String mapel2 = (hasilSiswa.getString("mapel2") == null) ? "-" : hasilSiswa.getString("mapel2");
                   String mapel3 = (hasilSiswa.getString("mapel3") == null) ? "-" : hasilSiswa.getString("mapel3");
                   String mapel4 = (hasilSiswa.getString("mapel4") == null) ? "-" : hasilSiswa.getString("mapel4");
                   
                   String nomor = no.toString();
                   String[] dataSiswa = {user_id_siswa, nomor, nis, nama, mapel1, mapel2, mapel3, mapel4};
                   tabmode.addRow(dataSiswa);
                   no++;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengabil data siswa join mapel siswa: " + e);
            System.out.println("Gagal mengabil data siswa join mapel siswa: " + e);
        }
    }
    
    private void simpanMataPelajaran(){
        
        int bar = tabelMapel.getSelectedRow();
        String user_id_siswa = tabmode.getValueAt(bar, 0).toString();
        String[] mapelDipilih = {   cb1.getSelectedItem().toString(), 
                                    cb2.getSelectedItem().toString(), 
                                    cb3.getSelectedItem().toString(), 
                                    cb4.getSelectedItem().toString()};
        int userIdSiswa = 0;
        
        if(user_id_siswa != ""){
            userIdSiswa = Integer.parseInt(user_id_siswa);
        }
        
        // cek apakah sudah ada data mapel
        String sqlCek = "select * from mata_pelajaran_siswa where user_id="+userIdSiswa;
        try {
            java.sql.Statement statMapel = conn.createStatement();
            ResultSet hasilMapel = statMapel.executeQuery(sqlCek);
            
            if(hasilMapel.next()){
                // jika ada data maka diupdate
                updateMataPelajaran(userIdSiswa, mapelDipilih);
                setTabel();
                resetForm();
                return;
            }
        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengabil data siswa join mapel siswa: " + e);
            System.out.println("Gagal mengabil data siswa join mapel siswa: " + e);
        }
        
        // ./ cek apakah sudah ada data mapel
        
        // insert ke tabel mata pelajaran siswa
        
        String sqlSimpan = "insert into mata_pelajaran_siswa(user_id, mapel1, mapel2, mapel3, mapel4) values(?,?,?,?,?)";
        try {
            PreparedStatement statMapel = conn.prepareStatement(sqlSimpan, 
                    PreparedStatement.RETURN_GENERATED_KEYS);
            statMapel.setString(2, mapelDipilih[0]);
            statMapel.setString(3, mapelDipilih[1]);
            statMapel.setString(4, mapelDipilih[2]);
            statMapel.setString(5, mapelDipilih[3]);
            statMapel.setInt(1, userIdSiswa);
            
            statMapel.executeUpdate();
            
            setTabel();
            resetForm();
            JOptionPane.showMessageDialog(null, "Berhasil Menyimpan Mata Pelajaran");
            
        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal Menyimpan Mata Pelajaran: " + e);
            System.out.println("Gagal Menyimpan Mata Pelajaran : " + e);
        }
        
        // ./ insert ke tabel mata pelajaran siswa
        
        
        
        
        
        
        
    }

    private void updateMataPelajaran(int user_id_siswa, String[] daftarMapel){
        
        String sqlUpdate = "update mata_pelajaran_siswa set mapel1=?, mapel2=?, mapel3=?, mapel4=? "
                + "where user_id="+user_id_siswa;
        try {
            PreparedStatement statMapel = conn.prepareStatement(sqlUpdate, 
                    PreparedStatement.RETURN_GENERATED_KEYS);
            statMapel.setString(1, daftarMapel[0]);
            statMapel.setString(2, daftarMapel[1]);
            statMapel.setString(3, daftarMapel[2]);
            statMapel.setString(4, daftarMapel[3]);
            
            statMapel.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Berhasil Edit Mata Pelajaran");
        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal Edit Mata Pelajaran: " + e);
            System.out.println("Gagal Edit Mata Pelajaran : " + e);
        }
        
    }
    
    private void hapusMataPelajaran(){
        
        int bar = tabelMapel.getSelectedRow();
        String user_id_siswa = tabmode.getValueAt(bar, 0).toString();
        int userIdSiswa = 0;
        
        if(user_id_siswa != ""){
            userIdSiswa = Integer.parseInt(user_id_siswa);
        }
        
        // cek apakah sudah ada data mapel
        String sqlHapus = "delete from mata_pelajaran_siswa where user_id="+userIdSiswa;
        try {
            java.sql.Statement statMapel = conn.createStatement();
            statMapel.executeUpdate(sqlHapus);
            
            setTabel();
            resetForm();
            JOptionPane.showMessageDialog(null, "Berhasil Menghapus Mata Pelajaran");
        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal Menghapus data mata pelajaran: " + e);
            System.out.println("Gagal menghapus data mata pelajaran: " + e);
        }
        
    }


    private void setForm(){
        
        int bar = tabelMapel.getSelectedRow();
        String nama = tabelMapel.getValueAt(bar, 2).toString();
        String mapel1 = tabelMapel.getValueAt(bar, 3).toString();
        String mapel2 = tabelMapel.getValueAt(bar, 4).toString();
        String mapel3 = tabelMapel.getValueAt(bar, 5).toString();
        String mapel4 = tabelMapel.getValueAt(bar, 6).toString();
        
        inputNama.setText(nama);
        
        if(mapel1 == "-") cb1.setSelectedItem("Pilih Satu"); else cb1.setSelectedItem(mapel1);
        if(mapel2 == "-") cb2.setSelectedItem("Pilih Satu"); else cb2.setSelectedItem(mapel2);
        if(mapel3 == "-") cb3.setSelectedItem("Pilih Satu"); else cb3.setSelectedItem(mapel3);
        if(mapel4 == "-") cb4.setSelectedItem("Pilih Satu"); else cb4.setSelectedItem(mapel4);
        
    }
    
    private void resetForm(){
        inputNama.setText("-");
        cb1.setSelectedItem("Pilih Satu");
        cb2.setSelectedItem("Pilih Satu");
        cb3.setSelectedItem("Pilih Satu");
        cb4.setSelectedItem("Pilih Satu");
        tabelMapel.clearSelection();
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
        jLabel2 = new javax.swing.JLabel();
        cb1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        cb2 = new javax.swing.JComboBox();
        cb3 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cb4 = new javax.swing.JComboBox();
        tombolSimpan = new javax.swing.JButton();
        tombolHapus = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        inputNama = new javax.swing.JTextField();
        tombolHapus1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelMapel = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        tombolSimpan1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(249, 248, 248));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel2.setText("Mata Pelajaran 1");

        cb1.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        cb1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih Satu" }));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel3.setText("Mata Pelajaran 2");

        cb2.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        cb2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih Satu" }));

        cb3.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        cb3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih Satu" }));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel4.setText("Mata Pelajaran 3");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel5.setText("Mata Pelajaran 4");

        cb4.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        cb4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih Satu" }));

        tombolSimpan.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        tombolSimpan.setText("Simpan");
        tombolSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolSimpanActionPerformed(evt);
            }
        });

        tombolHapus.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        tombolHapus.setText("Hapus");
        tombolHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolHapusActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel6.setText("Nama Siswa");

        inputNama.setEditable(false);
        inputNama.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        inputNama.setText("-");

        tombolHapus1.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        tombolHapus1.setText("Reset");
        tombolHapus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolHapus1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addComponent(cb1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(cb2, 0, 352, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addComponent(cb3, 0, 352, Short.MAX_VALUE)
                    .addComponent(jLabel5)
                    .addComponent(cb4, 0, 352, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(tombolSimpan)
                        .addGap(33, 33, 33)
                        .addComponent(tombolHapus1)
                        .addGap(36, 36, 36)
                        .addComponent(tombolHapus))
                    .addComponent(jLabel6)
                    .addComponent(inputNama))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tombolSimpan)
                    .addComponent(tombolHapus)
                    .addComponent(tombolHapus1))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(249, 248, 248));

        tabelMapel.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        tabelMapel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "NIS", "Nama", "Mapel 1", "Mapel 2", "Mapel 3", "Mapel 4"
            }
        ));
        tabelMapel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelMapelMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelMapel);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 26)); // NOI18N
        jLabel1.setText("Pilih Mata Pelajaran Siswa");

        tombolSimpan1.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        tombolSimpan1.setText("Dashboard");
        tombolSimpan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolSimpan1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tombolSimpan1)
                .addGap(80, 80, 80))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 9, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(323, 323, 323)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tombolSimpan1)
                .addGap(39, 39, 39)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tombolSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolSimpanActionPerformed
        // TODO add your handling code here:
        
        if(tabelMapel.getSelectedRow() < 0) { 
            JOptionPane.showMessageDialog(null, "Wajib memilih 1 siswa");
            return;
        };
        
        if(cb1.getSelectedItem().toString() == "Pilih Satu") { JOptionPane.showMessageDialog(null, "Belum Memilih Mata Pelajaran 1"); return; }
        if(cb2.getSelectedItem().toString() == "Pilih Satu") { JOptionPane.showMessageDialog(null, "Belum Memilih Mata Pelajaran 2"); return; }
        if(cb3.getSelectedItem().toString() == "Pilih Satu") { JOptionPane.showMessageDialog(null, "Belum Memilih Mata Pelajaran 3"); return; }
        if(cb4.getSelectedItem().toString() == "Pilih Satu") { JOptionPane.showMessageDialog(null, "Belum Memilih Mata Pelajaran 4"); return; }
        
        simpanMataPelajaran();
    }//GEN-LAST:event_tombolSimpanActionPerformed

    private void tombolHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolHapusActionPerformed
        // TODO add your handling code here:
        hapusMataPelajaran();
    }//GEN-LAST:event_tombolHapusActionPerformed

    private void tabelMapelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelMapelMouseClicked
        // TODO add your handling code here:
        setForm();
    }//GEN-LAST:event_tabelMapelMouseClicked

    private void tombolHapus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolHapus1ActionPerformed
        // TODO add your handling code here:
        resetForm();
    }//GEN-LAST:event_tombolHapus1ActionPerformed

    private void tombolSimpan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolSimpan1ActionPerformed
        // TODO add your handling code here:
        MenuWalikelas menuWaliKelas = new MenuWalikelas();
        menuWaliKelas.setVisible(true);
        this.dispose();
        return;
    }//GEN-LAST:event_tombolSimpan1ActionPerformed

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
            java.util.logging.Logger.getLogger(PilihMapelSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PilihMapelSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PilihMapelSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PilihMapelSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PilihMapelSiswa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cb1;
    private javax.swing.JComboBox cb2;
    private javax.swing.JComboBox cb3;
    private javax.swing.JComboBox cb4;
    private javax.swing.JTextField inputNama;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelMapel;
    private javax.swing.JButton tombolHapus;
    private javax.swing.JButton tombolHapus1;
    private javax.swing.JButton tombolSimpan;
    private javax.swing.JButton tombolSimpan1;
    // End of variables declaration//GEN-END:variables
}
