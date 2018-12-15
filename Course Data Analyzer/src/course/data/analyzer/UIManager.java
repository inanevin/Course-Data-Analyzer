/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author InanEvin
 */
public class UIManager extends javax.swing.JFrame {

    private Core core;
    private CourseManager courseManager;
    private ArrayList<MenuBarItem> menuBarItems;
    private MenuBarItem currentSelectedMenu;

    /**
     * Creates new form MainFrame
     */
    public UIManager() {

        initComponents();
        core = new Core();
        courseManager = core.GetCourseManager();

        SetIcons();
        SetSelectionImages();
        InitializeMenuBarItems();
        AddLabelListeners();
    }

    private void InitializeMenuBarItems() {
        // Create objects for menu bar items.
        MenuBarItem dashboard = new MenuBarItem(SelectionDashboard, DashboardLabel);
        MenuBarItem courses = new MenuBarItem(SelectionCourses, CoursesLabel);
        MenuBarItem syllabus = new MenuBarItem(SelectionSyllabus, SyllabusLabel);
        MenuBarItem attendance = new MenuBarItem(SelectionAttendance, AttendanceLabel);
        MenuBarItem exams = new MenuBarItem(SelectionExams, ExamsLabel);
        MenuBarItem reports = new MenuBarItem(SelectionReports, ReportsLabel);

        // Init list
        menuBarItems = new ArrayList<MenuBarItem>();

        // Init current selected menu
        currentSelectedMenu = null;

        // Add objets.
        menuBarItems.add(dashboard);
        menuBarItems.add(courses);
        menuBarItems.add(syllabus);
        menuBarItems.add(attendance);
        menuBarItems.add(exams);
        menuBarItems.add(reports);

        // Select dashboard as initial menu.
        SelectMenu(dashboard);
    }

    private void SetIcons() {
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

    void SetSelectionImages() {
        SelectionDashboard.setVisible(false);
        SelectionCourses.setVisible(false);
        SelectionSyllabus.setVisible(false);
        SelectionAttendance.setVisible(false);
        SelectionExams.setVisible(false);
        SelectionReports.setVisible(false);
    }

    void SelectMenu(MenuBarItem menu) {

        // Deselect if current menu is not null.
        if (currentSelectedMenu != null) {
            DeselectMenu(currentSelectedMenu);
        }

        // Select current menu.
        menu.GetSelectionImage().setVisible(true);

        // Set current selected.
        currentSelectedMenu = menu;

    }

    void DeselectMenu(MenuBarItem menu) {
        menu.GetSelectionImage().setVisible(false);
    }

    private void AddLabelListeners() {
        // Declare & init mouse adapter with mouse enter & exit events.
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Color clr = new Color(126,138,162);
                JLabel lbl = (JLabel) e.getSource();
                lbl.setForeground(clr);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Color clr = new Color(227, 227, 227);
                JLabel lbl = (JLabel) e.getSource();
                lbl.setForeground(clr);
            }
        };

        // Add listeners
        DashboardLabel.addMouseListener(ma);
        CoursesLabel.addMouseListener(ma);
        SyllabusLabel.addMouseListener(ma);
        AttendanceLabel.addMouseListener(ma);
        ExamsLabel.addMouseListener(ma);
        ReportsLabel.addMouseListener(ma);

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
        MenuPanel = new javax.swing.JPanel();
        DashboardPanel = new javax.swing.JPanel();
        SelectionDashboard = new javax.swing.JPanel();
        DashboardImage = new javax.swing.JLabel();
        DashboardLabel = new javax.swing.JLabel();
        CoursesPanel = new javax.swing.JPanel();
        SelectionCourses = new javax.swing.JPanel();
        CoursesImage = new javax.swing.JLabel();
        CoursesLabel = new javax.swing.JLabel();
        SyllabusPanel = new javax.swing.JPanel();
        SelectionSyllabus = new javax.swing.JPanel();
        SyllabusImage = new javax.swing.JLabel();
        SyllabusLabel = new javax.swing.JLabel();
        AttendancePanel = new javax.swing.JPanel();
        SelectionAttendance = new javax.swing.JPanel();
        AttendanceImage = new javax.swing.JLabel();
        AttendanceLabel = new javax.swing.JLabel();
        ExamsPanel = new javax.swing.JPanel();
        SelectionExams = new javax.swing.JPanel();
        ExamsImage = new javax.swing.JLabel();
        ExamsLabel = new javax.swing.JLabel();
        ReportsPanel = new javax.swing.JPanel();
        SelectionReports = new javax.swing.JPanel();
        ReportsImage = new javax.swing.JLabel();
        ReportsLabel = new javax.swing.JLabel();
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
        TitleLabel2.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        TitleLabel2.setForeground(new java.awt.Color(227, 227, 227));
        TitleLabel2.setLabelFor(LeftPanel);
        TitleLabel2.setText("Inan Evin & Alper Ozer");

