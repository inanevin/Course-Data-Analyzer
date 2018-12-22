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
public class UIManager extends javax.swing.JFrame
{

    private CourseManager courseManager;
    private ResourceManager resourceManager;
    private ArrayList<MenuBarItem> menuBarItems;
    private MenuBarItem currentSelectedMenu;
    private Color selectedMenuItemColor = new Color(36, 152, 249);
    private Color unselectedMenuItemColor = new Color(53, 55, 61);
    private Color unselectedInnerMenuItemColor = new Color(26, 24, 26);

    private ArrayList<CourseAction> courseActions;
    private int i_SelectedCourse = -1;
    private int i_SelectedSection = -1;

    /**
     * Creates new form MainFrame
     */
    public UIManager()
    {

        initComponents();

        courseActions = new ArrayList<CourseAction>();
        resourceManager = new ResourceManager();
        courseManager = new CourseManager(this, resourceManager);
        courseManager.PopulateCourses();

        SetIcons();
        InitializeMenuBarItems();
        AddComponentListeners();

        AddCourseWarning.setVisible(false);
        EditCourseWarning.setVisible(false);
        AddSectionWarning.setVisible(false);
    }

    public void UpdateCourseList(int toSelect)
    {

        ArrayList<Course> cl = courseManager.GetCourseList();
        DefaultListModel m = new DefaultListModel();

        for (int i = 0; i < cl.size(); i++)
        {
            String elementDisplay = (new StringBuilder()).append(cl.get(i).GetID()).append(" - ").append(cl.get(i).GetName()).toString();
            m.addElement(elementDisplay);
        }

        CoursesList.setModel(m);

        if (CoursesList.getComponentCount() == 0 || CoursesList.getSelectedIndex() == -1)
        {
            DuplicateCourseButton.setEnabled(false);
            RemoveCourseButton.setEnabled(false);
        }

        CoursesList.setSelectedIndex(toSelect);
        SelectCourse(toSelect);
        courseManager.SaveCourses();
    }

    // 1 is for adding a new course, 2 is for removing.
    public void SetLastActionForCourses(Course subject, int actionIndex)
    {

        try
        {
            CourseAction c = new CourseAction(subject, actionIndex, CoursesList.getSelectedIndex());
            courseActions.add(c);
        } catch (ActionIndexException e)
        {
            System.out.println(e.getMessage());
            return;
        }

        UndoButton.setEnabled(true);

    }

    private void SelectCourse(int index)
    {

        i_SelectedCourse = index;
        System.out.println(courseManager);
        String title = "";
        String id = "";
        String name = "";
        String desc = "";

        if (i_SelectedCourse != -1)
        {
            title = (new StringBuilder().append("Selected Course: ")
                    .append(courseManager.GetCourse(index).GetID())
                    .append(" - ")
                    .append(courseManager.GetCourse(index).GetName())).toString();

            id = courseManager.GetCourse(index).GetID();
            name = courseManager.GetCourse(index).GetName();
            desc = courseManager.GetCourse(index).GetDescription();
        }

        DashboardSelectedCourse.setText(title);
        CoursesSelectedCourse.setText(title);
        StudentsSelectedCourse.setText(title);
        SyllabusSelectedCourse.setText(title);
        AttendanceSelectedCourse.setText(title);
        ExamsSelectedCourse.setText(title);
        ReportsSelectedCourse.setText(title);

        CourseID.setText(id);
        CourseName.setText(name);
        CourseDescription.setText(desc);

        int toSelect = i_SelectedCourse == -1 ? -1 : courseManager.GetCourse(i_SelectedCourse).GetSelectedSectionIndex();

        UpdateSectionList(toSelect);

    }

    public void UpdateSectionList(int toSelect)
    {
        DefaultListModel m = new DefaultListModel();
        ArrayList<Section> sections = new ArrayList<Section>();

        if (i_SelectedCourse != -1)
        {

            sections = courseManager.GetCourse(i_SelectedCourse).GetSections();
            System.out.println(sections.size());
            for (int i = 0; i < sections.size(); i++)
            {
                String elementDisplay = (new StringBuilder()).append(sections.get(i).GetName()).toString();
                m.addElement(elementDisplay);
            }

        }

        SectionsList.setModel(m);
        SectionsList.setSelectedIndex(toSelect);
        courseManager.SaveCourses();
    }

    private void SelectSection(int index)
    {
        if (index == -1)
        {
            RemoveSectionButton.setEnabled(false);

        } else
        {
            RemoveSectionButton.setEnabled(true);
        }

        if (i_SelectedCourse != -1)
        {
            courseManager.GetCourse(i_SelectedCourse).SetSection(index);
        }
    }

