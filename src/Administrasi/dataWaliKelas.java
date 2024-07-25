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
public class dataWaliKelas extends javax.swing.JFrame {

    private Connection conn = new Koneksi().connect();
    private DefaultTableModel tabmode;
    private DefaultTableModel tabelModelKelas;
    private String[] kelas = {"10 A", "10 B", "10 C", "10 D", "10 E"};


    /**
     * Creates new form dataSiswa
     */
    public dataWaliKelas() {
        initComponents();
        dataTable();
        setDefaultValue();
//        JOptionPane.showMessageDialog(null, mata_pelajaran.getSelectedItem().toString());
    }

    //membuat datatable Pegawai
    public void dataTable() {
        Object[] Baris = {"id", "No", "Username", "Nama", "Kelas"};
        tabmode = new DefaultTableModel(null, Baris);
        tabelWalikelas.setModel(tabmode);
        tabelWalikelas.removeColumn(tabelWalikelas.getColumnModel().getColumn(0));

        String sql = "select * from users join walikelas on users.id=walikelas.user_id";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            Integer no = 1;
            while (hasil.next()) {
                String b = hasil.getString("users.username");
                String c = hasil.getString("walikelas.nama");
                String d = hasil.getString("walikelas.kelas");
                String nomor = no.toString();
                Integer id_user = hasil.getInt("users.id");
                String[] data = {id_user.toString(), nomor, b, c, d};
                tabmode.addRow(data);
                no++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mengabil database: " + e);
            System.out.println(e);
        }
    }

    //set nilai default
    private void setDefaultValue(){
        
        // set select item mata pelajaran
        for (int i = 0; i < kelas.length; i++) {
            //System.out.println("Element at index " + i + ": " + mapel[i]);
            cb_kelas.addItem(kelas[i]);
        }
    }
    
