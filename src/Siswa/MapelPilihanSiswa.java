/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Siswa;

import Autentifikasi.login;
import Dashboard.*;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;

/**
 *
 * @author mosyq
 */
public class MapelPilihanSiswa extends javax.swing.JFrame {

    private Connection conn = new Koneksi().connect();
    private DefaultTableModel tabmode;
    private int id_user_login = new login().getIdUserLogin();
    private String[] mata_pelajaran = {"Matematika Lanjutan", "Kimia", "Fisika", "Biologi", 
        "Sosiologi", "Geografi", "Ekonomi", "Sejarah"};
    private boolean isSetMapelPilihanSiswa = false;
    /**
     * Creates new form PilihMaPel
     */
    public MapelPilihanSiswa() {
        initComponents();
        setDefault();
        dataTable();
    }
    
    private void setDefault() {
    
        for(int i=0; i < mata_pelajaran.length; i++ ){
            cbpilihan1.addItem(mata_pelajaran[i]);
            cbpilihan2.addItem(mata_pelajaran[i]);
            cbpilihan3.addItem(mata_pelajaran[i]);
            cbpilihan4.addItem(mata_pelajaran[i]);
        }
    }
    
    public void dataTable(){
        Object[] Baris={"No","Pilihan 1","Pilihan 2","Pilihan 3","Pilihan 4"};
        tabmode = new DefaultTableModel(null,Baris);
        tabelMapel.setModel(tabmode);

        String sql = "select * from mapel_pilihan_siswa where id_user_siswa="+id_user_login;
        try{
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            Integer no = 1;
            int id_mapel1 =0; 
            int id_mapel2 =0;
            int id_mapel3 =0;
            int id_mapel4 =0;
            while(hasil.next()){
                
                isSetMapelPilihanSiswa = true;
                
                String id = hasil.getString("id");
                String pilihan1 = hasil.getString("pilihan1");
                String pilihan2 = hasil.getString("pilihan2");
                String pilihan3 = hasil.getString("pilihan3");
                String pilihan4 = hasil.getString("pilihan4");
                String nomor = no.toString();
                String[] data={nomor, pilihan1, pilihan2, pilihan3, pilihan4};
                tabmode.addRow(data);
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Gagal mengabil database pilihan mapel siswa untuk tabel : " + e);
            System.out.println("Gagal mengabil database pilihan mapel siswa untuk tabel : " +e);
        }
    }
    
    //set data
    private void setDataForm(){
        try{
            int bar = tabelMapel.getSelectedRow();
            String a = tabmode.getValueAt(bar, 1).toString();
            String b = tabmode.getValueAt(bar, 2).toString();
            String c = tabmode.getValueAt(bar, 3).toString();
            String d = tabmode.getValueAt(bar, 4).toString();

            cbpilihan1.setSelectedItem(a);
            cbpilihan2.setSelectedItem(b);
            cbpilihan3.setSelectedItem(c);
            cbpilihan4.setSelectedItem(d);
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    
    //set nilai pada combobox
    private void setComboBox(){
        String sql = "select * from mata_pelajaran";
        
        try{
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            
            while(hasil.next()){
                cbpilihan1.addItem(hasil.getString("nama"));
                cbpilihan2.addItem(hasil.getString("nama"));
                cbpilihan3.addItem(hasil.getString("nama"));
                cbpilihan4.addItem(hasil.getString("nama"));
                
            }
        }catch(Exception e){
            System.out.println("Gagal mengambil data mata pelajaran "+e);
        }
    }
    
    //get id mata pelajaran
    private int getIdMataPelajaran(String nama){
        String sql_id = "select * from mata_pelajaran where nama like '%"+nama+"%'";
        try{
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql_id);
            while(hasil.next()){
                return hasil.getInt("id");
                
            }
        }catch(Exception e){
            JOptionPane.showConfirmDialog(null, "data gagal id mata pelajaran disimpan"+e);
        }
        return 0;
    }
    
    //simpan hasil pilihan
    private void simpan(){
        
        if(isSetMapelPilihanSiswa){
            ubah();
            return;
        }
        
        String sql_simpan = "insert into "
                + "mapel_pilihan_siswa(id_user_siswa, pilihan1,pilihan2, pilihan3, pilihan4) "
                + "values(?,?,?,?,?)";
        
        try{
//            int id_mapel = getIdMataPelajaran(pilihan1.getSelectedItem().toString());
            PreparedStatement stat = conn.prepareStatement(sql_simpan);
            stat.setInt(1, id_user_login);
            stat.setString(2, cbpilihan1.getSelectedItem().toString());
            stat.setString(3, cbpilihan2.getSelectedItem().toString());
            stat.setString(4, cbpilihan3.getSelectedItem().toString());
            stat.setString(5, cbpilihan4.getSelectedItem().toString());
            
            stat.executeUpdate();
            dataTable();
        }catch(Exception e){
            JOptionPane.showConfirmDialog(null, "data gagal pilihan mata pelajaran disimpan"+e);
        }
    }
    
    //ubah data
    //simpan hasil pilihan
    private void ubah(){
        String sql_ubah = "update mapel_pilihan_siswa set "
                + "pilihan1=?,pilihan2=?, pilihan3=?, pilihan4=? "
                + "where id_user_siswa="+id_user_login;
        
        try{
//            int id_mapel = getIdMataPelajaran(pilihan1.getSelectedItem().toString());
            PreparedStatement stat = conn.prepareStatement(sql_ubah);
            stat.setString(1, cbpilihan1.getSelectedItem().toString());
            stat.setString(2, cbpilihan2.getSelectedItem().toString());
            stat.setString(3, cbpilihan3.getSelectedItem().toString());
            stat.setString(4, cbpilihan4.getSelectedItem().toString());
            
            stat.executeUpdate();
            dataTable();
            JOptionPane.showMessageDialog(null, "Berhasil ubah data");
        }catch(Exception e){
            JOptionPane.showConfirmDialog(null, "data gagal pilihan mata pelajaran disimpan"+e);
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cbpilihan1 = new javax.swing.JComboBox<String>();
        cbpilihan2 = new javax.swing.JComboBox<String>();
        cbpilihan3 = new javax.swing.JComboBox<String>();
        cbpilihan4 = new javax.swing.JComboBox<String>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelMapel = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 1, 25)); // NOI18N
        jLabel1.setText("Pilih Mata Pelajaran");

        jLabel2.setText("Mata Pelajaran 1");

        jLabel3.setText("Mata Pelajaran 2");

        jLabel4.setText("Mata Pelajaran 3");

        jLabel5.setText("Mata Pelajaran 4");

        cbpilihan1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih Mata Pelajaran" }));
        cbpilihan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbpilihan1ActionPerformed(evt);
            }
        });

        cbpilihan2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih Mata Pelajaran" }));

        cbpilihan3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih Mata Pelajaran" }));

        cbpilihan4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih Mata Pelajaran" }));

        jButton1.setText("Simpan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Ubah");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        tabelMapel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No", "Pilihan 1", "Pilihan 2", "Pilihan 3", "Pilihan 4"
            }
        ));
        tabelMapel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelMapelMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelMapel);

        jButton3.setText("Menu Siswa");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addContainerGap(306, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(27, 27, 27))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cbpilihan1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbpilihan2, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbpilihan3, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbpilihan4, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(50, 50, 50))
            .addGroup(layout.createSequentialGroup()
                .addGap(142, 142, 142)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbpilihan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbpilihan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbpilihan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbpilihan4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        simpan();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tabelMapelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelMapelMouseClicked
        // TODO add your handling code here:
        setDataForm();
    }//GEN-LAST:event_tabelMapelMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        ubah();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        MenuSiswa menuSiswa = new MenuSiswa();
        menuSiswa.setVisible(true);
        this.dispose();
        return;
    }//GEN-LAST:event_jButton3ActionPerformed

    private void cbpilihan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbpilihan1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbpilihan1ActionPerformed

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
            java.util.logging.Logger.getLogger(MapelPilihanSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MapelPilihanSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MapelPilihanSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MapelPilihanSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MapelPilihanSiswa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbpilihan1;
    private javax.swing.JComboBox<String> cbpilihan2;
    private javax.swing.JComboBox<String> cbpilihan3;
    private javax.swing.JComboBox<String> cbpilihan4;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelMapel;
    // End of variables declaration//GEN-END:variables
}
