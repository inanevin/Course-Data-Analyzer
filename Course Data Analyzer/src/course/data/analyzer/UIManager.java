/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

/**
 *
 * @author InanEvin
 */
public class UIManager extends javax.swing.JFrame {

    private Core core;
    private CourseManager courseManager;
    
    /**
     * Creates new form MainFrame
     */
    public UIManager() {
       
        initComponents();
        core = new Core();
        courseManager = core.GetCourseManager();

        SetIcons();
        
    }

    private void SetIcons()
    {
        // Set app ico 
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/appicon.png")));
  
        // Resize Image Icons in the Menu
        
        ImageIcon dashboardIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/dashboard.png")));
        ImageIcon coursesIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/courses.png")));
        ImageIcon examsIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/exam.png")));
        ImageIcon attendanceIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/attendance.png")));
        ImageIcon reportsIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/reports.png")));
        ImageIcon syllabusIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/syllabus.png")));

        // Dashboard
        Image img = dashboardIcon.getImage();
        Image img2 = img.getScaledInstance(DashboardImage.getWidth(), DashboardImage.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon i = new ImageIcon(img2);
        DashboardImage.setIcon(i);
        
        // Courses
        img = coursesIcon.getImage();
        img2 = img.getScaledInstance(CoursesImage.getWidth(), CoursesImage.getHeight(), Image.SCALE_SMOOTH);
        i = new ImageIcon(img2);
        CoursesImage.setIcon(i);
        
        // Syllabus
        img = syllabusIcon.getImage();
        img2 = img.getScaledInstance(SyllabusImage.getWidth(), SyllabusImage.getHeight(), Image.SCALE_SMOOTH);
        i = new ImageIcon(img2);
        SyllabusImage.setIcon(i);
        
        // Exams
        img = examsIcon.getImage();
        img2 = img.getScaledInstance(ExamsImage.getWidth(), ExamsImage.getHeight(), Image.SCALE_SMOOTH);
        i = new ImageIcon(img2);
        ExamsImage.setIcon(i);
        
        // Attendance
        img = attendanceIcon.getImage();
        img2 = img.getScaledInstance(AttendanceImage.getWidth(), AttendanceImage.getHeight(), Image.SCALE_SMOOTH);
        i = new ImageIcon(img2);
        AttendanceImage.setIcon(i);
        
        // Reports
        img = reportsIcon.getImage();
        img2 = img.getScaledInstance(ReportsImage.getWidth(), ReportsImage.getHeight(), Image.SCALE_SMOOTH);
        i = new ImageIcon(img2);
        ReportsImage.setIcon(i);
        
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LeftPanel = new javax.swing.JPanel();
        ImageLabel1 = new javax.swing.JLabel();
        TitleLabel2 = new javax.swing.JLabel();
        TitleLabel1 = new javax.swing.JLabel();
        DashboardImage = new javax.swing.JLabel();
        CoursesImage = new javax.swing.JLabel();
        SyllabusImage = new javax.swing.JLabel();
        ExamsImage = new javax.swing.JLabel();
        AttendanceImage = new javax.swing.JLabel();
        ReportsImage = new javax.swing.JLabel();
        CoursesLabel = new javax.swing.JLabel();
        RightPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tina Analyzer");
        setBackground(new java.awt.Color(30, 30, 32));
        setPreferredSize(new java.awt.Dimension(1024, 768));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        LeftPanel.setBackground(new java.awt.Color(10, 17, 31));
        LeftPanel.setMaximumSize(new java.awt.Dimension(281, 32767));
        LeftPanel.setMinimumSize(new java.awt.Dimension(250, 100));
        LeftPanel.setPreferredSize(new java.awt.Dimension(325, 777));

        ImageLabel1.setBackground(new java.awt.Color(217, 215, 172));
        ImageLabel1.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        ImageLabel1.setForeground(new java.awt.Color(217, 215, 172));
        ImageLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ImageLabel1.setLabelFor(LeftPanel);
        ImageLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        TitleLabel2.setBackground(new java.awt.Color(199, 50, 38));
        TitleLabel2.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 12)); // NOI18N
        TitleLabel2.setForeground(new java.awt.Color(227, 227, 227));
        TitleLabel2.setLabelFor(LeftPanel);
        TitleLabel2.setText("Inan Evin & Alper Ozer");

