/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Administrasi;

import Dashboard.MenuDashboard;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;

/**
 *
 * @author mosyq
 */


public class dataUniversitasJurusan extends javax.swing.JFrame {

    private Connection conn = new Koneksi().connect();
    private DefaultTableModel tabelModelJurusan;    
    private DefaultTableModel tabelModelUniversitas;

    /**
     * Creates new form dataUniversitas
     */
    public dataUniversitasJurusan() {
        initComponents();
        dataTableUniversitas();
    }
    
    
/* Universitas */
    
    private void setFormUniversitas() {
        //ambil data dari tabel
        
        int bar = tabelUniversitas.getSelectedRow();
        String a = tabelModelUniversitas.getValueAt(bar, 0).toString(); //no
        String b = tabelModelUniversitas.getValueAt(bar, 1).toString(); // id
        String c = tabelModelUniversitas.getValueAt(bar, 2).toString(); // nama universitas
        String d = tabelModelUniversitas.getValueAt(bar, 3).toString(); // kuota
        
        //set data ke form
        nama_univ.setText(c);
        kuota_univ.setText(d);
        
    }
    
    
    public void dataTableUniversitas() {
        Object[] Baris = {"No", "Id", "Universitas", "Kuota"};
        tabelModelUniversitas = new DefaultTableModel(null, Baris);
        tabelUniversitas.setModel(tabelModelUniversitas);
        
        //hapus kolom id agar tidak tampil tetapi bisa mengambil data id ketika barik di klik
        tabelUniversitas.removeColumn(tabelUniversitas.getColumnModel().getColumn(1));

        String sql = "select * from universitas";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            Integer no = 1;
            while (hasil.next()) {
                String nama = hasil.getString("nama");
                String kuota = hasil.getString("kuota");
                String nomor = no.toString();
                Integer id_universitas = hasil.getInt("id");
                String[] data = {nomor, id_universitas.toString(), nama, kuota};
                tabelModelUniversitas.addRow(data);
                no++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mengabil database: " + e);
        }
    }
    
