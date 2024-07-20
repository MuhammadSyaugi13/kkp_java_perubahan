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
public class dataUniversitas extends javax.swing.JFrame {

    private Connection conn = new Koneksi().connect();
    private DefaultTableModel tabmode;
    /**
     * Creates new form dataUniversitas
     */
    public dataUniversitas() {
        initComponents();
        dataTable();
        dataTableJurusan();
    }
    
    //membuat datatable Pegawai
    public void dataTable() {
        Object[] Baris = {"id", "No", "Universitas", "Kuota"};
        tabmode = new DefaultTableModel(null, Baris);
        tabelUniv.setModel(tabmode);
        tabelUniv.removeColumn(tabelUniv.getColumnModel().getColumn(0));

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
                String[] data = {id_universitas.toString(), nomor, nama, kuota};
                tabmode.addRow(data);
                no++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mengabil database: " + e);
        }
    }
    
    //membuat datatable Jurusan
    public void dataTableJurusan() {
        
        Object[] Baris = {"No", "id", "jurusan", "Check"};
        tabmode = new DefaultTableModel(null, Baris);
//        tabelJurusan.setModel(tabmode);

        String sql = "select * from jurusan";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            Integer no = 1;
            while (hasil.next()) {
                String jurusan = hasil.getString("jurusan.jurusan");
                String nomor = no.toString();
                Integer id_user = hasil.getInt("id");
//                String[] data = {id_user.toString(), nomor, jurusan};
//                tabmode.addRow(data);

                tabmode = (DefaultTableModel)tabelJurusan.getModel();
                tabmode.addRow(new Object[]{nomor, id_user.toString(), jurusan, false});
                no++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mengabil database: " + e);
        }
    }
    
    //simpan jurusan
    private void simpan(){
        try{
            String sql = "insert into universitas(nama, kuota) values (?, ?)";
            PreparedStatement stat_universitas = conn.prepareStatement(sql, 
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stat_universitas.setString(1, nama_univ.getText());
            stat_universitas.setInt(2, Integer.parseInt(kuota_univ.getText()));
            
            int rowsInserted = stat_universitas.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = stat_universitas.getGeneratedKeys();
                if (generatedKeys.next()) {
                    
                    int countTableJurusan = tabelJurusan.getRowCount();
                    for(int i = 0; i < countTableJurusan; i++){
                        Boolean isChecked = (Boolean)tabelJurusan.getValueAt(i, 3);
                        int id_jurusan = Integer.parseInt(tabelJurusan.getValueAt(i, 1).toString());
                        
                        //jika memlihin jurusan, maka input ke table mapping
                        if (isChecked) {
                            
                            String sql_maping = "insert into maping_universitas_jurusan"
                            + "(universitas_id, jurusan_id) "
                            + "values (?,?)";
                            int id_universitas = generatedKeys.getInt(1);
                            try {
                                PreparedStatement stat_maping = conn.prepareStatement(sql_maping);
                                stat_maping.setInt(1, id_universitas);
                                stat_maping.setInt(2, id_jurusan);

                                stat_maping.executeUpdate();
                                
                                
                            } catch (SQLException e) {
                                JOptionPane.showConfirmDialog(null, "data Maping gagal disimpan " + e);
                                System.out.println("data maping gagal disimpan" + e);
                            }
                            
                        }
                    }
                    
                    
                    

                    JOptionPane.showConfirmDialog(null,
                            "Data Universitas berhasil dimasukkan");
                    
                    
                }
                
            }
            // JOptionPane.showMessageDialog(null, "Data Jurusan Berhasil Disimpan");
            kosong();
            nama_univ.requestFocus();
            dataTable();
        
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data Jurusan Gagal Disimpan " + e);
        };
        
    }
    
    //Hapus data universitas
    private void hapus(){
        // id user yang akan diubah
        int bar = tabelUniv.getSelectedRow();
        int id_universitas = Integer.parseInt(tabmode.getValueAt(bar, 0).toString());
        
        //data universitas
        try{
            String sql_universitas = "delete from universitas where id ="+id_universitas;
            PreparedStatement stat_universitas= conn.prepareStatement(sql_universitas);
            
            stat_universitas.executeUpdate();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data Jurusan Gagal Dihapus " + e);
        }
        
        //data maping
        try{
            String sql_maping = "delete from maping_universitas_jurusan where universitas_id ="+id_universitas;
            PreparedStatement stat_maping= conn.prepareStatement(sql_maping);
            JOptionPane.showMessageDialog(null, "Data Maping Gagal Dihapus " + id_universitas);
            
            stat_maping.executeUpdate();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data Maping Gagal Dihapus " + e);
        }
        
        JOptionPane.showMessageDialog(null, "Data Univers Berhasil Hapus");
        kosong();
        nama_univ.requestFocus();
        dataTable();
    }
    
    private void kosong(){
        nama_univ.setText("");
        kuota_univ.setText("");
    }

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
        jLabel2 = new javax.swing.JLabel();
        nama_univ = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        kuota_univ = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelUniv = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelJurusan = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField2 = new javax.swing.JTextField();

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

        jLabel2.setText("Nama Univ.");

        jLabel3.setText("Kuota");

        jToggleButton1.setText("Simpan");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton3.setText("Hapus");
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        tabelUniv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "id", "No", "Universitas", "Kuota"
            }
        ));
        jScrollPane2.setViewportView(tabelUniv);

        jLabel4.setText("Pilihan Jurusan");

        tabelJurusan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Id", "Jurusan", "Kode Jurusan", "Check"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelJurusan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelJurusanMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabelJurusan);

        jButton1.setText("Menu Dashboard");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Jurusan");

        jLabel6.setText("Syarat Mata Pelajaran");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3))
                                        .addGap(43, 43, 43)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(kuota_univ, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(nama_univ, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(275, 275, 275)))
                        .addComponent(jButton1)
                        .addGap(28, 28, 28))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3)
                        .addGap(520, 520, 520))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToggleButton1)
                        .addGap(29, 29, 29)
                        .addComponent(jToggleButton3)
                        .addContainerGap(955, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jButton1)
                        .addGap(83, 83, 83)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(nama_univ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(kuota_univ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton1)
                    .addComponent(jToggleButton3))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        // TODO add your handling code here:
        hapus();
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        simpan();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void tabelJurusanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelJurusanMouseClicked
        // TODO add your handling code here:
//        System.out.println("Hello");
    }//GEN-LAST:event_tabelJurusanMouseClicked

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
            java.util.logging.Logger.getLogger(dataUniversitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dataUniversitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dataUniversitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dataUniversitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dataUniversitas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JTextField kuota_univ;
    private javax.swing.JTextField nama_univ;
    private javax.swing.JTable tabelJurusan;
    private javax.swing.JTable tabelUniv;
    // End of variables declaration//GEN-END:variables
}
