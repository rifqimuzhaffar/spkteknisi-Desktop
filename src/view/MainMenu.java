/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import connectionDB.koneksi;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import model.ModelAlternatifSAW;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author RIFQI
 */
public class MainMenu extends javax.swing.JFrame {
    // waktu sekarang 
     private void setJam(){
        ActionListener listener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                jam.setText(new WaktuSekarang().getWkt());
            }
        };
        new Timer(1000, listener).start();
    }
    
    public MainMenu() {
        setJam();
        setResizable(false);
        initComponents();
        datatable1();
        datatable2();
        datatable3();
        datatable6();
        fasilitas();
        ruangan();
        this.bobot = new double[]{0.15, 0.15, 0.30, 0.30, 0.10};
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
    }
   
    public double nilaiFasilitas(String nama)
    {
        double result = 0;
        try{
            String sqlf = "SELECT * FROM TABELKRITERIA WHERE KETERANGAN = '" + nama + "'";
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sqlf);
          
            while(hasil.next()){
                result = hasil.getInt("nilai");
            }
        }
        catch(Exception e){
            
        }
        return result;
    }
    public void fasilitas(){
        try{
            c1.removeAllItems();
            String sqlf = "select * from tabelkriteria where kriteria in ('fasilitas')";
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sqlf);
            while(hasil.next()){
                c1.addItem(hasil.getString("keterangan"));
            }
            hasil.last();
            int jumlahdata = hasil.getRow();
            hasil.first();
        }catch(Exception e){
        }
    }
    
    public void ruangan(){
        try{
            c2.removeAllItems();
            String sqlr = "select * from tabelkriteria where kriteria in ('ruangan')";
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sqlr);
            while(hasil.next()){
                c2.addItem(hasil.getString("keterangan"));
            }
            hasil.last();
            int jumlahdata = hasil.getRow();
            hasil.first();
        }catch(Exception e){
        }
    }
    
    String Tgl_masuk;
    public void tanggal(){
        if (tanggalmasuk.getDate() !=null){
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            Tgl_masuk = String.valueOf(format.format(tanggalmasuk.getDate()));
        }
    }
    
    public void tanggal_lahir() {
        try {
            String sql = "Select * from tabelalternatif1 where namapekerjaan='"+tnamapekerjaan.getText()+"'";
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()){
                tanggalmasuk.setDate(hasil.getDate("tanggalmasuk"));
            }
        }catch (SQLException ex){}
    }
    
    private Connection conn = new koneksi().connect();
    private DefaultTableModel tabmode;
    private DefaultTableModel tabmode1;
    private Statement statement;
    
    private final DecimalFormat decimalFormat = new DecimalFormat("#.###");
    private final double[] bobot;
    private final List<ModelAlternatifSAW> model = new ArrayList<>();
    private final List<ModelAlternatifSAW> modelForSAW = new ArrayList<>();
    private final List<ModelAlternatifSAW> modelSAWFinal = new ArrayList<>();
    private int buttonSawClicked = 0;
   
    protected void insertSAW(){
        ModelAlternatifSAW[] modelArray = new ModelAlternatifSAW[model.size()];
        model.toArray(modelArray);
        
        Double[][] matrix = new Double[modelArray.length][5];

        for (int i = 0; i < modelArray.length; i++) {
            matrix[i][0] = modelArray[i].getC1() * bobot[0];
            matrix[i][1] = modelArray[i].getC2() * bobot[1];
            matrix[i][2] = modelArray[i].getC3() * bobot[2];
            matrix[i][3] = modelArray[i].getC4() * bobot[3];
            matrix[i][4] = modelArray[i].getC5() * bobot[4];
        }

        // Printing the matrix
        for (int i = 0; i < matrix.length; i++) {
            ModelAlternatifSAW models = new ModelAlternatifSAW();
            Double sums = 0.00;
            models.setId(modelForSAW.get(i).getId());
            models.setTanggalmasuk(modelForSAW.get(i).getTanggalmasuk());
            models.setNamapekerjaan(modelForSAW.get(i).getNamapekerjaan());
            
            for (int j = 0; j < matrix[i].length; j++) {
                sums += matrix[i][j];
                
                switch (j) {
                    case 0:
                        models.setC1(Double.parseDouble(decimalFormat.format(matrix[i][j])));
                        break;
                    case 1:
                        models.setC2(Double.parseDouble(decimalFormat.format(matrix[i][j])));
                        break;
                    case 2: 
                        models.setC3(Double.parseDouble(decimalFormat.format(matrix[i][j])));
                        break;
                    case 3:
                        models.setC4(Double.parseDouble(decimalFormat.format(matrix[i][j])));
                        break;
                    case 4:
                        models.setC5(Double.parseDouble(decimalFormat.format(matrix[i][j])));
                        break;
                    default:
                        break;
                }
                System.out.print(decimalFormat.format(matrix[i][j]) + " ");
            }
            models.setSum(Double.parseDouble(decimalFormat.format(sums)));
            modelSAWFinal.add(models);
            System.out.println("  " + decimalFormat.format(sums));
        }
        
        ModelAlternatifSAW.deleteAllRowSAW();
        
        for(int i=0; i<modelSAWFinal.size(); i++){
            ModelAlternatifSAW insertTableSAW = new ModelAlternatifSAW();
            insertTableSAW.setId(i+1);
            insertTableSAW.setTanggalmasuk(modelSAWFinal.get(i).getTanggalmasuk());
            insertTableSAW.setNamapekerjaan(modelSAWFinal.get(i).getNamapekerjaan());
            insertTableSAW.setC1(modelSAWFinal.get(i).getC1());
            insertTableSAW.setC2(modelSAWFinal.get(i).getC2());
            insertTableSAW.setC3(modelSAWFinal.get(i).getC3());
            insertTableSAW.setC4(modelSAWFinal.get(i).getC4());
            insertTableSAW.setC5(modelSAWFinal.get(i).getC5());
            insertTableSAW.setSum(modelSAWFinal.get(i).getSum());
            
            insertTableSAW.insertDataSAW();
        }
    }
    
    protected void aktif(){
        tnamapekerjaan.setEnabled(true);
        c5.setEnabled(true);
        tnamapekerjaan.requestFocus();
    }
    
    protected void kosongkriteria(){
        idkriteria.setText("");
        kriteria.setSelectedIndex(0);
        keterangan.setText("");
        nilai.setSelectedIndex(0);
    }
    
    protected void kosong(){
        tanggalmasuk.setDate(null);
        tnamapekerjaan.setText("");
        c1.setSelectedIndex(0);
        c2.setSelectedIndex(0);
        c3.setSelectedIndex(0);
        c4.setSelectedIndex(0);
        c5.setText("");
    }
    
    protected void datatable1(){
    Object [] Baris = {"NO","TANGGAL","PEKERJAAN","FASILITAS","RUANGAN","KEAMANAN","KEPENTINGAN","BIAYA"};
    tabmode = new DefaultTableModel(null, Baris);
    tabmode1 = new DefaultTableModel(null,Baris);
    tabelalternatif1.setModel(tabmode);
    tabelalternatif1.setModel(tabmode1);
    try {
    String sql = "Select * from tabelalternatif1";
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        int nos = 1;
        while (hasil.next()){
            String a = String.valueOf(nos);
            String b = hasil.getString("tanggalmasuk");
            String c = hasil.getString("namapekerjaan");
            String d = hasil.getString("c1");
            String e = hasil.getString("c2");
            String f = hasil.getString("c3");
            String g = hasil.getString("c4");
            String h = hasil.getString("c5");
            String[] data={a,b,c,d,e,f,g,h};
            tabmode.addRow(data);
            tabmode1.addRow(data);
            nos++;
        }
    }catch (SQLException e){
        }
    }
    
    protected void datatable2(){
    Object [] Baris = {"NO","TANGGAL","PEKERJAAN","C1","C2","C3","C4","C5"};
    tabmode = new DefaultTableModel(null, Baris);
    tabelalternatif2.setModel(tabmode);
    
    try {
    String sql = "Select * from tabelalternatif2";
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        int nos = 1;
        while (hasil.next()){
            String a = String.valueOf(nos);
            String b = hasil.getString("tanggalmasuk");
            String c = hasil.getString("namapekerjaan");
            String d = hasil.getString("c1");
            String e = hasil.getString("c2");
            String f = hasil.getString("c3");
            String g = hasil.getString("c4");
            String h = hasil.getString("c5");
            String[] data={a,b,c,d,e,f,g,h};
            tabmode.addRow(data);
            
            nos++;
        }
    }catch (SQLException e){
        }
    }
    protected void datatable3(){
    Object [] Baris = {"NO","TANGGAL","PEKERJAAN","C1","C2","C3","C4","C5"};
    tabmode = new DefaultTableModel(null, Baris);
    tabelnormalisasi.setModel(tabmode);
    model.clear();
    try {
    String sql = "Select * from tabelalternatif2";
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        
     
        java.sql.Statement stat1 = conn.createStatement();
        ResultSet hasil1 = stat1.executeQuery(sql);
        
        List<Double> c1Array = new ArrayList<>();
        List<Double> c2Array = new ArrayList<>();
        List<Double> c3Array = new ArrayList<>();
        List<Double> c4Array = new ArrayList<>();
        List<Double> c5Array = new ArrayList<>();
        
        while (hasil1.next()){
            c1Array.add(hasil1.getDouble("c1"));
            c2Array.add(hasil1.getDouble("c2"));
            c3Array.add(hasil1.getDouble("c3"));
            c4Array.add(hasil1.getDouble("c4"));
            c5Array.add(hasil1.getDouble("c5"));
        }
        
        Double c1Max = Collections.max(c1Array);
        Double c2Max = Collections.max(c2Array);
        Double c3Max = Collections.max(c3Array);
        Double c4Max = Collections.max(c4Array);
        Double c5Max = Collections.max(c5Array);
        
        double[][] mat = null;
        int nos = 1;
        while (hasil.next()){
            String a = String.valueOf(nos);
            String b = hasil.getString("tanggalmasuk");
            String c = hasil.getString("namapekerjaan");
            Double d = hasil.getDouble("c1") / c1Max;
            Double e = hasil.getDouble("c2") / c2Max;
            Double f = hasil.getDouble("c3") / c3Max;
            Double g = hasil.getDouble("c4") / c4Max;
            Double h = hasil.getDouble("c5") / c5Max;
            nos ++;
            
            ModelAlternatifSAW model1 = new ModelAlternatifSAW();
            model1.setC1(Double.parseDouble(decimalFormat.format(d)));
            model1.setC2(Double.parseDouble(decimalFormat.format(e)));
            model1.setC3(Double.parseDouble(decimalFormat.format(f)));
            model1.setC4(Double.parseDouble(decimalFormat.format(g)));
            model1.setC5(Double.parseDouble(decimalFormat.format(h)));
            
            model.add(model1);
            
            ModelAlternatifSAW models = new ModelAlternatifSAW();
            models.setId(Integer.parseInt(a));
            models.setTanggalmasuk(b);
            models.setNamapekerjaan(c);
            
            modelForSAW.add(models);
            
            String[] data = {
                a,
                b,
                c,
                String.valueOf(decimalFormat.format(d)),
                String.valueOf(decimalFormat.format(e)),
                String.valueOf(decimalFormat.format(f)),
                String.valueOf(decimalFormat.format(g)),
                String.valueOf(decimalFormat.format(h)),
            };
            tabmode.addRow(data);
        }
    }catch (SQLException e){
        }
    }
    
    protected void datatable4(){
    Object [] Baris = {"NO","PEKERJAAN","C1","C2","C3","C4","C5","TOTAL","RANKING"};
    tabmode = new DefaultTableModel(null, Baris);
    tabelhasil.setModel(tabmode);
    try {
    String sql = "SELECT *, RANK() OVER (ORDER BY sum DESC) AS ranking FROM tb_saw";
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while (hasil.next()){
            String a = hasil.getString("id");
            //String b = hasil.getString("tanggalpekerjaan");
            String c = hasil.getString("namapekerjaan");
            String d = hasil.getString("c1");
            String e = hasil.getString("c2");
            String f = hasil.getString("c3");
            String g = hasil.getString("c4");
            String h = hasil.getString("c5");
            String i = hasil.getString("sum");
            String j = hasil.getString("ranking");
            String[] data={a,c,d,e,f,g,h,i,j};
            tabmode.addRow(data);
        }
    }catch (SQLException e){
        }
    }
    
    protected void datatable5(){
    Object [] Baris = {"NO","TANGGAL","PEKERJAAN","C1","C2","C3","C4","C5"};
    tabmode = new DefaultTableModel(null, Baris);
    tabelnormalisasi.setModel(tabmode);
    try {
    String sql = "Select * from tabelalternatif2";
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        
     
        java.sql.Statement stat1 = conn.createStatement();
        ResultSet hasil1 = stat1.executeQuery(sql);
        
        List<Double> c1Array = new ArrayList<>();
        List<Double> c2Array = new ArrayList<>();
        List<Double> c3Array = new ArrayList<>();
        List<Double> c4Array = new ArrayList<>();
        List<Double> c5Array = new ArrayList<>();
        
        while (hasil1.next()){
            c1Array.add(hasil1.getDouble("c1"));
            c2Array.add(hasil1.getDouble("c2"));
            c3Array.add(hasil1.getDouble("c3"));
            c4Array.add(hasil1.getDouble("c4"));
            c5Array.add(hasil1.getDouble("c5"));
        }
        
        Double c1Max = Collections.max(c1Array);
        Double c2Max = Collections.max(c2Array);
        Double c3Max = Collections.max(c3Array);
        Double c4Max = Collections.max(c4Array);
        Double c5Max = Collections.max(c5Array);
        
        double[][] mat = null;
        int nos = 1;
        while (hasil.next()){
            String a = String.valueOf(nos);
            String b = hasil.getString("tanggalmasuk");
            String c = hasil.getString("namapekerjaan");
            Double d = hasil.getDouble("c1") / c1Max;
            Double e = hasil.getDouble("c2") / c2Max;
            Double f = hasil.getDouble("c3") / c3Max;
            Double g = hasil.getDouble("c4") / c4Max;
            Double h = hasil.getDouble("c5") / c5Max;
            nos ++;
            
            
            String[] data = {
                a,
                b,
                c,
                String.valueOf(decimalFormat.format(d)),
                String.valueOf(decimalFormat.format(e)),
                String.valueOf(decimalFormat.format(f)),
                String.valueOf(decimalFormat.format(g)),
                String.valueOf(decimalFormat.format(h)),
            };
            tabmode.addRow(data);
        }
    }catch (SQLException e){
        }
    }
    
    protected void datatable6(){
    Object [] Baris = {"ID","KRITERA","KETERANGAN","NILAI"};
    tabmode = new DefaultTableModel(null, Baris);
    tabelkriteria.setModel(tabmode);            
    try {
    String sql = "Select * from tabelkriteria";
    
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while (hasil.next()){
            String a = hasil.getString("id");
            String b = hasil.getString("kriteria");
            String c = hasil.getString("keterangan");
            String d = hasil.getString("nilai");
            
            String[] data={a,b,c,d};
            tabmode.addRow(data);
        }
    }catch (Exception e){
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelutama = new javax.swing.JPanel();
        panelmenu = new javax.swing.JPanel();
        bhome = new javax.swing.JButton();
        bpekerjaan = new javax.swing.JButton();
        bhasil = new javax.swing.JButton();
        btentang = new javax.swing.JButton();
        panelwaktu = new javax.swing.JPanel();
        jam = new javax.swing.JLabel();
        tgl = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        panelmain = new javax.swing.JPanel();
        panelhome = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        paneldatapekerjaan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelalternatif1 = new javax.swing.JTable();
        c1 = new javax.swing.JComboBox<>();
        labelKodeSupplier = new javax.swing.JLabel();
        tanggalmasuk = new com.toedter.calendar.JDateChooser();
        labelKodeSupplier1 = new javax.swing.JLabel();
        tnamapekerjaan = new javax.swing.JTextField();
        labelKodeSupplier2 = new javax.swing.JLabel();
        labelKodeSupplier3 = new javax.swing.JLabel();
        c2 = new javax.swing.JComboBox<>();
        c3 = new javax.swing.JComboBox<>();
        labelKodeSupplier4 = new javax.swing.JLabel();
        labelKodeSupplier5 = new javax.swing.JLabel();
        c4 = new javax.swing.JComboBox<>();
        labelKodeSupplier6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelalternatif2 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        btnSimpan = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        c5 = new javax.swing.JTextField();
        btnClear = new javax.swing.JButton();
        panelhasil = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelnormalisasi = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabelhasil = new javax.swing.JTable();
        btnCetak = new javax.swing.JButton();
        btnProses = new javax.swing.JButton();
        panelkriteria = new javax.swing.JPanel();
        keterangan = new javax.swing.JTextField();
        idkriteria = new javax.swing.JTextField();
        labelKodeSupplier7 = new javax.swing.JLabel();
        labelKodeSupplier8 = new javax.swing.JLabel();
        labelKodeSupplier9 = new javax.swing.JLabel();
        labelKodeSupplier10 = new javax.swing.JLabel();
        btnSimpan1 = new javax.swing.JButton();
        btnHapus1 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tabelkriteria = new javax.swing.JTable();
        kriteria = new javax.swing.JComboBox<>();
        nilai = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pemilihan Pekerjaan ME");

        panelutama.setBackground(new java.awt.Color(0, 153, 153));

        panelmenu.setBackground(new java.awt.Color(0, 153, 153));
        panelmenu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));

        bhome.setBackground(new java.awt.Color(0, 204, 102));
        bhome.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        bhome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar2/iconplus.png"))); // NOI18N
        bhome.setText("KRITERIA");
        bhome.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        bhome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bhomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bhomeMouseExited(evt);
            }
        });
        bhome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bhomeActionPerformed(evt);
            }
        });

        bpekerjaan.setBackground(new java.awt.Color(0, 204, 102));
        bpekerjaan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        bpekerjaan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar2/pekerjaan-transformed.png"))); // NOI18N
        bpekerjaan.setText("DATA PEKERJAAN");
        bpekerjaan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bpekerjaanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bpekerjaanMouseExited(evt);
            }
        });
        bpekerjaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bpekerjaanActionPerformed(evt);
            }
        });

        bhasil.setBackground(new java.awt.Color(0, 204, 102));
        bhasil.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        bhasil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar2/hasil-transformed.png"))); // NOI18N
        bhasil.setText("HASIL");
        bhasil.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        bhasil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bhasilMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bhasilMouseExited(evt);
            }
        });
        bhasil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bhasilActionPerformed(evt);
            }
        });

        btentang.setBackground(new java.awt.Color(0, 204, 102));
        btentang.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btentang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar2/about.png"))); // NOI18N
        btentang.setText("TENTANG");
        btentang.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btentang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btentangMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btentangMouseExited(evt);
            }
        });
        btentang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btentangActionPerformed(evt);
            }
        });

        panelwaktu.setBackground(new java.awt.Color(255, 0, 0));

        jam.setFont(new java.awt.Font("Comic Sans MS", 1, 36)); // NOI18N
        jam.setForeground(new java.awt.Color(255, 255, 255));
        jam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        tgl.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        tgl.setForeground(new java.awt.Color(255, 255, 255));
        tgl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panelwaktuLayout = new javax.swing.GroupLayout(panelwaktu);
        panelwaktu.setLayout(panelwaktuLayout);
        panelwaktuLayout.setHorizontalGroup(
            panelwaktuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tgl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelwaktuLayout.setVerticalGroup(
            panelwaktuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelwaktuLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jam, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tgl, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jam.setText(new WaktuSekarang().getWkt());
        jam.setPreferredSize(new java.awt.Dimension(100,22));
        tgl.setText(new WaktuSekarang().getTgl());
        tgl.setPreferredSize(new java.awt.Dimension(120,22));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar2/logopanca.jpeg"))); // NOI18N

        javax.swing.GroupLayout panelmenuLayout = new javax.swing.GroupLayout(panelmenu);
        panelmenu.setLayout(panelmenuLayout);
        panelmenuLayout.setHorizontalGroup(
            panelmenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelmenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelmenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bhome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bpekerjaan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bhasil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btentang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelwaktu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(panelmenuLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelmenuLayout.setVerticalGroup(
            panelmenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelmenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bhome, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bpekerjaan, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bhasil, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btentang, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelwaktu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelmain.setBackground(new java.awt.Color(0, 153, 153));
        panelmain.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));
        panelmain.setLayout(new java.awt.CardLayout());

        panelhome.setBackground(new java.awt.Color(0, 153, 153));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar2/logopancahome.jpeg"))); // NOI18N

        javax.swing.GroupLayout panelhomeLayout = new javax.swing.GroupLayout(panelhome);
        panelhome.setLayout(panelhomeLayout);
        panelhomeLayout.setHorizontalGroup(
            panelhomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelhomeLayout.createSequentialGroup()
                .addGap(285, 285, 285)
                .addComponent(jLabel2)
                .addContainerGap(286, Short.MAX_VALUE))
        );
        panelhomeLayout.setVerticalGroup(
            panelhomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelhomeLayout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addComponent(jLabel2)
                .addContainerGap(135, Short.MAX_VALUE))
        );

        panelmain.add(panelhome, "card5");

        paneldatapekerjaan.setBackground(new java.awt.Color(0, 153, 153));

        tabelalternatif1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabelalternatif1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"
            }
        ));
        tabelalternatif1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabelalternatif1.setRowHeight(30);
        tabelalternatif1.setRowMargin(2);
        tabelalternatif1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelalternatif1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tabelalternatif1MouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(tabelalternatif1);

        c1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        c1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c1ActionPerformed(evt);
            }
        });

        labelKodeSupplier.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelKodeSupplier.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelKodeSupplier.setText("Tanggal Masuk");
        labelKodeSupplier.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        tanggalmasuk.setDateFormatString("d MMMM, yyyy");
        tanggalmasuk.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        labelKodeSupplier1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelKodeSupplier1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelKodeSupplier1.setText("Nama Pekerjaan");
        labelKodeSupplier1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        tnamapekerjaan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tnamapekerjaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tnamapekerjaanActionPerformed(evt);
            }
        });

        labelKodeSupplier2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelKodeSupplier2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelKodeSupplier2.setText("Fasilitas");
        labelKodeSupplier2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        labelKodeSupplier3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelKodeSupplier3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelKodeSupplier3.setText("Ruangan");
        labelKodeSupplier3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        c2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        c2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c2ActionPerformed(evt);
            }
        });

        c3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        c3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sangat Aman", "Aman", "Cukup", "Berbahaya", "Sangat Berbahaya" }));

        labelKodeSupplier4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelKodeSupplier4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelKodeSupplier4.setText("Keamanan");
        labelKodeSupplier4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        labelKodeSupplier5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelKodeSupplier5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelKodeSupplier5.setText("Kepentingan");
        labelKodeSupplier5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        c4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        c4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sangat Penting", "Penting", "Cukup", "Kurang Penting", "Tidak Penting" }));

        labelKodeSupplier6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelKodeSupplier6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelKodeSupplier6.setText("Biaya");
        labelKodeSupplier6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        tabelalternatif2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabelalternatif2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"
            }
        ));
        tabelalternatif2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabelalternatif2.setRowHeight(30);
        tabelalternatif2.setRowMargin(2);
        tabelalternatif2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelalternatif2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelalternatif2);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("DATA PEKERJAAN");

        btnSimpan.setBackground(new java.awt.Color(204, 204, 204));
        btnSimpan.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar2/save-30-w11.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.setContentAreaFilled(false);
        btnSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan.setIconTextGap(0);
        btnSimpan.setOpaque(true);
        btnSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSimpanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSimpanMouseExited(evt);
            }
        });
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnUbah.setBackground(new java.awt.Color(204, 204, 204));
        btnUbah.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar2/edit-30-w11.png"))); // NOI18N
        btnUbah.setText("Ubah");
        btnUbah.setContentAreaFilled(false);
        btnUbah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUbah.setIconTextGap(0);
        btnUbah.setOpaque(true);
        btnUbah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnUbahMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnUbahMouseExited(evt);
            }
        });
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        btnHapus.setBackground(new java.awt.Color(204, 204, 204));
        btnHapus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar2/delete-30-w11.png"))); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.setContentAreaFilled(false);
        btnHapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapus.setIconTextGap(0);
        btnHapus.setOpaque(true);
        btnHapus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHapusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHapusMouseExited(evt);
            }
        });
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        c5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        c5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                c5KeyTyped(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(204, 204, 204));
        btnClear.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnClear.setText("Bersihkan");
        btnClear.setContentAreaFilled(false);
        btnClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear.setOpaque(true);
        btnClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnClearMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnClearMouseExited(evt);
            }
        });
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneldatapekerjaanLayout = new javax.swing.GroupLayout(paneldatapekerjaan);
        paneldatapekerjaan.setLayout(paneldatapekerjaanLayout);
        paneldatapekerjaanLayout.setHorizontalGroup(
            paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneldatapekerjaanLayout.createSequentialGroup()
                .addGroup(paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneldatapekerjaanLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, paneldatapekerjaanLayout.createSequentialGroup()
                                .addGroup(paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(paneldatapekerjaanLayout.createSequentialGroup()
                                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(45, 45, 45)
                                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(labelKodeSupplier1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelKodeSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelKodeSupplier2, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                    .addComponent(labelKodeSupplier3, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                    .addComponent(labelKodeSupplier4, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                    .addComponent(labelKodeSupplier5, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                    .addComponent(labelKodeSupplier6, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(c2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(c1, javax.swing.GroupLayout.Alignment.TRAILING, 0, 213, Short.MAX_VALUE)
                                    .addComponent(c4, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(c3, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tnamapekerjaan, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(tanggalmasuk, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(c5)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addGroup(paneldatapekerjaanLayout.createSequentialGroup()
                        .addGap(208, 208, 208)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        paneldatapekerjaanLayout.setVerticalGroup(
            paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneldatapekerjaanLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(paneldatapekerjaanLayout.createSequentialGroup()
                        .addGroup(paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelKodeSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tanggalmasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tnamapekerjaan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelKodeSupplier1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelKodeSupplier2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(c1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelKodeSupplier3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(c2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(c3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelKodeSupplier4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelKodeSupplier5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(c4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelKodeSupplier6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(c5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(paneldatapekerjaanLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(paneldatapekerjaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        panelmain.add(paneldatapekerjaan, "card3");

        panelhasil.setBackground(new java.awt.Color(0, 153, 153));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("HASIL");

        tabelnormalisasi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabelnormalisasi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"
            }
        ));
        tabelnormalisasi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabelnormalisasi.setRowHeight(30);
        tabelnormalisasi.setRowMargin(2);
        tabelnormalisasi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelnormalisasiMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabelnormalisasi);

        tabelhasil.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabelhasil.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"
            }
        ));
        tabelhasil.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabelhasil.setRowHeight(30);
        tabelhasil.setRowMargin(2);
        tabelhasil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelhasilMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tabelhasil);

        btnCetak.setBackground(new java.awt.Color(204, 204, 204));
        btnCetak.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnCetak.setText("CETAK");
        btnCetak.setContentAreaFilled(false);
        btnCetak.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCetak.setIconTextGap(0);
        btnCetak.setOpaque(true);
        btnCetak.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCetakMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCetakMouseExited(evt);
            }
        });
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        btnProses.setBackground(new java.awt.Color(204, 204, 204));
        btnProses.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnProses.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar2/hasil-transformed.png"))); // NOI18N
        btnProses.setText("PROSES");
        btnProses.setContentAreaFilled(false);
        btnProses.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProses.setIconTextGap(0);
        btnProses.setOpaque(true);
        btnProses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnProsesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnProsesMouseExited(evt);
            }
        });
        btnProses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProsesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelhasilLayout = new javax.swing.GroupLayout(panelhasil);
        panelhasil.setLayout(panelhasilLayout);
        panelhasilLayout.setHorizontalGroup(
            panelhasilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelhasilLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelhasilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 751, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelhasilLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79)
                        .addComponent(btnCetak, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(panelhasilLayout.createSequentialGroup()
                .addGap(322, 322, 322)
                .addComponent(btnProses, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelhasilLayout.setVerticalGroup(
            panelhasilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelhasilLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelhasilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(btnCetak, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnProses, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelmain.add(panelhasil, "card4");

        panelkriteria.setBackground(new java.awt.Color(0, 153, 153));

        labelKodeSupplier7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelKodeSupplier7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelKodeSupplier7.setText("ID");
        labelKodeSupplier7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        labelKodeSupplier8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelKodeSupplier8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelKodeSupplier8.setText("KRITERIA");
        labelKodeSupplier8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        labelKodeSupplier9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelKodeSupplier9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelKodeSupplier9.setText("KETERANGAN");
        labelKodeSupplier9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        labelKodeSupplier10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelKodeSupplier10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelKodeSupplier10.setText("NILAI");
        labelKodeSupplier10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btnSimpan1.setBackground(new java.awt.Color(204, 204, 204));
        btnSimpan1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSimpan1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar2/save-30-w11.png"))); // NOI18N
        btnSimpan1.setText("Simpan");
        btnSimpan1.setContentAreaFilled(false);
        btnSimpan1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan1.setIconTextGap(0);
        btnSimpan1.setOpaque(true);
        btnSimpan1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSimpan1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSimpan1MouseExited(evt);
            }
        });
        btnSimpan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpan1ActionPerformed(evt);
            }
        });

        btnHapus1.setBackground(new java.awt.Color(204, 204, 204));
        btnHapus1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnHapus1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar2/delete-30-w11.png"))); // NOI18N
        btnHapus1.setText("Hapus");
        btnHapus1.setContentAreaFilled(false);
        btnHapus1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapus1.setIconTextGap(0);
        btnHapus1.setOpaque(true);
        btnHapus1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHapus1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHapus1MouseExited(evt);
            }
        });
        btnHapus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapus1ActionPerformed(evt);
            }
        });

        tabelkriteria.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabelkriteria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"
            }
        ));
        tabelkriteria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabelkriteria.setRowHeight(30);
        tabelkriteria.setRowMargin(2);
        tabelkriteria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelkriteriaMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tabelkriteria);

        kriteria.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        kriteria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "fasilitas", "ruangan" }));

        nilai.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        nilai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));

        javax.swing.GroupLayout panelkriteriaLayout = new javax.swing.GroupLayout(panelkriteria);
        panelkriteria.setLayout(panelkriteriaLayout);
        panelkriteriaLayout.setHorizontalGroup(
            panelkriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelkriteriaLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addGroup(panelkriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSimpan1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelkriteriaLayout.createSequentialGroup()
                        .addGroup(panelkriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelKodeSupplier8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelKodeSupplier7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelKodeSupplier9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelKodeSupplier10, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelkriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnHapus1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelkriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(keterangan, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(idkriteria, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kriteria, javax.swing.GroupLayout.Alignment.LEADING, 0, 200, Short.MAX_VALUE)
                                .addComponent(nilai, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelkriteriaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 751, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelkriteriaLayout.setVerticalGroup(
            panelkriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelkriteriaLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(panelkriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelKodeSupplier7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idkriteria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelkriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelKodeSupplier8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kriteria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelkriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelKodeSupplier9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(keterangan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(panelkriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelKodeSupplier10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nilai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(panelkriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapus1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        panelmain.add(panelkriteria, "card5");

        javax.swing.GroupLayout panelutamaLayout = new javax.swing.GroupLayout(panelutama);
        panelutama.setLayout(panelutamaLayout);
        panelutamaLayout.setHorizontalGroup(
            panelutamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelutamaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelmenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelmain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelutamaLayout.setVerticalGroup(
            panelutamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelutamaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelutamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelmain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelmenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelutama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelutama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void bhomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bhomeActionPerformed
        //remove panel
        panelmain.removeAll();
        panelmain.repaint();
        panelmain.revalidate();
        //add panel
        panelmain.add(panelkriteria);
        panelmain.repaint();
        panelmain.revalidate();
    }//GEN-LAST:event_bhomeActionPerformed

    private void bpekerjaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bpekerjaanActionPerformed
        //remove panel
        panelmain.removeAll();
        panelmain.repaint();
        panelmain.revalidate();
        //add panel
        panelmain.add(paneldatapekerjaan);
        panelmain.repaint();
        panelmain.revalidate();
    }//GEN-LAST:event_bpekerjaanActionPerformed

    private void bhasilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bhasilActionPerformed
        ModelAlternatifSAW.deleteAllRowSAW();
        panelmain.removeAll();
        panelmain.repaint();
        panelmain.revalidate();
        //add panel
        datatable4();
        datatable5();
        panelmain.add(panelhasil);
        panelmain.repaint();
        panelmain.revalidate();
    }//GEN-LAST:event_bhasilActionPerformed

    private void bhomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bhomeMouseEntered
        bhome.setBackground(new Color(0,204,51));
    }//GEN-LAST:event_bhomeMouseEntered

    private void bhomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bhomeMouseExited
        bhome.setBackground(new Color(0,204,102));
    }//GEN-LAST:event_bhomeMouseExited

    private void bpekerjaanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bpekerjaanMouseEntered
        bpekerjaan.setBackground(new Color(0,204,51));
    }//GEN-LAST:event_bpekerjaanMouseEntered

    private void bpekerjaanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bpekerjaanMouseExited
        bpekerjaan.setBackground(new Color(0,204,102));
    }//GEN-LAST:event_bpekerjaanMouseExited

    private void bhasilMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bhasilMouseEntered
        bhasil.setBackground(new Color(0,204,51));
    }//GEN-LAST:event_bhasilMouseEntered

    private void bhasilMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bhasilMouseExited
        bhasil.setBackground(new Color(0,204,102));
    }//GEN-LAST:event_bhasilMouseExited

    private void btentangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btentangMouseEntered
        btentang.setBackground(new Color(0,204,51));
    }//GEN-LAST:event_btentangMouseEntered

    private void btentangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btentangMouseExited
        btentang.setBackground(new Color(0,204,102));
    }//GEN-LAST:event_btentangMouseExited

    private void tabelalternatif1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelalternatif1MouseClicked
        int bar = tabelalternatif1.getSelectedRow();
        String a = tabmode1.getValueAt (bar, 0).toString();
        String b = tabmode1.getValueAt (bar, 1).toString();
        String c = tabmode1.getValueAt (bar, 2).toString();
        String d = tabmode1.getValueAt (bar, 3).toString();
        String e = tabmode1.getValueAt (bar, 4).toString();
        String f = tabmode1.getValueAt (bar, 5).toString();
        String g = tabmode1.getValueAt (bar, 6).toString();
        String h = tabmode1.getValueAt (bar, 7).toString();
        
        
        tnamapekerjaan.setText(c);
        c1.setSelectedItem(d);
        c2.setSelectedItem(e);
        c3.setSelectedItem(f);
        c4.setSelectedItem(g);
        c5.setText(h);
        try{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date tanggal = dateFormat.parse(b);
        tanggalmasuk.setDate(tanggal);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Format tanggal salah: " + ex.getMessage());
        }
    }//GEN-LAST:event_tabelalternatif1MouseClicked

    private void tnamapekerjaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tnamapekerjaanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tnamapekerjaanActionPerformed

    private void tabelalternatif2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelalternatif2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelalternatif2MouseClicked

    private void btnSimpanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMouseEntered
        btnSimpan.setBackground(new Color(0,0,204));
        btnSimpan.setForeground(Color.white);
    }//GEN-LAST:event_btnSimpanMouseEntered

    private void btnSimpanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMouseExited
        btnSimpan.setBackground(new Color(204,204,204));
        btnSimpan.setForeground(Color.black);
    }//GEN-LAST:event_btnSimpanMouseExited

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        try{
            String tampilan = "yyyy-MM-dd";
            SimpleDateFormat fm = new SimpleDateFormat(tampilan);
            String tanggal = String.valueOf(fm.format(tanggalmasuk.getDate()));
            String sql1 = "insert into tabelalternatif1 values (?,?,?,?,?,?,?,?)";
                PreparedStatement stat = conn.prepareStatement(sql1);
                stat.setString(1, null);
                stat.setString(2, tanggal);
                stat.setString(3, tnamapekerjaan.getText());
                stat.setString(4, c1.getSelectedItem().toString());
                stat.setString(5, c2.getSelectedItem().toString());           
                stat.setString(6, c3.getSelectedItem().toString());
                stat.setString(7, c4.getSelectedItem().toString());
                stat.setString(8, c5.getText());

                stat.executeUpdate();
                
                String sql2 = "insert into tabelalternatif2 values (?,?,?,?,?,?,?,?)";
                
                String pilihc1;
                double c1Double;
                pilihc1 = String.valueOf(c1.getSelectedItem());
                c1Double = nilaiFasilitas(String.valueOf(c1.getSelectedItem()));
                
//                if (pilihc1.equals ("Listrik & Penerangan")) {
//                    c1Double = 5;
//                }else if (pilihc1.equals ("Sistem Plumbing")){
//                    c1Double = 4;
//                }else if (pilihc1.equals ("Sistem Keamanan")){
//                    c1Double = 3;
//                }else if (pilihc1.equals ("Struktural Gedung")){
//                    c1Double = 2;
//                }else if (pilihc1.equals ("Sistem HVAC")){
//                    c1Double = 1;
//                }else {
//                    c1Double = 0;
//                }
//                
                String pilihc2;
                double c2Double;
                pilihc2 = String.valueOf(c2.getSelectedItem());
                c2Double = nilaiFasilitas(String.valueOf(c2.getSelectedItem()));
//               if (pilihc2.equals ("R.PIMPINAN")) {
//                    c2Double = 5;
//                }else if (pilihc2.equals ("R.KARYAWAN")){
//                    c2Double = 4;
//                }else if (pilihc2.equals ("R.DOSEN")){
//                    c2Double = 3;
//                }else if (pilihc2.equals ("LABORATORIUM")){
//                    c2Double = 2;
//                }else if (pilihc2.equals ("R.KELAS")){
//                    c2Double = 1;
//                }else {
//                    c2Double = 0;
//                }
                
                String pilihc3;
                double c3Double;
                pilihc3 = String.valueOf(c3.getSelectedItem());
                
                if (pilihc3.equals ("Sangat Aman")) {
                    c3Double = 5;
                }else if (pilihc3.equals ("Aman")){
                    c3Double = 4;
                }else if (pilihc3.equals ("Cukup")){
                    c3Double = 3;
                }else if (pilihc3.equals ("Berbahaya")){
                    c3Double = 2;
                }else if (pilihc3.equals ("Sangat Berbahaya")){
                    c3Double = 1;
                }else {
                    c3Double = 0;
                }
                
                String pilihc4;
                double c4Double;
                pilihc4 = String.valueOf(c4.getSelectedItem());
                
                if (pilihc4.equals ("Sangat Penting")) {
                    c4Double = 5;
                }else if (pilihc4.equals ("Penting")){
                    c4Double = 4;
                }else if (pilihc4.equals ("Cukup")){
                    c4Double = 3;
                }else if (pilihc4.equals ("Kurang Penting")){
                    c4Double = 2;
                }else if (pilihc4.equals ("Tidak Penting")){
                    c4Double = 1;
                }else {
                    c4Double = 0;
                }
                
                String nilai = c5.getText();

                int c5Double;
                int nilaikriteria = Integer.parseInt(nilai); // Mengubah nilai dari String ke tipe data int

                if (nilaikriteria >= 0 && nilaikriteria < 200000) {
                    c5Double = 5;
                } else if (nilaikriteria >= 200000 && nilaikriteria < 400000) {
                    c5Double = 4;
                } else if (nilaikriteria >= 400000 && nilaikriteria < 600000) {
                    c5Double = 3;
                } else if (nilaikriteria >= 600000 && nilaikriteria < 1000000) {
                    c5Double = 2;
                } else if (nilaikriteria >= 1000000) {
                    c5Double = 1;
                } else {
                    c5Double = 0; // Nilai default atau sesuai dengan kebutuhan Anda
                }
            
                PreparedStatement stats = conn.prepareStatement(sql2);
                stats.setString(1, null);
                stats.setString(2, tanggal);
                stats.setString(3, tnamapekerjaan.getText());
                stats.setString(4, String.valueOf(c1Double));
                stats.setString(5, String.valueOf(c2Double));           
                stats.setString(6, String.valueOf(c3Double));
                stats.setString(7, String.valueOf(c4Double));
                stats.setString(8, String.valueOf(c5Double));

                stats.executeUpdate();
                
                JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
                kosong();
                tnamapekerjaan.requestFocus();
                datatable1();
                datatable2();
                datatable3();
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Data Gagal Disimpan"+e);
            System.err.println(e.getMessage());
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnUbahMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUbahMouseEntered
        btnUbah.setBackground(new Color(0,0,204));
        btnUbah.setForeground(Color.white);
    }//GEN-LAST:event_btnUbahMouseEntered

    private void btnUbahMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUbahMouseExited
        btnUbah.setBackground(new Color(204,204,204));
        btnUbah.setForeground(Color.black);
    }//GEN-LAST:event_btnUbahMouseExited

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        String tampilan = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat(tampilan);
        String tanggal  = String.valueOf(fm.format(tanggalmasuk.getDate()));
        try{
            String sql = "UPDATE tabelalternatif1 SET tanggalmasuk=?, c1=?, c2=?, c3=?, c4=?, c5=? WHERE namapekerjaan = ?";
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, tanggal);
            stat.setString(2, c1.getSelectedItem().toString());
            stat.setString(3, c2.getSelectedItem().toString());
            stat.setString(4, c3.getSelectedItem().toString());
            stat.setString(5, c4.getSelectedItem().toString());
            stat.setString(6, c5.getText());
            stat.setString(7, tnamapekerjaan.getText());
            
            String sql2 = "UPDATE tabelalternatif2 SET tanggalmasuk=?, c1=?, c2=?, c3 =?, c4=?, c5=? WHERE namapekerjaan = ?";
            PreparedStatement stats = conn.prepareStatement(sql2);
            
            String pilihc1;
                double c1Double;
                pilihc1 = String.valueOf(c1.getSelectedItem());
                c1Double = nilaiFasilitas(String.valueOf(c1.getSelectedItem()));