        TitleLabel1.setBackground(new java.awt.Color(199, 50, 38));
        TitleLabel1.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        TitleLabel1.setForeground(new java.awt.Color(227, 227, 227));
        TitleLabel1.setLabelFor(LeftPanel);
        TitleLabel1.setText("Tina Analyzer");

        MenuPanel.setLayout(new javax.swing.BoxLayout(MenuPanel, javax.swing.BoxLayout.Y_AXIS));

        DashboardPanel.setBackground(new java.awt.Color(10, 17, 31));
        DashboardPanel.setPreferredSize(new java.awt.Dimension(286, 100));

        SelectionDashboard.setBackground(new java.awt.Color(96, 194, 30));
        SelectionDashboard.setPreferredSize(new java.awt.Dimension(6, 0));

        javax.swing.GroupLayout SelectionDashboardLayout = new javax.swing.GroupLayout(SelectionDashboard);
        SelectionDashboard.setLayout(SelectionDashboardLayout);
        SelectionDashboardLayout.setHorizontalGroup(
            SelectionDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 6, Short.MAX_VALUE)
        );
        SelectionDashboardLayout.setVerticalGroup(
            SelectionDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        DashboardImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        DashboardImage.setForeground(new java.awt.Color(255, 255, 255));
        DashboardImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/courses.png"))); // NOI18N
        DashboardImage.setToolTipText("");

        DashboardLabel.setBackground(new java.awt.Color(199, 50, 38));
        DashboardLabel.setFont(new java.awt.Font("Monospaced", 0, 28)); // NOI18N
        DashboardLabel.setForeground(new java.awt.Color(227, 227, 227));
        DashboardLabel.setText("Dashboard");
        DashboardLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DashboardLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout DashboardPanelLayout = new javax.swing.GroupLayout(DashboardPanel);
        DashboardPanel.setLayout(DashboardPanelLayout);
        DashboardPanelLayout.setHorizontalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addComponent(SelectionDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(DashboardImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(DashboardLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 51, Short.MAX_VALUE))
        );
        DashboardPanelLayout.setVerticalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SelectionDashboard, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addGroup(DashboardPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(DashboardImage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DashboardLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        MenuPanel.add(DashboardPanel);

        CoursesPanel.setBackground(new java.awt.Color(10, 17, 31));
        CoursesPanel.setPreferredSize(new java.awt.Dimension(286, 100));

        SelectionCourses.setBackground(new java.awt.Color(96, 194, 30));
        SelectionCourses.setPreferredSize(new java.awt.Dimension(6, 0));

        javax.swing.GroupLayout SelectionCoursesLayout = new javax.swing.GroupLayout(SelectionCourses);
        SelectionCourses.setLayout(SelectionCoursesLayout);
        SelectionCoursesLayout.setHorizontalGroup(
            SelectionCoursesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 6, Short.MAX_VALUE)
        );
        SelectionCoursesLayout.setVerticalGroup(
            SelectionCoursesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        CoursesImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        CoursesImage.setForeground(new java.awt.Color(255, 255, 255));
        CoursesImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/courses.png"))); // NOI18N
        CoursesImage.setToolTipText("");

        CoursesLabel.setBackground(new java.awt.Color(199, 50, 38));
        CoursesLabel.setFont(new java.awt.Font("Monospaced", 0, 28)); // NOI18N
        CoursesLabel.setForeground(new java.awt.Color(227, 227, 227));
        CoursesLabel.setText("Courses");
        CoursesLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CoursesLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout CoursesPanelLayout = new javax.swing.GroupLayout(CoursesPanel);
        CoursesPanel.setLayout(CoursesPanelLayout);
        CoursesPanelLayout.setHorizontalGroup(
            CoursesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CoursesPanelLayout.createSequentialGroup()
                .addComponent(SelectionCourses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(CoursesImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(CoursesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 51, Short.MAX_VALUE))
        );
        CoursesPanelLayout.setVerticalGroup(
            CoursesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CoursesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CoursesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SelectionCourses, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addGroup(CoursesPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(CoursesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(CoursesImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CoursesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 16, Short.MAX_VALUE)))
                .addContainerGap())
        );

        MenuPanel.add(CoursesPanel);

        SyllabusPanel.setBackground(new java.awt.Color(10, 17, 31));
        SyllabusPanel.setPreferredSize(new java.awt.Dimension(286, 100));

        SelectionSyllabus.setBackground(new java.awt.Color(96, 194, 30));
        SelectionSyllabus.setPreferredSize(new java.awt.Dimension(6, 0));

        javax.swing.GroupLayout SelectionSyllabusLayout = new javax.swing.GroupLayout(SelectionSyllabus);
        SelectionSyllabus.setLayout(SelectionSyllabusLayout);
        SelectionSyllabusLayout.setHorizontalGroup(
            SelectionSyllabusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 6, Short.MAX_VALUE)
        );
        SelectionSyllabusLayout.setVerticalGroup(
            SelectionSyllabusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        SyllabusImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        SyllabusImage.setForeground(new java.awt.Color(255, 255, 255));
        SyllabusImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/courses.png"))); // NOI18N
        SyllabusImage.setToolTipText("");

        SyllabusLabel.setBackground(new java.awt.Color(199, 50, 38));
        SyllabusLabel.setFont(new java.awt.Font("Monospaced", 0, 28)); // NOI18N
        SyllabusLabel.setForeground(new java.awt.Color(227, 227, 227));
        SyllabusLabel.setText("Sylabus");
        SyllabusLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SyllabusLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout SyllabusPanelLayout = new javax.swing.GroupLayout(SyllabusPanel);
        SyllabusPanel.setLayout(SyllabusPanelLayout);
        SyllabusPanelLayout.setHorizontalGroup(
            SyllabusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SyllabusPanelLayout.createSequentialGroup()
                .addComponent(SelectionSyllabus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SyllabusImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SyllabusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 51, Short.MAX_VALUE))
        );
        SyllabusPanelLayout.setVerticalGroup(
            SyllabusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SyllabusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SyllabusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SelectionSyllabus, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addGroup(SyllabusPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(SyllabusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(SyllabusImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SyllabusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 16, Short.MAX_VALUE)))
                .addContainerGap())
        );

        MenuPanel.add(SyllabusPanel);

        AttendancePanel.setBackground(new java.awt.Color(10, 17, 31));
        AttendancePanel.setPreferredSize(new java.awt.Dimension(286, 100));

        SelectionAttendance.setBackground(new java.awt.Color(96, 194, 30));
        SelectionAttendance.setPreferredSize(new java.awt.Dimension(6, 0));

        javax.swing.GroupLayout SelectionAttendanceLayout = new javax.swing.GroupLayout(SelectionAttendance);
        SelectionAttendance.setLayout(SelectionAttendanceLayout);
        SelectionAttendanceLayout.setHorizontalGroup(
            SelectionAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 6, Short.MAX_VALUE)
        );
        SelectionAttendanceLayout.setVerticalGroup(
            SelectionAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        AttendanceImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        AttendanceImage.setForeground(new java.awt.Color(255, 255, 255));
        AttendanceImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/courses.png"))); // NOI18N
        AttendanceImage.setToolTipText("");

        AttendanceLabel.setBackground(new java.awt.Color(199, 50, 38));
        AttendanceLabel.setFont(new java.awt.Font("Monospaced", 0, 28)); // NOI18N
        AttendanceLabel.setForeground(new java.awt.Color(227, 227, 227));
        AttendanceLabel.setText("Attendance");
        AttendanceLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AttendanceLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout AttendancePanelLayout = new javax.swing.GroupLayout(AttendancePanel);
        AttendancePanel.setLayout(AttendancePanelLayout);
        AttendancePanelLayout.setHorizontalGroup(
            AttendancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AttendancePanelLayout.createSequentialGroup()
                .addComponent(SelectionAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(AttendanceImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(AttendanceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 24, Short.MAX_VALUE))
        );
        AttendancePanelLayout.setVerticalGroup(
            AttendancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AttendancePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AttendancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SelectionAttendance, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addGroup(AttendancePanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(AttendancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(AttendanceImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AttendanceLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 16, Short.MAX_VALUE)))
                .addContainerGap())
        );

        MenuPanel.add(AttendancePanel);

        ExamsPanel.setBackground(new java.awt.Color(10, 17, 31));
        ExamsPanel.setPreferredSize(new java.awt.Dimension(286, 100));

        SelectionExams.setBackground(new java.awt.Color(96, 194, 30));
        SelectionExams.setPreferredSize(new java.awt.Dimension(6, 0));

        javax.swing.GroupLayout SelectionExamsLayout = new javax.swing.GroupLayout(SelectionExams);
        SelectionExams.setLayout(SelectionExamsLayout);
        SelectionExamsLayout.setHorizontalGroup(
            SelectionExamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 6, Short.MAX_VALUE)
        );
        SelectionExamsLayout.setVerticalGroup(
            SelectionExamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        ExamsImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ExamsImage.setForeground(new java.awt.Color(255, 255, 255));
        ExamsImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/courses.png"))); // NOI18N
        ExamsImage.setToolTipText("");

        ExamsLabel.setBackground(new java.awt.Color(199, 50, 38));
        ExamsLabel.setFont(new java.awt.Font("Monospaced", 0, 28)); // NOI18N
        ExamsLabel.setForeground(new java.awt.Color(227, 227, 227));
        ExamsLabel.setText("Exams");
        ExamsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ExamsLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ExamsPanelLayout = new javax.swing.GroupLayout(ExamsPanel);
        ExamsPanel.setLayout(ExamsPanelLayout);
        ExamsPanelLayout.setHorizontalGroup(
            ExamsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ExamsPanelLayout.createSequentialGroup()
                .addComponent(SelectionExams, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ExamsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ExamsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 51, Short.MAX_VALUE))
        );
        ExamsPanelLayout.setVerticalGroup(
            ExamsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ExamsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ExamsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SelectionExams, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addGroup(ExamsPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(ExamsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ExamsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ExamsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 16, Short.MAX_VALUE)))
                .addContainerGap())
        );

        MenuPanel.add(ExamsPanel);

        ReportsPanel.setBackground(new java.awt.Color(10, 17, 31));
        ReportsPanel.setPreferredSize(new java.awt.Dimension(286, 100));

        SelectionReports.setBackground(new java.awt.Color(96, 194, 30));
        SelectionReports.setPreferredSize(new java.awt.Dimension(6, 0));

        javax.swing.GroupLayout SelectionReportsLayout = new javax.swing.GroupLayout(SelectionReports);
        SelectionReports.setLayout(SelectionReportsLayout);
        SelectionReportsLayout.setHorizontalGroup(
            SelectionReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 6, Short.MAX_VALUE)
        );
        SelectionReportsLayout.setVerticalGroup(
            SelectionReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        ReportsImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ReportsImage.setForeground(new java.awt.Color(255, 255, 255));
        ReportsImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/courses.png"))); // NOI18N
        ReportsImage.setToolTipText("");

        ReportsLabel.setBackground(new java.awt.Color(199, 50, 38));
        ReportsLabel.setFont(new java.awt.Font("Monospaced", 0, 28)); // NOI18N
        ReportsLabel.setForeground(new java.awt.Color(227, 227, 227));
        ReportsLabel.setText("Reports");
        ReportsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ReportsLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ReportsPanelLayout = new javax.swing.GroupLayout(ReportsPanel);
        ReportsPanel.setLayout(ReportsPanelLayout);
        ReportsPanelLayout.setHorizontalGroup(
            ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsPanelLayout.createSequentialGroup()
                .addComponent(SelectionReports, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ReportsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ReportsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 51, Short.MAX_VALUE))
        );
        ReportsPanelLayout.setVerticalGroup(
            ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SelectionReports, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addGroup(ReportsPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ReportsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ReportsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 16, Short.MAX_VALUE)))
                .addContainerGap())
        );

        MenuPanel.add(ReportsPanel);

        javax.swing.GroupLayout LeftPanelLayout = new javax.swing.GroupLayout(LeftPanel);
        LeftPanel.setLayout(LeftPanelLayout);
        LeftPanelLayout.setHorizontalGroup(
            LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LeftPanelLayout.createSequentialGroup()
                        .addComponent(MenuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(LeftPanelLayout.createSequentialGroup()
                        .addComponent(ImageLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TitleLabel1)
                            .addComponent(TitleLabel2))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        LeftPanelLayout.setVerticalGroup(
            LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(LeftPanelLayout.createSequentialGroup()
                        .addComponent(TitleLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TitleLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ImageLabel1))
                .addGap(29, 29, 29)
                .addComponent(MenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
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
            .addGap(0, 787, Short.MAX_VALUE)
        );

        getContentPane().add(RightPanel);

        getAccessibleContext().setAccessibleName("Tina");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CoursesLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CoursesLabelMouseClicked
        SelectMenu(menuBarItems.get(1));
    }//GEN-LAST:event_CoursesLabelMouseClicked

    private void SyllabusLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SyllabusLabelMouseClicked
        SelectMenu(menuBarItems.get(2));
    }//GEN-LAST:event_SyllabusLabelMouseClicked

    private void ReportsLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReportsLabelMouseClicked
        SelectMenu(menuBarItems.get(5));
    }//GEN-LAST:event_ReportsLabelMouseClicked

    private void AttendanceLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AttendanceLabelMouseClicked
        SelectMenu(menuBarItems.get(3));
    }//GEN-LAST:event_AttendanceLabelMouseClicked

    private void ExamsLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExamsLabelMouseClicked
        SelectMenu(menuBarItems.get(4));
    }//GEN-LAST:event_ExamsLabelMouseClicked

    private void DashboardLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DashboardLabelMouseClicked
        // TODO add your handling code here:
        SelectMenu(menuBarItems.get(0));
    }//GEN-LAST:event_DashboardLabelMouseClicked

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
    private javax.swing.JLabel AttendanceLabel;
    private javax.swing.JPanel AttendancePanel;
    private javax.swing.JLabel CoursesImage;
    private javax.swing.JLabel CoursesLabel;
    private javax.swing.JPanel CoursesPanel;
    private javax.swing.JLabel DashboardImage;
    private javax.swing.JLabel DashboardLabel;
    private javax.swing.JPanel DashboardPanel;
    private javax.swing.JLabel ExamsImage;
    private javax.swing.JLabel ExamsLabel;
    private javax.swing.JPanel ExamsPanel;
    private javax.swing.JLabel ImageLabel1;
    private javax.swing.JPanel LeftPanel;
    private javax.swing.JPanel MenuPanel;
    private javax.swing.JLabel ReportsImage;
    private javax.swing.JLabel ReportsLabel;
    private javax.swing.JPanel ReportsPanel;
    private javax.swing.JPanel RightPanel;
    private javax.swing.JPanel SelectionAttendance;
    private javax.swing.JPanel SelectionCourses;
    private javax.swing.JPanel SelectionDashboard;
    private javax.swing.JPanel SelectionExams;
    private javax.swing.JPanel SelectionReports;
    private javax.swing.JPanel SelectionSyllabus;
    private javax.swing.JLabel SyllabusImage;
    private javax.swing.JLabel SyllabusLabel;
    private javax.swing.JPanel SyllabusPanel;
    private javax.swing.JLabel TitleLabel1;
    private javax.swing.JLabel TitleLabel2;
    // End of variables declaration//GEN-END:variables
}
