/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Siswa;

import Autentifikasi.login;
import Dashboard.MenuSiswa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;

/**
 *
 * @author mosyq
 */
public class InputNilai extends javax.swing.JFrame {
    private Connection conn = new Koneksi().connect();
    private DefaultTableModel tabmode;
    private int id_user_login = new login().getIdUserLogin();
    private static int i_pabp;
    private static int i_ppkn;
    private static int i_b_indonesia;
    private static int i_ipa;
    private static int i_mtk;
    private static int i_sejarah;
    private static int i_pai;
    private static int i_kesenian;
    private static int i_pjok;

    /**
     * Creates new form InputNilai
     */
    public InputNilai() {
        initComponents();
        dataTable();
        
    }
    
    
    //membuat datatable siswa    
    public void dataTable(){
        Object[] Baris={"No","PABP","PPKN","B. Indonesia","IPAS", "MTK",
            "Sejarah", "PAI", "Kesenian", "PJOK"};
        tabmode = new DefaultTableModel(null,Baris);
        tabelNilai.setModel(tabmode);

        String sql = "select * from nilai where user_id="+id_user_login;
        try{
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            Integer no = 1;
            while(hasil.next()){
                String a = hasil.getString("pabp");
                String b = hasil.getString("ppkn");
                String c = hasil.getString("b_indonesia");
                String d = hasil.getString("ipa");
                String e = hasil.getString("mtk");
                String f = hasil.getString("sejarah");
                String g = hasil.getString("pai");
                String h = hasil.getString("kesenian");
                String i = hasil.getString("pjok");
                String nomor = no.toString();
                String[] data={nomor, a,b,c,d,e,f,g,h,i};
                tabmode.addRow(data);
                no++;
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Gagal mengabil database untuk tabel : " + e);
        }
    }
    
    private void simpan(){
        i_pabp = Integer.parseInt(pabp.getText());
        i_ppkn = Integer.parseInt(ppkn.getText());
        i_b_indonesia = Integer.parseInt(b_indonesia.getText());
        i_ipa = Integer.parseInt(ipas.getText());
        i_mtk = Integer.parseInt(mtk.getText());
        i_sejarah = Integer.parseInt(sejarah.getText());
        i_pai = Integer.parseInt(pai.getText());
        i_kesenian = Integer.parseInt(kesenian.getText());
        i_pjok = Integer.parseInt(pjok.getText());
        String sql = "insert into nilai(user_id, pabp, ppkn, b_indonesia, "
                + "ipa, mtk, sejarah, pai, kesenian, pjok) values (?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setInt(1, id_user_login);
            stat.setInt(2, i_pabp);
            stat.setInt(3, i_ppkn);
            stat.setInt(4, i_b_indonesia);
            stat.setInt(5, i_ipa);
            stat.setInt(6, i_mtk);
            stat.setInt(7, i_sejarah);
            stat.setInt(8, i_pai);
            stat.setInt(9, i_kesenian);
            stat.setInt(10, i_pjok);
            
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
            kosong();
            pabp.requestFocus();
            dataTable();
        }catch (SQLException e){
            JOptionPane.showConfirmDialog(null, "data gagal disimpan"+e);
            }
    }
    
    //ubah data
    private void edit(){
    
        i_pabp = Integer.parseInt(pabp.getText());
        i_ppkn = Integer.parseInt(ppkn.getText());
        i_b_indonesia = Integer.parseInt(b_indonesia.getText());
        i_ipa = Integer.parseInt(ipas.getText());
        i_mtk = Integer.parseInt(mtk.getText());
        i_sejarah = Integer.parseInt(sejarah.getText());
        i_pai = Integer.parseInt(pai.getText());
        i_kesenian = Integer.parseInt(kesenian.getText());
        i_pjok = Integer.parseInt(pjok.getText());
        String sql = "update nilai set pabp=?, ppkn=?, b_indonesia=?, "
                + "ipa=?, mtk=?, sejarah=?, pai=?, kesenian=?, pjok=?"
                + " where user_id="+id_user_login;
        try{
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setInt(1, i_pabp);
            stat.setInt(2, i_ppkn);
            stat.setInt(3, i_b_indonesia);
            stat.setInt(4, i_ipa);
            stat.setInt(5, i_mtk);
            stat.setInt(6, i_sejarah);
            stat.setInt(7, i_pai);
            stat.setInt(8, i_kesenian);
            stat.setInt(9, i_pjok);
            
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
            kosong();
            pabp.requestFocus();
            dataTable();
        }catch (SQLException e){
            JOptionPane.showConfirmDialog(null, "data gagal diubah "+e);
        }
    
    }
    