        TitleLabel1.setBackground(new java.awt.Color(199, 50, 38));
        TitleLabel1.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 24)); // NOI18N
        TitleLabel1.setForeground(new java.awt.Color(227, 227, 227));
        TitleLabel1.setLabelFor(LeftPanel);
        TitleLabel1.setText("Tina Analyzer");

        DashboardImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        DashboardImage.setForeground(new java.awt.Color(255, 255, 255));
        DashboardImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        DashboardImage.setToolTipText("");

        CoursesImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        CoursesImage.setForeground(new java.awt.Color(255, 255, 255));
        CoursesImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        CoursesImage.setToolTipText("");

        SyllabusImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        SyllabusImage.setForeground(new java.awt.Color(255, 255, 255));
        SyllabusImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        SyllabusImage.setToolTipText("");

        ExamsImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ExamsImage.setForeground(new java.awt.Color(255, 255, 255));
        ExamsImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ExamsImage.setToolTipText("");

        AttendanceImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        AttendanceImage.setForeground(new java.awt.Color(255, 255, 255));
        AttendanceImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        AttendanceImage.setToolTipText("");

        ReportsImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ReportsImage.setForeground(new java.awt.Color(255, 255, 255));
        ReportsImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ReportsImage.setToolTipText("");

        CoursesLabel.setBackground(new java.awt.Color(199, 50, 38));
        CoursesLabel.setFont(new java.awt.Font("Monospaced", 0, 28)); // NOI18N
        CoursesLabel.setForeground(new java.awt.Color(227, 227, 227));
        CoursesLabel.setText("Courses");

        javax.swing.GroupLayout LeftPanelLayout = new javax.swing.GroupLayout(LeftPanel);
        LeftPanel.setLayout(LeftPanelLayout);
        LeftPanelLayout.setHorizontalGroup(
            LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LeftPanelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ReportsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ExamsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AttendanceImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SyllabusImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CoursesImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(LeftPanelLayout.createSequentialGroup()
                                .addComponent(DashboardImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(CoursesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(LeftPanelLayout.createSequentialGroup()
                        .addComponent(ImageLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TitleLabel1)
                            .addComponent(TitleLabel2))))
                .addContainerGap(92, Short.MAX_VALUE))
        );
        LeftPanelLayout.setVerticalGroup(
            LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LeftPanelLayout.createSequentialGroup()
                        .addComponent(TitleLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TitleLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ImageLabel1))
                .addGroup(LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LeftPanelLayout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(DashboardImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(LeftPanelLayout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(CoursesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(39, 39, 39)
                .addComponent(CoursesImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(SyllabusImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(AttendanceImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(ExamsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(ReportsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(166, Short.MAX_VALUE))
        );

        getContentPane().add(LeftPanel);

        RightPanel.setBackground(new java.awt.Color(38, 50, 72));

        javax.swing.GroupLayout RightPanelLayout = new javax.swing.GroupLayout(RightPanel);
        RightPanel.setLayout(RightPanelLayout);
        RightPanelLayout.setHorizontalGroup(
            RightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 739, Short.MAX_VALUE)
        );
        RightPanelLayout.setVerticalGroup(
            RightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 777, Short.MAX_VALUE)
        );

        getContentPane().add(RightPanel);

        getAccessibleContext().setAccessibleName("Tina");

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UIManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UIManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UIManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UIManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UIManager().setVisible(true);
            }
        });
        
     
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AttendanceImage;
    private javax.swing.JLabel CoursesImage;
    private javax.swing.JLabel CoursesLabel;
    private javax.swing.JLabel DashboardImage;
    private javax.swing.JLabel ExamsImage;
    private javax.swing.JLabel ImageLabel1;
    private javax.swing.JPanel LeftPanel;
    private javax.swing.JLabel ReportsImage;
    private javax.swing.JPanel RightPanel;
    private javax.swing.JLabel SyllabusImage;
    private javax.swing.JLabel TitleLabel1;
    private javax.swing.JLabel TitleLabel2;
    // End of variables declaration//GEN-END:variables
}