    //simpan data pegawai
    private void save() {
        
        String sql = "insert into users(username, password, level) values (?,?,?)";
        try {
            PreparedStatement stat = conn.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stat.setString(1, username_walikelas.getText());
            stat.setString(2, password_walikelas.getText());
            stat.setString(3, "walikelas");

            int rowsInserted = stat.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = stat.getGeneratedKeys();
                if (generatedKeys.next()) {
                    String sql_guru_mapel = "insert into walikelas(user_id, nama, kelas) "
                            + "values (?,?,?)";
                    int id_user = generatedKeys.getInt(1);
                    
                    // mata pelajaran
                    String mataPelajaran = cb_kelas.getSelectedItem().toString();
                    
                    try {
                        PreparedStatement stat_guru_mapel = conn.prepareStatement(sql_guru_mapel);
                        stat_guru_mapel.setString(2, nama_walikelas.getText());
                        stat_guru_mapel.setString(3, mataPelajaran);
                        stat_guru_mapel.setInt(1, id_user);
                        
                        stat_guru_mapel.executeUpdate();
                    } catch (SQLException e) {
                        JOptionPane.showConfirmDialog(null, "data walikelas gagal disimpan " + e);
                        System.out.println("data pegawai gagal disimpan" + e);
                    }

                    JOptionPane.showConfirmDialog(null,
                            "Data berhasil dimasukkan ke database dengan ID: " + id_user);
                }
            }

            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
            kosong();
            username_walikelas.requestFocus();
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
            String sql = "Update users set username=?, password=?, level=?"
                    + " where id=?";
            if (password_walikelas.getText().equals("")){
                sql = "Update users set username=?, level=?"
                    + " where id=?";
            }
            PreparedStatement stat_user= conn.prepareStatement(sql);
            
            // id user yang akan diubah
            int bar = tabelWalikelas.getSelectedRow();
            String id_user = tabmode.getValueAt(bar, 0).toString();
            int id = Integer.parseInt(id_user);
            
            if (password_walikelas.getText().equals("")){
                stat_user.setString(1, username_walikelas.getText());
                stat_user.setString(2, cb_kelas.getSelectedItem().toString());
                stat_user.setInt(3, id);
            }else{
                stat_user.setString(1, username_walikelas.getText());
                stat_user.setString(2, password_walikelas.getText());
                stat_user.setString(3, cb_kelas.getSelectedItem().toString());
                stat_user.setInt(4, id);
            }
            
            stat_user.executeUpdate();
            // ./ ubah data tabel users
            
            // ubah data tabel pegawai
            String sql_pegawai = "update pegawai set nama=?, jabatan=?, alamat=?"
                    + " where user_id=?";
            PreparedStatement stat_pegawai= conn.prepareStatement(sql_pegawai);
            
            stat_pegawai.setString(1, nama_walikelas.getText());
            stat_pegawai.setString(2, "Guru Pelajaran");
            stat_pegawai.setInt(4, id);
            
            stat_pegawai.executeUpdate();
            // ./ ubah data tabel pegawai
            
            JOptionPane.showMessageDialog(null, "Data Berhasil diubah");
            kosong();
            username_walikelas.requestFocus();
            dataTable();
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Data Gagal diubah" +e);
            }      
    }
    
    //data hapus
    private void hapus(){
        try{
            // id user yang akan diubah
            int bar = tabelWalikelas.getSelectedRow();
            String id_user = tabmode.getValueAt(bar, 0).toString();
            int id = Integer.parseInt(id_user);
            
            // hapus users
            String sql_users = "delete from users where id="+id;
            PreparedStatement stat_user= conn.prepareStatement(sql_users);
            
            stat_user.executeUpdate();
            
            // hapus pegawai
            String sql_walikelas = "delete from walikelas where user_id="+id;
            PreparedStatement stat_walikelas= conn.prepareStatement(sql_walikelas);
            
            stat_walikelas.executeUpdate();
            
            
            
            JOptionPane.showMessageDialog(null, "Data Berhasil dihapus");
            kosong();
            username_walikelas.requestFocus();
            dataTable();
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Data Gagal dihapus" +e);
        }
    }
    
    //ambil data pada tabel
    private void data(){
        //ambil data dari tabel
        int bar = tabelWalikelas.getSelectedRow();
        String a = tabmode.getValueAt(bar, 0).toString(); //id
        String b = tabmode.getValueAt(bar, 1).toString(); // no
        String c = tabmode.getValueAt(bar, 2).toString(); // username
        String d = tabmode.getValueAt(bar, 3).toString(); // nama
        String e = tabmode.getValueAt(bar, 4).toString(); // level
        String f = tabmode.getValueAt(bar, 5).toString(); // jabatan
        String g = tabmode.getValueAt(bar, 6).toString(); // alamat
        
        //set data ke form
        username_walikelas.setText(c);
        nama_walikelas.setText(d);
        password_walikelas.setText("");
        
        int index_level = 0;
        if(e.equals("guru")){
            index_level = 1;
        }
        
        cb_kelas.setSelectedIndex(index_level);
    }

    //kosongkan input data siswa    
    protected void kosong() {
        username_walikelas.setText("");
        nama_walikelas.setText("");
        cb_kelas.setSelectedIndex(0);
        password_walikelas.setText("");
        

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
        username_walikelas = new javax.swing.JTextField();
        nama_walikelas = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelWalikelas = new javax.swing.JTable();
        password_walikelas = new javax.swing.JPasswordField();
        cb_kelas = new javax.swing.JComboBox<String>();
        jLabel7 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

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
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Agency FB", 0, 24)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        jLabel1.setText("Data Wali Kelas");

        jLabel2.setText("Username");

        jLabel3.setText("Nama");

        jLabel4.setText("Password");

        tabelWalikelas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "id", "No", "Username", "Nama", "Kelas", "Alamat"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tabelWalikelas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelWalikelasMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabelWalikelas);

        cb_kelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih Satu" }));
        cb_kelas.setToolTipText("");

        jLabel7.setText("Kelas");

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

        jLabel5.setText("Level");

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(204, 204, 204));
        jTextField1.setText("Walikelas");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(185, 185, 185))
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7))
                        .addGap(72, 72, 72)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cb_kelas, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(password_walikelas)
                            .addComponent(username_walikelas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                            .addComponent(nama_walikelas, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(107, 107, 107)
                                .addComponent(jTextField1)
                                .addGap(89, 89, 89))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(333, 333, 333)
                                .addComponent(jButton1)))
                        .addGap(0, 30, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(nama_walikelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(username_walikelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(password_walikelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_kelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(88, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tabelWalikelasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelWalikelasMouseClicked
        // TODO add your handling code here:
//        data();
        
    }//GEN-LAST:event_tabelWalikelasMouseClicked

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
            java.util.logging.Logger.getLogger(dataWaliKelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dataWaliKelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dataWaliKelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dataWaliKelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dataWaliKelas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cb_kelas;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField nama_walikelas;
    private javax.swing.JPasswordField password_walikelas;
    private javax.swing.JTable tabelWalikelas;
    private javax.swing.JTextField username_walikelas;
    // End of variables declaration//GEN-END:variables
}
