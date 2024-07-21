/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuruMataPelajaran;

import Autentifikasi.login;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import koneksi.Koneksi;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author ThinkPad X390
 */
public class PenilaianSiswa extends javax.swing.JFrame {

    /**
     * Creates new form PenilaianSiswa
     */

    private Connection conn = new Koneksi().connect();
    private int id_user_login = new login().getIdUserLogin();
    private int idGuruMapel = 5;
    private DefaultTableModel tabelModelNilai;
    private String mapel;
    
    public PenilaianSiswa() {
        initComponents();
        setDefault();
    }
    
    private void setDefault() {
        
        /* mengabil data mata pelajaran guru */
        String sqlGetPelajaran = "select mata_pelajaran from guru_mata_pelajaran where user_id=? limit 1";
        
        try{
            PreparedStatement stat = conn.prepareStatement(sqlGetPelajaran);
            stat.setInt(1, id_user_login);
//            stat.setInt(1, 5);
            ResultSet hasil = stat.executeQuery();
            hasil.next();
            mapel = hasil.getString("mata_pelajaran");
            
        
            mata_pelajaran.setText(mapel);
            mata_pelajaran.setEditable(false);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Gagal mengabil database untuk tabel guru_mata_pelajaran : " + e);
            System.out.println(e);
        }
        
        /* ./ mengabil data mata pelajaran guru */
        
        /* mengabil data kelas guru */
        JSONParser parser = new JSONParser();
        String sqlGetKelas = "select kelas from guru_mata_pelajaran where id=? limit 1";
        
        try{
            PreparedStatement stat = conn.prepareStatement(sqlGetKelas);
//            stat.setInt(1, id_user_login);
            stat.setInt(1, 5);
            ResultSet hasil = stat.executeQuery();
            hasil.next();
            String daftarKelas = hasil.getString("kelas");
            
            System.out.println(daftarKelas);
            
            JSONArray jsonArrayKelas = (JSONArray) parser.parse(daftarKelas);
            
            for (int i = 0; i < (jsonArrayKelas.size()); i++) {
                //System.out.println("Element at index " + i + ": " + mapel[i]);
                cb_pilih_kelas.addItem(jsonArrayKelas.get(i));
            }
            
        
//            mata_pelajaran.setText(mapel);
//            mata_pelajaran.setEditable(false);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Gagal mengabil database untuk tabel guru_mata_pelajaran : " + e);
            System.out.println(e);
        }
        
        /* ./ mengabil data kelas guru */
        
        
    }

    private void setTabelNilai(String kelasDipilih) {
        Object[] Baris = {"id_nilai", "id_siswa", "No", "NIS", "Nama Siswa", "Nilai"};
        tabelModelNilai = new DefaultTableModel(null, Baris);
        tabelNilaiSiswa.setModel(tabelModelNilai);
        tabelNilaiSiswa.removeColumn(tabelNilaiSiswa.getColumnModel().getColumn(0));
        tabelNilaiSiswa.removeColumn(tabelNilaiSiswa.getColumnModel().getColumn(0));

        
        String sqlNilaiSiswa = "select "
                + "nilai.*, siswa.id as siswa_id, siswa.nama as nama_siswa, siswa.nis "
                + "from siswa left "
                + "join nilai on siswa.id=nilai.id_siswa "
                + "where siswa.kelas='"+kelasDipilih+"'";
        
        try{
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sqlNilaiSiswa);
            Integer no = 1;
            while(hasil.next()){
                
                //cek nilai apakah null/ belum diisi
                
                String nilai = "0";
                if(hasil.getString(mapel) != null){
                    nilai = hasil.getString(mapel);
                }
                
                String idNilai = "";
                if(hasil.getString("id") != null){
                    idNilai = hasil.getString("id");
                }
                
                String siswa_id = hasil.getString("siswa_id"); //id_siswa
                String nis = hasil.getString("nis");
                String nama_siswa = hasil.getString("nama_siswa");
                String nomor = no.toString();
                String[] data={idNilai, siswa_id, nomor, nis, nama_siswa, nilai};
                tabelModelNilai.addRow(data);
                no++;
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Gagal mengabil database untuk setTabelNilai : " + e);
        }
    }
    
