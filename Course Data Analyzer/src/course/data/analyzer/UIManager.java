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
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author InanEvin
 */
public class UIManager extends javax.swing.JFrame {

    private CourseManager courseManager;
    private ResourceManager resourceManager;
    private ArrayList<MenuBarItem> menuBarItems;
    private MenuBarItem currentSelectedMenu;
    private Color selectedMenuItemColor = new Color(36, 152, 249);
    private Color unselectedMenuItemColor = new Color(53, 55, 61);
    private Color unselectedInnerMenuItemColor = new Color(26, 24, 26);

    private ArrayList<CourseAction> courseActions;
    private int i_SelectedCourse = -1;

    /**
     * Creates new form MainFrame
     */
    public UIManager() {

        initComponents();

        courseActions = new ArrayList<CourseAction>();
        resourceManager = new ResourceManager();
        courseManager = new CourseManager(this, resourceManager);
        courseManager.PopulateCourses();
        
        SetIcons();
        InitializeMenuBarItems();
        AddComponentListeners();

    }

    public void UpdateCourseList(ArrayList<Course> cl, int toSelect) {

        DefaultListModel m = new DefaultListModel();

        for (int i = 0; i < cl.size(); i++) {
            String elementDisplay = (new StringBuilder()).append(cl.get(i).GetID()).append(" - ").append(cl.get(i).GetDescription()).toString();
            m.addElement(elementDisplay);
        }

        CoursesList.setModel(m);

        if (CoursesList.getComponentCount() == 0 || CoursesList.getSelectedIndex() == -1) {
            DuplicateCourseButton.setEnabled(false);
            RemoveCourseButton.setEnabled(false);
        }

        CoursesList.setSelectedIndex(toSelect);
        SelectCourse(toSelect);
    }

    // 1 is for adding a new course, 2 is for removing.
    public void SetLastActionForCourses(Course subject, int actionIndex) {

        try {
            CourseAction c = new CourseAction(subject, actionIndex, CoursesList.getSelectedIndex());
            courseActions.add(c);
        } catch (ActionIndexException e) {
            System.out.println(e.getMessage());
            return;
        }

        UndoButton.setEnabled(true);

    }

    private void SelectCourse(int index) {

        i_SelectedCourse = index;
        System.out.println(courseManager);
        String title = "";
        
        if(i_SelectedCourse != -1)
        {
             title = (new StringBuilder().append("Selected Course: ")
                .append(courseManager.GetCourse(index).GetID())
                .append(" - ")
                .append(courseManager.GetCourse(index).GetName())).toString();

        }
       
        DashboardSelectedCourse.setText(title);
        CoursesSelectedCourse.setText(title);
        StudentsSelectedCourse.setText(title);
        SyllabusSelectedCourse.setText(title);
        AttendanceSelectedCourse.setText(title);
        ExamsSelectedCourse.setText(title);
        ReportsSelectedCourse.setText(title);

    }

    private void InitializeMenuBarItems() {
        // Create objects for menu bar items.
        MenuBarItem dashboard = new MenuBarItem(DashboardWrapper, DashboardMainPanel);
        MenuBarItem courses = new MenuBarItem(CoursesWrapper, CoursesMainPanel);
        MenuBarItem students = new MenuBarItem(StudentsWrapper, StudentsMainPanel);
        MenuBarItem syllabus = new MenuBarItem(SyllabusWrapper, SyllabusMainPanel);
        MenuBarItem attendance = new MenuBarItem(AttendanceWrapper, AttendanceMainPanel);
        MenuBarItem exams = new MenuBarItem(ExamsWrapper, ExamsMainPanel);
        MenuBarItem reports = new MenuBarItem(ReportsWrapper, ReportsMainPanel);

        // Init list
        menuBarItems = new ArrayList<MenuBarItem>();

        // Init current selected menu
        currentSelectedMenu = null;

        // Add objets.
        menuBarItems.add(dashboard);
        menuBarItems.add(courses);
        menuBarItems.add(students);
        menuBarItems.add(syllabus);
        menuBarItems.add(attendance);
        menuBarItems.add(exams);
        menuBarItems.add(reports);

        // Deselect all menus.
        DeselectAllMenu();

        // Select dashboard as initial menu.
        SelectMenu(dashboard);
    }

    private void SetIcons() {
        // Set app ico 
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/appicon.png")));

        // Resize Image Icons in the Menu
        ImageIcon dashboardIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/dashboard.png")));
        ImageIcon coursesIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/courses.png")));
        ImageIcon studentsIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/students.png")));
        ImageIcon examsIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/exam.png")));
        ImageIcon attendanceIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/attendance.png")));
        ImageIcon reportsIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/reports.png")));
        ImageIcon syllabusIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/syllabus.png")));
        ImageIcon settingsIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/settings.png")));
        ImageIcon chart1Icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/chart1.png")));
        ImageIcon chart2Icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/chart2.png")));
        ImageIcon importIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/import.png")));
        ImageIcon exportIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/export.png")));

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

        // Students
        img = studentsIcon.getImage();
        img2 = img.getScaledInstance(StudentsImage.getWidth(), StudentsImage.getHeight(), Image.SCALE_SMOOTH);
        i = new ImageIcon(img2);
        StudentsImage.setIcon(i);

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

        // Settings
        img = settingsIcon.getImage();
        img2 = img.getScaledInstance(SettingsImage.getWidth(), SettingsImage.getHeight(), Image.SCALE_SMOOTH);
        i = new ImageIcon(img2);
        SettingsImage.setIcon(i);

        // Chart1 inside main dashboard panel.
        img = chart1Icon.getImage();
        img2 = img.getScaledInstance(ChartImage1.getWidth(), ChartImage1.getHeight(), Image.SCALE_SMOOTH);
        i = new ImageIcon(img2);
        ChartImage1.setIcon(i);

        // Chart2 inside main dashboard panel.
        img = chart2Icon.getImage();
        img2 = img.getScaledInstance(ChartImage2.getWidth(), ChartImage2.getHeight(), Image.SCALE_SMOOTH);
        i = new ImageIcon(img2);
        ChartImage2.setIcon(i);

        // Import icon inside main dashboard panel.
        img = importIcon.getImage();
        img2 = img.getScaledInstance(ImportImage.getWidth(), ImportImage.getHeight(), Image.SCALE_SMOOTH);
        i = new ImageIcon(img2);
        ImportImage.setIcon(i);

        // Import icon inside main dashboard panel.
        img = exportIcon.getImage();
        img2 = img.getScaledInstance(ExportImage.getWidth(), ExportImage.getHeight(), Image.SCALE_SMOOTH);
        i = new ImageIcon(img2);
        ExportImage.setIcon(i);

    }

    private void AddComponentListeners() {

        MouseAdapterForMenu db = new MouseAdapterForMenu(0, this);
        MouseAdapterForMenu cs = new MouseAdapterForMenu(1, this);
        MouseAdapterForMenu st = new MouseAdapterForMenu(2, this);
        MouseAdapterForMenu syl = new MouseAdapterForMenu(3, this);
        MouseAdapterForMenu att = new MouseAdapterForMenu(4, this);
        MouseAdapterForMenu exm = new MouseAdapterForMenu(5, this);
        MouseAdapterForMenu rp = new MouseAdapterForMenu(6, this);

        // Add listeners to labels
        DashboardLabel.addMouseListener(db);
        CoursesLabel.addMouseListener(cs);
        StudentsLabel.addMouseListener(st);
        SyllabusLabel.addMouseListener(syl);
        AttendanceLabel.addMouseListener(att);
        ExamsLabel.addMouseListener(exm);
        ReportsLabel.addMouseListener(rp);

        // Add listeners to wrappers.
        DashboardWrapper.addMouseListener(db);
        CoursesWrapper.addMouseListener(cs);
        StudentsWrapper.addMouseListener(st);
        SyllabusWrapper.addMouseListener(syl);
        AttendanceWrapper.addMouseListener(att);
        ExamsWrapper.addMouseListener(exm);
        ReportsWrapper.addMouseListener(rp);

        // Add listeners to images. (icons)
        DashboardImage.addMouseListener(db);
        CoursesImage.addMouseListener(cs);
        StudentsImage.addMouseListener(st);
        SyllabusImage.addMouseListener(syl);
        AttendanceImage.addMouseListener(att);
        ExamsImage.addMouseListener(exm);
        ReportsImage.addMouseListener(rp);

    }

    public void SelectMenu(int barItemIndex) {
        SelectMenu(menuBarItems.get(barItemIndex));
    }

    public void SelectMenu(MenuBarItem menu) {

        // Deselect if current menu is not null.
        if (currentSelectedMenu != null) {
            DeselectMenu(currentSelectedMenu);
        }

        // Set selectable panel color to selected.
        menu.GetSelectablePanel().setBackground(selectedMenuItemColor);

        // Enable menu's main panel.
        menu.GetMainPanel().setVisible(true);
        menu.GetMainPanel().setEnabled(true);

        // Set current selected.
        currentSelectedMenu = menu;

    }

    private void DeselectAllMenu() {
        for (int i = 0; i < menuBarItems.size(); i++) {
            DeselectMenu(menuBarItems.get(i));
        }
    }

