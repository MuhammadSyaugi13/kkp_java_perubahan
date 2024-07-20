/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Administrasi;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;
import Dashboard.MenuDashboard;

/**
 *
 * @author mosyq
 */
public class dataAdmin extends javax.swing.JFrame {

    private Connection conn = new Koneksi().connect();
    private DefaultTableModel tabmode;

    /**
     * Creates new form dataSiswa
     */
    public dataAdmin() {
        initComponents();
        dataTable();
    }

    //membuat datatable Pegawai
    public void dataTable() {
        Object[] Baris = {"id", "No", "Username", "Nama", "Jabatan", "Alamat"};
        tabmode = new DefaultTableModel(null, Baris);
        tabelPegawai.setModel(tabmode);
        tabelPegawai.removeColumn(tabelPegawai.getColumnModel().getColumn(0));

        String sql = "select * from users join pegawai on users.id=pegawai.user_id";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            Integer no = 1;
            while (hasil.next()) {
                String b = hasil.getString("users.username");
                String c = hasil.getString("pegawai.nama");
                String e = hasil.getString("pegawai.jabatan");
                String f = hasil.getString("pegawai.alamat");
                String nomor = no.toString();
                Integer id_user = hasil.getInt("users.id");
                String[] data = {id_user.toString(), nomor, b, c, e, f};
                tabmode.addRow(data);
                no++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mengabil database: " + e);
        }
    }

