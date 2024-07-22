/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Administrasi;
import Guru.*;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;
import Autentifikasi.login;
import Dashboard.*;

/**
 *
 * @author mosyq
 */
public class dataSiswa extends javax.swing.JFrame {
    private Connection conn = new Koneksi().connect();
    private DefaultTableModel tabmode;
    private int id_user_login = new login().getIdUserLogin();
    private String[] daftarKelas = {"10 A", "10 B", "10 C", "10 D", "10 E"};

    /**
     * Creates new form dataSiswa
     */
    
    public dataSiswa() {
        initComponents();
        dataTable();
        setDefaultValue();
        
    }
    
     private void setDefaultValue(){
        
        // set select item mata pelajaran
        for (int i = 0; i < daftarKelas.length; i++) {
            //System.out.println("Element at index " + i + ": " + mapel[i]);
            kelas.addItem(daftarKelas[i]);
        }
    }
    
    //membuat datatable siswa    
    public void dataTable(){
        Object[] Baris={"id","No","Nis","Nama","Kelas","Alamat"};
        tabmode = new DefaultTableModel(null,Baris);
        tabelSiswa.setModel(tabmode);
        tabelSiswa.removeColumn(tabelSiswa.getColumnModel().getColumn(0));

        String sql = "select * from users join siswa on users.id=siswa.user_id";
        try{
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            Integer no = 1;
            while(hasil.next()){
                String b = hasil.getString("nis");
                String c = hasil.getString("nama");
                String d = hasil.getString("kelas");
                String e = hasil.getString("alamat");
                String nomor = no.toString();
                String id_user = hasil.getString("id").toString();
                String[] data={id_user,nomor, b,c,d,e};
                tabmode.addRow(data);
                no++;
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Gagal mengabil database: " + e);
        }
    }
    
    //simpan data siswa
    private void save(){
        //ambil data dari form
        
        String nis_input = nis.getText();
        String nama_input = nama.getText();
        String kelas_input = kelas.getSelectedItem().toString();
        String alamat_input = alamat.getText();
        int id_user;
        
        //./ ambil data dari form
        String sql_users = "insert into users(username, password, level) values (?,?,?)";
        try{
            PreparedStatement stat = conn.prepareStatement(sql_users,
          PreparedStatement.RETURN_GENERATED_KEYS);
            stat.setString(1, nis_input);
            stat.setString(2, nis_input);
            stat.setString(3, "siswa");
            
            int rowsInserted = stat.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = stat.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id_user = generatedKeys.getInt(1);
                     //simpan nilai ke tabel user
                    String sql_siswa = "insert into siswa(user_id, id_guru, nis, nama, kelas, alamat)"
                            + " values (?,?,?,?,?,?)";
                    try{
                        PreparedStatement stat_siswa = conn.prepareStatement(sql_siswa);
                        stat_siswa.setString(3, nis_input);
                        stat_siswa.setString(4, nama_input);
                        stat_siswa.setString(5, kelas_input);
                        stat_siswa.setString(6, alamat_input);
                        stat_siswa.setInt(1, id_user);
                        stat_siswa.setInt(2, id_user_login);

                        stat_siswa.executeUpdate();
                        
                    }catch (SQLException e){
                        JOptionPane.showConfirmDialog(null, "data siswa gagal disimpan"+e);
                    }
                }
            }
            
            JOptionPane.showMessageDialog(null, "Data Siswa Berhasil Disimpan");
            kosong();
            nis.requestFocus();
            dataTable();
        }catch (SQLException e){
            JOptionPane.showConfirmDialog(null, "data user gagal disimpan "+e);
        }
    }
    
    //Ubah data pegawai
    private void edit() {
        try{
            // ubah data tabel siswa
            String sql_siswa = "update siswa set nis=?, nama=?, kelas=?, alamat=?"
                    + " where user_id=?";
            PreparedStatement stat_siswa= conn.prepareStatement(sql_siswa);
            
            // id user yang akan diubah
            int bar = tabelSiswa.getSelectedRow();
            String id_user = tabmode.getValueAt(bar, 0).toString();
            int id = Integer.parseInt(id_user);
            
            stat_siswa.setString(1, nis.getText());
            stat_siswa.setString(2, nama.getText());
            stat_siswa.setString(3, kelas.getSelectedItem().toString());
            stat_siswa.setString(4, alamat.getText());
            stat_siswa.setInt(5, id);
            
            stat_siswa.executeUpdate();
            // ./ ubah data tabel siswa

            
            kosong();
            nis.requestFocus();
            dataTable();
            JOptionPane.showMessageDialog(null, "Data Berhasil diubah");
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Data Gagal diubah" +e);
            }      
    }
    
    //hapus data siswa
    private void hapus(){
        int ok = JOptionPane.showConfirmDialog(null, "Apakah anda yakin data ini akan dihapus?", "Konfirmasi Dialog", 
                JOptionPane.YES_NO_CANCEL_OPTION);
        if(ok == 0){
            try{
                // id user yang akan diubah
                int bar = tabelSiswa.getSelectedRow();
                String id_user = tabmode.getValueAt(bar, 0).toString();
                int id = Integer.parseInt(id_user);

                // hapus users
                String sql_users = "delete from users where id="+id;
                PreparedStatement stat_user= conn.prepareStatement(sql_users);

                stat_user.executeUpdate();

                // hapus siswa
                String sql_siswa = "delete from siswa where user_id="+id;
                PreparedStatement stat_siswa= conn.prepareStatement(sql_siswa);

                stat_siswa.executeUpdate();



                JOptionPane.showMessageDialog(null, "Data Berhasil dihapus");
                kosong();
                nis.requestFocus();
                dataTable();
            }catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Data Gagal dihapus" +e);
            }
        }
    }
    
    //kosongkan input data siswa    
    protected void kosong(){
        nis.setText("");
        nama.setText("");
        kelas.setSelectedIndex(0);
        alamat.setText("");
        
    }
    
    //setting data ke form
    protected void setData(){
        try{
        
        
        int bar = tabelSiswa.getSelectedRow();
        String b = tabmode.getValueAt(bar, 2).toString();
        String c = tabmode.getValueAt(bar, 3).toString();
        String d = tabmode.getValueAt(bar, 4).toString();
        String e = tabmode.getValueAt(bar, 5).toString();
        
        nis.setText(b);
        nama.setText(c);
        kelas.setSelectedItem(d);
        alamat.setText(e);
        
        }catch(Exception e){
            System.out.println(e);
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nis = new javax.swing.JTextField();
        nama = new javax.swing.JTextField();
        kelas = new javax.swing.JComboBox<String>();
        jScrollPane1 = new javax.swing.JScrollPane();
        alamat = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelSiswa = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        jLabel1.setText("Data Siswa");

        jLabel2.setText("nis");

        jLabel3.setText("Nama");

        jLabel4.setText("Kelas");

        jLabel5.setText("Alamat");

        kelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih Satu" }));

        alamat.setColumns(20);
        alamat.setRows(5);
        jScrollPane1.setViewportView(alamat);

        tabelSiswa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "id", "No", "NIS", "Nama", "Kelas", "Alamat"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelSiswa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelSiswaMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabelSiswa);

        jButton1.setText("Simpan");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
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

        jButton3.setText("Hapus");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Menu Dashboard");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nis)
                            .addComponent(nama)
                            .addComponent(kelas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(96, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(231, 231, 231)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton4))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(kelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(131, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        save();
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        edit();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tabelSiswaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelSiswaMouseClicked
        // TODO add your handling code here:
        setData();
    }//GEN-LAST:event_tabelSiswaMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        hapus();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        MenuDashboard menuDashboard = new MenuDashboard();
        menuDashboard.setVisible(true);
        this.dispose();
        return;
    }//GEN-LAST:event_jButton4ActionPerformed

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
            java.util.logging.Logger.getLogger(dataSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dataSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dataSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dataSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dataSiswa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea alamat;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> kelas;
    private javax.swing.JTextField nama;
    private javax.swing.JTextField nis;
    private javax.swing.JTable tabelSiswa;
    // End of variables declaration//GEN-END:variables
}