    private void InitializeMenuBarItems()
    {
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

    private void SetIcons()
    {
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

    private void AddComponentListeners()
    {

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

    public void SelectMenu(int barItemIndex)
    {
        SelectMenu(menuBarItems.get(barItemIndex));
    }

    public void SelectMenu(MenuBarItem menu)
    {

        // Deselect if current menu is not null.
        if (currentSelectedMenu != null)
        {
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

    private void DeselectAllMenu()
    {
        for (int i = 0; i < menuBarItems.size(); i++)
        {
            DeselectMenu(menuBarItems.get(i));
        }
    }

    void DeselectMenu(MenuBarItem menu)
    {

        // Set selectable panel color to unselected.
        menu.GetSelectablePanel().setBackground(unselectedMenuItemColor);

        // Disable menu's main panel.
        menu.GetMainPanel().setVisible(false);
        menu.GetMainPanel().setEnabled(false);
    }

    private void DisposeAddNewCourseDialog()
    {

        AddCourseIDField.setText("");
        AddCourseNameField.setText("");
        AddCourseDescField.setText("");
        AddCourseWarning.setVisible(false);
        AddNewCourseDialog.dispose();
    }

    private void DisposeEditCourseDialog()
    {
        EditCourseIDField.setText("");
        EditCourseNameField.setText("");
        EditCourseDescField.setText("");
        EditCourseWarning.setVisible(false);
        EditCourseDialog.dispose();
    }

    private void DisposeAddSectionDialog()
    {
        AddSectionNameField.setText("");
        AddSectionWarning.setVisible(false);
        AddSectionAddButton.setEnabled(true);
        AddSectionDialog.dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        java.awt.GridBagConstraints gridBagConstraints;

        EditCourseDialog = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        EditCourseID = new javax.swing.JLabel();
        EditCourseIDField = new javax.swing.JTextField();
        EditCourseName = new javax.swing.JLabel();
        EditCourseNameField = new javax.swing.JTextField();
        EditCourseDesc = new javax.swing.JLabel();
        EditCourseDescPane = new javax.swing.JScrollPane();
        EditCourseDescField = new javax.swing.JTextArea();
        EditCourseCancelButton = new javax.swing.JButton();
        EditCourseWarning = new javax.swing.JLabel();
        EditCourseSaveButton = new javax.swing.JButton();
        AddNewCourseDialog = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        AddCourseID = new javax.swing.JLabel();
        AddCourseIDField = new javax.swing.JTextField();
        AddCourseName = new javax.swing.JLabel();
        AddCourseNameField = new javax.swing.JTextField();
        AddCourseDesc = new javax.swing.JLabel();
        AddCourseDescPane = new javax.swing.JScrollPane();
        AddCourseDescField = new javax.swing.JTextArea();
        AddCourseCancelButton = new javax.swing.JButton();
        AddCourseWarning = new javax.swing.JLabel();
        AddCourseAddButton = new javax.swing.JButton();
        AddSectionDialog = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        AddSectionName = new javax.swing.JLabel();
        AddSectionNameField = new javax.swing.JTextField();
        AddSectionCancelButton = new javax.swing.JButton();
        AddSectionWarning = new javax.swing.JLabel();
        AddSectionAddButton = new javax.swing.JButton();
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
        jPanel3 = new javax.swing.JPanel();
        CourseNameTitle = new javax.swing.JLabel();
        CourseDescriptionTitle = new javax.swing.JLabel();
        CourseIDTitle = new javax.swing.JLabel();
        CourseName = new javax.swing.JLabel();
        CourseID = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        CourseDescription = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        CoursesList = new javax.swing.JList<>();
        kGradientPanel3 = new keeptoo.KGradientPanel();
        AddNewCourseButton = new javax.swing.JButton();
        DuplicateCourseButton = new javax.swing.JButton();
        RemoveCourseButton = new javax.swing.JButton();
        UndoButton = new javax.swing.JButton();
        kGradientPanel5 = new keeptoo.KGradientPanel();
        CoursesMainTitle1 = new javax.swing.JLabel();
        AddSectionButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        SectionsList = new javax.swing.JList<>();
        RemoveSectionButton = new javax.swing.JButton();
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

        EditCourseDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        EditCourseDialog.setTitle("Add New Course");
        EditCourseDialog.setAlwaysOnTop(true);
        EditCourseDialog.setBackground(new java.awt.Color(26, 24, 26));
        EditCourseDialog.setBounds(new java.awt.Rectangle(0, 0, 800, 600));
        EditCourseDialog.setModal(true);
        EditCourseDialog.getContentPane().setLayout(new javax.swing.OverlayLayout(EditCourseDialog.getContentPane()));

        jPanel4.setBackground(new java.awt.Color(26, 24, 26));

        EditCourseID.setBackground(new java.awt.Color(26, 24, 26));
        EditCourseID.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        EditCourseID.setForeground(new java.awt.Color(227, 227, 227));
        EditCourseID.setText("ID:");
        EditCourseID.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        EditCourseID.setPreferredSize(new java.awt.Dimension(100, 50));

        EditCourseIDField.setBackground(new java.awt.Color(26, 24, 26));
        EditCourseIDField.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        EditCourseIDField.setForeground(new java.awt.Color(227, 227, 227));
        EditCourseIDField.setCaretColor(new java.awt.Color(204, 204, 204));
        EditCourseIDField.setPreferredSize(new java.awt.Dimension(200, 40));
        EditCourseIDField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                EditCourseIDFieldActionPerformed(evt);
            }
        });
        EditCourseIDField.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                EditCourseIDFieldKeyReleased(evt);
            }
        });