    private void setFormNilai(){
        int bar = tabelNilaiSiswa.getSelectedRow();
        String id_nilai = tabelModelNilai.getValueAt(bar, 0).toString();
        String id_siswa = tabelModelNilai.getValueAt(bar, 1).toString();
        int idNilai = 0;
        
        if(id_nilai != ""){
            idNilai = Integer.parseInt(id_nilai);
        }
        
        nis.setText(tabelModelNilai.getValueAt(bar, 3).toString());
        nama.setText(tabelModelNilai.getValueAt(bar, 4).toString());
        nilai.setText(tabelModelNilai.getValueAt(bar, 5).toString());
        
    }
    
    private void simpanNilai(){
        
        try{
        
            int bar = tabelNilaiSiswa.getSelectedRow();
            String id_nilai = tabelModelNilai.getValueAt(bar, 0).toString();
            String siswa_id = tabelModelNilai.getValueAt(bar, 1).toString();

            int idNilai = 0;
            if(id_nilai != ""){
                idNilai = Integer.parseInt(id_nilai);
            }

            int idSiswa = Integer.parseInt(siswa_id);
            int nilaiSiswa = Integer.parseInt(nilai.getText());

            String sql = "";


            /* jika tidak ada user_id di tabel nilai */

            if(idNilai == 0){
                sql = "insert into nilai(id_siswa, "+ mapel +") values (?,?)";


                try{
                    PreparedStatement stat = conn.prepareStatement(sql);
                    stat.setInt(1, idSiswa);
                    stat.setInt(2, nilaiSiswa);

                    stat.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Nilai Berhasil Disimpan");
                    nis.setText("");
                    nama.setText("");
                    nilai.setText("");

                    nilai.requestFocus();

                    String itemCBKelas = cb_pilih_kelas.getSelectedItem().toString();
                    setTabelNilai(itemCBKelas);
                }catch (SQLException e){
                    JOptionPane.showConfirmDialog(null, "Nilai gagal disimpan"+e);
                }
            }
        /* ./ jika tidak ada user_id di tabel nilai */
            
        /* jika ada user_id di tabel nilai */
            if(idNilai > 0){
            
                sql = "update nilai set "+mapel+"=? where id="+idNilai;


                try{
                    PreparedStatement stat = conn.prepareStatement(sql);
                    stat.setInt(1, nilaiSiswa);

                    stat.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Nilai Berhasil Diubah");
                    nis.setText("");
                    nama.setText("");
                    nilai.setText("");
                    nilai.requestFocus();

                    String itemCBKelas = cb_pilih_kelas.getSelectedItem().toString();
                    setTabelNilai(itemCBKelas);
                }catch (SQLException e){
                    JOptionPane.showConfirmDialog(null, "Nilai gagal diubah"+e);
                }
                
            }
        
        
        
        
        /* ./ jika ada user_id di tabel nilai */
            
            
            
        }catch(Exception eSimpan){
            System.out.println(eSimpan);
        }
        
    }
    