//                if (pilihc1.equals ("Listrik & Penerangan")) {
//                    c1Double = 5;
//                }else if (pilihc1.equals ("Sistem Plumbing")){
//                    c1Double = 4;
//                }else if (pilihc1.equals ("Sistem Keamanan")){
//                    c1Double = 3;
//                }else if (pilihc1.equals ("Struktural Gedung")){
//                    c1Double = 2;
//                }else if (pilihc1.equals ("Sistem HVAC")){
//                    c1Double = 1;
//                }else {
//                    c1Double = 0;
//                }
                
                String pilihc2;
                double c2Double;
                pilihc2 = String.valueOf(c2.getSelectedItem());
                c2Double = nilaiFasilitas(String.valueOf(c2.getSelectedItem()));
//                if (pilihc2.equals ("R.PIMPINAN")) {
//                    c2Double = 5;
//                }else if (pilihc2.equals ("R.KARYAWAN")){
//                    c2Double = 4;
//                }else if (pilihc2.equals ("R.DOSEN")){
//                    c2Double = 3;
//                }else if (pilihc2.equals ("LABORATORIUM")){
//                    c2Double = 2;
//                }else if (pilihc2.equals ("R.KELAS")){
//                    c2Double = 1;
//                }else {
//                    c2Double = 0;
//                }
                
                String pilihc3;
                double c3Double;
                pilihc3 = String.valueOf(c3.getSelectedItem());
                
                if (pilihc3.equals ("Sangat Aman")) {
                    c3Double = 5;
                }else if (pilihc3.equals ("Aman")){
                    c3Double = 4;
                }else if (pilihc3.equals ("Cukup")){
                    c3Double = 3;
                }else if (pilihc3.equals ("Berbahaya")){
                    c3Double = 2;
                }else if (pilihc3.equals ("Sangat Berbahaya")){
                    c3Double = 1;
                }else {
                    c3Double = 0;
                }
                
                String pilihc4;
                double c4Double;
                pilihc4 = String.valueOf(c4.getSelectedItem());
                
                if (pilihc4.equals ("Sangat Penting")) {
                    c4Double = 5;
                }else if (pilihc4.equals ("Penting")){
                    c4Double = 4;
                }else if (pilihc4.equals ("Cukup")){
                    c4Double = 3;
                }else if (pilihc4.equals ("Kurang Penting")){
                    c4Double = 2;
                }else if (pilihc4.equals ("Tidak Penting")){
                    c4Double = 1;
                }else {
                    c4Double = 0;
                }
                
                String nilai = c5.getText();

                int c5Double;
                int nilaikriteria = Integer.parseInt(nilai); // Mengubah nilai dari String ke tipe data int

                if (nilaikriteria >= 0 && nilaikriteria < 200000) {
                    c5Double = 5;
                } else if (nilaikriteria >= 200000 && nilaikriteria < 400000) {
                    c5Double = 4;
                } else if (nilaikriteria >= 400000 && nilaikriteria < 600000) {
                    c5Double = 3;
                } else if (nilaikriteria >= 600000 && nilaikriteria < 1000000) {
                    c5Double = 2;
                } else if (nilaikriteria >= 1000000) {
                    c5Double = 1;
                } else {
                    c5Double = 0; // Nilai default atau sesuai dengan kebutuhan Anda
                }
            
            stats.setString(1, tanggal);
            stats.setString(2, String.valueOf(c1Double));
            stats.setString(3, String.valueOf(c2Double));
            stats.setString(4, String.valueOf(c3Double));
            stats.setString(5, String.valueOf(c4Double));
            stats.setString(6, String.valueOf(c5Double));
            stats.setString(7, tnamapekerjaan.getText());
            
            stat.executeUpdate();
            stats.executeUpdate();
            JOptionPane.showMessageDialog(null,"Data Berhasil diubah");
            kosong();
            tnamapekerjaan.requestFocus();
            datatable1();
            datatable2();
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah"+e);
        }
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHapusMouseEntered
        btnHapus.setBackground(new Color(0,0,204));
        btnHapus.setForeground(Color.white);
    }//GEN-LAST:event_btnHapusMouseEntered

    private void btnHapusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHapusMouseExited
        btnHapus.setBackground(new Color(204,204,204));
        btnHapus.setForeground(Color.black);
    }//GEN-LAST:event_btnHapusMouseExited

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        int ok = JOptionPane.showConfirmDialog(null,"Hapus","Konfirmasi Dialog", JOptionPane.YES_NO_OPTION);
        if (ok==0){
            String sql = "DELETE from tabelalternatif1 WHERE namapekerjaan = '"+tnamapekerjaan.getText()+"' ";
            String sql2 = "DELETE from tabelalternatif2 WHERE namapekerjaan = '"+tnamapekerjaan.getText()+"' ";
            try {
                PreparedStatement stat = conn.prepareStatement(sql);
                PreparedStatement stats = conn.prepareStatement(sql2);
                stat.executeUpdate();
                stats.executeUpdate();
                
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
                kosong();
                tnamapekerjaan.requestFocus();
                datatable1();
                datatable2();
                datatable3();
        } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Data gagal dihapus"+e);
            }
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void tabelnormalisasiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelnormalisasiMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelnormalisasiMouseClicked

    private void tabelhasilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelhasilMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelhasilMouseClicked

    private void btnCetakMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCetakMouseEntered
        btnCetak.setBackground(new Color(0,0,204));
        btnCetak.setForeground(Color.white);
    }//GEN-LAST:event_btnCetakMouseEntered

    private void btnCetakMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCetakMouseExited
        btnCetak.setBackground(new Color(204,204,204));
        btnCetak.setForeground(Color.black);
    }//GEN-LAST:event_btnCetakMouseExited

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
         //TODO add your handling code here:
        try {
            String namaFile = "src/Report/report1.jasper";
            Connection conn = new koneksi().connect();
            HashMap parameter = new HashMap();
            File report_file = new File(namaFile);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(report_file.getPath());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, conn);
            JasperViewer.viewReport(jasperPrint, false); //coba
            JasperViewer.setDefaultLookAndFeelDecorated(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }//GEN-LAST:event_btnCetakActionPerformed

    private void btnProsesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProsesMouseEntered
        btnProses.setBackground(new Color(0,0,204));
        btnProses.setForeground(Color.white);
    }//GEN-LAST:event_btnProsesMouseEntered

    private void btnProsesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProsesMouseExited
        btnProses.setBackground(new Color(204,204,204));
        btnProses.setForeground(Color.black);
    }//GEN-LAST:event_btnProsesMouseExited

    private void btnProsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProsesActionPerformed
        if(buttonSawClicked == 0) {
            insertSAW();
            datatable4();
            buttonSawClicked++;
        } else {
            System.err.println("Tidak bisa menghitung lebih dari 1 kali");
            JOptionPane.showMessageDialog(null, "Tidak bisa menghitung kebih dari 1 kali");
        }
    }//GEN-LAST:event_btnProsesActionPerformed

    private void btnClearMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearMouseEntered
        btnClear.setBackground(new Color(0,0,204));
        btnClear.setForeground(Color.white);
    }//GEN-LAST:event_btnClearMouseEntered

    private void btnClearMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearMouseExited
        btnClear.setBackground(new Color(204,204,204));
        btnClear.setForeground(Color.black);
    }//GEN-LAST:event_btnClearMouseExited

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        kosong();
    }//GEN-LAST:event_btnClearActionPerformed

    private void tabelalternatif1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelalternatif1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelalternatif1MouseEntered

    private void btentangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btentangActionPerformed
        tentang t = new tentang();
        t.setVisible(true);
    }//GEN-LAST:event_btentangActionPerformed

    private void c1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c1ActionPerformed

    private void c2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c2ActionPerformed

    private void c5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c5KeyTyped
        // TODO add your handling code here:
        char nomoraja = evt.getKeyChar();
        if (!(Character.isDigit(nomoraja) || nomoraja == KeyEvent.VK_BACK_SPACE || nomoraja == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_c5KeyTyped

    private void btnSimpan1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpan1MouseEntered
        btnSimpan1.setBackground(new Color(0,0,204));
        btnSimpan1.setForeground(Color.white);
    }//GEN-LAST:event_btnSimpan1MouseEntered

    private void btnSimpan1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpan1MouseExited
        btnSimpan1.setBackground(new Color(204,204,204));
        btnSimpan1.setForeground(Color.black);
    }//GEN-LAST:event_btnSimpan1MouseExited

    private void btnSimpan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpan1ActionPerformed
        String sql = "insert into tabelkriteria values (?,?,?,?)";
        try{
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, idkriteria.getText());
            stat.setString(2, kriteria.getSelectedItem().toString());
            stat.setString(3, keterangan.getText());
            stat.setString(4, nilai.getSelectedItem().toString());
            
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
            kosongkriteria();
            idkriteria.requestFocus();
            datatable6();
            fasilitas();
            ruangan();
            
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Data Gagal Disimpan"+e);
        }
    }//GEN-LAST:event_btnSimpan1ActionPerformed

    private void btnHapus1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHapus1MouseEntered
        btnHapus1.setBackground(new Color(0,0,204));
        btnHapus1.setForeground(Color.white);
    }//GEN-LAST:event_btnHapus1MouseEntered

    private void btnHapus1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHapus1MouseExited
        btnHapus1.setBackground(new Color(204,204,204));
        btnHapus1.setForeground(Color.black);
    }//GEN-LAST:event_btnHapus1MouseExited

    private void btnHapus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapus1ActionPerformed
        int ok = JOptionPane.showConfirmDialog(null,"hapus","Konfirmasi Dialog", JOptionPane.YES_NO_CANCEL_OPTION);
            if (ok==0){
                String sql="delete from tabelkriteria where id='"+idkriteria.getText()+"'";
                try {
                    PreparedStatement stat = conn.prepareStatement(sql);
                    stat.executeUpdate();
                    JOptionPane.showMessageDialog(null, "data berhasil dihapus");
                    kosongkriteria();
                    idkriteria.requestFocus();
                    datatable6();
                    fasilitas();
                    ruangan();
            }catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Data gagal dihapus"+e);
            }
            }
    }//GEN-LAST:event_btnHapus1ActionPerformed

    private void tabelkriteriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelkriteriaMouseClicked
        int bar = tabelkriteria.getSelectedRow();
        String a = tabmode.getValueAt (bar, 0).toString();
        String b = tabmode.getValueAt (bar, 1).toString();
        String c = tabmode.getValueAt (bar, 2).toString();
        String d = tabmode.getValueAt (bar, 3).toString();
        
        idkriteria.setText(a);
        kriteria.setSelectedItem(b);
        keterangan.setText(c);
        nilai.setSelectedItem(d);
    }//GEN-LAST:event_tabelkriteriaMouseClicked

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
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bhasil;
    private javax.swing.JButton bhome;
    private javax.swing.JButton bpekerjaan;
    private javax.swing.JButton btentang;
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnHapus1;
    private javax.swing.JButton btnProses;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnSimpan1;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> c1;
    private javax.swing.JComboBox<String> c2;
    private javax.swing.JComboBox<String> c3;
    private javax.swing.JComboBox<String> c4;
    private javax.swing.JTextField c5;
    private javax.swing.JTextField idkriteria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel jam;
    private javax.swing.JTextField keterangan;
    private javax.swing.JComboBox<String> kriteria;
    private javax.swing.JLabel labelKodeSupplier;
    private javax.swing.JLabel labelKodeSupplier1;
    private javax.swing.JLabel labelKodeSupplier10;
    private javax.swing.JLabel labelKodeSupplier2;
    private javax.swing.JLabel labelKodeSupplier3;
    private javax.swing.JLabel labelKodeSupplier4;
    private javax.swing.JLabel labelKodeSupplier5;
    private javax.swing.JLabel labelKodeSupplier6;
    private javax.swing.JLabel labelKodeSupplier7;
    private javax.swing.JLabel labelKodeSupplier8;
    private javax.swing.JLabel labelKodeSupplier9;
    private javax.swing.JComboBox<String> nilai;
    private javax.swing.JPanel paneldatapekerjaan;
    private javax.swing.JPanel panelhasil;
    private javax.swing.JPanel panelhome;
    private javax.swing.JPanel panelkriteria;
    private javax.swing.JPanel panelmain;
    private javax.swing.JPanel panelmenu;
    private javax.swing.JPanel panelutama;
    private javax.swing.JPanel panelwaktu;
    private javax.swing.JTable tabelalternatif1;
    private javax.swing.JTable tabelalternatif2;
    private javax.swing.JTable tabelhasil;
    private javax.swing.JTable tabelkriteria;
    private javax.swing.JTable tabelnormalisasi;
    private com.toedter.calendar.JDateChooser tanggalmasuk;
    private javax.swing.JLabel tgl;
    private javax.swing.JTextField tnamapekerjaan;
    // End of variables declaration//GEN-END:variables
}