    //simpan data pegawai
    private void save() {
        String sql = "insert into users(username, password,jabatan, level) values (?,?,?,?)";
        try {
            PreparedStatement stat = conn.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stat.setString(1, username_pegawai.getText());
            stat.setString(2, password_pegawai.getText());
            stat.setString(3, "Tata Usaha");
            stat.setString(4, "admin");


            int rowsInserted = stat.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
            kosong();
            username_pegawai.requestFocus();
            dataTable();
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, "data user gagal disimpan " + e);
            System.out.println("data pegawai gagal disimpan" + e);
        }
    }
    
    //Ubah data pegawai
    private void edit() {
        try{
            // ubah data tabel users
            String sql = "Update users set username=?, password=?, level=admin"
                    + " where id=?";
            if (password_pegawai.getText().equals("")){
                sql = "Update users set username=?, level=?"
                    + " where id=?";
            }
            PreparedStatement stat_user= conn.prepareStatement(sql);
            
            // id user yang akan diubah
            int bar = tabelPegawai.getSelectedRow();
            String id_user = tabmode.getValueAt(bar, 0).toString();
            int id = Integer.parseInt(id_user);
            
            if (password_pegawai.getText().equals("")){
                stat_user.setString(1, username_pegawai.getText());
                stat_user.setInt(3, id);
            }else{
                stat_user.setString(1, username_pegawai.getText());
                stat_user.setString(2, password_pegawai.getText());
                stat_user.setInt(4, id);
            }
            
            stat_user.executeUpdate();
            // ./ ubah data tabel users
            
            // ubah data tabel pegawai
            String sql_pegawai = "update pegawai set nama=?, jabatan=?, alamat=?"
                    + " where user_id=?";
            PreparedStatement stat_pegawai= conn.prepareStatement(sql_pegawai);
            
            stat_pegawai.setString(1, nama_pegawai.getText());
            stat_pegawai.setString(2, "Tata Usaha");
            stat_pegawai.setString(3, alamat_pegawai.getText());
            stat_pegawai.setInt(4, id);
            
            stat_pegawai.executeUpdate();
            // ./ ubah data tabel pegawai
            
            JOptionPane.showMessageDialog(null, "Data Berhasil diubah");
            kosong();
            username_pegawai.requestFocus();
            dataTable();
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Data Gagal diubah" +e);
            }      
    }
    
    //data hapus
    private void hapus(){
        try{
            // id user yang akan diubah
            int bar = tabelPegawai.getSelectedRow();
            String id_user = tabmode.getValueAt(bar, 0).toString();
            int id = Integer.parseInt(id_user);
            
            // hapus users
            String sql_users = "delete from users where id="+id;
            PreparedStatement stat_user= conn.prepareStatement(sql_users);
            
            stat_user.executeUpdate();
            
            // hapus pegawai
            String sql_pegawai = "delete from pegawai where user_id="+id;
            PreparedStatement stat_pegawai= conn.prepareStatement(sql_pegawai);
            
            stat_pegawai.executeUpdate();
            
            
            
            JOptionPane.showMessageDialog(null, "Data Berhasil dihapus");
            kosong();
            username_pegawai.requestFocus();
            dataTable();
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Data Gagal dihapus" +e);
        }
    }
    
    //ambil data pada tabel
    private void data(){
        //ambil data dari tabel
        int bar = tabelPegawai.getSelectedRow();
        String a = tabmode.getValueAt(bar, 0).toString(); //id
        String b = tabmode.getValueAt(bar, 1).toString(); // no
        String c = tabmode.getValueAt(bar, 2).toString(); // username
        String d = tabmode.getValueAt(bar, 3).toString(); // nama
        String g = tabmode.getValueAt(bar, 4).toString(); // alamat
        
        //set data ke form
        username_pegawai.setText(c);
        nama_pegawai.setText(d);
        password_pegawai.setText("");
        alamat_pegawai.setText(g);
        
        int index_level = 0;
    }

    //kosongkan input data siswa    
    protected void kosong() {
        username_pegawai.setText("");
        nama_pegawai.setText("");
        alamat_pegawai.setText("");
        password_pegawai.setText("");
        

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
        username_pegawai = new javax.swing.JTextField();
        nama_pegawai = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        alamat_pegawai = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelPegawai = new javax.swing.JTable();
        tombol_edit = new javax.swing.JButton();
        password_pegawai = new javax.swing.JPasswordField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

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
        setBackground(new java.awt.Color(255, 102, 102));

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        jLabel1.setText("Data Admin");

        jLabel2.setText("Username");

        jLabel3.setText("Nama");

        jLabel4.setText("Password");

        jLabel5.setText("Alamat");

        alamat_pegawai.setColumns(20);
        alamat_pegawai.setRows(5);
        jScrollPane1.setViewportView(alamat_pegawai);

        tabelPegawai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "id", "No", "Username", "Nama", "Alamat"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tabelPegawai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelPegawaiMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabelPegawai);

        tombol_edit.setText("Edit");
        tombol_edit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tombol_editMouseClicked(evt);
            }
        });
        tombol_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombol_editActionPerformed(evt);
            }
        });

        jButton2.setText("Simpan");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Hapus");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton1.setText("Menu Dashboard");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(197, 197, 197)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(43, 43, 43)
                                .addComponent(username_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3))
                                .addGap(46, 46, 46)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(password_pegawai)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                                    .addComponent(nama_pegawai)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tombol_edit)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton3))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(323, 323, 323)
                                        .addComponent(jButton1))
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(0, 29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(34, 34, 34)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(nama_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(username_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(password_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(tombol_edit)
                    .addComponent(jButton3))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(112, 112, 112))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tombol_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombol_editActionPerformed
        // TODO add your handling code here:
        edit();
    }//GEN-LAST:event_tombol_editActionPerformed

    private void tombol_editMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tombol_editMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tombol_editMouseClicked

    private void tabelPegawaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelPegawaiMouseClicked
        // TODO add your handling code here:
        data();
        
    }//GEN-LAST:event_tabelPegawaiMouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        save();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        hapus();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        MenuDashboard menuDashboard = new MenuDashboard();
        menuDashboard.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(dataAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dataAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dataAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dataAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dataAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea alamat_pegawai;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField nama_pegawai;
    private javax.swing.JPasswordField password_pegawai;
    private javax.swing.JTable tabelPegawai;
    private javax.swing.JButton tombol_edit;
    private javax.swing.JTextField username_pegawai;
    // End of variables declaration//GEN-END:variables
}