    private void resetNilai(){
        
        try{
        
        
        
            int bar = tabelNilaiSiswa.getSelectedRow();
            String id_nilai = tabelModelNilai.getValueAt(bar, 0).toString();
            String siswa_id = tabelModelNilai.getValueAt(bar, 1).toString();

            int idNilai = 0;
            if(id_nilai != ""){
                idNilai = Integer.parseInt(id_nilai);
            }

            int idSiswa = Integer.parseInt(siswa_id);

            String sql = "";
            
        /* jika ada user_id di tabel nilai */
            if(idNilai > 0){
            
                sql = "update nilai set "+mapel+"=? where id="+idNilai;


                try{
                    PreparedStatement stat = conn.prepareStatement(sql);
                    stat.setInt(1, 0);

                    stat.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Nilai Berhasil Diubah");
                    nis.setText("");
                    nama.setText("");
                    nilai.setText("");
                    nilai.requestFocus();

                    String itemCBKelas = cb_pilih_kelas.getSelectedItem().toString();
                    setTabelNilai(itemCBKelas);
                }catch (SQLException e){
                    JOptionPane.showConfirmDialog(null, "Nilai gagal diubah"+e);
                }
                
            }else{
                JOptionPane.showMessageDialog(null, "Nilai Tidak Ada!");
            }
        
        
        
        
        /* ./ jika ada user_id di tabel nilai */
            
            
            
        }catch(Exception eSimpan){
            System.out.println(eSimpan);
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
        cb_pilih_kelas = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        mata_pelajaran = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nis = new javax.swing.JTextField();
        nama = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        nilai = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelNilaiSiswa = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 27)); // NOI18N
        jLabel1.setText("Halaman Penilaian Siswa");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel2.setText("Pilih Kelas");

        cb_pilih_kelas.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        cb_pilih_kelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih Satu" }));
        cb_pilih_kelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_pilih_kelasActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel3.setText("Mata Pelajaran");

        mata_pelajaran.setBackground(new java.awt.Color(217, 217, 217));
        mata_pelajaran.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        mata_pelajaran.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel4.setText("NIS :");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel5.setText("Nama Siswa");

        nis.setBackground(new java.awt.Color(217, 217, 217));
        nis.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        nis.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        nama.setBackground(new java.awt.Color(217, 217, 217));
        nama.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        nama.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel6.setText("Nilai :");

        nilai.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N

        tabelNilaiSiswa.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tabelNilaiSiswa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No", "NIS", "Nama Siswa", "Nilai"
            }
        ));
        tabelNilaiSiswa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelNilaiSiswaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelNilaiSiswa);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jButton1.setText("Simpan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jButton3.setText("Reset");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jButton4.setText("Logout");
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
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(74, 74, 74)
                        .addComponent(jButton4)
                        .addGap(41, 41, 41))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(nis, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(nama, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(nilai, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cb_pilih_kelas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(mata_pelajaran, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE))
                        .addContainerGap(53, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jButton4))
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(mata_pelajaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cb_pilih_kelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nilai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton3)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(107, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if(nama.getText().length() < 1){
            JOptionPane.showMessageDialog(null, "Wajib Memilih Siswa!");
        }else{
            resetNilai();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void cb_pilih_kelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_pilih_kelasActionPerformed
        String itemCBKelas = cb_pilih_kelas.getSelectedItem().toString();
        setTabelNilai(itemCBKelas);
    }//GEN-LAST:event_cb_pilih_kelasActionPerformed

    private void tabelNilaiSiswaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelNilaiSiswaMouseClicked

        // TODO add your handling code here:
        setFormNilai();
    }//GEN-LAST:event_tabelNilaiSiswaMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // validasi
        
        if(nama.getText().length() < 1){
            JOptionPane.showMessageDialog(null, "Wajib Memilih Siswa!");
        }else{
            simpanNilai();
        }
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        login login_page = new login();
        String pesan = "berhasil logout";
        JOptionPane.showMessageDialog(null, pesan);
        login_page.setVisible(true);
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
            java.util.logging.Logger.getLogger(PenilaianSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PenilaianSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PenilaianSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PenilaianSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PenilaianSiswa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cb_pilih_kelas;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField mata_pelajaran;
    private javax.swing.JTextField nama;
    private javax.swing.JTextField nilai;
    private javax.swing.JTextField nis;
    private javax.swing.JTable tabelNilaiSiswa;
    // End of variables declaration//GEN-END:variables
}