    //hapus
    private void hapus(){
        String sql = "delete from nilai "
                + " where user_id="+id_user_login;
        try{
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
            kosong();
            pabp.requestFocus();
            dataTable();
        }catch (SQLException e){
            JOptionPane.showConfirmDialog(null, "data gagal dihapus "+e);
        }
    
    }
    
    
    //menghapus data
    private void kosong(){
        pabp.setText("");
        ppkn.setText("");
        b_indonesia.setText("");
        ipas.setText("");
        mtk.setText("");
        sejarah.setText("");
        pai.setText("");
        kesenian.setText("");
        pjok.setText("");
    }
    
    //setting data ke form
    protected void setData(){
        try{
        
        
        int bar = tabelNilai.getSelectedRow();
        String a = tabmode.getValueAt(bar, 1).toString();
        String b = tabmode.getValueAt(bar, 2).toString();
        String c = tabmode.getValueAt(bar, 3).toString();
        String d = tabmode.getValueAt(bar, 4).toString();
        String e = tabmode.getValueAt(bar, 5).toString();
        String f = tabmode.getValueAt(bar, 6).toString();
        String g = tabmode.getValueAt(bar, 7).toString();
        String h = tabmode.getValueAt(bar, 8).toString();
        String i = tabmode.getValueAt(bar, 9).toString();
        
        pabp.setText(a);
        ppkn.setText(b);
        b_indonesia.setText(c);
        ipas.setText(d);
        mtk.setText(e);
        sejarah.setText(f);
        pai.setText(g);
        kesenian.setText(h);
        pjok.setText(i);
        
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pabp = new javax.swing.JTextField();
        ppkn = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        b_indonesia = new javax.swing.JTextField();
        ipas = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        mtk = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        sejarah = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        pai = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        kesenian = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        pjok = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelNilai = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 1, 23)); // NOI18N
        jLabel1.setText("Halaman Input Nilai");

        jLabel2.setText("PABP");

        jLabel3.setText("PPKN");

        jLabel4.setText("B.Indonesia");

        jLabel5.setText("IPAS");

        jLabel6.setText("Matematika");

        jLabel7.setText("Sejarah");

        jLabel8.setText("PAI");

        jLabel9.setText("Kesenian");

        jLabel10.setText("PJOK");

        tabelNilai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "PABP", "PPKN", "B. Indonesia", "IPAS", "MTK", "Sejarah", "PAI", "Kesenian", "PJOK"
            }
        ));
        tabelNilai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelNilaiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelNilai);
        if (tabelNilai.getColumnModel().getColumnCount() > 0) {
            tabelNilai.getColumnModel().getColumn(5).setResizable(false);
            tabelNilai.getColumnModel().getColumn(9).setResizable(false);
        }

        jButton1.setText("Simpan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Edit");
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

        jButton4.setText("Menu Siswa");
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
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel3)
                        .addComponent(jLabel2))
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addGap(57, 57, 57)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addGap(0, 241, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(b_indonesia, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(ppkn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pabp, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(ipas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(mtk, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pai, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(kesenian, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pjok, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(sejarah, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34))))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(231, 231, 231)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton4)))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(pabp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addComponent(ppkn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(b_indonesia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ipas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                                .addComponent(mtk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sejarah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(pai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(kesenian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(pjok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(32, 32, 32)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        simpan();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        MenuSiswa menuSiswa = new MenuSiswa();
        menuSiswa.setVisible(true);
        this.dispose();
        return;
    }//GEN-LAST:event_jButton4ActionPerformed

    private void tabelNilaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelNilaiMouseClicked
        // TODO add your handling code here:
        setData();
    }//GEN-LAST:event_tabelNilaiMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        edit();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        hapus();
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(InputNilai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InputNilai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InputNilai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InputNilai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InputNilai().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField b_indonesia;
    private javax.swing.JTextField ipas;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField kesenian;
    private javax.swing.JTextField mtk;
    private javax.swing.JTextField pabp;
    private javax.swing.JTextField pai;
    private javax.swing.JTextField pjok;
    private javax.swing.JTextField ppkn;
    private javax.swing.JTextField sejarah;
    private javax.swing.JTable tabelNilai;
    // End of variables declaration//GEN-END:variables
}