    void DeselectMenu(MenuBarItem menu) {

        // Set selectable panel color to unselected.
        menu.GetSelectablePanel().setBackground(unselectedMenuItemColor);

        // Disable menu's main panel.
        menu.GetMainPanel().setVisible(false);
        menu.GetMainPanel().setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Main = new javax.swing.JPanel();
        Left = new javax.swing.JPanel();
        MenuPanel = new javax.swing.JPanel();
        DashboardPanel = new javax.swing.JPanel();
        DashboardWrapper = new javax.swing.JPanel();
        DashboardImage = new javax.swing.JLabel();
        DashboardLabel = new javax.swing.JLabel();
        CoursesPanel = new javax.swing.JPanel();
        CoursesLabel = new javax.swing.JLabel();
        CoursesWrapper = new javax.swing.JPanel();
        CoursesImage = new javax.swing.JLabel();
        StudentsPanel = new javax.swing.JPanel();
        StudentsLabel = new javax.swing.JLabel();
        StudentsWrapper = new javax.swing.JPanel();
        StudentsImage = new javax.swing.JLabel();
        SyllabusPanel = new javax.swing.JPanel();
        SyllabusLabel = new javax.swing.JLabel();
        SyllabusWrapper = new javax.swing.JPanel();
        SyllabusImage = new javax.swing.JLabel();
        AttendancePanel = new javax.swing.JPanel();
        AttendanceLabel = new javax.swing.JLabel();
        AttendanceWrapper = new javax.swing.JPanel();
        AttendanceImage = new javax.swing.JLabel();
        ExamsPanel = new javax.swing.JPanel();
        ExamsLabel = new javax.swing.JLabel();
        ExamsWrapper = new javax.swing.JPanel();
        ExamsImage = new javax.swing.JLabel();
        ReportsPanel = new javax.swing.JPanel();
        ReportsLabel = new javax.swing.JLabel();
        ReportsWrapper = new javax.swing.JPanel();
        ReportsImage = new javax.swing.JLabel();
        Top = new javax.swing.JPanel();
        TopCenter = new javax.swing.JPanel();
        TopLeft = new javax.swing.JPanel();
        Title = new javax.swing.JLabel();
        SettingsImage = new javax.swing.JLabel();
        TopRight = new javax.swing.JPanel();
        Center = new javax.swing.JPanel();
        CoursesMainPanel = new javax.swing.JPanel();
        CoursesTitlePanel = new javax.swing.JPanel();
        CoursesMainTitle = new javax.swing.JLabel();
        CoursesSelectedCourse = new javax.swing.JLabel();
        CoursesCenterPanel = new keeptoo.KGradientPanel();
        kGradientPanel4 = new keeptoo.KGradientPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        CoursesList = new javax.swing.JList<>();
        kGradientPanel3 = new keeptoo.KGradientPanel();
        DuplicateCourseButton = new javax.swing.JButton();
        AddNewCourseButton = new javax.swing.JButton();
        RemoveCourseButton = new javax.swing.JButton();
        UndoButton = new javax.swing.JButton();
        StudentsMainPanel = new javax.swing.JPanel();
        DBTitlePanel6 = new javax.swing.JPanel();
        DashboardMainTitle6 = new javax.swing.JLabel();
        StudentsSelectedCourse = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        Grid17 = new javax.swing.JPanel();
        ViewAvgSuccess4 = new javax.swing.JButton();
        ChartImage9 = new javax.swing.JLabel();
        AvgSuccessTitle4 = new javax.swing.JLabel();
        Grid18 = new javax.swing.JPanel();
        ViewAvgAttendance4 = new javax.swing.JButton();
        ChartImage10 = new javax.swing.JLabel();
        AvgAttendanceTitle4 = new javax.swing.JLabel();
        Grid19 = new javax.swing.JPanel();
        DBImportBut4 = new javax.swing.JButton();
        ImportImage4 = new javax.swing.JLabel();
        CourseDataTitle8 = new javax.swing.JLabel();
        Grid20 = new javax.swing.JPanel();
        DbExportBut4 = new javax.swing.JButton();
        ExportImage4 = new javax.swing.JLabel();
        CourseDataTitle9 = new javax.swing.JLabel();
        ExamsMainPanel = new javax.swing.JPanel();
        DBTitlePanel4 = new javax.swing.JPanel();
        DashboardMainTitle4 = new javax.swing.JLabel();
        ExamsSelectedCourse = new javax.swing.JLabel();
        kGradientPanel2 = new keeptoo.KGradientPanel();
        ReportsMainPanel = new javax.swing.JPanel();
        DBTitlePanel5 = new javax.swing.JPanel();
        DashboardMainTitle5 = new javax.swing.JLabel();
        ReportsSelectedCourse = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        Grid21 = new javax.swing.JPanel();
        ViewAvgSuccess5 = new javax.swing.JButton();
        ChartImage11 = new javax.swing.JLabel();
        AvgSuccessTitle5 = new javax.swing.JLabel();
        Grid22 = new javax.swing.JPanel();
        ViewAvgAttendance5 = new javax.swing.JButton();
        ChartImage12 = new javax.swing.JLabel();
        AvgAttendanceTitle5 = new javax.swing.JLabel();
        Grid23 = new javax.swing.JPanel();
        DBImportBut5 = new javax.swing.JButton();
        ImportImage5 = new javax.swing.JLabel();
        CourseDataTitle10 = new javax.swing.JLabel();
        Grid24 = new javax.swing.JPanel();
        DbExportBut5 = new javax.swing.JButton();
        ExportImage5 = new javax.swing.JLabel();
        CourseDataTitle11 = new javax.swing.JLabel();
        AttendanceMainPanel = new javax.swing.JPanel();
        AttendanceTitlePanel = new javax.swing.JPanel();
        AttendanceMainTitle = new javax.swing.JLabel();
        AttendanceSelectedCourse = new javax.swing.JLabel();
        AttMainPanel = new javax.swing.JPanel();
        Grid13 = new javax.swing.JPanel();
        ViewAvgSuccess3 = new javax.swing.JButton();
        ChartImage7 = new javax.swing.JLabel();
        AvgSuccessTitle3 = new javax.swing.JLabel();
        Grid14 = new javax.swing.JPanel();
        ViewAvgAttendance3 = new javax.swing.JButton();
        ChartImage8 = new javax.swing.JLabel();
        AvgAttendanceTitle3 = new javax.swing.JLabel();
        Grid15 = new javax.swing.JPanel();
        DBImportBut3 = new javax.swing.JButton();
        ImportImage3 = new javax.swing.JLabel();
        CourseDataTitle6 = new javax.swing.JLabel();
        Grid16 = new javax.swing.JPanel();
        DbExportBut3 = new javax.swing.JButton();
        ExportImage3 = new javax.swing.JLabel();
        CourseDataTitle7 = new javax.swing.JLabel();
        SyllabusMainPanel = new javax.swing.JPanel();
        SyllabusTitlePanel = new javax.swing.JPanel();
        SyllabusMainTitle = new javax.swing.JLabel();
        SyllabusSelectedCourse = new javax.swing.JLabel();
        SylMainPanel = new javax.swing.JPanel();
        Grid9 = new javax.swing.JPanel();
        ViewAvgSuccess2 = new javax.swing.JButton();
        ChartImage5 = new javax.swing.JLabel();
        AvgSuccessTitle2 = new javax.swing.JLabel();
        Grid10 = new javax.swing.JPanel();
        ViewAvgAttendance2 = new javax.swing.JButton();
        ChartImage6 = new javax.swing.JLabel();
        AvgAttendanceTitle2 = new javax.swing.JLabel();
        Grid11 = new javax.swing.JPanel();
        DBImportBut2 = new javax.swing.JButton();
        ImportImage2 = new javax.swing.JLabel();
        CourseDataTitle4 = new javax.swing.JLabel();
        Grid12 = new javax.swing.JPanel();
        DbExportBut2 = new javax.swing.JButton();
        ExportImage2 = new javax.swing.JLabel();
        CourseDataTitle5 = new javax.swing.JLabel();
        DashboardMainPanel = new javax.swing.JPanel();
        DBMain = new javax.swing.JPanel();
        DashboardTitlePanel = new keeptoo.KGradientPanel();
        DashboardMainTitle = new javax.swing.JLabel();
        DashboardSelectedCourse = new javax.swing.JLabel();
        DBCenterPanel = new keeptoo.KGradientPanel();
        jPanel2 = new javax.swing.JPanel();
        AvgSuccessTitle = new javax.swing.JLabel();
        ChartImage1 = new javax.swing.JLabel();
        ViewAvgSuccess = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        ViewAvgAttendance = new javax.swing.JButton();
        ChartImage2 = new javax.swing.JLabel();
        AvgAttendanceTitle = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        DBImportBut = new javax.swing.JButton();
        ImportImage = new javax.swing.JLabel();
        CourseDataTitle = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        DbExportBut = new javax.swing.JButton();
        ExportImage = new javax.swing.JLabel();
        CourseDataTitle2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tina Analyzer");
        setBackground(new java.awt.Color(30, 30, 32));
        setPreferredSize(new java.awt.Dimension(1024, 768));

        Main.setBackground(new java.awt.Color(40, 41, 45));
        Main.setPreferredSize(new java.awt.Dimension(800, 600));
        Main.setLayout(new java.awt.BorderLayout());

        Left.setBackground(new java.awt.Color(40, 41, 45));
        Left.setMaximumSize(new java.awt.Dimension(250, 32767));
        Left.setPreferredSize(new java.awt.Dimension(250, 0));

        MenuPanel.setLayout(new javax.swing.BoxLayout(MenuPanel, javax.swing.BoxLayout.Y_AXIS));

        DashboardPanel.setBackground(new java.awt.Color(40, 41, 45));
        DashboardPanel.setPreferredSize(new java.awt.Dimension(286, 70));

        DashboardWrapper.setBackground(new java.awt.Color(53, 55, 61));

        DashboardImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        DashboardImage.setForeground(new java.awt.Color(255, 255, 255));
        DashboardImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/courses.png"))); // NOI18N
        DashboardImage.setToolTipText("");

        javax.swing.GroupLayout DashboardWrapperLayout = new javax.swing.GroupLayout(DashboardWrapper);
        DashboardWrapper.setLayout(DashboardWrapperLayout);
        DashboardWrapperLayout.setHorizontalGroup(
            DashboardWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardWrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DashboardImage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        DashboardWrapperLayout.setVerticalGroup(
            DashboardWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardWrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DashboardImage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        DashboardLabel.setBackground(new java.awt.Color(199, 50, 38));
        DashboardLabel.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        DashboardLabel.setForeground(new java.awt.Color(227, 227, 227));
        DashboardLabel.setText("Dashboard");

        javax.swing.GroupLayout DashboardPanelLayout = new javax.swing.GroupLayout(DashboardPanel);
        DashboardPanel.setLayout(DashboardPanelLayout);
        DashboardPanelLayout.setHorizontalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(DashboardWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(DashboardLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        DashboardPanelLayout.setVerticalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(DashboardWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(DashboardPanelLayout.createSequentialGroup()
                        .addComponent(DashboardLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        MenuPanel.add(DashboardPanel);

        CoursesPanel.setBackground(new java.awt.Color(40, 41, 45));
        CoursesPanel.setPreferredSize(new java.awt.Dimension(286, 70));

        CoursesLabel.setBackground(new java.awt.Color(199, 50, 38));
        CoursesLabel.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        CoursesLabel.setForeground(new java.awt.Color(227, 227, 227));
        CoursesLabel.setText("Courses");

        CoursesWrapper.setBackground(new java.awt.Color(53, 55, 61));

        CoursesImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        CoursesImage.setForeground(new java.awt.Color(255, 255, 255));
        CoursesImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/courses.png"))); // NOI18N
        CoursesImage.setToolTipText("");

        javax.swing.GroupLayout CoursesWrapperLayout = new javax.swing.GroupLayout(CoursesWrapper);
        CoursesWrapper.setLayout(CoursesWrapperLayout);
        CoursesWrapperLayout.setHorizontalGroup(
            CoursesWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CoursesWrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CoursesImage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        CoursesWrapperLayout.setVerticalGroup(
            CoursesWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CoursesWrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CoursesImage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout CoursesPanelLayout = new javax.swing.GroupLayout(CoursesPanel);
        CoursesPanel.setLayout(CoursesPanelLayout);
        CoursesPanelLayout.setHorizontalGroup(
            CoursesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CoursesPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(CoursesWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(CoursesLabel)
                .addContainerGap(62, Short.MAX_VALUE))
        );
        CoursesPanelLayout.setVerticalGroup(
            CoursesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CoursesPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(CoursesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CoursesWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CoursesPanelLayout.createSequentialGroup()
                        .addComponent(CoursesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)))
                .addContainerGap())
        );

        MenuPanel.add(CoursesPanel);

        StudentsPanel.setBackground(new java.awt.Color(40, 41, 45));
        StudentsPanel.setPreferredSize(new java.awt.Dimension(286, 70));

        StudentsLabel.setBackground(new java.awt.Color(199, 50, 38));
        StudentsLabel.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        StudentsLabel.setForeground(new java.awt.Color(227, 227, 227));
        StudentsLabel.setText("Students");

        StudentsWrapper.setBackground(new java.awt.Color(53, 55, 61));

        StudentsImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        StudentsImage.setForeground(new java.awt.Color(255, 255, 255));
        StudentsImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/courses.png"))); // NOI18N
        StudentsImage.setToolTipText("");

        javax.swing.GroupLayout StudentsWrapperLayout = new javax.swing.GroupLayout(StudentsWrapper);
        StudentsWrapper.setLayout(StudentsWrapperLayout);
        StudentsWrapperLayout.setHorizontalGroup(
            StudentsWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentsWrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StudentsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        StudentsWrapperLayout.setVerticalGroup(
            StudentsWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentsWrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StudentsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout StudentsPanelLayout = new javax.swing.GroupLayout(StudentsPanel);
        StudentsPanel.setLayout(StudentsPanelLayout);
        StudentsPanelLayout.setHorizontalGroup(
            StudentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentsPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(StudentsWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(StudentsLabel)
                .addContainerGap(51, Short.MAX_VALUE))
        );
        StudentsPanelLayout.setVerticalGroup(
            StudentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StudentsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(StudentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(StudentsWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StudentsPanelLayout.createSequentialGroup()
                        .addComponent(StudentsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)))
                .addContainerGap())
        );

        MenuPanel.add(StudentsPanel);

        SyllabusPanel.setBackground(new java.awt.Color(40, 41, 45));
        SyllabusPanel.setPreferredSize(new java.awt.Dimension(286, 70));

        SyllabusLabel.setBackground(new java.awt.Color(199, 50, 38));
        SyllabusLabel.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        SyllabusLabel.setForeground(new java.awt.Color(227, 227, 227));
        SyllabusLabel.setText("Syllabus");

        SyllabusWrapper.setBackground(new java.awt.Color(53, 55, 61));

        SyllabusImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        SyllabusImage.setForeground(new java.awt.Color(255, 255, 255));
        SyllabusImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/courses.png"))); // NOI18N
        SyllabusImage.setToolTipText("");

        javax.swing.GroupLayout SyllabusWrapperLayout = new javax.swing.GroupLayout(SyllabusWrapper);
        SyllabusWrapper.setLayout(SyllabusWrapperLayout);
        SyllabusWrapperLayout.setHorizontalGroup(
            SyllabusWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SyllabusWrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SyllabusImage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        SyllabusWrapperLayout.setVerticalGroup(
            SyllabusWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SyllabusWrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SyllabusImage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout SyllabusPanelLayout = new javax.swing.GroupLayout(SyllabusPanel);
        SyllabusPanel.setLayout(SyllabusPanelLayout);
        SyllabusPanelLayout.setHorizontalGroup(
            SyllabusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SyllabusPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(SyllabusWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SyllabusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 32, Short.MAX_VALUE))
        );
        SyllabusPanelLayout.setVerticalGroup(
            SyllabusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SyllabusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SyllabusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SyllabusWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(SyllabusPanelLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(SyllabusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        MenuPanel.add(SyllabusPanel);

        AttendancePanel.setBackground(new java.awt.Color(40, 41, 45));
        AttendancePanel.setPreferredSize(new java.awt.Dimension(286, 70));

        AttendanceLabel.setBackground(new java.awt.Color(19, 23, 31));
        AttendanceLabel.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        AttendanceLabel.setForeground(new java.awt.Color(227, 227, 227));
        AttendanceLabel.setText("Attendance");

        AttendanceWrapper.setBackground(new java.awt.Color(53, 55, 61));

        AttendanceImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        AttendanceImage.setForeground(new java.awt.Color(255, 255, 255));
        AttendanceImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/courses.png"))); // NOI18N
        AttendanceImage.setToolTipText("");

        javax.swing.GroupLayout AttendanceWrapperLayout = new javax.swing.GroupLayout(AttendanceWrapper);
        AttendanceWrapper.setLayout(AttendanceWrapperLayout);
        AttendanceWrapperLayout.setHorizontalGroup(
            AttendanceWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AttendanceWrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AttendanceImage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        AttendanceWrapperLayout.setVerticalGroup(
            AttendanceWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AttendanceWrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AttendanceImage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout AttendancePanelLayout = new javax.swing.GroupLayout(AttendancePanel);
        AttendancePanel.setLayout(AttendancePanelLayout);
        AttendancePanelLayout.setHorizontalGroup(
            AttendancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AttendancePanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(AttendanceWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(AttendanceLabel)
                .addGap(0, 29, Short.MAX_VALUE))
        );
        AttendancePanelLayout.setVerticalGroup(
            AttendancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AttendancePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AttendancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AttendanceWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(AttendancePanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(AttendanceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        MenuPanel.add(AttendancePanel);

        ExamsPanel.setBackground(new java.awt.Color(40, 41, 45));
        ExamsPanel.setPreferredSize(new java.awt.Dimension(286, 70));

        ExamsLabel.setBackground(new java.awt.Color(199, 50, 38));
        ExamsLabel.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        ExamsLabel.setForeground(new java.awt.Color(227, 227, 227));
        ExamsLabel.setText("Exams");

        ExamsWrapper.setBackground(new java.awt.Color(53, 55, 61));

        ExamsImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ExamsImage.setForeground(new java.awt.Color(255, 255, 255));
        ExamsImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/courses.png"))); // NOI18N
        ExamsImage.setToolTipText("");

        javax.swing.GroupLayout ExamsWrapperLayout = new javax.swing.GroupLayout(ExamsWrapper);
        ExamsWrapper.setLayout(ExamsWrapperLayout);
        ExamsWrapperLayout.setHorizontalGroup(
            ExamsWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ExamsWrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ExamsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        ExamsWrapperLayout.setVerticalGroup(
            ExamsWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ExamsWrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ExamsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout ExamsPanelLayout = new javax.swing.GroupLayout(ExamsPanel);
        ExamsPanel.setLayout(ExamsPanelLayout);
        ExamsPanelLayout.setHorizontalGroup(
            ExamsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ExamsPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(ExamsWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ExamsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );
        ExamsPanelLayout.setVerticalGroup(
            ExamsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ExamsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ExamsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ExamsWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ExamsPanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(ExamsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        MenuPanel.add(ExamsPanel);

        ReportsPanel.setBackground(new java.awt.Color(40, 41, 45));
        ReportsPanel.setPreferredSize(new java.awt.Dimension(286, 70));

        ReportsLabel.setBackground(new java.awt.Color(199, 50, 38));
        ReportsLabel.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        ReportsLabel.setForeground(new java.awt.Color(227, 227, 227));
        ReportsLabel.setText("Reports");

        ReportsWrapper.setBackground(new java.awt.Color(53, 55, 61));

        ReportsImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ReportsImage.setForeground(new java.awt.Color(255, 255, 255));
        ReportsImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/courses.png"))); // NOI18N
        ReportsImage.setToolTipText("");

        javax.swing.GroupLayout ReportsWrapperLayout = new javax.swing.GroupLayout(ReportsWrapper);
        ReportsWrapper.setLayout(ReportsWrapperLayout);
        ReportsWrapperLayout.setHorizontalGroup(
            ReportsWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsWrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ReportsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        ReportsWrapperLayout.setVerticalGroup(
            ReportsWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsWrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ReportsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout ReportsPanelLayout = new javax.swing.GroupLayout(ReportsPanel);
        ReportsPanel.setLayout(ReportsPanelLayout);
        ReportsPanelLayout.setHorizontalGroup(
            ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(ReportsWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ReportsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 44, Short.MAX_VALUE))
        );
        ReportsPanelLayout.setVerticalGroup(
            ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ReportsWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ReportsPanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(ReportsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        MenuPanel.add(ReportsPanel);

        javax.swing.GroupLayout LeftLayout = new javax.swing.GroupLayout(Left);
        Left.setLayout(LeftLayout);
        LeftLayout.setHorizontalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftLayout.createSequentialGroup()
                .addComponent(MenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );
        LeftLayout.setVerticalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(MenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(254, Short.MAX_VALUE))
        );

        Main.add(Left, java.awt.BorderLayout.WEST);

        Top.setBackground(new java.awt.Color(40, 41, 45));
        Top.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Top.setPreferredSize(new java.awt.Dimension(1064, 75));
        Top.setLayout(new java.awt.BorderLayout());

        TopCenter.setBackground(new java.awt.Color(40, 41, 45));
        TopCenter.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        TopCenter.setMaximumSize(new java.awt.Dimension(250, 32767));
        TopCenter.setRequestFocusEnabled(false);

        javax.swing.GroupLayout TopCenterLayout = new javax.swing.GroupLayout(TopCenter);
        TopCenter.setLayout(TopCenterLayout);
        TopCenterLayout.setHorizontalGroup(
            TopCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 517, Short.MAX_VALUE)
        );
        TopCenterLayout.setVerticalGroup(
            TopCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 71, Short.MAX_VALUE)
        );

        Top.add(TopCenter, java.awt.BorderLayout.CENTER);

        TopLeft.setBackground(new java.awt.Color(40, 41, 45));
        TopLeft.setMaximumSize(new java.awt.Dimension(250, 32767));
        TopLeft.setPreferredSize(new java.awt.Dimension(250, 73));

        Title.setBackground(new java.awt.Color(217, 203, 158));
        Title.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        Title.setForeground(new java.awt.Color(238, 239, 247));
        Title.setLabelFor(TopLeft);
        Title.setText("Tina Analyzer");
        Title.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        Title.setDoubleBuffered(true);

        SettingsImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/settings.png"))); // NOI18N

        javax.swing.GroupLayout TopLeftLayout = new javax.swing.GroupLayout(TopLeft);
        TopLeft.setLayout(TopLeftLayout);
        TopLeftLayout.setHorizontalGroup(
            TopLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopLeftLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(Title)
                .addGap(18, 18, 18)
                .addComponent(SettingsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        TopLeftLayout.setVerticalGroup(
            TopLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopLeftLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(TopLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Title)
                    .addComponent(SettingsImage, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        Top.add(TopLeft, java.awt.BorderLayout.WEST);

        TopRight.setBackground(new java.awt.Color(40, 41, 45));
        TopRight.setMaximumSize(new java.awt.Dimension(250, 32767));
        TopRight.setPreferredSize(new java.awt.Dimension(250, 73));

        javax.swing.GroupLayout TopRightLayout = new javax.swing.GroupLayout(TopRight);
        TopRight.setLayout(TopRightLayout);
        TopRightLayout.setHorizontalGroup(
            TopRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
        TopRightLayout.setVerticalGroup(
            TopRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 73, Short.MAX_VALUE)
        );

        Top.add(TopRight, java.awt.BorderLayout.EAST);

        Main.add(Top, java.awt.BorderLayout.NORTH);

        Center.setBackground(new java.awt.Color(26, 24, 26));
        Center.setLayout(new javax.swing.OverlayLayout(Center));

        CoursesMainPanel.setBackground(new java.awt.Color(26, 24, 26));
        CoursesMainPanel.setLayout(new java.awt.BorderLayout(5, 25));

        CoursesTitlePanel.setBackground(new java.awt.Color(26, 24, 26));
        CoursesTitlePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        CoursesMainTitle.setBackground(new java.awt.Color(199, 50, 38));
        CoursesMainTitle.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CoursesMainTitle.setForeground(new java.awt.Color(227, 227, 227));
        CoursesMainTitle.setText("Courses");
        CoursesMainTitle.setToolTipText("");

        CoursesSelectedCourse.setBackground(new java.awt.Color(199, 50, 38));
        CoursesSelectedCourse.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CoursesSelectedCourse.setForeground(new java.awt.Color(227, 227, 227));
        CoursesSelectedCourse.setText("Selected Course:");
        CoursesSelectedCourse.setToolTipText("");

        javax.swing.GroupLayout CoursesTitlePanelLayout = new javax.swing.GroupLayout(CoursesTitlePanel);
        CoursesTitlePanel.setLayout(CoursesTitlePanelLayout);
        CoursesTitlePanelLayout.setHorizontalGroup(
            CoursesTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CoursesTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CoursesMainTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 223, Short.MAX_VALUE)
                .addComponent(CoursesSelectedCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        CoursesTitlePanelLayout.setVerticalGroup(
            CoursesTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CoursesTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CoursesTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CoursesMainTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CoursesSelectedCourse))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        CoursesMainPanel.add(CoursesTitlePanel, java.awt.BorderLayout.NORTH);

        CoursesCenterPanel.setkEndColor(new java.awt.Color(26, 24, 26));
        CoursesCenterPanel.setkGradientFocus(100);
        CoursesCenterPanel.setkStartColor(new java.awt.Color(26, 24, 26));
        CoursesCenterPanel.setLayout(new java.awt.BorderLayout(25, 25));

        kGradientPanel4.setkEndColor(new java.awt.Color(26, 24, 26));
        kGradientPanel4.setkStartColor(new java.awt.Color(26, 24, 26));
        kGradientPanel4.setMaximumSize(new java.awt.Dimension(32767, 50));
        kGradientPanel4.setPreferredSize(new java.awt.Dimension(771, 250));

        CoursesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        CoursesList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CoursesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                CoursesListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(CoursesList);

        javax.swing.GroupLayout kGradientPanel4Layout = new javax.swing.GroupLayout(kGradientPanel4);
        kGradientPanel4.setLayout(kGradientPanel4Layout);
        kGradientPanel4Layout.setHorizontalGroup(
            kGradientPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel4Layout.createSequentialGroup()
                .addGap(208, 208, 208)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                .addGap(152, 152, 152))
        );
        kGradientPanel4Layout.setVerticalGroup(
            kGradientPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel4Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        CoursesCenterPanel.add(kGradientPanel4, java.awt.BorderLayout.PAGE_START);

        kGradientPanel3.setkEndColor(new java.awt.Color(26, 24, 26));
        kGradientPanel3.setkStartColor(new java.awt.Color(26, 24, 26));
        kGradientPanel3.setMaximumSize(new java.awt.Dimension(32767, 50));
        kGradientPanel3.setPreferredSize(new java.awt.Dimension(771, 50));
        kGradientPanel3.setLayout(new javax.swing.BoxLayout(kGradientPanel3, javax.swing.BoxLayout.LINE_AXIS));

        DuplicateCourseButton.setText("Duplicate");
        DuplicateCourseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DuplicateCourseButtonActionPerformed(evt);
            }
        });
        kGradientPanel3.add(DuplicateCourseButton);

        AddNewCourseButton.setText("Add New Course");
        kGradientPanel3.add(AddNewCourseButton);

        RemoveCourseButton.setText("Remove");
        RemoveCourseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveCourseButtonActionPerformed(evt);
            }
        });
        kGradientPanel3.add(RemoveCourseButton);

        UndoButton.setText("Undo");
        UndoButton.setEnabled(false);
        UndoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UndoButtonActionPerformed(evt);
            }
        });
        kGradientPanel3.add(UndoButton);

        CoursesCenterPanel.add(kGradientPanel3, java.awt.BorderLayout.CENTER);

        CoursesMainPanel.add(CoursesCenterPanel, java.awt.BorderLayout.CENTER);

        Center.add(CoursesMainPanel);

        StudentsMainPanel.setBackground(new java.awt.Color(26, 24, 26));
        StudentsMainPanel.setLayout(new java.awt.BorderLayout());

        DBTitlePanel6.setBackground(new java.awt.Color(26, 24, 26));
        DBTitlePanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        DashboardMainTitle6.setBackground(new java.awt.Color(199, 50, 38));
        DashboardMainTitle6.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        DashboardMainTitle6.setForeground(new java.awt.Color(227, 227, 227));
        DashboardMainTitle6.setText("Dashboard");
        DashboardMainTitle6.setToolTipText("");

        StudentsSelectedCourse.setBackground(new java.awt.Color(199, 50, 38));
        StudentsSelectedCourse.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        StudentsSelectedCourse.setForeground(new java.awt.Color(227, 227, 227));
        StudentsSelectedCourse.setText("Selected Course:");
        StudentsSelectedCourse.setToolTipText("");

        javax.swing.GroupLayout DBTitlePanel6Layout = new javax.swing.GroupLayout(DBTitlePanel6);
        DBTitlePanel6.setLayout(DBTitlePanel6Layout);
        DBTitlePanel6Layout.setHorizontalGroup(
            DBTitlePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DBTitlePanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DashboardMainTitle6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 193, Short.MAX_VALUE)
                .addComponent(StudentsSelectedCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        DBTitlePanel6Layout.setVerticalGroup(
            DBTitlePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DBTitlePanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DBTitlePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DashboardMainTitle6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(StudentsSelectedCourse))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        StudentsMainPanel.add(DBTitlePanel6, java.awt.BorderLayout.NORTH);

        jPanel5.setBackground(new java.awt.Color(26, 24, 26));
        jPanel5.setPreferredSize(new java.awt.Dimension(100, 200));
        jPanel5.setLayout(new java.awt.GridLayout(3, 2));

        Grid17.setBackground(new java.awt.Color(26, 24, 26));
        Grid17.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid17.setLayout(new java.awt.GridBagLayout());

        ViewAvgSuccess4.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ViewAvgSuccess4.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid17.add(ViewAvgSuccess4, gridBagConstraints);

        ChartImage9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ChartImage9.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid17.add(ChartImage9, gridBagConstraints);

        AvgSuccessTitle4.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        AvgSuccessTitle4.setForeground(new java.awt.Color(227, 227, 227));
        AvgSuccessTitle4.setText("Average Success");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid17.add(AvgSuccessTitle4, gridBagConstraints);

        jPanel5.add(Grid17);

        Grid18.setBackground(new java.awt.Color(26, 24, 26));
        Grid18.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid18.setLayout(new java.awt.GridBagLayout());

        ViewAvgAttendance4.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ViewAvgAttendance4.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid18.add(ViewAvgAttendance4, gridBagConstraints);

        ChartImage10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ChartImage10.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid18.add(ChartImage10, gridBagConstraints);

        AvgAttendanceTitle4.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        AvgAttendanceTitle4.setForeground(new java.awt.Color(227, 227, 227));
        AvgAttendanceTitle4.setText("Average Attendance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid18.add(AvgAttendanceTitle4, gridBagConstraints);

        jPanel5.add(Grid18);

        Grid19.setBackground(new java.awt.Color(26, 24, 26));
        Grid19.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid19.setLayout(new java.awt.GridBagLayout());

        DBImportBut4.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        DBImportBut4.setText("Import");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid19.add(DBImportBut4, gridBagConstraints);

        ImportImage4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ImportImage4.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid19.add(ImportImage4, gridBagConstraints);

        CourseDataTitle8.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CourseDataTitle8.setForeground(new java.awt.Color(227, 227, 227));
        CourseDataTitle8.setText("Course Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid19.add(CourseDataTitle8, gridBagConstraints);

        jPanel5.add(Grid19);

        Grid20.setBackground(new java.awt.Color(26, 24, 26));
        Grid20.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid20.setLayout(new java.awt.GridBagLayout());

        DbExportBut4.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        DbExportBut4.setText("Export");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid20.add(DbExportBut4, gridBagConstraints);

        ExportImage4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ExportImage4.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid20.add(ExportImage4, gridBagConstraints);

        CourseDataTitle9.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CourseDataTitle9.setForeground(new java.awt.Color(227, 227, 227));
        CourseDataTitle9.setText("Course Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid20.add(CourseDataTitle9, gridBagConstraints);

        jPanel5.add(Grid20);

        StudentsMainPanel.add(jPanel5, java.awt.BorderLayout.CENTER);

        Center.add(StudentsMainPanel);

        ExamsMainPanel.setBackground(new java.awt.Color(26, 24, 26));
        ExamsMainPanel.setLayout(new java.awt.BorderLayout());

        DBTitlePanel4.setBackground(new java.awt.Color(26, 24, 26));
        DBTitlePanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        DashboardMainTitle4.setBackground(new java.awt.Color(199, 50, 38));
        DashboardMainTitle4.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        DashboardMainTitle4.setForeground(new java.awt.Color(227, 227, 227));
        DashboardMainTitle4.setText("Dashboard");
        DashboardMainTitle4.setToolTipText("");

        ExamsSelectedCourse.setBackground(new java.awt.Color(199, 50, 38));
        ExamsSelectedCourse.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        ExamsSelectedCourse.setForeground(new java.awt.Color(227, 227, 227));
        ExamsSelectedCourse.setText("Selected Course:");
        ExamsSelectedCourse.setToolTipText("");

        javax.swing.GroupLayout DBTitlePanel4Layout = new javax.swing.GroupLayout(DBTitlePanel4);
        DBTitlePanel4.setLayout(DBTitlePanel4Layout);
        DBTitlePanel4Layout.setHorizontalGroup(
            DBTitlePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DBTitlePanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DashboardMainTitle4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 193, Short.MAX_VALUE)
                .addComponent(ExamsSelectedCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        DBTitlePanel4Layout.setVerticalGroup(
            DBTitlePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DBTitlePanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DBTitlePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DashboardMainTitle4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExamsSelectedCourse))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ExamsMainPanel.add(DBTitlePanel4, java.awt.BorderLayout.NORTH);

        kGradientPanel2.setkEndColor(new java.awt.Color(0, 0, 0));
        kGradientPanel2.setkStartColor(new java.awt.Color(0, 51, 51));

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 771, Short.MAX_VALUE)
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 669, Short.MAX_VALUE)
        );

        ExamsMainPanel.add(kGradientPanel2, java.awt.BorderLayout.CENTER);

        Center.add(ExamsMainPanel);

        ReportsMainPanel.setBackground(new java.awt.Color(26, 24, 26));
        ReportsMainPanel.setLayout(new java.awt.BorderLayout());

        DBTitlePanel5.setBackground(new java.awt.Color(26, 24, 26));
        DBTitlePanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        DashboardMainTitle5.setBackground(new java.awt.Color(199, 50, 38));
        DashboardMainTitle5.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        DashboardMainTitle5.setForeground(new java.awt.Color(227, 227, 227));
        DashboardMainTitle5.setText("Dashboard");
        DashboardMainTitle5.setToolTipText("");

        ReportsSelectedCourse.setBackground(new java.awt.Color(199, 50, 38));
        ReportsSelectedCourse.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        ReportsSelectedCourse.setForeground(new java.awt.Color(227, 227, 227));
        ReportsSelectedCourse.setText("Selected Course:");
        ReportsSelectedCourse.setToolTipText("");

        javax.swing.GroupLayout DBTitlePanel5Layout = new javax.swing.GroupLayout(DBTitlePanel5);
        DBTitlePanel5.setLayout(DBTitlePanel5Layout);
        DBTitlePanel5Layout.setHorizontalGroup(
            DBTitlePanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DBTitlePanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DashboardMainTitle5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 193, Short.MAX_VALUE)
                .addComponent(ReportsSelectedCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        DBTitlePanel5Layout.setVerticalGroup(
            DBTitlePanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DBTitlePanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DBTitlePanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DashboardMainTitle5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ReportsSelectedCourse))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ReportsMainPanel.add(DBTitlePanel5, java.awt.BorderLayout.NORTH);

        jPanel6.setBackground(new java.awt.Color(26, 24, 26));
        jPanel6.setPreferredSize(new java.awt.Dimension(100, 200));
        jPanel6.setLayout(new java.awt.GridLayout(3, 2));

        Grid21.setBackground(new java.awt.Color(26, 24, 26));
        Grid21.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid21.setLayout(new java.awt.GridBagLayout());

        ViewAvgSuccess5.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ViewAvgSuccess5.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid21.add(ViewAvgSuccess5, gridBagConstraints);

        ChartImage11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ChartImage11.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid21.add(ChartImage11, gridBagConstraints);

        AvgSuccessTitle5.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        AvgSuccessTitle5.setForeground(new java.awt.Color(227, 227, 227));
        AvgSuccessTitle5.setText("Average Success");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid21.add(AvgSuccessTitle5, gridBagConstraints);

        jPanel6.add(Grid21);

        Grid22.setBackground(new java.awt.Color(26, 24, 26));
        Grid22.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid22.setLayout(new java.awt.GridBagLayout());

        ViewAvgAttendance5.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ViewAvgAttendance5.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid22.add(ViewAvgAttendance5, gridBagConstraints);

        ChartImage12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ChartImage12.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid22.add(ChartImage12, gridBagConstraints);

        AvgAttendanceTitle5.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        AvgAttendanceTitle5.setForeground(new java.awt.Color(227, 227, 227));
        AvgAttendanceTitle5.setText("Average Attendance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid22.add(AvgAttendanceTitle5, gridBagConstraints);

        jPanel6.add(Grid22);

        Grid23.setBackground(new java.awt.Color(26, 24, 26));
        Grid23.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid23.setLayout(new java.awt.GridBagLayout());

        DBImportBut5.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        DBImportBut5.setText("Import");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid23.add(DBImportBut5, gridBagConstraints);

        ImportImage5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ImportImage5.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid23.add(ImportImage5, gridBagConstraints);

        CourseDataTitle10.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CourseDataTitle10.setForeground(new java.awt.Color(227, 227, 227));
        CourseDataTitle10.setText("Course Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid23.add(CourseDataTitle10, gridBagConstraints);

        jPanel6.add(Grid23);

        Grid24.setBackground(new java.awt.Color(26, 24, 26));
        Grid24.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid24.setLayout(new java.awt.GridBagLayout());

        DbExportBut5.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        DbExportBut5.setText("Export");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid24.add(DbExportBut5, gridBagConstraints);

        ExportImage5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ExportImage5.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid24.add(ExportImage5, gridBagConstraints);

        CourseDataTitle11.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CourseDataTitle11.setForeground(new java.awt.Color(227, 227, 227));
        CourseDataTitle11.setText("Course Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid24.add(CourseDataTitle11, gridBagConstraints);

        jPanel6.add(Grid24);

        ReportsMainPanel.add(jPanel6, java.awt.BorderLayout.CENTER);

        Center.add(ReportsMainPanel);

        AttendanceMainPanel.setBackground(new java.awt.Color(26, 24, 26));
        AttendanceMainPanel.setLayout(new java.awt.BorderLayout());

        AttendanceTitlePanel.setBackground(new java.awt.Color(26, 24, 26));
        AttendanceTitlePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AttendanceMainTitle.setBackground(new java.awt.Color(199, 50, 38));
        AttendanceMainTitle.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        AttendanceMainTitle.setForeground(new java.awt.Color(227, 227, 227));
        AttendanceMainTitle.setText("Dashboard");
        AttendanceMainTitle.setToolTipText("");

        AttendanceSelectedCourse.setBackground(new java.awt.Color(199, 50, 38));
        AttendanceSelectedCourse.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        AttendanceSelectedCourse.setForeground(new java.awt.Color(227, 227, 227));
        AttendanceSelectedCourse.setText("Selected Course:");
        AttendanceSelectedCourse.setToolTipText("");

        javax.swing.GroupLayout AttendanceTitlePanelLayout = new javax.swing.GroupLayout(AttendanceTitlePanel);
        AttendanceTitlePanel.setLayout(AttendanceTitlePanelLayout);
        AttendanceTitlePanelLayout.setHorizontalGroup(
            AttendanceTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AttendanceTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AttendanceMainTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 193, Short.MAX_VALUE)
                .addComponent(AttendanceSelectedCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        AttendanceTitlePanelLayout.setVerticalGroup(
            AttendanceTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AttendanceTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AttendanceTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AttendanceMainTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AttendanceSelectedCourse))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        AttendanceMainPanel.add(AttendanceTitlePanel, java.awt.BorderLayout.NORTH);

        AttMainPanel.setBackground(new java.awt.Color(26, 24, 26));
        AttMainPanel.setPreferredSize(new java.awt.Dimension(100, 200));
        AttMainPanel.setLayout(new java.awt.GridLayout(3, 2));

        Grid13.setBackground(new java.awt.Color(26, 24, 26));
        Grid13.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid13.setLayout(new java.awt.GridBagLayout());

        ViewAvgSuccess3.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ViewAvgSuccess3.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid13.add(ViewAvgSuccess3, gridBagConstraints);

        ChartImage7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ChartImage7.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid13.add(ChartImage7, gridBagConstraints);

        AvgSuccessTitle3.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        AvgSuccessTitle3.setForeground(new java.awt.Color(227, 227, 227));
        AvgSuccessTitle3.setText("Average Success");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid13.add(AvgSuccessTitle3, gridBagConstraints);

        AttMainPanel.add(Grid13);

        Grid14.setBackground(new java.awt.Color(26, 24, 26));
        Grid14.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid14.setLayout(new java.awt.GridBagLayout());

        ViewAvgAttendance3.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ViewAvgAttendance3.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid14.add(ViewAvgAttendance3, gridBagConstraints);

        ChartImage8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ChartImage8.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid14.add(ChartImage8, gridBagConstraints);

        AvgAttendanceTitle3.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        AvgAttendanceTitle3.setForeground(new java.awt.Color(227, 227, 227));
        AvgAttendanceTitle3.setText("Average Attendance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid14.add(AvgAttendanceTitle3, gridBagConstraints);

        AttMainPanel.add(Grid14);

        Grid15.setBackground(new java.awt.Color(26, 24, 26));
        Grid15.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid15.setLayout(new java.awt.GridBagLayout());

        DBImportBut3.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        DBImportBut3.setText("Import");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid15.add(DBImportBut3, gridBagConstraints);

        ImportImage3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ImportImage3.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid15.add(ImportImage3, gridBagConstraints);

        CourseDataTitle6.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CourseDataTitle6.setForeground(new java.awt.Color(227, 227, 227));
        CourseDataTitle6.setText("Course Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid15.add(CourseDataTitle6, gridBagConstraints);

        AttMainPanel.add(Grid15);

        Grid16.setBackground(new java.awt.Color(26, 24, 26));
        Grid16.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid16.setLayout(new java.awt.GridBagLayout());

        DbExportBut3.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        DbExportBut3.setText("Export");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid16.add(DbExportBut3, gridBagConstraints);

        ExportImage3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ExportImage3.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid16.add(ExportImage3, gridBagConstraints);

        CourseDataTitle7.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CourseDataTitle7.setForeground(new java.awt.Color(227, 227, 227));
        CourseDataTitle7.setText("Course Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid16.add(CourseDataTitle7, gridBagConstraints);

        AttMainPanel.add(Grid16);

        AttendanceMainPanel.add(AttMainPanel, java.awt.BorderLayout.CENTER);

        Center.add(AttendanceMainPanel);

        SyllabusMainPanel.setBackground(new java.awt.Color(26, 24, 26));
        SyllabusMainPanel.setLayout(new java.awt.BorderLayout());

        SyllabusTitlePanel.setBackground(new java.awt.Color(26, 24, 26));
        SyllabusTitlePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        SyllabusMainTitle.setBackground(new java.awt.Color(199, 50, 38));
        SyllabusMainTitle.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        SyllabusMainTitle.setForeground(new java.awt.Color(227, 227, 227));
        SyllabusMainTitle.setText("Dashboard");
        SyllabusMainTitle.setToolTipText("");

        SyllabusSelectedCourse.setBackground(new java.awt.Color(199, 50, 38));
        SyllabusSelectedCourse.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        SyllabusSelectedCourse.setForeground(new java.awt.Color(227, 227, 227));
        SyllabusSelectedCourse.setText("Selected Course:");
        SyllabusSelectedCourse.setToolTipText("");

        javax.swing.GroupLayout SyllabusTitlePanelLayout = new javax.swing.GroupLayout(SyllabusTitlePanel);
        SyllabusTitlePanel.setLayout(SyllabusTitlePanelLayout);
        SyllabusTitlePanelLayout.setHorizontalGroup(
            SyllabusTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SyllabusTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SyllabusMainTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 193, Short.MAX_VALUE)
                .addComponent(SyllabusSelectedCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        SyllabusTitlePanelLayout.setVerticalGroup(
            SyllabusTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SyllabusTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SyllabusTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SyllabusMainTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SyllabusSelectedCourse))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        SyllabusMainPanel.add(SyllabusTitlePanel, java.awt.BorderLayout.NORTH);

        SylMainPanel.setBackground(new java.awt.Color(26, 24, 26));
        SylMainPanel.setPreferredSize(new java.awt.Dimension(100, 200));
        SylMainPanel.setLayout(new java.awt.GridLayout(3, 2));

        Grid9.setBackground(new java.awt.Color(26, 24, 26));
        Grid9.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid9.setLayout(new java.awt.GridBagLayout());

        ViewAvgSuccess2.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ViewAvgSuccess2.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid9.add(ViewAvgSuccess2, gridBagConstraints);

        ChartImage5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ChartImage5.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid9.add(ChartImage5, gridBagConstraints);

        AvgSuccessTitle2.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        AvgSuccessTitle2.setForeground(new java.awt.Color(227, 227, 227));
        AvgSuccessTitle2.setText("Average Success");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid9.add(AvgSuccessTitle2, gridBagConstraints);

        SylMainPanel.add(Grid9);

        Grid10.setBackground(new java.awt.Color(26, 24, 26));
        Grid10.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid10.setLayout(new java.awt.GridBagLayout());

        ViewAvgAttendance2.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ViewAvgAttendance2.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid10.add(ViewAvgAttendance2, gridBagConstraints);

        ChartImage6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ChartImage6.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid10.add(ChartImage6, gridBagConstraints);

        AvgAttendanceTitle2.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        AvgAttendanceTitle2.setForeground(new java.awt.Color(227, 227, 227));
        AvgAttendanceTitle2.setText("Average Attendance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid10.add(AvgAttendanceTitle2, gridBagConstraints);

        SylMainPanel.add(Grid10);

        Grid11.setBackground(new java.awt.Color(26, 24, 26));
        Grid11.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid11.setLayout(new java.awt.GridBagLayout());

        DBImportBut2.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        DBImportBut2.setText("Import");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid11.add(DBImportBut2, gridBagConstraints);

        ImportImage2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ImportImage2.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid11.add(ImportImage2, gridBagConstraints);

        CourseDataTitle4.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CourseDataTitle4.setForeground(new java.awt.Color(227, 227, 227));
        CourseDataTitle4.setText("Course Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid11.add(CourseDataTitle4, gridBagConstraints);

        SylMainPanel.add(Grid11);

        Grid12.setBackground(new java.awt.Color(26, 24, 26));
        Grid12.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid12.setLayout(new java.awt.GridBagLayout());

        DbExportBut2.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        DbExportBut2.setText("Export");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid12.add(DbExportBut2, gridBagConstraints);

        ExportImage2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ExportImage2.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid12.add(ExportImage2, gridBagConstraints);

        CourseDataTitle5.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CourseDataTitle5.setForeground(new java.awt.Color(227, 227, 227));
        CourseDataTitle5.setText("Course Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid12.add(CourseDataTitle5, gridBagConstraints);

        SylMainPanel.add(Grid12);

        SyllabusMainPanel.add(SylMainPanel, java.awt.BorderLayout.CENTER);

        Center.add(SyllabusMainPanel);

        DashboardMainPanel.setBackground(new java.awt.Color(26, 24, 26));
        DashboardMainPanel.setLayout(new javax.swing.OverlayLayout(DashboardMainPanel));

        DBMain.setLayout(new javax.swing.BoxLayout(DBMain, javax.swing.BoxLayout.Y_AXIS));

        DashboardTitlePanel.setkEndColor(new java.awt.Color(22, 25, 33));
        DashboardTitlePanel.setkStartColor(new java.awt.Color(51, 51, 51));
        DashboardTitlePanel.setMaximumSize(new java.awt.Dimension(32767, 60));

        DashboardMainTitle.setBackground(new java.awt.Color(199, 50, 38));
        DashboardMainTitle.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        DashboardMainTitle.setForeground(new java.awt.Color(227, 227, 227));
        DashboardMainTitle.setText("Dashboard");
        DashboardMainTitle.setToolTipText("");

        DashboardSelectedCourse.setBackground(new java.awt.Color(199, 50, 38));
        DashboardSelectedCourse.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        DashboardSelectedCourse.setForeground(new java.awt.Color(227, 227, 227));
        DashboardSelectedCourse.setText("Selected Course:");
        DashboardSelectedCourse.setToolTipText("");

        javax.swing.GroupLayout DashboardTitlePanelLayout = new javax.swing.GroupLayout(DashboardTitlePanel);
        DashboardTitlePanel.setLayout(DashboardTitlePanelLayout);
        DashboardTitlePanelLayout.setHorizontalGroup(
            DashboardTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DashboardMainTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 195, Short.MAX_VALUE)
                .addComponent(DashboardSelectedCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        DashboardTitlePanelLayout.setVerticalGroup(
            DashboardTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DashboardTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DashboardMainTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DashboardSelectedCourse))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        DBMain.add(DashboardTitlePanel);

        DBCenterPanel.setkEndColor(new java.awt.Color(35, 29, 45));
        DBCenterPanel.setkGradientFocus(200);
        DBCenterPanel.setkStartColor(new java.awt.Color(0, 0, 0));
        DBCenterPanel.setLayout(new java.awt.GridLayout(3, 2));

        jPanel2.setBackground(new java.awt.Color(240, 136, 195));
        jPanel2.setOpaque(false);

        AvgSuccessTitle.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        AvgSuccessTitle.setForeground(new java.awt.Color(227, 227, 227));
        AvgSuccessTitle.setText("Average Success");

        ChartImage1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ChartImage1.setPreferredSize(new java.awt.Dimension(128, 128));

        ViewAvgSuccess.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ViewAvgSuccess.setText("View");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 385, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 99, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(AvgSuccessTitle)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(29, 29, 29)
                            .addComponent(ChartImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(ViewAvgSuccess, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 100, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 243, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(AvgSuccessTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(4, 4, 4)
                    .addComponent(ChartImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(ViewAvgSuccess)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        DBCenterPanel.add(jPanel2);

        jPanel7.setOpaque(false);

        ViewAvgAttendance.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ViewAvgAttendance.setText("View");

        ChartImage2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ChartImage2.setPreferredSize(new java.awt.Dimension(128, 128));

        AvgAttendanceTitle.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        AvgAttendanceTitle.setForeground(new java.awt.Color(227, 227, 227));
        AvgAttendanceTitle.setText("Average Attendance");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 385, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 82, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(AvgAttendanceTitle)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGap(46, 46, 46)
                            .addComponent(ChartImage2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(ViewAvgAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 82, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 243, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(AvgAttendanceTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(ChartImage2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(9, 9, 9)
                    .addComponent(ViewAvgAttendance)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        DBCenterPanel.add(jPanel7);

        jPanel8.setOpaque(false);

        DBImportBut.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        DBImportBut.setText("Import");

        ImportImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ImportImage.setPreferredSize(new java.awt.Dimension(128, 128));

        CourseDataTitle.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CourseDataTitle.setForeground(new java.awt.Color(227, 227, 227));
        CourseDataTitle.setText("Course Data");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 385, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 127, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(CourseDataTitle)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(ImportImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(DBImportBut, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 128, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 243, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(CourseDataTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(ImportImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(9, 9, 9)
                    .addComponent(DBImportBut)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        DBCenterPanel.add(jPanel8);

        jPanel9.setOpaque(false);

        DbExportBut.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        DbExportBut.setText("Export");

        ExportImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ExportImage.setPreferredSize(new java.awt.Dimension(128, 128));

        CourseDataTitle2.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CourseDataTitle2.setForeground(new java.awt.Color(227, 227, 227));
        CourseDataTitle2.setText("Course Data");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 385, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(0, 127, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(CourseDataTitle2)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(ExportImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(DbExportBut, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 128, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 243, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(CourseDataTitle2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(ExportImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(9, 9, 9)
                    .addComponent(DbExportBut)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        DBCenterPanel.add(jPanel9);

        DBMain.add(DBCenterPanel);

        DashboardMainPanel.add(DBMain);

        Center.add(DashboardMainPanel);

        Main.add(Center, java.awt.BorderLayout.CENTER);

        getContentPane().add(Main, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleName("Tina");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CoursesListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_CoursesListValueChanged
        // TODO add your handling code here:

        
        if (CoursesList.getSelectedIndex() != -1 && !evt.getValueIsAdjusting()) {
            DuplicateCourseButton.setEnabled(true);
            RemoveCourseButton.setEnabled(true);
           //SelectCourse(CoursesList.getSelectedIndex());
        }

    }//GEN-LAST:event_CoursesListValueChanged

    private void RemoveCourseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveCourseButtonActionPerformed
        // TODO add your handling code here:

        courseManager.RemoveCourse(CoursesList.getSelectedIndex());
    }//GEN-LAST:event_RemoveCourseButtonActionPerformed

    private void DuplicateCourseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DuplicateCourseButtonActionPerformed
        // TODO add your handling code here:
        courseManager.DuplicateCourse(CoursesList.getSelectedIndex());
    }//GEN-LAST:event_DuplicateCourseButtonActionPerformed

    private void UndoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UndoButtonActionPerformed
        // TODO add your handling code here:

        CourseAction lastAction = courseActions.get(courseActions.size() - 1);

        if (lastAction.GetIndex() == 1) {

            courseManager.RemoveCourse(lastAction.GetSubject());
        } else if (lastAction.GetIndex() == 2) {
            courseManager.AddNewCourse(lastAction.GetSubject(), lastAction.GetListIndex());
        }

        courseActions.remove(lastAction);

        if (courseActions.size() < 1) {
            UndoButton.setEnabled(false);
        }

    }//GEN-LAST:event_UndoButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        // Set anti aliasing on for fonts.
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

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
    private javax.swing.JButton AddNewCourseButton;
    private javax.swing.JPanel AttMainPanel;
    private javax.swing.JLabel AttendanceImage;
    private javax.swing.JLabel AttendanceLabel;
    private javax.swing.JPanel AttendanceMainPanel;
    private javax.swing.JLabel AttendanceMainTitle;
    private javax.swing.JPanel AttendancePanel;
    private javax.swing.JLabel AttendanceSelectedCourse;
    private javax.swing.JPanel AttendanceTitlePanel;
    private javax.swing.JPanel AttendanceWrapper;
    private javax.swing.JLabel AvgAttendanceTitle;
    private javax.swing.JLabel AvgAttendanceTitle2;
    private javax.swing.JLabel AvgAttendanceTitle3;
    private javax.swing.JLabel AvgAttendanceTitle4;
    private javax.swing.JLabel AvgAttendanceTitle5;
    private javax.swing.JLabel AvgSuccessTitle;
    private javax.swing.JLabel AvgSuccessTitle2;
    private javax.swing.JLabel AvgSuccessTitle3;
    private javax.swing.JLabel AvgSuccessTitle4;
    private javax.swing.JLabel AvgSuccessTitle5;
    private javax.swing.JPanel Center;
    private javax.swing.JLabel ChartImage1;
    private javax.swing.JLabel ChartImage10;
    private javax.swing.JLabel ChartImage11;
    private javax.swing.JLabel ChartImage12;
    private javax.swing.JLabel ChartImage2;
    private javax.swing.JLabel ChartImage5;
    private javax.swing.JLabel ChartImage6;
    private javax.swing.JLabel ChartImage7;
    private javax.swing.JLabel ChartImage8;
    private javax.swing.JLabel ChartImage9;
    private javax.swing.JLabel CourseDataTitle;
    private javax.swing.JLabel CourseDataTitle10;
    private javax.swing.JLabel CourseDataTitle11;
    private javax.swing.JLabel CourseDataTitle2;
    private javax.swing.JLabel CourseDataTitle4;
    private javax.swing.JLabel CourseDataTitle5;
    private javax.swing.JLabel CourseDataTitle6;
    private javax.swing.JLabel CourseDataTitle7;
    private javax.swing.JLabel CourseDataTitle8;
    private javax.swing.JLabel CourseDataTitle9;
    private keeptoo.KGradientPanel CoursesCenterPanel;
    private javax.swing.JLabel CoursesImage;
    private javax.swing.JLabel CoursesLabel;
    private javax.swing.JList<String> CoursesList;
    private javax.swing.JPanel CoursesMainPanel;
    private javax.swing.JLabel CoursesMainTitle;
    private javax.swing.JPanel CoursesPanel;
    private javax.swing.JLabel CoursesSelectedCourse;
    private javax.swing.JPanel CoursesTitlePanel;
    private javax.swing.JPanel CoursesWrapper;
    private keeptoo.KGradientPanel DBCenterPanel;
    private javax.swing.JButton DBImportBut;
    private javax.swing.JButton DBImportBut2;
    private javax.swing.JButton DBImportBut3;
    private javax.swing.JButton DBImportBut4;
    private javax.swing.JButton DBImportBut5;
    private javax.swing.JPanel DBMain;
    private javax.swing.JPanel DBTitlePanel4;
    private javax.swing.JPanel DBTitlePanel5;
    private javax.swing.JPanel DBTitlePanel6;
    private javax.swing.JLabel DashboardImage;
    private javax.swing.JLabel DashboardLabel;
    private javax.swing.JPanel DashboardMainPanel;
    private javax.swing.JLabel DashboardMainTitle;
    private javax.swing.JLabel DashboardMainTitle4;
    private javax.swing.JLabel DashboardMainTitle5;
    private javax.swing.JLabel DashboardMainTitle6;
    private javax.swing.JPanel DashboardPanel;
    private javax.swing.JLabel DashboardSelectedCourse;
    private keeptoo.KGradientPanel DashboardTitlePanel;
    private javax.swing.JPanel DashboardWrapper;
    private javax.swing.JButton DbExportBut;
    private javax.swing.JButton DbExportBut2;
    private javax.swing.JButton DbExportBut3;
    private javax.swing.JButton DbExportBut4;
    private javax.swing.JButton DbExportBut5;
    private javax.swing.JButton DuplicateCourseButton;
    private javax.swing.JLabel ExamsImage;
    private javax.swing.JLabel ExamsLabel;
    private javax.swing.JPanel ExamsMainPanel;
    private javax.swing.JPanel ExamsPanel;
    private javax.swing.JLabel ExamsSelectedCourse;
    private javax.swing.JPanel ExamsWrapper;
    private javax.swing.JLabel ExportImage;
    private javax.swing.JLabel ExportImage2;
    private javax.swing.JLabel ExportImage3;
    private javax.swing.JLabel ExportImage4;
    private javax.swing.JLabel ExportImage5;
    private javax.swing.JPanel Grid10;
    private javax.swing.JPanel Grid11;
    private javax.swing.JPanel Grid12;
    private javax.swing.JPanel Grid13;
    private javax.swing.JPanel Grid14;
    private javax.swing.JPanel Grid15;
    private javax.swing.JPanel Grid16;
    private javax.swing.JPanel Grid17;
    private javax.swing.JPanel Grid18;
    private javax.swing.JPanel Grid19;
    private javax.swing.JPanel Grid20;
    private javax.swing.JPanel Grid21;
    private javax.swing.JPanel Grid22;
    private javax.swing.JPanel Grid23;
    private javax.swing.JPanel Grid24;
    private javax.swing.JPanel Grid9;
    private javax.swing.JLabel ImportImage;
    private javax.swing.JLabel ImportImage2;
    private javax.swing.JLabel ImportImage3;
    private javax.swing.JLabel ImportImage4;
    private javax.swing.JLabel ImportImage5;
    private javax.swing.JPanel Left;
    private javax.swing.JPanel Main;
    private javax.swing.JPanel MenuPanel;
    private javax.swing.JButton RemoveCourseButton;
    private javax.swing.JLabel ReportsImage;
    private javax.swing.JLabel ReportsLabel;
    private javax.swing.JPanel ReportsMainPanel;
    private javax.swing.JPanel ReportsPanel;
    private javax.swing.JLabel ReportsSelectedCourse;
    private javax.swing.JPanel ReportsWrapper;
    private javax.swing.JLabel SettingsImage;
    private javax.swing.JLabel StudentsImage;
    private javax.swing.JLabel StudentsLabel;
    private javax.swing.JPanel StudentsMainPanel;
    private javax.swing.JPanel StudentsPanel;
    private javax.swing.JLabel StudentsSelectedCourse;
    private javax.swing.JPanel StudentsWrapper;
    private javax.swing.JPanel SylMainPanel;
    private javax.swing.JLabel SyllabusImage;
    private javax.swing.JLabel SyllabusLabel;
    private javax.swing.JPanel SyllabusMainPanel;
    private javax.swing.JLabel SyllabusMainTitle;
    private javax.swing.JPanel SyllabusPanel;
    private javax.swing.JLabel SyllabusSelectedCourse;
    private javax.swing.JPanel SyllabusTitlePanel;
    private javax.swing.JPanel SyllabusWrapper;
    private javax.swing.JLabel Title;
    private javax.swing.JPanel Top;
    private javax.swing.JPanel TopCenter;
    private javax.swing.JPanel TopLeft;
    private javax.swing.JPanel TopRight;
    private javax.swing.JButton UndoButton;
    private javax.swing.JButton ViewAvgAttendance;
    private javax.swing.JButton ViewAvgAttendance2;
    private javax.swing.JButton ViewAvgAttendance3;
    private javax.swing.JButton ViewAvgAttendance4;
    private javax.swing.JButton ViewAvgAttendance5;
    private javax.swing.JButton ViewAvgSuccess;
    private javax.swing.JButton ViewAvgSuccess2;
    private javax.swing.JButton ViewAvgSuccess3;
    private javax.swing.JButton ViewAvgSuccess4;
    private javax.swing.JButton ViewAvgSuccess5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private keeptoo.KGradientPanel kGradientPanel2;
    private keeptoo.KGradientPanel kGradientPanel3;
    private keeptoo.KGradientPanel kGradientPanel4;
    // End of variables declaration//GEN-END:variables
}