    //simpan jurusan
    private void simpanUniversitas(){
        try{
            String sql = "insert into universitas(nama, kuota) values (?, ?)";
            PreparedStatement stat_universitas = conn.prepareStatement(sql, 
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stat_universitas.setString(1, nama_univ.getText());
            stat_universitas.setInt(2, Integer.parseInt(kuota_univ.getText()));
            stat_universitas.executeUpdate();
            
            kosong();
            nama_univ.requestFocus();
            dataTableUniversitas();
        
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data Universitas Gagal Disimpan " + e);
        };
        
    }
    
    //Hapus data universitas
    private void hapusUniversitas(){
        // id user yang akan diubah
        int bar = tabelUniversitas.getSelectedRow();
        int id_universitas = Integer.parseInt(tabelModelUniversitas.getValueAt(bar, 1).toString());
        
        //hapus data jurusan
        try{
            String sql_universitas = "delete from jurusan where id_universitas ="+id_universitas;
            PreparedStatement stat_universitas= conn.prepareStatement(sql_universitas);
            
            stat_universitas.executeUpdate();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data Jurusan Gagal Dihapus " + e);
        }
        
        //hapus data universitas
        try{
            String sql_universitas = "delete from universitas where id ="+id_universitas;
            PreparedStatement stat_universitas= conn.prepareStatement(sql_universitas);
            
            stat_universitas.executeUpdate();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data Jurusan Gagal Dihapus " + e);
        }
        
        
        JOptionPane.showMessageDialog(null, "Data Universitas Berhasil Hapus");
        kosong();
        nama_univ.requestFocus();
        dataTableUniversitas();
    }
    
    private void kosong(){
        nama_univ.setText("");
        kuota_univ.setText("");
    }

/* ./ Universitas */
    
/* ----------------------------------------------------------------- */
    
/* Jurusan */

    //membuat datatable Jurusan
    public void dataTableJurusan() {
        
        Object[] Baris = {"No", "id", "jurusan", "Syarat Mapel"};
        tabelModelJurusan = new DefaultTableModel(null, Baris);
        tabelJurusan.setModel(tabelModelJurusan);
        
        //hapus kolom id agar tidak tampil tetapi bisa mengambil data id ketika barik di klik
        tabelJurusan.removeColumn(tabelJurusan.getColumnModel().getColumn(1));
        
        //ambil id universitas
        int bar = tabelUniversitas.getSelectedRow();
        String id_universitas = tabelModelUniversitas.getValueAt(bar, 1).toString(); // id

        String sql = "select * from jurusan where id_universitas="+id_universitas;
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            Integer no = 1;
            while (hasil.next()) {
                String jurusan = hasil.getString("jurusan");
                String syarat_mata_pelajaran = hasil.getString("syarat_mata_pelajaran");

                String nomor = no.toString();
                Integer id_user = hasil.getInt("id");
//                String[] data = {id_user.toString(), nomor, jurusan};
//                tabelModelJurusan.addRow(data);

                tabelModelJurusan = (DefaultTableModel)tabelJurusan.getModel();
                tabelModelJurusan.addRow(new Object[]{nomor, id_user.toString(), jurusan, syarat_mata_pelajaran});
                no++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mengabil database: " + e);
        }
    }
    
    private void simpanJurusan() {
        try{
            
            int bar = tabelUniversitas.getSelectedRow();
            int id_universitas = Integer.parseInt(tabelModelUniversitas.getValueAt(bar, 1).toString());
            
            String sql = "insert into jurusan(id_universitas, jurusan, syarat_mata_pelajaran) values (?, ?, ?)";
            PreparedStatement stat_universitas = conn.prepareStatement(sql, 
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stat_universitas.setInt(1, id_universitas);
            stat_universitas.setString(2, nama_jurusan.getText());
            stat_universitas.setString(3, syarat_mapel.getText());
            stat_universitas.executeUpdate();
            
            System.out.println(nama_jurusan.getText());
            System.out.println(syarat_mapel.getText());
            
            kosong();
            nama_jurusan.requestFocus();
            nama_jurusan.setText("");
            syarat_mapel.setText("");
            dataTableJurusan();
        
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data Universitas Gagal Disimpan " + e);
            System.out.println(e);
        };
        
    }
    
    private void hapusJurusan() {
        // id user yang akan diubah
        int bar = tabelJurusan.getSelectedRow();
        int id_jurusan = Integer.parseInt(tabelModelJurusan.getValueAt(bar, 1).toString());
        
        //hapus data jurusan
        try{
            String sql_jurusan = "delete from jurusan where id ="+id_jurusan;
            PreparedStatement stat_jurusan= conn.prepareStatement(sql_jurusan);
            
            stat_jurusan.executeUpdate();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data Jurusan Gagal Dihapus " + e);
        }
        
        
        JOptionPane.showMessageDialog(null, "Data Jurusan Berhasil Hapus");
        kosong();
        nama_jurusan.requestFocus();
        dataTableJurusan();
    }
    
    
/* ./ Jurusan */
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelUniversitas = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nama_univ = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        kuota_univ = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        nama_jurusan = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        syarat_mapel = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabelJurusan = new javax.swing.JTable();
        SimpanJurusan = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

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
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        jLabel1.setText("Data Universitas");

        tabelUniversitas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No", "Id", "Universitas", "Kuota"
            }
        ));
        tabelUniversitas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelUniversitasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelUniversitas);

        jButton1.setText("Menu Dashboard");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));

        jLabel2.setText("Nama Universitas");

        jLabel3.setText("Kuota");

        jToggleButton1.setText("Simpan Universitas");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton3.setText("Hapus Universitas");
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(nama_univ, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                                .addComponent(kuota_univ))
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jToggleButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jToggleButton3)))
                        .addGap(0, 12, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nama_univ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kuota_univ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton3)
                    .addComponent(jToggleButton1))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        jLabel5.setText("Jurusan");

        jLabel6.setText("Syarat Mata Pelajaran");

        syarat_mapel.setColumns(20);
        syarat_mapel.setRows(5);
        jScrollPane4.setViewportView(syarat_mapel);

        tabelJurusan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "No", "Jurusan", "Syarat Mapel"
            }
        ));
        jScrollPane5.setViewportView(tabelJurusan);

        SimpanJurusan.setBackground(new java.awt.Color(51, 255, 204));
        SimpanJurusan.setText("Simpan Jurusan");
        SimpanJurusan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SimpanJurusanActionPerformed(evt);
            }
        });

        jButton2.setText("Hapus Jurusan");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane4)
                            .addComponent(nama_jurusan))
                        .addGap(26, 26, 26))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(SimpanJurusan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2)))
                        .addGap(0, 226, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel5)
                .addGap(3, 3, 3)
                .addComponent(nama_jurusan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SimpanJurusan)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(229, 229, 229)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 36, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 870, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))
                        .addGap(110, 110, 110)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(252, 252, 252))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        // TODO add your handling code here:
        hapusUniversitas();
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        simpanUniversitas();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        MenuDashboard menuDashboard = new MenuDashboard();
        menuDashboard.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void SimpanJurusanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SimpanJurusanActionPerformed
        // TODO add your handling code here:
        simpanJurusan();
    }//GEN-LAST:event_SimpanJurusanActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        hapusJurusan();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tabelUniversitasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelUniversitasMouseClicked
        // TODO add your handling code here:
        setFormUniversitas();
        dataTableJurusan();
//        JOptionPane.showMessageDialog(null, "testtt");
    }//GEN-LAST:event_tabelUniversitasMouseClicked

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
            java.util.logging.Logger.getLogger(dataUniversitasJurusan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dataUniversitasJurusan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dataUniversitasJurusan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dataUniversitasJurusan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dataUniversitasJurusan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton SimpanJurusan;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JTextField kuota_univ;
    private javax.swing.JTextField nama_jurusan;
    private javax.swing.JTextField nama_univ;
    private javax.swing.JTextArea syarat_mapel;
    private javax.swing.JTable tabelJurusan;
    private javax.swing.JTable tabelUniversitas;
    // End of variables declaration//GEN-END:variables
}