        EditCourseName.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        EditCourseName.setForeground(new java.awt.Color(227, 227, 227));
        EditCourseName.setText("Name:");
        EditCourseName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        EditCourseName.setPreferredSize(new java.awt.Dimension(100, 50));

        EditCourseNameField.setBackground(new java.awt.Color(26, 24, 26));
        EditCourseNameField.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        EditCourseNameField.setForeground(new java.awt.Color(227, 227, 227));
        EditCourseNameField.setCaretColor(new java.awt.Color(204, 204, 204));
        EditCourseNameField.setPreferredSize(new java.awt.Dimension(200, 40));

        EditCourseDesc.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        EditCourseDesc.setForeground(new java.awt.Color(227, 227, 227));
        EditCourseDesc.setText("Description:");
        EditCourseDesc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        EditCourseDesc.setMaximumSize(new java.awt.Dimension(121, 225));
        EditCourseDesc.setPreferredSize(new java.awt.Dimension(121, 225));

        EditCourseDescField.setBackground(new java.awt.Color(26, 24, 26));
        EditCourseDescField.setColumns(20);
        EditCourseDescField.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        EditCourseDescField.setForeground(new java.awt.Color(227, 227, 227));
        EditCourseDescField.setLineWrap(true);
        EditCourseDescField.setRows(5);
        EditCourseDescField.setCaretColor(new java.awt.Color(204, 204, 204));
        EditCourseDescPane.setViewportView(EditCourseDescField);

        EditCourseCancelButton.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        EditCourseCancelButton.setText("Cancel");
        EditCourseCancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                EditCourseCancelButtonActionPerformed(evt);
            }
        });

        EditCourseWarning.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        EditCourseWarning.setForeground(new java.awt.Color(204, 0, 51));
        EditCourseWarning.setText("This Course Already Exists");
        EditCourseWarning.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        EditCourseWarning.setPreferredSize(new java.awt.Dimension(121, 125));

        EditCourseSaveButton.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        EditCourseSaveButton.setText("Save");
        EditCourseSaveButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                EditCourseSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(EditCourseSaveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(EditCourseDesc, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                    .addComponent(EditCourseName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(EditCourseID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(EditCourseCancelButton)
                        .addGap(34, 34, 34)
                        .addComponent(EditCourseWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 198, Short.MAX_VALUE))
                    .addComponent(EditCourseDescPane)
                    .addComponent(EditCourseIDField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(EditCourseNameField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(33, 33, 33))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EditCourseID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EditCourseIDField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EditCourseNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EditCourseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(EditCourseDescPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(EditCourseDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(41, 41, 41)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EditCourseSaveButton)
                    .addComponent(EditCourseCancelButton)
                    .addComponent(EditCourseWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        EditCourseDialog.getContentPane().add(jPanel4);

        AddNewCourseDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        AddNewCourseDialog.setTitle("Add New Course");
        AddNewCourseDialog.setAlwaysOnTop(true);
        AddNewCourseDialog.setBackground(new java.awt.Color(26, 24, 26));
        AddNewCourseDialog.setBounds(new java.awt.Rectangle(0, 0, 800, 600));
        AddNewCourseDialog.setModal(true);
        AddNewCourseDialog.getContentPane().setLayout(new javax.swing.OverlayLayout(AddNewCourseDialog.getContentPane()));

        jPanel1.setBackground(new java.awt.Color(26, 24, 26));

        AddCourseID.setBackground(new java.awt.Color(26, 24, 26));
        AddCourseID.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        AddCourseID.setForeground(new java.awt.Color(227, 227, 227));
        AddCourseID.setText("ID:");
        AddCourseID.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        AddCourseID.setPreferredSize(new java.awt.Dimension(100, 50));

        AddCourseIDField.setBackground(new java.awt.Color(26, 24, 26));
        AddCourseIDField.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        AddCourseIDField.setForeground(new java.awt.Color(227, 227, 227));
        AddCourseIDField.setCaretColor(new java.awt.Color(204, 204, 204));
        AddCourseIDField.setPreferredSize(new java.awt.Dimension(200, 40));
        AddCourseIDField.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                AddCourseIDFieldKeyReleased(evt);
            }
        });

        AddCourseName.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        AddCourseName.setForeground(new java.awt.Color(227, 227, 227));
        AddCourseName.setText("Name:");
        AddCourseName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        AddCourseName.setPreferredSize(new java.awt.Dimension(100, 50));

        AddCourseNameField.setBackground(new java.awt.Color(26, 24, 26));
        AddCourseNameField.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        AddCourseNameField.setForeground(new java.awt.Color(227, 227, 227));
        AddCourseNameField.setCaretColor(new java.awt.Color(204, 204, 204));
        AddCourseNameField.setPreferredSize(new java.awt.Dimension(200, 40));

        AddCourseDesc.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        AddCourseDesc.setForeground(new java.awt.Color(227, 227, 227));
        AddCourseDesc.setText("Description:");
        AddCourseDesc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        AddCourseDesc.setMaximumSize(new java.awt.Dimension(121, 225));
        AddCourseDesc.setPreferredSize(new java.awt.Dimension(121, 225));

        AddCourseDescField.setBackground(new java.awt.Color(26, 24, 26));
        AddCourseDescField.setColumns(20);
        AddCourseDescField.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        AddCourseDescField.setForeground(new java.awt.Color(227, 227, 227));
        AddCourseDescField.setLineWrap(true);
        AddCourseDescField.setRows(5);
        AddCourseDescField.setCaretColor(new java.awt.Color(204, 204, 204));
        AddCourseDescPane.setViewportView(AddCourseDescField);

        AddCourseCancelButton.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        AddCourseCancelButton.setText("Cancel");
        AddCourseCancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                AddCourseCancelButtonActionPerformed(evt);
            }
        });

        AddCourseWarning.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        AddCourseWarning.setForeground(new java.awt.Color(204, 0, 51));
        AddCourseWarning.setText("This Course Already Exists");
        AddCourseWarning.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        AddCourseWarning.setPreferredSize(new java.awt.Dimension(121, 125));

        AddCourseAddButton.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        AddCourseAddButton.setText("Add Course");
        AddCourseAddButton.setEnabled(false);
        AddCourseAddButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                AddCourseAddButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(AddCourseAddButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AddCourseDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AddCourseName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AddCourseID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(AddCourseCancelButton)
                        .addGap(34, 34, 34)
                        .addComponent(AddCourseWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 193, Short.MAX_VALUE))
                    .addComponent(AddCourseDescPane)
                    .addComponent(AddCourseIDField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AddCourseNameField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(33, 33, 33))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddCourseID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddCourseIDField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddCourseNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddCourseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(AddCourseDescPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(AddCourseDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddCourseAddButton)
                    .addComponent(AddCourseCancelButton)
                    .addComponent(AddCourseWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        AddNewCourseDialog.getContentPane().add(jPanel1);

        AddSectionDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        AddSectionDialog.setTitle("Add New Course");
        AddSectionDialog.setAlwaysOnTop(true);
        AddSectionDialog.setBackground(new java.awt.Color(26, 24, 26));
        AddSectionDialog.setBounds(new java.awt.Rectangle(0, 0, 800, 600));
        AddSectionDialog.setModal(true);
        AddSectionDialog.getContentPane().setLayout(new javax.swing.OverlayLayout(AddSectionDialog.getContentPane()));

        jPanel10.setBackground(new java.awt.Color(26, 24, 26));

        AddSectionName.setBackground(new java.awt.Color(26, 24, 26));
        AddSectionName.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        AddSectionName.setForeground(new java.awt.Color(227, 227, 227));
        AddSectionName.setText("Name:");
        AddSectionName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        AddSectionName.setPreferredSize(new java.awt.Dimension(100, 50));

        AddSectionNameField.setBackground(new java.awt.Color(26, 24, 26));
        AddSectionNameField.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        AddSectionNameField.setForeground(new java.awt.Color(227, 227, 227));
        AddSectionNameField.setCaretColor(new java.awt.Color(204, 204, 204));
        AddSectionNameField.setPreferredSize(new java.awt.Dimension(200, 40));
        AddSectionNameField.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                AddSectionNameFieldKeyReleased(evt);
            }
        });

        AddSectionCancelButton.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        AddSectionCancelButton.setText("Cancel");
        AddSectionCancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                AddSectionCancelButtonActionPerformed(evt);
            }
        });

        AddSectionWarning.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        AddSectionWarning.setForeground(new java.awt.Color(204, 0, 51));
        AddSectionWarning.setText("This Section Already Exists!");
        AddSectionWarning.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        AddSectionWarning.setPreferredSize(new java.awt.Dimension(121, 125));

        AddSectionAddButton.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        AddSectionAddButton.setText("Add Section");
        AddSectionAddButton.setEnabled(false);
        AddSectionAddButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                AddSectionAddButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(AddSectionAddButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AddSectionName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(AddSectionCancelButton)
                        .addGap(34, 34, 34)
                        .addComponent(AddSectionWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(AddSectionNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 709, Short.MAX_VALUE))
                .addGap(33, 33, 33))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddSectionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddSectionNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddSectionAddButton)
                    .addComponent(AddSectionCancelButton)
                    .addComponent(AddSectionWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        AddSectionDialog.getContentPane().add(jPanel10);

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
                .addContainerGap(161, Short.MAX_VALUE))
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
            .addGap(0, 310, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
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
        CoursesCenterPanel.setLayout(new javax.swing.BoxLayout(CoursesCenterPanel, javax.swing.BoxLayout.Y_AXIS));

        kGradientPanel4.setkEndColor(new java.awt.Color(26, 24, 26));
        kGradientPanel4.setkStartColor(new java.awt.Color(26, 24, 26));
        kGradientPanel4.setMaximumSize(new java.awt.Dimension(32767, 500));
        kGradientPanel4.setMinimumSize(new java.awt.Dimension(0, 250));
        kGradientPanel4.setPreferredSize(new java.awt.Dimension(400, 300));
        kGradientPanel4.setLayout(new javax.swing.BoxLayout(kGradientPanel4, javax.swing.BoxLayout.Y_AXIS));

        jPanel3.setBackground(new java.awt.Color(26, 24, 26));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 1000));
        jPanel3.setMinimumSize(new java.awt.Dimension(150, 0));
        jPanel3.setPreferredSize(new java.awt.Dimension(32767, 1000));

        CourseNameTitle.setBackground(new java.awt.Color(199, 50, 38));
        CourseNameTitle.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        CourseNameTitle.setForeground(new java.awt.Color(227, 227, 227));
        CourseNameTitle.setText("Name:");

        CourseDescriptionTitle.setBackground(new java.awt.Color(199, 50, 38));
        CourseDescriptionTitle.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        CourseDescriptionTitle.setForeground(new java.awt.Color(227, 227, 227));
        CourseDescriptionTitle.setText("Description ");

        CourseIDTitle.setBackground(new java.awt.Color(199, 50, 38));
        CourseIDTitle.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        CourseIDTitle.setForeground(new java.awt.Color(227, 227, 227));
        CourseIDTitle.setText("ID:");

        CourseName.setBackground(new java.awt.Color(199, 50, 38));
        CourseName.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        CourseName.setForeground(new java.awt.Color(227, 227, 227));

        CourseID.setBackground(new java.awt.Color(199, 50, 38));
        CourseID.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        CourseID.setForeground(new java.awt.Color(227, 227, 227));

        CourseDescription.setEditable(false);
        CourseDescription.setBackground(new java.awt.Color(26, 24, 26));
        CourseDescription.setColumns(20);
        CourseDescription.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        CourseDescription.setForeground(new java.awt.Color(227, 227, 227));
        CourseDescription.setLineWrap(true);
        CourseDescription.setRows(5);
        CourseDescription.setWrapStyleWord(true);
        jScrollPane2.setViewportView(CourseDescription);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(100, 23));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 130));
        jScrollPane1.setVerifyInputWhenFocusTarget(false);

        CoursesList.setBackground(new java.awt.Color(53, 55, 61));
        CoursesList.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        CoursesList.setForeground(new java.awt.Color(227, 227, 227));
        CoursesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        CoursesList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CoursesList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                CoursesListMouseClicked(evt);
            }
        });
        CoursesList.addListSelectionListener(new javax.swing.event.ListSelectionListener()
        {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt)
            {
                CoursesListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(CoursesList);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(CourseIDTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CourseID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(19, 19, 19))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(CourseDescriptionTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 5, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(CourseNameTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CourseName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CourseIDTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CourseID, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(CourseNameTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CourseName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CourseDescriptionTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(148, 148, 148))
        );

        kGradientPanel4.add(jPanel3);

        CoursesCenterPanel.add(kGradientPanel4);

        kGradientPanel3.setkEndColor(new java.awt.Color(26, 24, 26));
        kGradientPanel3.setkStartColor(new java.awt.Color(26, 24, 26));
        kGradientPanel3.setMaximumSize(new java.awt.Dimension(770, 175));
        kGradientPanel3.setMinimumSize(new java.awt.Dimension(771, 175));
        kGradientPanel3.setName(""); // NOI18N
        kGradientPanel3.setPreferredSize(new java.awt.Dimension(771, 75));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 50, 15);
        flowLayout1.setAlignOnBaseline(true);
        kGradientPanel3.setLayout(flowLayout1);

        AddNewCourseButton.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        AddNewCourseButton.setForeground(new java.awt.Color(51, 51, 51));
        AddNewCourseButton.setText("Add New Course");
        AddNewCourseButton.setPreferredSize(new java.awt.Dimension(135, 50));
        AddNewCourseButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                AddNewCourseButtonActionPerformed(evt);
            }
        });
        kGradientPanel3.add(AddNewCourseButton);

        DuplicateCourseButton.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        DuplicateCourseButton.setForeground(new java.awt.Color(51, 51, 51));
        DuplicateCourseButton.setText("Duplicate");
        DuplicateCourseButton.setMaximumSize(new java.awt.Dimension(77, 50));
        DuplicateCourseButton.setMinimumSize(new java.awt.Dimension(77, 50));
        DuplicateCourseButton.setPreferredSize(new java.awt.Dimension(135, 50));
        DuplicateCourseButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                DuplicateCourseButtonActionPerformed(evt);
            }
        });
        kGradientPanel3.add(DuplicateCourseButton);

        RemoveCourseButton.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        RemoveCourseButton.setForeground(new java.awt.Color(51, 51, 51));
        RemoveCourseButton.setText("Remove");
        RemoveCourseButton.setPreferredSize(new java.awt.Dimension(135, 50));
        RemoveCourseButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                RemoveCourseButtonActionPerformed(evt);
            }
        });
        kGradientPanel3.add(RemoveCourseButton);

        UndoButton.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        UndoButton.setForeground(new java.awt.Color(51, 51, 51));
        UndoButton.setText("Undo");
        UndoButton.setEnabled(false);
        UndoButton.setPreferredSize(new java.awt.Dimension(135, 50));
        UndoButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                UndoButtonActionPerformed(evt);
            }
        });
        kGradientPanel3.add(UndoButton);

        CoursesCenterPanel.add(kGradientPanel3);

        kGradientPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        kGradientPanel5.setkEndColor(new java.awt.Color(26, 24, 26));
        kGradientPanel5.setkStartColor(new java.awt.Color(26, 24, 26));
        kGradientPanel5.setMaximumSize(new java.awt.Dimension(32767, 10000));
        kGradientPanel5.setMinimumSize(new java.awt.Dimension(100, 100));
        kGradientPanel5.setName(""); // NOI18N
        kGradientPanel5.setPreferredSize(new java.awt.Dimension(771, 250));

        CoursesMainTitle1.setBackground(new java.awt.Color(199, 50, 38));
        CoursesMainTitle1.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CoursesMainTitle1.setForeground(new java.awt.Color(227, 227, 227));
        CoursesMainTitle1.setText("Sections");
        CoursesMainTitle1.setToolTipText("");

        AddSectionButton.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        AddSectionButton.setForeground(new java.awt.Color(51, 51, 51));
        AddSectionButton.setText("Add Section");
        AddSectionButton.setPreferredSize(new java.awt.Dimension(135, 50));
        AddSectionButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                AddSectionButtonActionPerformed(evt);
            }
        });

        jScrollPane3.setMinimumSize(new java.awt.Dimension(100, 23));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(400, 130));
        jScrollPane3.setVerifyInputWhenFocusTarget(false);

        SectionsList.setBackground(new java.awt.Color(53, 55, 61));
        SectionsList.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        SectionsList.setForeground(new java.awt.Color(227, 227, 227));
        SectionsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        SectionsList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        SectionsList.addListSelectionListener(new javax.swing.event.ListSelectionListener()
        {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt)
            {
                SectionsListValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(SectionsList);

        RemoveSectionButton.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        RemoveSectionButton.setForeground(new java.awt.Color(51, 51, 51));
        RemoveSectionButton.setText("Remove Section");
        RemoveSectionButton.setPreferredSize(new java.awt.Dimension(135, 50));
        RemoveSectionButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                RemoveSectionButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel5Layout = new javax.swing.GroupLayout(kGradientPanel5);
        kGradientPanel5.setLayout(kGradientPanel5Layout);
        kGradientPanel5Layout.setHorizontalGroup(
            kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel5Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CoursesMainTitle1)
                    .addGroup(kGradientPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addGroup(kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AddSectionButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(RemoveSectionButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        kGradientPanel5Layout.setVerticalGroup(
            kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CoursesMainTitle1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(kGradientPanel5Layout.createSequentialGroup()
                        .addComponent(AddSectionButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(RemoveSectionButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        CoursesCenterPanel.add(kGradientPanel5);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            .addGap(0, 564, Short.MAX_VALUE)
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 635, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            .addGap(0, 282, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 48, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(AvgSuccessTitle)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(29, 29, 29)
                            .addComponent(ChartImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(ViewAvgSuccess, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 48, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 214, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 8, Short.MAX_VALUE)
                    .addComponent(AvgSuccessTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(4, 4, 4)
                    .addComponent(ChartImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(ViewAvgSuccess)
                    .addGap(0, 8, Short.MAX_VALUE)))
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
            .addGap(0, 282, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 30, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(AvgAttendanceTitle)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGap(46, 46, 46)
                            .addComponent(ChartImage2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(ViewAvgAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 31, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 214, Short.MAX_VALUE)
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
            .addGap(0, 282, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 76, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(CourseDataTitle)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(ImportImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(DBImportBut, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 76, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 214, Short.MAX_VALUE)
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
            .addGap(0, 282, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(0, 76, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(CourseDataTitle2)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(ExportImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(DbExportBut, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 76, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 214, Short.MAX_VALUE)
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

        if (CoursesList.getSelectedIndex() != -1 && !evt.getValueIsAdjusting())
        {
            DuplicateCourseButton.setEnabled(true);
            RemoveCourseButton.setEnabled(true);
            SelectCourse(CoursesList.getSelectedIndex());
            SectionsList.setEnabled(true);
            AddSectionButton.setEnabled(true);
        } else
        {
            if (CoursesList.getSelectedIndex() == -1)
            {
                SectionsList.setEnabled(false);
                AddSectionButton.setEnabled(false);
                RemoveSectionButton.setEnabled(false);
            }
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

        if (lastAction.GetIndex() == 1)
        {

            courseManager.RemoveCourse(lastAction.GetSubject());
        } else
        {
            if (lastAction.GetIndex() == 2)
            {
                courseManager.AddNewCourse(lastAction.GetSubject(), lastAction.GetListIndex());
            }
        }

        courseActions.remove(lastAction);

        if (courseActions.size() < 1)
        {
            UndoButton.setEnabled(false);
        }

    }//GEN-LAST:event_UndoButtonActionPerformed

    private void AddNewCourseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddNewCourseButtonActionPerformed
        // TODO add your handling code here:
        AddNewCourseDialog.show();
    }//GEN-LAST:event_AddNewCourseButtonActionPerformed

    private void AddCourseCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddCourseCancelButtonActionPerformed
        // TODO add your handling code here:
        DisposeAddNewCourseDialog();
    }//GEN-LAST:event_AddCourseCancelButtonActionPerformed

    private void AddCourseIDFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AddCourseIDFieldKeyReleased
        // TODO add your handling code here:
        if (courseManager.CheckIfExists(AddCourseIDField.getText()))
        {
            AddCourseWarning.setText("This course already exists!");
            AddCourseWarning.setVisible(true);
            AddCourseAddButton.setEnabled(false);
        } else
        {
            if (AddCourseIDField.getText().equals(""))
            {
                AddCourseWarning.setText("Course ID can not be empty!");
                AddCourseWarning.setVisible(true);
                AddCourseAddButton.setEnabled(false);
            } else
            {
                AddCourseWarning.setVisible(false);
                AddCourseAddButton.setEnabled(true);
            }
        }
    }//GEN-LAST:event_AddCourseIDFieldKeyReleased

    private void AddCourseAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddCourseAddButtonActionPerformed
        // TODO add your handling code here:

        courseManager.AddNewCourse(AddCourseIDField.getText(), AddCourseNameField.getText(), AddCourseDescField.getText());
        DisposeAddNewCourseDialog();
    }//GEN-LAST:event_AddCourseAddButtonActionPerformed

    private void EditCourseIDFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EditCourseIDFieldKeyReleased
        // TODO add your handling code here:

        if (!courseManager.GetCourse(i_SelectedCourse).GetID().equals(EditCourseIDField.getText()))
        {
            if (courseManager.CheckIfExists(EditCourseIDField.getText()))
            {
                EditCourseWarning.setText("This course already exists!");
                EditCourseWarning.setVisible(true);
                EditCourseSaveButton.setEnabled(false);
            } else
            {
                if (EditCourseIDField.getText().equals(""))
                {
                    EditCourseWarning.setText("Course ID can not be empty!");
                    EditCourseWarning.setVisible(true);
                    EditCourseSaveButton.setEnabled(false);
                } else
                {
                    EditCourseWarning.setVisible(false);
                    EditCourseSaveButton.setEnabled(true);
                }
            }
        } else
        {
            EditCourseWarning.setVisible(false);
            EditCourseSaveButton.setEnabled(true);
        }


    }//GEN-LAST:event_EditCourseIDFieldKeyReleased

    private void EditCourseCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditCourseCancelButtonActionPerformed
        // TODO add your handling code here:
        DisposeEditCourseDialog();
    }//GEN-LAST:event_EditCourseCancelButtonActionPerformed

    private void EditCourseSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditCourseSaveButtonActionPerformed
        // TODO add your handling code here:
        courseManager.GetCourse(i_SelectedCourse).Edit(EditCourseIDField.getText(), EditCourseNameField.getText(), EditCourseDescField.getText());
        UpdateCourseList(i_SelectedCourse);

        DisposeEditCourseDialog();
    }//GEN-LAST:event_EditCourseSaveButtonActionPerformed

    private void EditCourseIDFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditCourseIDFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EditCourseIDFieldActionPerformed

    private void CoursesListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CoursesListMouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 2)
        {
            Course c = courseManager.GetCourse(i_SelectedCourse);

            EditCourseIDField.setText(c.GetID());
            EditCourseNameField.setText(c.GetName());
            EditCourseDescField.setText(c.GetDescription());

            EditCourseDialog.show();

        }
    }//GEN-LAST:event_CoursesListMouseClicked

    private void AddSectionNameFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AddSectionNameFieldKeyReleased
        // TODO add your handling code here:

        if (courseManager.GetCourse(i_SelectedCourse).CheckIfSectionExists(AddSectionNameField.getText()))
        {
            AddSectionWarning.setText("This section already exists!");
            AddSectionAddButton.setEnabled(false);
            AddSectionWarning.setVisible(true);
        } else
        {
            if (AddSectionNameField.getText().equals(""))
            {
                AddSectionWarning.setText("Section name can not be empty!");
                AddSectionAddButton.setEnabled(false);
                AddSectionWarning.setVisible(true);
            } else
            {
                AddSectionAddButton.setEnabled(true);
                AddSectionWarning.setVisible(false);
            }
        }
    }//GEN-LAST:event_AddSectionNameFieldKeyReleased

    private void AddSectionCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddSectionCancelButtonActionPerformed
        // TODO add your handling code here:

        DisposeAddSectionDialog();
    }//GEN-LAST:event_AddSectionCancelButtonActionPerformed

    private void AddSectionAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddSectionAddButtonActionPerformed
        // TODO add your handling code here:
        courseManager.AddSectionToCourse(i_SelectedCourse, AddSectionNameField.getText());
        DisposeAddSectionDialog();
    }//GEN-LAST:event_AddSectionAddButtonActionPerformed

    private void SectionsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_SectionsListValueChanged
        // TODO add your handling code here:

        if (SectionsList.getSelectedIndex() != -1 && !evt.getValueIsAdjusting())
        {
            if (courseManager.GetCourse(i_SelectedCourse).GetSections().size() < 2)
            {
                RemoveSectionButton.setEnabled(false);
            } else
            {
                RemoveSectionButton.setEnabled(true);
            }
            SelectSection(SectionsList.getSelectedIndex());
        }

        if (courseManager.GetCourse(i_SelectedCourse).GetSections().size() < 2)
        {
            RemoveSectionButton.setEnabled(false);
        }


    }//GEN-LAST:event_SectionsListValueChanged

    private void AddSectionButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_AddSectionButtonActionPerformed
    {//GEN-HEADEREND:event_AddSectionButtonActionPerformed
        // TODO add your handling code here:
        AddSectionDialog.show();
    }//GEN-LAST:event_AddSectionButtonActionPerformed

    private void RemoveSectionButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_RemoveSectionButtonActionPerformed
    {//GEN-HEADEREND:event_RemoveSectionButtonActionPerformed
        // TODO add your handling code here:
        courseManager.RemoveSectionFromCourse(i_SelectedCourse);
    }//GEN-LAST:event_RemoveSectionButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {

        // Set anti aliasing on for fonts.
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Metal".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(UIManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(UIManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(UIManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(UIManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new UIManager().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddCourseAddButton;
    private javax.swing.JButton AddCourseCancelButton;
    private javax.swing.JLabel AddCourseDesc;
    private javax.swing.JTextArea AddCourseDescField;
    private javax.swing.JScrollPane AddCourseDescPane;
    private javax.swing.JLabel AddCourseID;
    private javax.swing.JTextField AddCourseIDField;
    private javax.swing.JLabel AddCourseName;
    private javax.swing.JTextField AddCourseNameField;
    private javax.swing.JLabel AddCourseWarning;
    private javax.swing.JButton AddNewCourseButton;
    private javax.swing.JDialog AddNewCourseDialog;
    private javax.swing.JButton AddSectionAddButton;
    private javax.swing.JButton AddSectionButton;
    private javax.swing.JButton AddSectionCancelButton;
    private javax.swing.JDialog AddSectionDialog;
    private javax.swing.JLabel AddSectionName;
    private javax.swing.JTextField AddSectionNameField;
    private javax.swing.JLabel AddSectionWarning;
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
    private javax.swing.JTextArea CourseDescription;
    private javax.swing.JLabel CourseDescriptionTitle;
    private javax.swing.JLabel CourseID;
    private javax.swing.JLabel CourseIDTitle;
    private javax.swing.JLabel CourseName;
    private javax.swing.JLabel CourseNameTitle;
    private keeptoo.KGradientPanel CoursesCenterPanel;
    private javax.swing.JLabel CoursesImage;
    private javax.swing.JLabel CoursesLabel;
    private javax.swing.JList<String> CoursesList;
    private javax.swing.JPanel CoursesMainPanel;
    private javax.swing.JLabel CoursesMainTitle;
    private javax.swing.JLabel CoursesMainTitle1;
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
    private javax.swing.JButton EditCourseCancelButton;
    private javax.swing.JLabel EditCourseDesc;
    private javax.swing.JTextArea EditCourseDescField;
    private javax.swing.JScrollPane EditCourseDescPane;
    private javax.swing.JDialog EditCourseDialog;
    private javax.swing.JLabel EditCourseID;
    private javax.swing.JTextField EditCourseIDField;
    private javax.swing.JLabel EditCourseName;
    private javax.swing.JTextField EditCourseNameField;
    private javax.swing.JButton EditCourseSaveButton;
    private javax.swing.JLabel EditCourseWarning;
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
    private javax.swing.JButton RemoveSectionButton;
    private javax.swing.JLabel ReportsImage;
    private javax.swing.JLabel ReportsLabel;
    private javax.swing.JPanel ReportsMainPanel;
    private javax.swing.JPanel ReportsPanel;
    private javax.swing.JLabel ReportsSelectedCourse;
    private javax.swing.JPanel ReportsWrapper;
    private javax.swing.JList<String> SectionsList;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private keeptoo.KGradientPanel kGradientPanel2;
    private keeptoo.KGradientPanel kGradientPanel3;
    private keeptoo.KGradientPanel kGradientPanel4;
    private keeptoo.KGradientPanel kGradientPanel5;
    // End of variables declaration//GEN-END:variables
}
