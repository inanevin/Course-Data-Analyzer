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
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

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

        /*try
        {
            resourceManager.LoadStudentXLSX();
        } catch (IOException ex)
        {
            Logger.getLogger(UIManager.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        SetIcons();
        InitializeMenuBarItems();
        AddComponentListeners();

        AddCourseWarning.setVisible(false);
        EditCourseWarning.setVisible(false);
        AddSectionWarning.setVisible(false);
        EditSectionWarning.setVisible(false);
        AddLOWarning.setVisible(false);
        SaveLOWarning.setVisible(false);

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

    public void SelectCourse(int course)
    {
        i_SelectedCourse = course;

        if (i_SelectedCourse != -1)
        {
            SelectSection(courseManager.GetCourse(i_SelectedCourse).GetSelectedSectionIndex());

        } else
        {
            UpdateSections();
        }

        UpdateCourseList();
        UpdateSyllabus();
        UpdateExams();

    }

    public void SelectExam(int exam)
    {
        courseManager.GetCourse(i_SelectedCourse).SetSelectedExam(exam);
        UpdateExams();
    }

    public void SelectSection(int section)
    {
        if (i_SelectedCourse != -1)
        {
            courseManager.GetCourse(i_SelectedCourse).SetSection(section);
        }

        UpdateSections();
        UpdateStudents();
    }

    public void SelectSyllabusWeek(int week)
    {
        if (i_SelectedCourse != -1)
        {
            courseManager.GetCourse(i_SelectedCourse).GetSyllabus().SetSelectedWeek(week);
        }

        UpdateSyllabus();
    }

    public void SelectLearningOutcome(int outcome)
    {
        if (i_SelectedCourse != -1)
        {
            courseManager.GetCourse(i_SelectedCourse).GetSyllabus().SetSelectedLO(outcome);
        }

        UpdateSyllabus();
    }

    public void UpdateCourseList()
    {
        if (i_SelectedCourse == -1)
        {
            DuplicateCourseButton.setEnabled(false);
            RemoveCourseButton.setEnabled(false);

            CourseID.setText("");
            CourseName.setText("");
            CourseDescription.setText("");
        } else
        {
            DuplicateCourseButton.setEnabled(true);
            RemoveCourseButton.setEnabled(true);

            String id = courseManager.GetCourse(i_SelectedCourse).GetID();
            String name = courseManager.GetCourse(i_SelectedCourse).GetName();
            String desc = courseManager.GetCourse(i_SelectedCourse).GetDescription();
            CourseID.setText(id);
            CourseName.setText(name);
            CourseDescription.setText(desc);
        }

        ArrayList<Course> courses = courseManager.GetCourseList();
        DefaultListModel courseListModel = new DefaultListModel();
        DefaultComboBoxModel courseComboBoxModel = new DefaultComboBoxModel();

        for (int i = 0; i < courses.size(); i++)
        {
            String elementDisplay = (new StringBuilder()).append(courses.get(i).GetID()).append(" - ").append(courses.get(i).GetName()).toString();
            courseListModel.addElement(elementDisplay);
            courseComboBoxModel.addElement(elementDisplay);
        }

        CoursesList.setModel(courseListModel);
        CourseSelectionComboBox.setModel(courseComboBoxModel);

        CoursesList.setSelectedIndex(i_SelectedCourse);
        CourseSelectionComboBox.setSelectedIndex(i_SelectedCourse);

        courseManager.SaveCourses();
    }

    public void UpdateSections()
    {
        DefaultListModel m = new DefaultListModel();
        DefaultComboBoxModel cm = new DefaultComboBoxModel();

        if (i_SelectedCourse != -1)
        {
            ArrayList<Section> sections = new ArrayList<Section>();
            sections = courseManager.GetCourse(i_SelectedCourse).GetSections();

            for (int i = 0; i < sections.size(); i++)
            {
                String elementDisplay = (new StringBuilder()).append(sections.get(i).GetName()).toString();
                m.addElement(elementDisplay);
                cm.addElement(elementDisplay);
            }

            if (courseManager.GetCourse(i_SelectedCourse).GetSections().size() > 1)
            {
                RemoveSectionButton.setEnabled(true);
            } else
            {
                RemoveSectionButton.setEnabled(false);
            }

            AddSectionButton.setEnabled(true);

        } else
        {
            AddSectionButton.setEnabled(false);
            RemoveSectionButton.setEnabled(false);
        }

        SectionsList.setModel(m);
        SectionSelectionComboBox.setModel(cm);
        int toSelect = i_SelectedCourse == -1 ? -1 : courseManager.GetCourse(i_SelectedCourse).GetSelectedSectionIndex();
        SectionsList.setSelectedIndex(toSelect);
        SectionSelectionComboBox.setSelectedIndex(toSelect);

        courseManager.SaveCourses();
    }

    private void UpdateStudents()
    {
        if (i_SelectedCourse == -1)
        {

            return;
        }

        ArrayList<Student> students = courseManager.GetCourse(i_SelectedCourse).GetSelectedSection().GetStudents();
        DefaultListModel studentsModel = new DefaultListModel();

        for (int i = 0; i < students.size(); i++)
        {
            String elementDisplay = new StringBuilder().append(students.get(i).getID()).toString();
            studentsModel.addElement(elementDisplay);
        }

        StudentList.setModel(studentsModel);

        courseManager.SaveCourses();
    }

    private void UpdateExams()
    {
        if (i_SelectedCourse == -1)
        {
            return;
        }

        int selectedExam = courseManager.GetCourse(i_SelectedCourse).GetSelectedExamIndex();

        ArrayList<Exam> exams = courseManager.GetCourse(i_SelectedCourse).GetExams();
        DefaultListModel examsModel = new DefaultListModel();

        for (int i = 0; i < exams.size(); i++)
        {
            String elementDisplay = new StringBuilder().append(exams.get(i).getType().toString()).toString();
            examsModel.addElement(elementDisplay);
        }

        if (selectedExam == -1)
        {
            RemoveExamButton.setEnabled(false);
            ExamEditPanel.setVisible(false);

        } else
        {
            RemoveExamButton.setEnabled(true);
            ExamEditPanel.setVisible(true);

            ExamTypeComboBox.setSelectedIndex(courseManager.GetCourse(i_SelectedCourse).GetSelectedExam().getType().getValue());
            ExamDateChooser.setDate(courseManager.GetCourse(i_SelectedCourse).GetSelectedExam().getDate());

            ExamPercentageField.setText(Integer.toString(courseManager.GetCourse(i_SelectedCourse).GetSelectedExam().getPercentage()));

        }

        ExamList.setModel(examsModel);
        ExamList.setSelectedIndex(selectedExam);
        courseManager.SaveCourses();
    }

    private void UpdateSyllabus()
    {
        if (i_SelectedCourse == -1)
        {
            SyllabusStartDateChooser.setDate(null);
            SyllabusEndDateChooser.setDate(null);
            SyllabusWeekTopicsArea.setText("");
            SyllabusWeekList.setModel(new DefaultListModel());
            LearningOutcomeList.setModel(new DefaultListModel());
            return;
        }

        ArrayList<Week> weeks = courseManager.GetCourse(i_SelectedCourse).GetSyllabus().getWeeks();

        DefaultListModel syllabusWeeksModel = new DefaultListModel();

        for (int i = 0; i < weeks.size(); i++)
        {
            String elementDisplay = (new StringBuilder()).append("Week ").append((i + 1)).toString();
            syllabusWeeksModel.addElement(elementDisplay);
        }

        Date start = courseManager.GetCourse(i_SelectedCourse).GetSyllabus().getStartDate();
        Date end = courseManager.GetCourse(i_SelectedCourse).GetSyllabus().getEndDate();

        SyllabusStartDateChooser.setDate(start);
        SyllabusEndDateChooser.setDate(end);

        if (start == null)
        {
            SyllabusEndDateChooser.setEnabled(false);
        }

        if (courseManager.GetCourse(i_SelectedCourse).GetSyllabus().getWeeks().size() > 0)
        {
            String topic = courseManager.GetCourse(i_SelectedCourse).GetSyllabus().getWeeks().get(0).getTopic();
            SyllabusWeekTopicsArea.setText(topic);
            SyllabusWeekTopicsArea.setEditable(true);
        } else
        {
            SyllabusWeekTopicsArea.setText("");
            SyllabusWeekTopicsArea.setEditable(false);
        }

        SyllabusWeekList.setModel(syllabusWeeksModel);
        SyllabusWeekList.setSelectedIndex(courseManager.GetCourse(i_SelectedCourse).GetSyllabus().GetSelectedWeek());

        ArrayList<String> lo = courseManager.GetCourse(i_SelectedCourse).GetSyllabus().getLearningOutcomes();

        if (lo == null)
        {
            return;
        }

        DefaultListModel m = new DefaultListModel();

        for (int i = 0; i < lo.size(); i++)
        {
            String elementDisplay = (new StringBuilder()).append("L.O  ").append((i + 1) + ": ").append(lo.get(i)).toString();
            m.addElement(elementDisplay);

        }

        LearningOutcomeList.setModel(m);

        int loIndex = courseManager.GetCourse(i_SelectedCourse).GetSyllabus().GetSelectedLO();
        LearningOutcomeList.setSelectedIndex(loIndex);

        System.out.println(loIndex);
        if (loIndex == -1)
        {
            RemoveLearningOutcomeButton.setEnabled(false);
        } else
        {
            RemoveLearningOutcomeButton.setEnabled(true);
        }

        courseManager.SaveCourses();
    }

    private void InitializeMenuBarItems()
    {
        // Create objects for menu bar items.
        MenuBarItem dashboard = new MenuBarItem(DashboardWrapper, DashboardMainPanel, null, null);
        MenuBarItem courses = new MenuBarItem(CoursesWrapper, CoursesMainPanel, null, null);
        MenuBarItem students = new MenuBarItem(StudentsWrapper, StudentsMainPanel, StudentsScrollPane, StudentsNoCoursePanel);
        MenuBarItem syllabus = new MenuBarItem(SyllabusWrapper, SyllabusMainPanel, SyllabusScrollPane, SyllabusNoCoursePanel);
        MenuBarItem exams = new MenuBarItem(ExamsWrapper, ExamsMainPanel, ExamsScrollPane, ExamsNoCoursePanel);
        MenuBarItem reports = new MenuBarItem(ReportsWrapper, ReportsMainPanel, ReportsScrollPane, ReportsNoCoursePanel);

        // Init list
        menuBarItems = new ArrayList<MenuBarItem>();

        // Init current selected menu
        currentSelectedMenu = null;

        // Add objets.
        menuBarItems.add(dashboard);
        menuBarItems.add(courses);
        menuBarItems.add(students);
        menuBarItems.add(syllabus);
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
        MouseAdapterForMenu exm = new MouseAdapterForMenu(4, this);
        MouseAdapterForMenu rp = new MouseAdapterForMenu(5, this);

        // Add listeners to labels
        DashboardLabel.addMouseListener(db);
        CoursesLabel.addMouseListener(cs);
        StudentsLabel.addMouseListener(st);
        SyllabusLabel.addMouseListener(syl);
        ExamsLabel.addMouseListener(exm);
        ReportsLabel.addMouseListener(rp);

        // Add listeners to wrappers.
        DashboardWrapper.addMouseListener(db);
        CoursesWrapper.addMouseListener(cs);
        StudentsWrapper.addMouseListener(st);
        SyllabusWrapper.addMouseListener(syl);
        ExamsWrapper.addMouseListener(exm);
        ReportsWrapper.addMouseListener(rp);

        // Add listeners to images. (icons)
        DashboardImage.addMouseListener(db);
        CoursesImage.addMouseListener(cs);
        StudentsImage.addMouseListener(st);
        SyllabusImage.addMouseListener(syl);
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

        if (menu.GetNoCoursePanel() != null)
        {
            menu.GetNoCoursePanel().setVisible(i_SelectedCourse == -1);
        }

        if (menu.GetInnerPanel() != null)
        {
            menu.GetInnerPanel().setVisible(i_SelectedCourse != -1);
        }

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
        AddCourseAddButton.setEnabled(false);
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
        AddSectionAddButton.setEnabled(false);

        AddSectionDialog.dispose();
    }

    private void DisposeEditSectionDialog()
    {
        EditSectionNameField.setText("");
        EditSectionWarning.setVisible(false);
        EditSectionSaveButton.setEnabled(false);

        EditSectionDialog.dispose();
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
        EditSectionDialog = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        EditSectionName = new javax.swing.JLabel();
        EditSectionNameField = new javax.swing.JTextField();
        EditSectionCancel = new javax.swing.JButton();
        EditSectionWarning = new javax.swing.JLabel();
        EditSectionSaveButton = new javax.swing.JButton();
        AddLearningOutcomeDialog = new javax.swing.JDialog();
        jPanel13 = new javax.swing.JPanel();
        AddLOLabel = new javax.swing.JLabel();
        AddLOCancel = new javax.swing.JButton();
        AddLOAddButton = new javax.swing.JButton();
        AddLOWarning = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        AddLOTextField = new javax.swing.JTextArea();
        EditLearningOutcomeDialog = new javax.swing.JDialog();
        jPanel15 = new javax.swing.JPanel();
        SaveLOLabel = new javax.swing.JLabel();
        SaveLOCancel = new javax.swing.JButton();
        SaveLOSaveButton = new javax.swing.JButton();
        SaveLOWarning = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        SaveLOTextField = new javax.swing.JTextArea();
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
        SyllabusPanel = new javax.swing.JPanel();
        SyllabusLabel = new javax.swing.JLabel();
        SyllabusWrapper = new javax.swing.JPanel();
        SyllabusImage = new javax.swing.JLabel();
        StudentsPanel = new javax.swing.JPanel();
        StudentsLabel = new javax.swing.JLabel();
        StudentsWrapper = new javax.swing.JPanel();
        StudentsImage = new javax.swing.JLabel();
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
        SelectedCourseLabel = new javax.swing.JLabel();
        SelectedSectionLabel = new javax.swing.JLabel();
        SectionSelectionComboBox = new javax.swing.JComboBox<>();
        CourseSelectionComboBox = new javax.swing.JComboBox<>();
        TopLeft = new javax.swing.JPanel();
        Title = new javax.swing.JLabel();
        SettingsImage = new javax.swing.JLabel();
        TopRight = new javax.swing.JPanel();
        Center = new javax.swing.JPanel();
        ExamsMainPanel = new javax.swing.JPanel();
        DBTitlePanel4 = new javax.swing.JPanel();
        DashboardMainTitle4 = new javax.swing.JLabel();
        ExamsMP = new keeptoo.KGradientPanel();
        ExamsNoCoursePanel = new keeptoo.KGradientPanel();
        NoCourseSelectedLabel1 = new javax.swing.JLabel();
        ExamsScrollPane = new javax.swing.JScrollPane();
        ExamsInnerPanel = new keeptoo.KGradientPanel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        ExamList = new javax.swing.JList<>();
        DashboardMainTitle6 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        AddExamButton = new javax.swing.JButton();
        RemoveExamButton = new javax.swing.JButton();
        ExamEditPanel = new javax.swing.JPanel();
        DashboardMainTitle8 = new javax.swing.JLabel();
        ExamTypeComboBox = new javax.swing.JComboBox<>();
        DashboardMainTitle9 = new javax.swing.JLabel();
        ExamDateChooser = new com.toedter.calendar.JDateChooser();
        DashboardMainTitle10 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        DashboardMainTitle7 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        ImportAttendanceData3 = new javax.swing.JButton();
        ImportAttendanceData4 = new javax.swing.JButton();
        DashboardMainTitle11 = new javax.swing.JLabel();
        DashboardMainTitle12 = new javax.swing.JLabel();
        DashboardMainTitle13 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        ExamPercentageField = new javax.swing.JTextField();
        ReportsMainPanel = new javax.swing.JPanel();
        DBTitlePanel5 = new javax.swing.JPanel();
        DashboardMainTitle5 = new javax.swing.JLabel();
        ReportsMP = new javax.swing.JPanel();
        ReportsNoCoursePanel = new keeptoo.KGradientPanel();
        NoCourseSelectedLabel2 = new javax.swing.JLabel();
        ReportsScrollPane = new javax.swing.JScrollPane();
        ReportsInnerPanel = new keeptoo.KGradientPanel();
        Grid29 = new javax.swing.JPanel();
        ViewAvgSuccess7 = new javax.swing.JButton();
        ChartImage15 = new javax.swing.JLabel();
        AvgSuccessTitle7 = new javax.swing.JLabel();
        Grid30 = new javax.swing.JPanel();
        ViewAvgAttendance7 = new javax.swing.JButton();
        ChartImage16 = new javax.swing.JLabel();
        AvgAttendanceTitle7 = new javax.swing.JLabel();
        Grid31 = new javax.swing.JPanel();
        DBImportBut7 = new javax.swing.JButton();
        ImportImage7 = new javax.swing.JLabel();
        CourseDataTitle14 = new javax.swing.JLabel();
        Grid32 = new javax.swing.JPanel();
        DbExportBut7 = new javax.swing.JButton();
        ExportImage7 = new javax.swing.JLabel();
        CourseDataTitle15 = new javax.swing.JLabel();
        SyllabusMainPanel = new javax.swing.JPanel();
        SyllabusTitlePanel = new javax.swing.JPanel();
        SyllabusMainTitle = new javax.swing.JLabel();
        SylMainPanel = new javax.swing.JPanel();
        SyllabusScrollPane = new javax.swing.JScrollPane();
        SyllabusInnerPanel = new keeptoo.KGradientPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        SyllabusStartDateChooser = new com.toedter.calendar.JDateChooser();
        SyllabusEndDateChooser = new com.toedter.calendar.JDateChooser();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        SyllabusMainTitle1 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        SyllabusWeekList = new javax.swing.JList<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        SyllabusWeekTopicsArea = new javax.swing.JTextArea();
        SyllabusMainTitle3 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        SyllabusMainTitle2 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        LearningOutcomeList = new javax.swing.JList<>();
        RemoveLearningOutcomeButton = new javax.swing.JButton();
        AddNewLearningOutcomeButton = new javax.swing.JButton();
        SyllabusNoCoursePanel = new keeptoo.KGradientPanel();
        NoCourseSelectedLabel4 = new javax.swing.JLabel();
        StudentsMainPanel = new javax.swing.JPanel();
        DBTitlePanel6 = new javax.swing.JPanel();
        StudentsMainTitle = new javax.swing.JLabel();
        StudentsMP = new javax.swing.JPanel();
        StudentsScrollPane = new javax.swing.JScrollPane();
        StudentsInnerPanel = new keeptoo.KGradientPanel();
        StudentsIP_Panel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        ImportAttendanceData = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        StudentList = new javax.swing.JList<>();
        StudentsIP_Panel2 = new javax.swing.JPanel();
        StudentsNoCoursePanel = new keeptoo.KGradientPanel();
        NoCourseSelectedLabel = new javax.swing.JLabel();
        CoursesMainPanel = new javax.swing.JPanel();
        CoursesTitlePanel = new javax.swing.JPanel();
        CoursesMainTitle = new javax.swing.JLabel();
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
        DashboardMainPanel = new javax.swing.JPanel();
        DBMain = new javax.swing.JPanel();
        DashboardTitlePanel = new keeptoo.KGradientPanel();
        DashboardMainTitle = new javax.swing.JLabel();
        DBCenterPanel = new keeptoo.KGradientPanel();
        jPanel2 = new javax.swing.JPanel();
        AvgSuccessTitle = new javax.swing.JLabel();
        ChartImage1 = new javax.swing.JLabel();
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

        EditSectionDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        EditSectionDialog.setTitle("Add New Course");
        EditSectionDialog.setAlwaysOnTop(true);
        EditSectionDialog.setBackground(new java.awt.Color(26, 24, 26));
        EditSectionDialog.setBounds(new java.awt.Rectangle(0, 0, 800, 600));
        EditSectionDialog.setModal(true);
        EditSectionDialog.getContentPane().setLayout(new javax.swing.OverlayLayout(EditSectionDialog.getContentPane()));

        jPanel11.setBackground(new java.awt.Color(26, 24, 26));

        EditSectionName.setBackground(new java.awt.Color(26, 24, 26));
        EditSectionName.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        EditSectionName.setForeground(new java.awt.Color(227, 227, 227));
        EditSectionName.setText("Name:");
        EditSectionName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        EditSectionName.setPreferredSize(new java.awt.Dimension(100, 50));

        EditSectionNameField.setBackground(new java.awt.Color(26, 24, 26));
        EditSectionNameField.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        EditSectionNameField.setForeground(new java.awt.Color(227, 227, 227));
        EditSectionNameField.setCaretColor(new java.awt.Color(204, 204, 204));
        EditSectionNameField.setPreferredSize(new java.awt.Dimension(200, 40));
        EditSectionNameField.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                EditSectionNameFieldKeyReleased(evt);
            }
        });

        EditSectionCancel.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        EditSectionCancel.setText("Cancel");
        EditSectionCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                EditSectionCancelActionPerformed(evt);
            }
        });

        EditSectionWarning.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        EditSectionWarning.setForeground(new java.awt.Color(204, 0, 51));
        EditSectionWarning.setText("This Section Already Exists!");
        EditSectionWarning.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        EditSectionWarning.setPreferredSize(new java.awt.Dimension(121, 125));

        EditSectionSaveButton.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        EditSectionSaveButton.setText("Save Section");
        EditSectionSaveButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                EditSectionSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(EditSectionSaveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(EditSectionName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(EditSectionCancel)
                        .addGap(34, 34, 34)
                        .addComponent(EditSectionWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(EditSectionNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE))
                .addGap(33, 33, 33))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EditSectionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EditSectionNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EditSectionSaveButton)
                    .addComponent(EditSectionCancel)
                    .addComponent(EditSectionWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        EditSectionDialog.getContentPane().add(jPanel11);

        AddLearningOutcomeDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        AddLearningOutcomeDialog.setTitle("Add New Course");
        AddLearningOutcomeDialog.setAlwaysOnTop(true);
        AddLearningOutcomeDialog.setBackground(new java.awt.Color(26, 24, 26));
        AddLearningOutcomeDialog.setBounds(new java.awt.Rectangle(0, 0, 800, 600));
        AddLearningOutcomeDialog.setModal(true);
        AddLearningOutcomeDialog.getContentPane().setLayout(new javax.swing.OverlayLayout(AddLearningOutcomeDialog.getContentPane()));

        jPanel13.setBackground(new java.awt.Color(26, 24, 26));

        AddLOLabel.setBackground(new java.awt.Color(26, 24, 26));
        AddLOLabel.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        AddLOLabel.setForeground(new java.awt.Color(227, 227, 227));
        AddLOLabel.setText("Outcome:");
        AddLOLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        AddLOLabel.setPreferredSize(new java.awt.Dimension(100, 50));

        AddLOCancel.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        AddLOCancel.setText("Cancel");
        AddLOCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                AddLOCancelActionPerformed(evt);
            }
        });

        AddLOAddButton.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        AddLOAddButton.setText("Add");
        AddLOAddButton.setEnabled(false);
        AddLOAddButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                AddLOAddButtonActionPerformed(evt);
            }
        });

        AddLOWarning.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        AddLOWarning.setForeground(new java.awt.Color(204, 0, 51));
        AddLOWarning.setText("Can not add empty field.");
        AddLOWarning.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        AddLOWarning.setPreferredSize(new java.awt.Dimension(121, 125));

        AddLOTextField.setBackground(new java.awt.Color(26, 24, 26));
        AddLOTextField.setColumns(20);
        AddLOTextField.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        AddLOTextField.setForeground(new java.awt.Color(227, 227, 227));
        AddLOTextField.setLineWrap(true);
        AddLOTextField.setRows(5);
        AddLOTextField.addCaretListener(new javax.swing.event.CaretListener()
        {
            public void caretUpdate(javax.swing.event.CaretEvent evt)
            {
                AddLOTextFieldCaretUpdate(evt);
            }
        });
        jScrollPane9.setViewportView(AddLOTextField);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(AddLOAddButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AddLOLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(AddLOCancel)
                        .addGap(44, 44, 44)
                        .addComponent(AddLOWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(39, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane9)
                        .addContainerGap())))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                        .addGap(25, 25, 25))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(AddLOLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddLOCancel)
                    .addComponent(AddLOAddButton)
                    .addComponent(AddLOWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        AddLearningOutcomeDialog.getContentPane().add(jPanel13);

        EditLearningOutcomeDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        EditLearningOutcomeDialog.setTitle("Add New Course");
        EditLearningOutcomeDialog.setAlwaysOnTop(true);
        EditLearningOutcomeDialog.setBackground(new java.awt.Color(26, 24, 26));
        EditLearningOutcomeDialog.setBounds(new java.awt.Rectangle(0, 0, 800, 600));
        EditLearningOutcomeDialog.setModal(true);
        EditLearningOutcomeDialog.getContentPane().setLayout(new javax.swing.OverlayLayout(EditLearningOutcomeDialog.getContentPane()));

        jPanel15.setBackground(new java.awt.Color(26, 24, 26));

        SaveLOLabel.setBackground(new java.awt.Color(26, 24, 26));
        SaveLOLabel.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        SaveLOLabel.setForeground(new java.awt.Color(227, 227, 227));
        SaveLOLabel.setText("Outcome:");
        SaveLOLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        SaveLOLabel.setPreferredSize(new java.awt.Dimension(100, 50));

        SaveLOCancel.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        SaveLOCancel.setText("Cancel");
        SaveLOCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                SaveLOCancelActionPerformed(evt);
            }
        });

        SaveLOSaveButton.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        SaveLOSaveButton.setText("Save");
        SaveLOSaveButton.setEnabled(false);
        SaveLOSaveButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                SaveLOSaveButtonActionPerformed(evt);
            }
        });

        SaveLOWarning.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        SaveLOWarning.setForeground(new java.awt.Color(204, 0, 51));
        SaveLOWarning.setText("Can not save empty field.");
        SaveLOWarning.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        SaveLOWarning.setPreferredSize(new java.awt.Dimension(121, 125));

        SaveLOTextField.setBackground(new java.awt.Color(26, 24, 26));
        SaveLOTextField.setColumns(20);
        SaveLOTextField.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        SaveLOTextField.setForeground(new java.awt.Color(227, 227, 227));
        SaveLOTextField.setLineWrap(true);
        SaveLOTextField.setRows(5);
        SaveLOTextField.addCaretListener(new javax.swing.event.CaretListener()
        {
            public void caretUpdate(javax.swing.event.CaretEvent evt)
            {
                SaveLOTextFieldCaretUpdate(evt);
            }
        });
        jScrollPane5.setViewportView(SaveLOTextField);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(SaveLOSaveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SaveLOLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(SaveLOCancel)
                        .addGap(44, 44, 44)
                        .addComponent(SaveLOWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane5))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(SaveLOLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(25, 25, 25)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SaveLOCancel)
                    .addComponent(SaveLOSaveButton)
                    .addComponent(SaveLOWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        EditLearningOutcomeDialog.getContentPane().add(jPanel15);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tina Analyzer");
        setBackground(new java.awt.Color(30, 30, 32));
        setMaximumSize(new java.awt.Dimension(2147483647, 1024));
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
                .addContainerGap(474, Short.MAX_VALUE))
        );

        Main.add(Left, java.awt.BorderLayout.WEST);

        Top.setBackground(new java.awt.Color(40, 41, 45));
        Top.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Top.setPreferredSize(new java.awt.Dimension(1064, 100));
        Top.setLayout(new java.awt.BorderLayout());

        TopCenter.setBackground(new java.awt.Color(40, 41, 45));
        TopCenter.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        TopCenter.setMaximumSize(new java.awt.Dimension(250, 32767));
        TopCenter.setRequestFocusEnabled(false);

        SelectedCourseLabel.setBackground(new java.awt.Color(199, 50, 38));
        SelectedCourseLabel.setFont(new java.awt.Font("Prototype", 0, 18)); // NOI18N
        SelectedCourseLabel.setForeground(new java.awt.Color(227, 227, 227));
        SelectedCourseLabel.setText("Selected Course:");
        SelectedCourseLabel.setToolTipText("");

        SelectedSectionLabel.setBackground(new java.awt.Color(199, 50, 38));
        SelectedSectionLabel.setFont(new java.awt.Font("Prototype", 0, 18)); // NOI18N
        SelectedSectionLabel.setForeground(new java.awt.Color(227, 227, 227));
        SelectedSectionLabel.setText("Section:");
        SelectedSectionLabel.setToolTipText("");

        SectionSelectionComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                SectionSelectionComboBoxActionPerformed(evt);
            }
        });

        CourseSelectionComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                CourseSelectionComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout TopCenterLayout = new javax.swing.GroupLayout(TopCenter);
        TopCenter.setLayout(TopCenterLayout);
        TopCenterLayout.setHorizontalGroup(
            TopCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopCenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TopCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(SelectedCourseLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SelectedSectionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(56, 56, 56)
                .addGroup(TopCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CourseSelectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SectionSelectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        TopCenterLayout.setVerticalGroup(
            TopCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopCenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TopCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SelectedCourseLabel)
                    .addComponent(CourseSelectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(TopCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SelectedSectionLabel)
                    .addComponent(SectionSelectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
                .addContainerGap(41, Short.MAX_VALUE))
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
            .addGap(0, 98, Short.MAX_VALUE)
        );

        Top.add(TopRight, java.awt.BorderLayout.EAST);

        Main.add(Top, java.awt.BorderLayout.NORTH);

        Center.setBackground(new java.awt.Color(26, 24, 26));
        Center.setLayout(new javax.swing.OverlayLayout(Center));

        ExamsMainPanel.setBackground(new java.awt.Color(26, 24, 26));
        ExamsMainPanel.setLayout(new java.awt.BorderLayout());

        DBTitlePanel4.setBackground(new java.awt.Color(26, 24, 26));
        DBTitlePanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        DashboardMainTitle4.setBackground(new java.awt.Color(199, 50, 38));
        DashboardMainTitle4.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        DashboardMainTitle4.setForeground(new java.awt.Color(227, 227, 227));
        DashboardMainTitle4.setText("Exams");
        DashboardMainTitle4.setToolTipText("");

        javax.swing.GroupLayout DBTitlePanel4Layout = new javax.swing.GroupLayout(DBTitlePanel4);
        DBTitlePanel4.setLayout(DBTitlePanel4Layout);
        DBTitlePanel4Layout.setHorizontalGroup(
            DBTitlePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DBTitlePanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DashboardMainTitle4)
                .addContainerGap(784, Short.MAX_VALUE))
        );
        DBTitlePanel4Layout.setVerticalGroup(
            DBTitlePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DBTitlePanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DashboardMainTitle4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ExamsMainPanel.add(DBTitlePanel4, java.awt.BorderLayout.NORTH);

        ExamsMP.setkEndColor(new java.awt.Color(26, 24, 26));
        ExamsMP.setkStartColor(new java.awt.Color(26, 24, 26));
        ExamsMP.setLayout(new java.awt.CardLayout());

        ExamsNoCoursePanel.setkEndColor(new java.awt.Color(26, 24, 26));
        ExamsNoCoursePanel.setkStartColor(new java.awt.Color(26, 24, 26));

        NoCourseSelectedLabel1.setBackground(new java.awt.Color(153, 153, 153));
        NoCourseSelectedLabel1.setFont(new java.awt.Font("Monospaced", 2, 18)); // NOI18N
        NoCourseSelectedLabel1.setForeground(new java.awt.Color(153, 153, 153));
        NoCourseSelectedLabel1.setText("No course is selected. Please select or add a course from Courses Menu.");

        javax.swing.GroupLayout ExamsNoCoursePanelLayout = new javax.swing.GroupLayout(ExamsNoCoursePanel);
        ExamsNoCoursePanel.setLayout(ExamsNoCoursePanelLayout);
        ExamsNoCoursePanelLayout.setHorizontalGroup(
            ExamsNoCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ExamsNoCoursePanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(NoCourseSelectedLabel1)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        ExamsNoCoursePanelLayout.setVerticalGroup(
            ExamsNoCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ExamsNoCoursePanelLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(NoCourseSelectedLabel1)
                .addContainerGap(806, Short.MAX_VALUE))
        );

        ExamsMP.add(ExamsNoCoursePanel, "card3");

        ExamsInnerPanel.setkEndColor(new java.awt.Color(26, 24, 26));
        ExamsInnerPanel.setkStartColor(new java.awt.Color(26, 24, 26));
        ExamsInnerPanel.setLayout(new javax.swing.BoxLayout(ExamsInnerPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel14.setMaximumSize(new java.awt.Dimension(32767, 320));
        jPanel14.setOpaque(false);
        jPanel14.setPreferredSize(new java.awt.Dimension(811, 320));

        ExamList.setBackground(new java.awt.Color(26, 24, 26));
        ExamList.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        ExamList.setForeground(new java.awt.Color(227, 227, 227));
        ExamList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                ExamListMouseReleased(evt);
            }
        });
        jScrollPane11.setViewportView(ExamList);

        DashboardMainTitle6.setBackground(new java.awt.Color(199, 50, 38));
        DashboardMainTitle6.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        DashboardMainTitle6.setForeground(new java.awt.Color(227, 227, 227));
        DashboardMainTitle6.setText("All Exams");
        DashboardMainTitle6.setToolTipText("");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 829, Short.MAX_VALUE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(DashboardMainTitle6, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(DashboardMainTitle6, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        ExamsInnerPanel.add(jPanel14);

        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel16.setMaximumSize(new java.awt.Dimension(32767, 75));
        jPanel16.setOpaque(false);
        jPanel16.setPreferredSize(new java.awt.Dimension(826, 60));

        AddExamButton.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        AddExamButton.setForeground(new java.awt.Color(51, 51, 51));
        AddExamButton.setText("Add Exam");
        AddExamButton.setPreferredSize(new java.awt.Dimension(135, 50));
        AddExamButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                AddExamButtonActionPerformed(evt);
            }
        });

        RemoveExamButton.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        RemoveExamButton.setForeground(new java.awt.Color(51, 51, 51));
        RemoveExamButton.setText("Remove Exam");
        RemoveExamButton.setEnabled(false);
        RemoveExamButton.setPreferredSize(new java.awt.Dimension(135, 50));
        RemoveExamButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                RemoveExamButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AddExamButton, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(RemoveExamButton, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(502, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddExamButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(RemoveExamButton, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addGap(24, 24, 24))
        );

        ExamsInnerPanel.add(jPanel16);

        ExamEditPanel.setOpaque(false);

        DashboardMainTitle8.setBackground(new java.awt.Color(199, 50, 38));
        DashboardMainTitle8.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        DashboardMainTitle8.setForeground(new java.awt.Color(227, 227, 227));
        DashboardMainTitle8.setText("Type:");
        DashboardMainTitle8.setToolTipText("");

        ExamTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Midterm", "Final", "Quiz", "Lab", "Other" }));
        ExamTypeComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ExamTypeComboBoxActionPerformed(evt);
            }
        });

        DashboardMainTitle9.setBackground(new java.awt.Color(199, 50, 38));
        DashboardMainTitle9.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        DashboardMainTitle9.setForeground(new java.awt.Color(227, 227, 227));
        DashboardMainTitle9.setText("Date:");
        DashboardMainTitle9.setToolTipText("");

        ExamDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                ExamDateChooserPropertyChange(evt);
            }
        });

        DashboardMainTitle10.setBackground(new java.awt.Color(199, 50, 38));
        DashboardMainTitle10.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        DashboardMainTitle10.setForeground(new java.awt.Color(227, 227, 227));
        DashboardMainTitle10.setText("Percentage:");
        DashboardMainTitle10.setToolTipText("");

        jList2.setBackground(new java.awt.Color(26, 24, 26));
        jList2.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jList2.setForeground(new java.awt.Color(227, 227, 227));
        jScrollPane12.setViewportView(jList2);

        DashboardMainTitle7.setBackground(new java.awt.Color(199, 50, 38));
        DashboardMainTitle7.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        DashboardMainTitle7.setForeground(new java.awt.Color(227, 227, 227));
        DashboardMainTitle7.setText("Questions");
        DashboardMainTitle7.setToolTipText("");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel());

        ImportAttendanceData3.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        ImportAttendanceData3.setForeground(new java.awt.Color(51, 51, 51));
        ImportAttendanceData3.setText("Add Question");
        ImportAttendanceData3.setPreferredSize(new java.awt.Dimension(135, 50));
        ImportAttendanceData3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ImportAttendanceData3ActionPerformed(evt);
            }
        });

        ImportAttendanceData4.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        ImportAttendanceData4.setForeground(new java.awt.Color(51, 51, 51));
        ImportAttendanceData4.setText("Remove Question");
        ImportAttendanceData4.setPreferredSize(new java.awt.Dimension(135, 50));
        ImportAttendanceData4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ImportAttendanceData4ActionPerformed(evt);
            }
        });

        DashboardMainTitle11.setBackground(new java.awt.Color(199, 50, 38));
        DashboardMainTitle11.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        DashboardMainTitle11.setForeground(new java.awt.Color(227, 227, 227));
        DashboardMainTitle11.setText("Points:");
        DashboardMainTitle11.setToolTipText("");

        DashboardMainTitle12.setBackground(new java.awt.Color(199, 50, 38));
        DashboardMainTitle12.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        DashboardMainTitle12.setForeground(new java.awt.Color(227, 227, 227));
        DashboardMainTitle12.setText("Topics");
        DashboardMainTitle12.setToolTipText("");

        DashboardMainTitle13.setBackground(new java.awt.Color(199, 50, 38));
        DashboardMainTitle13.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        DashboardMainTitle13.setForeground(new java.awt.Color(227, 227, 227));
        DashboardMainTitle13.setText("Learning Outcomes");
        DashboardMainTitle13.setToolTipText("");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Title 1"
            }
        ));
        jScrollPane14.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Title 1"
            }
        ));
        jScrollPane15.setViewportView(jTable2);

        ExamPercentageField.setBackground(new java.awt.Color(26, 24, 26));
        ExamPercentageField.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        ExamPercentageField.setForeground(new java.awt.Color(227, 227, 227));
        ExamPercentageField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ExamPercentageFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ExamEditPanelLayout = new javax.swing.GroupLayout(ExamEditPanel);
        ExamEditPanel.setLayout(ExamEditPanelLayout);
        ExamEditPanelLayout.setHorizontalGroup(
            ExamEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ExamEditPanelLayout.createSequentialGroup()
                .addGroup(ExamEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ExamEditPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(DashboardMainTitle7, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ExamEditPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(ExamEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ExamEditPanelLayout.createSequentialGroup()
                                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addGroup(ExamEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(ExamEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ImportAttendanceData3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ImportAttendanceData4, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(ExamEditPanelLayout.createSequentialGroup()
                                        .addComponent(DashboardMainTitle11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(ExamEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ExamEditPanelLayout.createSequentialGroup()
                                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ExamEditPanelLayout.createSequentialGroup()
                                    .addComponent(DashboardMainTitle12, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(154, 154, 154)
                                    .addComponent(DashboardMainTitle13, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(ExamEditPanelLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(DashboardMainTitle8)
                        .addGap(18, 18, 18)
                        .addComponent(ExamTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(DashboardMainTitle9)
                        .addGap(18, 18, 18)
                        .addComponent(ExamDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(DashboardMainTitle10)
                        .addGap(18, 18, 18)
                        .addComponent(ExamPercentageField, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(259, Short.MAX_VALUE))
        );
        ExamEditPanelLayout.setVerticalGroup(
            ExamEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ExamEditPanelLayout.createSequentialGroup()
                .addGroup(ExamEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ExamEditPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(ExamEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(DashboardMainTitle8, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ExamTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DashboardMainTitle9, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(ExamEditPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(ExamDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ExamEditPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(ExamEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(DashboardMainTitle10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ExamPercentageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(32, 32, 32)
                .addComponent(DashboardMainTitle7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ExamEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ExamEditPanelLayout.createSequentialGroup()
                        .addComponent(ImportAttendanceData3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ImportAttendanceData4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(ExamEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(DashboardMainTitle11, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(22, 22, 22)
                .addGroup(ExamEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DashboardMainTitle12, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DashboardMainTitle13, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(ExamEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(72, Short.MAX_VALUE))
        );

        ExamsInnerPanel.add(ExamEditPanel);

        ExamsScrollPane.setViewportView(ExamsInnerPanel);

        ExamsMP.add(ExamsScrollPane, "card4");

        ExamsMainPanel.add(ExamsMP, java.awt.BorderLayout.CENTER);

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

        javax.swing.GroupLayout DBTitlePanel5Layout = new javax.swing.GroupLayout(DBTitlePanel5);
        DBTitlePanel5.setLayout(DBTitlePanel5Layout);
        DBTitlePanel5Layout.setHorizontalGroup(
            DBTitlePanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DBTitlePanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DashboardMainTitle5)
                .addContainerGap(741, Short.MAX_VALUE))
        );
        DBTitlePanel5Layout.setVerticalGroup(
            DBTitlePanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DBTitlePanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DashboardMainTitle5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ReportsMainPanel.add(DBTitlePanel5, java.awt.BorderLayout.NORTH);

        ReportsMP.setBackground(new java.awt.Color(26, 24, 26));
        ReportsMP.setPreferredSize(new java.awt.Dimension(100, 200));
        ReportsMP.setLayout(new java.awt.CardLayout());

        ReportsNoCoursePanel.setkEndColor(new java.awt.Color(26, 24, 26));
        ReportsNoCoursePanel.setkStartColor(new java.awt.Color(26, 24, 26));

        NoCourseSelectedLabel2.setBackground(new java.awt.Color(153, 153, 153));
        NoCourseSelectedLabel2.setFont(new java.awt.Font("Monospaced", 2, 18)); // NOI18N
        NoCourseSelectedLabel2.setForeground(new java.awt.Color(153, 153, 153));
        NoCourseSelectedLabel2.setText("No course is selected. Please select or add a course from Courses Menu.");

        javax.swing.GroupLayout ReportsNoCoursePanelLayout = new javax.swing.GroupLayout(ReportsNoCoursePanel);
        ReportsNoCoursePanel.setLayout(ReportsNoCoursePanelLayout);
        ReportsNoCoursePanelLayout.setHorizontalGroup(
            ReportsNoCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsNoCoursePanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(NoCourseSelectedLabel2)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        ReportsNoCoursePanelLayout.setVerticalGroup(
            ReportsNoCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsNoCoursePanelLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(NoCourseSelectedLabel2)
                .addContainerGap(806, Short.MAX_VALUE))
        );

        ReportsMP.add(ReportsNoCoursePanel, "card3");

        ReportsInnerPanel.setkEndColor(new java.awt.Color(26, 24, 26));
        ReportsInnerPanel.setkStartColor(new java.awt.Color(26, 24, 26));
        ReportsInnerPanel.setLayout(new java.awt.GridLayout(3, 2));

        Grid29.setBackground(new java.awt.Color(26, 24, 26));
        Grid29.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid29.setLayout(new java.awt.GridBagLayout());

        ViewAvgSuccess7.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ViewAvgSuccess7.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid29.add(ViewAvgSuccess7, gridBagConstraints);

        ChartImage15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ChartImage15.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid29.add(ChartImage15, gridBagConstraints);

        AvgSuccessTitle7.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        AvgSuccessTitle7.setForeground(new java.awt.Color(227, 227, 227));
        AvgSuccessTitle7.setText("Average Success");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid29.add(AvgSuccessTitle7, gridBagConstraints);

        ReportsInnerPanel.add(Grid29);

        Grid30.setBackground(new java.awt.Color(26, 24, 26));
        Grid30.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid30.setLayout(new java.awt.GridBagLayout());

        ViewAvgAttendance7.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ViewAvgAttendance7.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid30.add(ViewAvgAttendance7, gridBagConstraints);

        ChartImage16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ChartImage16.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid30.add(ChartImage16, gridBagConstraints);

        AvgAttendanceTitle7.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        AvgAttendanceTitle7.setForeground(new java.awt.Color(227, 227, 227));
        AvgAttendanceTitle7.setText("Average Attendance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid30.add(AvgAttendanceTitle7, gridBagConstraints);

        ReportsInnerPanel.add(Grid30);

        Grid31.setBackground(new java.awt.Color(26, 24, 26));
        Grid31.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid31.setLayout(new java.awt.GridBagLayout());

        DBImportBut7.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        DBImportBut7.setText("Import");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid31.add(DBImportBut7, gridBagConstraints);

        ImportImage7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ImportImage7.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid31.add(ImportImage7, gridBagConstraints);

        CourseDataTitle14.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CourseDataTitle14.setForeground(new java.awt.Color(227, 227, 227));
        CourseDataTitle14.setText("Course Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid31.add(CourseDataTitle14, gridBagConstraints);

        ReportsInnerPanel.add(Grid31);

        Grid32.setBackground(new java.awt.Color(26, 24, 26));
        Grid32.setPreferredSize(new java.awt.Dimension(50, 50));
        Grid32.setLayout(new java.awt.GridBagLayout());

        DbExportBut7.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        DbExportBut7.setText("Export");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 0.1;
        Grid32.add(DbExportBut7, gridBagConstraints);

        ExportImage7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/appicon.png"))); // NOI18N
        ExportImage7.setPreferredSize(new java.awt.Dimension(128, 128));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.weighty = 0.1;
        Grid32.add(ExportImage7, gridBagConstraints);

        CourseDataTitle15.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CourseDataTitle15.setForeground(new java.awt.Color(227, 227, 227));
        CourseDataTitle15.setText("Course Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        Grid32.add(CourseDataTitle15, gridBagConstraints);

        ReportsInnerPanel.add(Grid32);

        ReportsScrollPane.setViewportView(ReportsInnerPanel);

        ReportsMP.add(ReportsScrollPane, "card4");

        ReportsMainPanel.add(ReportsMP, java.awt.BorderLayout.CENTER);

        Center.add(ReportsMainPanel);

        SyllabusMainPanel.setBackground(new java.awt.Color(26, 24, 26));
        SyllabusMainPanel.setLayout(new java.awt.BorderLayout());

        SyllabusTitlePanel.setBackground(new java.awt.Color(26, 24, 26));
        SyllabusTitlePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        SyllabusMainTitle.setBackground(new java.awt.Color(199, 50, 38));
        SyllabusMainTitle.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        SyllabusMainTitle.setForeground(new java.awt.Color(227, 227, 227));
        SyllabusMainTitle.setText("Syllabus");
        SyllabusMainTitle.setToolTipText("");

        javax.swing.GroupLayout SyllabusTitlePanelLayout = new javax.swing.GroupLayout(SyllabusTitlePanel);
        SyllabusTitlePanel.setLayout(SyllabusTitlePanelLayout);
        SyllabusTitlePanelLayout.setHorizontalGroup(
            SyllabusTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SyllabusTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SyllabusMainTitle)
                .addContainerGap(767, Short.MAX_VALUE))
        );
        SyllabusTitlePanelLayout.setVerticalGroup(
            SyllabusTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SyllabusTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SyllabusMainTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        SyllabusMainPanel.add(SyllabusTitlePanel, java.awt.BorderLayout.NORTH);

        SylMainPanel.setBackground(new java.awt.Color(26, 24, 26));
        SylMainPanel.setPreferredSize(new java.awt.Dimension(100, 200));
        SylMainPanel.setLayout(new java.awt.CardLayout());

        SyllabusInnerPanel.setkEndColor(new java.awt.Color(26, 24, 26));
        SyllabusInnerPanel.setkStartColor(new java.awt.Color(26, 24, 26));
        SyllabusInnerPanel.setLayout(new javax.swing.BoxLayout(SyllabusInnerPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel5.setMaximumSize(new java.awt.Dimension(32767, 250));
        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(791, 250));

        jLabel1.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(227, 227, 227));
        jLabel1.setText("End Date:");

        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(227, 227, 227));
        jLabel2.setText("Start Date:");

        SyllabusStartDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                SyllabusStartDateChooserPropertyChange(evt);
            }
        });

        SyllabusEndDateChooser.setEnabled(false);
        SyllabusEndDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                SyllabusEndDateChooserPropertyChange(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(26, 24, 26));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(227, 227, 227));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("Please specify the start and end dates of the course. You should selectthe start day for one of the sections, and the end day must correspond to the same day of the week for calculations to work properly.");
        jScrollPane4.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SyllabusEndDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SyllabusStartDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(235, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(SyllabusStartDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(34, 34, 34)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(SyllabusEndDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        SyllabusInnerPanel.add(jPanel5);

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setOpaque(false);
        jPanel6.setPreferredSize(new java.awt.Dimension(791, 300));

        SyllabusMainTitle1.setBackground(new java.awt.Color(199, 50, 38));
        SyllabusMainTitle1.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        SyllabusMainTitle1.setForeground(new java.awt.Color(227, 227, 227));
        SyllabusMainTitle1.setText("Weeks");
        SyllabusMainTitle1.setToolTipText("");

        jScrollPane8.setMinimumSize(new java.awt.Dimension(100, 23));
        jScrollPane8.setPreferredSize(new java.awt.Dimension(400, 130));
        jScrollPane8.setVerifyInputWhenFocusTarget(false);

        SyllabusWeekList.setBackground(new java.awt.Color(53, 55, 61));
        SyllabusWeekList.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        SyllabusWeekList.setForeground(new java.awt.Color(227, 227, 227));
        SyllabusWeekList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        SyllabusWeekList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        SyllabusWeekList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                SyllabusWeekListMouseReleased(evt);
            }
        });
        jScrollPane8.setViewportView(SyllabusWeekList);

        SyllabusWeekTopicsArea.setBackground(new java.awt.Color(26, 24, 26));
        SyllabusWeekTopicsArea.setColumns(20);
        SyllabusWeekTopicsArea.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        SyllabusWeekTopicsArea.setForeground(new java.awt.Color(227, 227, 227));
        SyllabusWeekTopicsArea.setRows(5);
        SyllabusWeekTopicsArea.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                SyllabusWeekTopicsAreaKeyReleased(evt);
            }
        });
        jScrollPane6.setViewportView(SyllabusWeekTopicsArea);

        SyllabusMainTitle3.setBackground(new java.awt.Color(199, 50, 38));
        SyllabusMainTitle3.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        SyllabusMainTitle3.setForeground(new java.awt.Color(227, 227, 227));
        SyllabusMainTitle3.setText("Topic");
        SyllabusMainTitle3.setToolTipText("");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(SyllabusMainTitle1)
                        .addGap(278, 278, 278)
                        .addComponent(SyllabusMainTitle3)))
                .addContainerGap(159, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SyllabusMainTitle1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SyllabusMainTitle3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        SyllabusInnerPanel.add(jPanel6);

        jPanel12.setOpaque(false);
        jPanel12.setPreferredSize(new java.awt.Dimension(791, 150));

        SyllabusMainTitle2.setBackground(new java.awt.Color(199, 50, 38));
        SyllabusMainTitle2.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        SyllabusMainTitle2.setForeground(new java.awt.Color(227, 227, 227));
        SyllabusMainTitle2.setText("Learning Outcomes");
        SyllabusMainTitle2.setToolTipText("");

        jScrollPane7.setMinimumSize(new java.awt.Dimension(100, 23));
        jScrollPane7.setPreferredSize(new java.awt.Dimension(400, 130));
        jScrollPane7.setVerifyInputWhenFocusTarget(false);

        LearningOutcomeList.setBackground(new java.awt.Color(53, 55, 61));
        LearningOutcomeList.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        LearningOutcomeList.setForeground(new java.awt.Color(227, 227, 227));
        LearningOutcomeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        LearningOutcomeList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        LearningOutcomeList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                LearningOutcomeListMouseReleased(evt);
            }
        });
        jScrollPane7.setViewportView(LearningOutcomeList);

        RemoveLearningOutcomeButton.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        RemoveLearningOutcomeButton.setForeground(new java.awt.Color(51, 51, 51));
        RemoveLearningOutcomeButton.setText("Remove");
        RemoveLearningOutcomeButton.setEnabled(false);
        RemoveLearningOutcomeButton.setPreferredSize(new java.awt.Dimension(135, 50));
        RemoveLearningOutcomeButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                RemoveLearningOutcomeButtonActionPerformed(evt);
            }
        });

        AddNewLearningOutcomeButton.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        AddNewLearningOutcomeButton.setForeground(new java.awt.Color(51, 51, 51));
        AddNewLearningOutcomeButton.setText("Add New Outcome");
        AddNewLearningOutcomeButton.setPreferredSize(new java.awt.Dimension(135, 50));
        AddNewLearningOutcomeButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                AddNewLearningOutcomeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(SyllabusMainTitle2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(RemoveLearningOutcomeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AddNewLearningOutcomeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(321, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(SyllabusMainTitle2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addComponent(AddNewLearningOutcomeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(RemoveLearningOutcomeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(77, Short.MAX_VALUE))
        );

        SyllabusInnerPanel.add(jPanel12);

        SyllabusScrollPane.setViewportView(SyllabusInnerPanel);

        SylMainPanel.add(SyllabusScrollPane, "card4");

        SyllabusNoCoursePanel.setkEndColor(new java.awt.Color(26, 24, 26));
        SyllabusNoCoursePanel.setkStartColor(new java.awt.Color(26, 24, 26));

        NoCourseSelectedLabel4.setBackground(new java.awt.Color(153, 153, 153));
        NoCourseSelectedLabel4.setFont(new java.awt.Font("Monospaced", 2, 18)); // NOI18N
        NoCourseSelectedLabel4.setForeground(new java.awt.Color(153, 153, 153));
        NoCourseSelectedLabel4.setText("No course is selected. Please select or add a course from Courses Menu.");

        javax.swing.GroupLayout SyllabusNoCoursePanelLayout = new javax.swing.GroupLayout(SyllabusNoCoursePanel);
        SyllabusNoCoursePanel.setLayout(SyllabusNoCoursePanelLayout);
        SyllabusNoCoursePanelLayout.setHorizontalGroup(
            SyllabusNoCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SyllabusNoCoursePanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(NoCourseSelectedLabel4)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        SyllabusNoCoursePanelLayout.setVerticalGroup(
            SyllabusNoCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SyllabusNoCoursePanelLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(NoCourseSelectedLabel4)
                .addContainerGap(806, Short.MAX_VALUE))
        );

        SylMainPanel.add(SyllabusNoCoursePanel, "card3");

        SyllabusMainPanel.add(SylMainPanel, java.awt.BorderLayout.CENTER);

        Center.add(SyllabusMainPanel);

        StudentsMainPanel.setBackground(new java.awt.Color(26, 24, 26));
        StudentsMainPanel.setLayout(new java.awt.BorderLayout());

        DBTitlePanel6.setBackground(new java.awt.Color(26, 24, 26));
        DBTitlePanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        StudentsMainTitle.setBackground(new java.awt.Color(199, 50, 38));
        StudentsMainTitle.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        StudentsMainTitle.setForeground(new java.awt.Color(227, 227, 227));
        StudentsMainTitle.setText("Students");
        StudentsMainTitle.setToolTipText("");

        javax.swing.GroupLayout DBTitlePanel6Layout = new javax.swing.GroupLayout(DBTitlePanel6);
        DBTitlePanel6.setLayout(DBTitlePanel6Layout);
        DBTitlePanel6Layout.setHorizontalGroup(
            DBTitlePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DBTitlePanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StudentsMainTitle)
                .addContainerGap(762, Short.MAX_VALUE))
        );
        DBTitlePanel6Layout.setVerticalGroup(
            DBTitlePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DBTitlePanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StudentsMainTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        StudentsMainPanel.add(DBTitlePanel6, java.awt.BorderLayout.NORTH);

        StudentsMP.setBackground(new java.awt.Color(26, 24, 26));
        StudentsMP.setPreferredSize(new java.awt.Dimension(100, 200));
        StudentsMP.setLayout(new java.awt.CardLayout());

        StudentsInnerPanel.setkEndColor(new java.awt.Color(26, 24, 26));
        StudentsInnerPanel.setkStartColor(new java.awt.Color(26, 24, 26));
        StudentsInnerPanel.setLayout(new javax.swing.BoxLayout(StudentsInnerPanel, javax.swing.BoxLayout.Y_AXIS));

        StudentsIP_Panel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        StudentsIP_Panel1.setMaximumSize(new java.awt.Dimension(32767, 600));
        StudentsIP_Panel1.setOpaque(false);
        StudentsIP_Panel1.setPreferredSize(new java.awt.Dimension(791, 600));

        jLabel3.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(227, 227, 227));
        jLabel3.setText("Student List");

        ImportAttendanceData.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        ImportAttendanceData.setForeground(new java.awt.Color(51, 51, 51));
        ImportAttendanceData.setText("Import Attendance Data");
        ImportAttendanceData.setPreferredSize(new java.awt.Dimension(135, 50));
        ImportAttendanceData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ImportAttendanceDataActionPerformed(evt);
            }
        });

        StudentList.setBackground(new java.awt.Color(26, 24, 26));
        StudentList.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        StudentList.setForeground(new java.awt.Color(227, 227, 227));
        jScrollPane10.setViewportView(StudentList);

        javax.swing.GroupLayout StudentsIP_Panel1Layout = new javax.swing.GroupLayout(StudentsIP_Panel1);
        StudentsIP_Panel1.setLayout(StudentsIP_Panel1Layout);
        StudentsIP_Panel1Layout.setHorizontalGroup(
            StudentsIP_Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentsIP_Panel1Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(StudentsIP_Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ImportAttendanceData, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(337, Short.MAX_VALUE))
        );
        StudentsIP_Panel1Layout.setVerticalGroup(
            StudentsIP_Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentsIP_Panel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel3)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(ImportAttendanceData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
        );

        StudentsInnerPanel.add(StudentsIP_Panel1);

        StudentsIP_Panel2.setOpaque(false);
        StudentsIP_Panel2.setPreferredSize(new java.awt.Dimension(791, 300));

        javax.swing.GroupLayout StudentsIP_Panel2Layout = new javax.swing.GroupLayout(StudentsIP_Panel2);
        StudentsIP_Panel2.setLayout(StudentsIP_Panel2Layout);
        StudentsIP_Panel2Layout.setHorizontalGroup(
            StudentsIP_Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 849, Short.MAX_VALUE)
        );
        StudentsIP_Panel2Layout.setVerticalGroup(
            StudentsIP_Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        StudentsInnerPanel.add(StudentsIP_Panel2);

        StudentsScrollPane.setViewportView(StudentsInnerPanel);

        StudentsMP.add(StudentsScrollPane, "card4");

        StudentsNoCoursePanel.setkEndColor(new java.awt.Color(26, 24, 26));
        StudentsNoCoursePanel.setkStartColor(new java.awt.Color(26, 24, 26));

        NoCourseSelectedLabel.setBackground(new java.awt.Color(153, 153, 153));
        NoCourseSelectedLabel.setFont(new java.awt.Font("Monospaced", 2, 18)); // NOI18N
        NoCourseSelectedLabel.setForeground(new java.awt.Color(153, 153, 153));
        NoCourseSelectedLabel.setText("No course is selected. Please select or add a course from Courses Menu.");

        javax.swing.GroupLayout StudentsNoCoursePanelLayout = new javax.swing.GroupLayout(StudentsNoCoursePanel);
        StudentsNoCoursePanel.setLayout(StudentsNoCoursePanelLayout);
        StudentsNoCoursePanelLayout.setHorizontalGroup(
            StudentsNoCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentsNoCoursePanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(NoCourseSelectedLabel)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        StudentsNoCoursePanelLayout.setVerticalGroup(
            StudentsNoCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentsNoCoursePanelLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(NoCourseSelectedLabel)
                .addContainerGap(778, Short.MAX_VALUE))
        );

        StudentsMP.add(StudentsNoCoursePanel, "card3");

        StudentsMainPanel.add(StudentsMP, java.awt.BorderLayout.CENTER);

        Center.add(StudentsMainPanel);

        CoursesMainPanel.setBackground(new java.awt.Color(26, 24, 26));
        CoursesMainPanel.setLayout(new java.awt.BorderLayout(5, 25));

        CoursesTitlePanel.setBackground(new java.awt.Color(26, 24, 26));
        CoursesTitlePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        CoursesMainTitle.setBackground(new java.awt.Color(199, 50, 38));
        CoursesMainTitle.setFont(new java.awt.Font("Prototype", 0, 24)); // NOI18N
        CoursesMainTitle.setForeground(new java.awt.Color(227, 227, 227));
        CoursesMainTitle.setText("Courses");
        CoursesMainTitle.setToolTipText("");

        javax.swing.GroupLayout CoursesTitlePanelLayout = new javax.swing.GroupLayout(CoursesTitlePanel);
        CoursesTitlePanel.setLayout(CoursesTitlePanelLayout);
        CoursesTitlePanelLayout.setHorizontalGroup(
            CoursesTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CoursesTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CoursesMainTitle)
                .addContainerGap(771, Short.MAX_VALUE))
        );
        CoursesTitlePanelLayout.setVerticalGroup(
            CoursesTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CoursesTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CoursesMainTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        CoursesList.setToolTipText("");
        CoursesList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CoursesList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                CoursesListMouseReleased(evt);
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
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(CourseIDTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CourseID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(19, 19, 19))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(CourseDescriptionTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
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
        SectionsList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                SectionsListMouseReleased(evt);
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
                .addContainerGap(374, Short.MAX_VALUE))
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
                .addContainerGap(293, Short.MAX_VALUE))
        );

        CoursesCenterPanel.add(kGradientPanel5);

        CoursesMainPanel.add(CoursesCenterPanel, java.awt.BorderLayout.CENTER);

        Center.add(CoursesMainPanel);

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

        javax.swing.GroupLayout DashboardTitlePanelLayout = new javax.swing.GroupLayout(DashboardTitlePanel);
        DashboardTitlePanel.setLayout(DashboardTitlePanelLayout);
        DashboardTitlePanelLayout.setHorizontalGroup(
            DashboardTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DashboardMainTitle)
                .addContainerGap(743, Short.MAX_VALUE))
        );
        DashboardTitlePanelLayout.setVerticalGroup(
            DashboardTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DashboardMainTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 434, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 124, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(AvgSuccessTitle)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(29, 29, 29)
                            .addComponent(ChartImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 124, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 293, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 55, Short.MAX_VALUE)
                    .addComponent(AvgSuccessTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(4, 4, 4)
                    .addComponent(ChartImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 68, Short.MAX_VALUE)))
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
            .addGap(0, 434, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 106, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(AvgAttendanceTitle)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGap(46, 46, 46)
                            .addComponent(ChartImage2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(ViewAvgAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 107, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 293, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 39, Short.MAX_VALUE)
                    .addComponent(AvgAttendanceTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(ChartImage2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(9, 9, 9)
                    .addComponent(ViewAvgAttendance)
                    .addGap(0, 40, Short.MAX_VALUE)))
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
            .addGap(0, 434, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 152, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(CourseDataTitle)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(ImportImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(DBImportBut, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 152, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 293, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 39, Short.MAX_VALUE)
                    .addComponent(CourseDataTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(ImportImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(9, 9, 9)
                    .addComponent(DBImportBut)
                    .addGap(0, 40, Short.MAX_VALUE)))
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
            .addGap(0, 434, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(0, 152, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(CourseDataTitle2)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(ExportImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(DbExportBut, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 152, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 293, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(0, 39, Short.MAX_VALUE)
                    .addComponent(CourseDataTitle2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(ExportImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(9, 9, 9)
                    .addComponent(DbExportBut)
                    .addGap(0, 40, Short.MAX_VALUE)))
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
        SelectCourse(courseManager.GetCourseList().size() - 1);
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
        SelectCourse(i_SelectedCourse);

        DisposeEditCourseDialog();
    }//GEN-LAST:event_EditCourseSaveButtonActionPerformed

    private void EditCourseIDFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditCourseIDFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EditCourseIDFieldActionPerformed

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
        SelectSection(courseManager.GetCourse(i_SelectedCourse).GetSections().size() - 1);
        DisposeAddSectionDialog();
    }//GEN-LAST:event_AddSectionAddButtonActionPerformed

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

    private void EditSectionNameFieldKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_EditSectionNameFieldKeyReleased
    {//GEN-HEADEREND:event_EditSectionNameFieldKeyReleased
        // TODO add your handling code here:

        if (courseManager.GetCourse(i_SelectedCourse).CheckIfSectionExists(EditSectionNameField.getText()))
        {
            if (!courseManager.GetCourse(i_SelectedCourse).GetSelectedSection().GetName().equals(EditSectionNameField.getText()))
            {
                EditSectionWarning.setText("This section already exists!");
                EditSectionSaveButton.setEnabled(false);
                EditSectionWarning.setVisible(true);
            }

        } else
        {
            if (EditSectionNameField.getText().equals(""))
            {
                EditSectionWarning.setText("Section name can not be empty!");
                EditSectionSaveButton.setEnabled(false);
                EditSectionWarning.setVisible(true);
            } else
            {
                EditSectionSaveButton.setEnabled(true);
                EditSectionWarning.setVisible(false);
            }
        }
    }//GEN-LAST:event_EditSectionNameFieldKeyReleased

    private void EditSectionCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_EditSectionCancelActionPerformed
    {//GEN-HEADEREND:event_EditSectionCancelActionPerformed
        // TODO add your handling code here:
        DisposeEditSectionDialog();
    }//GEN-LAST:event_EditSectionCancelActionPerformed

    private void EditSectionSaveButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_EditSectionSaveButtonActionPerformed
    {//GEN-HEADEREND:event_EditSectionSaveButtonActionPerformed
        // TODO add your handling code here:
        courseManager.GetCourse(i_SelectedCourse).GetSelectedSection().SetName(EditSectionNameField.getText());
        SelectSection(courseManager.GetCourse(i_SelectedCourse).GetSelectedSectionIndex());
        DisposeEditSectionDialog();
    }//GEN-LAST:event_EditSectionSaveButtonActionPerformed

    private void RemoveLearningOutcomeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_RemoveLearningOutcomeButtonActionPerformed
    {//GEN-HEADEREND:event_RemoveLearningOutcomeButtonActionPerformed
        // TODO add your handling code here:

        courseManager.GetCourse(i_SelectedCourse).GetSyllabus().RemoveLearningOutcome(LearningOutcomeList.getSelectedIndex());
        SelectLearningOutcome(courseManager.GetCourse(i_SelectedCourse).GetSyllabus().GetSelectedLO());
    }//GEN-LAST:event_RemoveLearningOutcomeButtonActionPerformed

    private void AddNewLearningOutcomeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_AddNewLearningOutcomeButtonActionPerformed
    {//GEN-HEADEREND:event_AddNewLearningOutcomeButtonActionPerformed
        // TODO add your handling code here:
        AddLearningOutcomeDialog.show();

    }//GEN-LAST:event_AddNewLearningOutcomeButtonActionPerformed

    private void SyllabusStartDateChooserPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_SyllabusStartDateChooserPropertyChange
    {//GEN-HEADEREND:event_SyllabusStartDateChooserPropertyChange
        // TODO add your handling code here:
        Date date = SyllabusStartDateChooser.getDate();

        if (date == null)
        {
            return;
        }

        if (SyllabusEndDateChooser.isEnabled())
        {
            Date courseEndDate = courseManager.GetCourse(i_SelectedCourse).GetSyllabus().getEndDate();

            if (courseEndDate != null)
            {
                if (date.after(courseEndDate))
                {
                    SyllabusStartDateChooser.setDate(courseEndDate);
                    courseManager.GetCourse(i_SelectedCourse).GetSyllabus().setStartDate(courseEndDate);
                } else
                {
                    courseManager.GetCourse(i_SelectedCourse).GetSyllabus().setStartDate(date);
                }
            }

        } else
        {
            SyllabusEndDateChooser.setEnabled(true);
            courseManager.GetCourse(i_SelectedCourse).GetSyllabus().setStartDate(date);
        }

        UpdateSyllabus();
    }//GEN-LAST:event_SyllabusStartDateChooserPropertyChange

    private void SyllabusEndDateChooserPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_SyllabusEndDateChooserPropertyChange
    {//GEN-HEADEREND:event_SyllabusEndDateChooserPropertyChange
        // TODO add your handling code here:

        Date date = SyllabusEndDateChooser.getDate();

        if (date == null)
        {
            return;
        }

        Date courseStartDate = courseManager.GetCourse(i_SelectedCourse).GetSyllabus().getStartDate();

        if (date.before(courseStartDate))
        {
            SyllabusEndDateChooser.setDate(courseStartDate);
            courseManager.GetCourse(i_SelectedCourse).GetSyllabus().setEndDate(courseStartDate);
        } else
        {
            courseManager.GetCourse(i_SelectedCourse).GetSyllabus().setEndDate(date);
        }

        UpdateSyllabus();

    }//GEN-LAST:event_SyllabusEndDateChooserPropertyChange

    private void SyllabusWeekTopicsAreaKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_SyllabusWeekTopicsAreaKeyReleased
    {//GEN-HEADEREND:event_SyllabusWeekTopicsAreaKeyReleased
        // TODO add your handling code here:
        // TODO add your handling code here:

        int selected = SyllabusWeekList.getSelectedIndex();
        String text = SyllabusWeekTopicsArea.getText();

        if (i_SelectedCourse != -1 && courseManager.GetCourse(i_SelectedCourse).GetSyllabus() != null)
        {
            courseManager.GetCourse(i_SelectedCourse).GetSyllabus().getWeeks().get(selected).setTopic(text);
            courseManager.SaveCourses();
        }


    }//GEN-LAST:event_SyllabusWeekTopicsAreaKeyReleased

    private void AddLOCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_AddLOCancelActionPerformed
    {//GEN-HEADEREND:event_AddLOCancelActionPerformed
        // TODO add your handling code here:
        AddLearningOutcomeDialog.dispose();
    }//GEN-LAST:event_AddLOCancelActionPerformed

    private void AddLOAddButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_AddLOAddButtonActionPerformed
    {//GEN-HEADEREND:event_AddLOAddButtonActionPerformed
        // TODO add your handling code here:

        courseManager.GetCourse(i_SelectedCourse).GetSyllabus().AddLearningOutcome(AddLOTextField.getText());
        AddLOTextField.setText("");
        AddLOWarning.setVisible(false);
        AddLOAddButton.setEnabled(false);

        SelectLearningOutcome(courseManager.GetCourse(i_SelectedCourse).GetSyllabus().GetSelectedLO());

        AddLearningOutcomeDialog.dispose();

    }//GEN-LAST:event_AddLOAddButtonActionPerformed

    private void SaveLOCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_SaveLOCancelActionPerformed
    {//GEN-HEADEREND:event_SaveLOCancelActionPerformed
        // TODO add your handling code here:
        EditLearningOutcomeDialog.dispose();
    }//GEN-LAST:event_SaveLOCancelActionPerformed

    private void SaveLOSaveButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_SaveLOSaveButtonActionPerformed
    {//GEN-HEADEREND:event_SaveLOSaveButtonActionPerformed
        // TODO add your handling code here:

        courseManager.GetCourse(i_SelectedCourse).GetSyllabus().EditLearningOutcome(LearningOutcomeList.getSelectedIndex(), SaveLOTextField.getText());
        SaveLOTextField.setText("");
        SaveLOWarning.setVisible(false);
        SaveLOSaveButton.setEnabled(false);
        UpdateSyllabus();
        EditLearningOutcomeDialog.dispose();
    }//GEN-LAST:event_SaveLOSaveButtonActionPerformed

    private void AddLOTextFieldCaretUpdate(javax.swing.event.CaretEvent evt)//GEN-FIRST:event_AddLOTextFieldCaretUpdate
    {//GEN-HEADEREND:event_AddLOTextFieldCaretUpdate
        if (AddLOTextField.getText().equals(""))
        {
            AddLOWarning.setVisible(true);
            AddLOAddButton.setEnabled(false);
        } else
        {
            AddLOWarning.setVisible(false);
            AddLOAddButton.setEnabled(true);
        }
    }//GEN-LAST:event_AddLOTextFieldCaretUpdate

    private void SaveLOTextFieldCaretUpdate(javax.swing.event.CaretEvent evt)//GEN-FIRST:event_SaveLOTextFieldCaretUpdate
    {//GEN-HEADEREND:event_SaveLOTextFieldCaretUpdate
        if (SaveLOTextField.getText().equals(""))
        {
            SaveLOWarning.setVisible(true);
            SaveLOSaveButton.setEnabled(false);
        } else
        {
            SaveLOWarning.setVisible(false);
            SaveLOSaveButton.setEnabled(true);
        }
    }//GEN-LAST:event_SaveLOTextFieldCaretUpdate

    private void CourseSelectionComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_CourseSelectionComboBoxActionPerformed
    {//GEN-HEADEREND:event_CourseSelectionComboBoxActionPerformed
        // TODO add your handling code here:
        SelectCourse(CourseSelectionComboBox.getSelectedIndex());
    }//GEN-LAST:event_CourseSelectionComboBoxActionPerformed

    private void SectionSelectionComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_SectionSelectionComboBoxActionPerformed
    {//GEN-HEADEREND:event_SectionSelectionComboBoxActionPerformed
        // TODO add your handling code here:

        SelectSection(SectionSelectionComboBox.getSelectedIndex());
    }//GEN-LAST:event_SectionSelectionComboBoxActionPerformed

    private void ImportAttendanceDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ImportAttendanceDataActionPerformed
    {//GEN-HEADEREND:event_ImportAttendanceDataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ImportAttendanceDataActionPerformed

    private void SectionsListMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_SectionsListMouseReleased
    {//GEN-HEADEREND:event_SectionsListMouseReleased
        // TODO add your handling code here:
        if (i_SelectedCourse == -1)
        {
            return;
        }

        if (SectionsList.getSelectedIndex() != -1)
        {
            if (courseManager.GetCourse(i_SelectedCourse).GetSections().size() < 2)
            {
                RemoveSectionButton.setEnabled(false);
            } else
            {
                RemoveSectionButton.setEnabled(true);
            }

            courseManager.GetCourse(i_SelectedCourse).SetSection(SectionsList.getSelectedIndex());

            SelectSection(SectionsList.getSelectedIndex());
        }

        if (evt.getClickCount() == 2)
        {
            Course c = courseManager.GetCourse(i_SelectedCourse);

            EditSectionNameField.setText(c.GetSelectedSection().GetName());

            EditSectionDialog.show();

        }
    }//GEN-LAST:event_SectionsListMouseReleased

    private void CoursesListMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_CoursesListMouseReleased
    {//GEN-HEADEREND:event_CoursesListMouseReleased
        // TODO add your handling code here:
        SelectCourse(CoursesList.getSelectedIndex());

        if (evt.getClickCount() == 2)
        {
            Course c = courseManager.GetCourse(i_SelectedCourse);

            EditCourseIDField.setText(c.GetID());
            EditCourseNameField.setText(c.GetName());
            EditCourseDescField.setText(c.GetDescription());

            EditCourseDialog.show();

        }
    }//GEN-LAST:event_CoursesListMouseReleased

    private void SyllabusWeekListMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_SyllabusWeekListMouseReleased
    {//GEN-HEADEREND:event_SyllabusWeekListMouseReleased
        // TODO add your handling code here:
        int selected = SyllabusWeekList.getSelectedIndex();

        if (selected == -1)
        {
            return;
        }

        String topic = courseManager.GetCourse(i_SelectedCourse).GetSyllabus().getWeeks().get(selected).getTopic();
        SyllabusWeekTopicsArea.setText(topic);
    }//GEN-LAST:event_SyllabusWeekListMouseReleased

    private void LearningOutcomeListMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_LearningOutcomeListMouseReleased
    {//GEN-HEADEREND:event_LearningOutcomeListMouseReleased
        // TODO add your handling code here:
        if (LearningOutcomeList.getSelectedIndex() != -1)
        {
            RemoveLearningOutcomeButton.setEnabled(true);

            if (evt.getClickCount() == 2)
            {
                String lo = courseManager.GetCourse(i_SelectedCourse).GetSyllabus().getLearningOutcomes().get(LearningOutcomeList.getSelectedIndex());
                SaveLOTextField.setText(lo);
                EditLearningOutcomeDialog.show();
            }
        } else
        {
            RemoveLearningOutcomeButton.setEnabled(false);

        }

        SelectLearningOutcome(LearningOutcomeList.getSelectedIndex());
    }//GEN-LAST:event_LearningOutcomeListMouseReleased

    private void AddExamButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_AddExamButtonActionPerformed
    {//GEN-HEADEREND:event_AddExamButtonActionPerformed
        // TODO add your handling code here:
        Exam ex = new Exam(Exam.ExamType.Midterm, 0, null);
        courseManager.GetCourse(i_SelectedCourse).AddExam(ex);
        UpdateExams();
    }//GEN-LAST:event_AddExamButtonActionPerformed

    private void RemoveExamButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_RemoveExamButtonActionPerformed
    {//GEN-HEADEREND:event_RemoveExamButtonActionPerformed
        // TODO add your handling code here:
        courseManager.GetCourse(i_SelectedCourse).RemoveExam(ExamList.getSelectedIndex());
        UpdateExams();
    }//GEN-LAST:event_RemoveExamButtonActionPerformed

    private void ImportAttendanceData3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ImportAttendanceData3ActionPerformed
    {//GEN-HEADEREND:event_ImportAttendanceData3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ImportAttendanceData3ActionPerformed

    private void ImportAttendanceData4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ImportAttendanceData4ActionPerformed
    {//GEN-HEADEREND:event_ImportAttendanceData4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ImportAttendanceData4ActionPerformed

    private void ExamListMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_ExamListMouseReleased
    {//GEN-HEADEREND:event_ExamListMouseReleased
        // TODO add your handling code here:

        SelectExam(ExamList.getSelectedIndex());
    }//GEN-LAST:event_ExamListMouseReleased

    private void ExamTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ExamTypeComboBoxActionPerformed
    {//GEN-HEADEREND:event_ExamTypeComboBoxActionPerformed
        // TODO add your handling code here:
        Exam.ExamType examType = Exam.ExamType.valueOf(ExamTypeComboBox.getSelectedIndex());
        courseManager.GetCourse(i_SelectedCourse).GetSelectedExam().setType(examType);
        UpdateExams();
    }//GEN-LAST:event_ExamTypeComboBoxActionPerformed

    private void ExamDateChooserPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_ExamDateChooserPropertyChange
    {//GEN-HEADEREND:event_ExamDateChooserPropertyChange
        // TODO add your handling code here:
        if (i_SelectedCourse == -1 || courseManager.GetCourse(i_SelectedCourse).GetSelectedExamIndex() == -1)
        {
            return;
        }

        courseManager.GetCourse(i_SelectedCourse).GetSelectedExam().setDate(ExamDateChooser.getDate());
    }//GEN-LAST:event_ExamDateChooserPropertyChange

    private void ExamPercentageFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ExamPercentageFieldActionPerformed
    {//GEN-HEADEREND:event_ExamPercentageFieldActionPerformed
        // TODO add your handling code here:
        if (i_SelectedCourse == -1 || courseManager.GetCourse(i_SelectedCourse).GetSelectedExamIndex() == -1)
        {
            return;
        }
        try
        {
            int spinnerVal = Integer.parseInt(ExamPercentageField.getText());
            int examPercentage = courseManager.GetCourse(i_SelectedCourse).GetRemainingExamPercentage();
            int currentPercentage = courseManager.GetCourse(i_SelectedCourse).GetSelectedExam().getPercentage();
            if (spinnerVal > currentPercentage && spinnerVal > examPercentage)
            {
                spinnerVal = currentPercentage + examPercentage;
            }

            ExamPercentageField.setText(Integer.toString(spinnerVal));

            courseManager.GetCourse(i_SelectedCourse).GetSelectedExam().setPercentage(spinnerVal);
            courseManager.GetCourse(i_SelectedCourse).CalculateRemainingExamPercentage();
        } catch (NumberFormatException e)
        {

        }

        
    }//GEN-LAST:event_ExamPercentageFieldActionPerformed

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
    private javax.swing.JButton AddExamButton;
    private javax.swing.JButton AddLOAddButton;
    private javax.swing.JButton AddLOCancel;
    private javax.swing.JLabel AddLOLabel;
    private javax.swing.JTextArea AddLOTextField;
    private javax.swing.JLabel AddLOWarning;
    private javax.swing.JDialog AddLearningOutcomeDialog;
    private javax.swing.JButton AddNewCourseButton;
    private javax.swing.JDialog AddNewCourseDialog;
    private javax.swing.JButton AddNewLearningOutcomeButton;
    private javax.swing.JButton AddSectionAddButton;
    private javax.swing.JButton AddSectionButton;
    private javax.swing.JButton AddSectionCancelButton;
    private javax.swing.JDialog AddSectionDialog;
    private javax.swing.JLabel AddSectionName;
    private javax.swing.JTextField AddSectionNameField;
    private javax.swing.JLabel AddSectionWarning;
    private javax.swing.JLabel AvgAttendanceTitle;
    private javax.swing.JLabel AvgAttendanceTitle7;
    private javax.swing.JLabel AvgSuccessTitle;
    private javax.swing.JLabel AvgSuccessTitle7;
    private javax.swing.JPanel Center;
    private javax.swing.JLabel ChartImage1;
    private javax.swing.JLabel ChartImage15;
    private javax.swing.JLabel ChartImage16;
    private javax.swing.JLabel ChartImage2;
    private javax.swing.JLabel CourseDataTitle;
    private javax.swing.JLabel CourseDataTitle14;
    private javax.swing.JLabel CourseDataTitle15;
    private javax.swing.JLabel CourseDataTitle2;
    private javax.swing.JTextArea CourseDescription;
    private javax.swing.JLabel CourseDescriptionTitle;
    private javax.swing.JLabel CourseID;
    private javax.swing.JLabel CourseIDTitle;
    private javax.swing.JLabel CourseName;
    private javax.swing.JLabel CourseNameTitle;
    private javax.swing.JComboBox<String> CourseSelectionComboBox;
    private keeptoo.KGradientPanel CoursesCenterPanel;
    private javax.swing.JLabel CoursesImage;
    private javax.swing.JLabel CoursesLabel;
    private javax.swing.JList<String> CoursesList;
    private javax.swing.JPanel CoursesMainPanel;
    private javax.swing.JLabel CoursesMainTitle;
    private javax.swing.JLabel CoursesMainTitle1;
    private javax.swing.JPanel CoursesPanel;
    private javax.swing.JPanel CoursesTitlePanel;
    private javax.swing.JPanel CoursesWrapper;
    private keeptoo.KGradientPanel DBCenterPanel;
    private javax.swing.JButton DBImportBut;
    private javax.swing.JButton DBImportBut7;
    private javax.swing.JPanel DBMain;
    private javax.swing.JPanel DBTitlePanel4;
    private javax.swing.JPanel DBTitlePanel5;
    private javax.swing.JPanel DBTitlePanel6;
    private javax.swing.JLabel DashboardImage;
    private javax.swing.JLabel DashboardLabel;
    private javax.swing.JPanel DashboardMainPanel;
    private javax.swing.JLabel DashboardMainTitle;
    private javax.swing.JLabel DashboardMainTitle10;
    private javax.swing.JLabel DashboardMainTitle11;
    private javax.swing.JLabel DashboardMainTitle12;
    private javax.swing.JLabel DashboardMainTitle13;
    private javax.swing.JLabel DashboardMainTitle4;
    private javax.swing.JLabel DashboardMainTitle5;
    private javax.swing.JLabel DashboardMainTitle6;
    private javax.swing.JLabel DashboardMainTitle7;
    private javax.swing.JLabel DashboardMainTitle8;
    private javax.swing.JLabel DashboardMainTitle9;
    private javax.swing.JPanel DashboardPanel;
    private keeptoo.KGradientPanel DashboardTitlePanel;
    private javax.swing.JPanel DashboardWrapper;
    private javax.swing.JButton DbExportBut;
    private javax.swing.JButton DbExportBut7;
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
    private javax.swing.JDialog EditLearningOutcomeDialog;
    private javax.swing.JButton EditSectionCancel;
    private javax.swing.JDialog EditSectionDialog;
    private javax.swing.JLabel EditSectionName;
    private javax.swing.JTextField EditSectionNameField;
    private javax.swing.JButton EditSectionSaveButton;
    private javax.swing.JLabel EditSectionWarning;
    private com.toedter.calendar.JDateChooser ExamDateChooser;
    private javax.swing.JPanel ExamEditPanel;
    private javax.swing.JList<String> ExamList;
    private javax.swing.JTextField ExamPercentageField;
    private javax.swing.JComboBox<String> ExamTypeComboBox;
    private javax.swing.JLabel ExamsImage;
    private keeptoo.KGradientPanel ExamsInnerPanel;
    private javax.swing.JLabel ExamsLabel;
    private keeptoo.KGradientPanel ExamsMP;
    private javax.swing.JPanel ExamsMainPanel;
    private keeptoo.KGradientPanel ExamsNoCoursePanel;
    private javax.swing.JPanel ExamsPanel;
    private javax.swing.JScrollPane ExamsScrollPane;
    private javax.swing.JPanel ExamsWrapper;
    private javax.swing.JLabel ExportImage;
    private javax.swing.JLabel ExportImage7;
    private javax.swing.JPanel Grid29;
    private javax.swing.JPanel Grid30;
    private javax.swing.JPanel Grid31;
    private javax.swing.JPanel Grid32;
    private javax.swing.JButton ImportAttendanceData;
    private javax.swing.JButton ImportAttendanceData3;
    private javax.swing.JButton ImportAttendanceData4;
    private javax.swing.JLabel ImportImage;
    private javax.swing.JLabel ImportImage7;
    private javax.swing.JList<String> LearningOutcomeList;
    private javax.swing.JPanel Left;
    private javax.swing.JPanel Main;
    private javax.swing.JPanel MenuPanel;
    private javax.swing.JLabel NoCourseSelectedLabel;
    private javax.swing.JLabel NoCourseSelectedLabel1;
    private javax.swing.JLabel NoCourseSelectedLabel2;
    private javax.swing.JLabel NoCourseSelectedLabel4;
    private javax.swing.JButton RemoveCourseButton;
    private javax.swing.JButton RemoveExamButton;
    private javax.swing.JButton RemoveLearningOutcomeButton;
    private javax.swing.JButton RemoveSectionButton;
    private javax.swing.JLabel ReportsImage;
    private keeptoo.KGradientPanel ReportsInnerPanel;
    private javax.swing.JLabel ReportsLabel;
    private javax.swing.JPanel ReportsMP;
    private javax.swing.JPanel ReportsMainPanel;
    private keeptoo.KGradientPanel ReportsNoCoursePanel;
    private javax.swing.JPanel ReportsPanel;
    private javax.swing.JScrollPane ReportsScrollPane;
    private javax.swing.JPanel ReportsWrapper;
    private javax.swing.JButton SaveLOCancel;
    private javax.swing.JLabel SaveLOLabel;
    private javax.swing.JButton SaveLOSaveButton;
    private javax.swing.JTextArea SaveLOTextField;
    private javax.swing.JLabel SaveLOWarning;
    private javax.swing.JComboBox<String> SectionSelectionComboBox;
    private javax.swing.JList<String> SectionsList;
    private javax.swing.JLabel SelectedCourseLabel;
    private javax.swing.JLabel SelectedSectionLabel;
    private javax.swing.JLabel SettingsImage;
    private javax.swing.JList<String> StudentList;
    private javax.swing.JPanel StudentsIP_Panel1;
    private javax.swing.JPanel StudentsIP_Panel2;
    private javax.swing.JLabel StudentsImage;
    private keeptoo.KGradientPanel StudentsInnerPanel;
    private javax.swing.JLabel StudentsLabel;
    private javax.swing.JPanel StudentsMP;
    private javax.swing.JPanel StudentsMainPanel;
    private javax.swing.JLabel StudentsMainTitle;
    private keeptoo.KGradientPanel StudentsNoCoursePanel;
    private javax.swing.JPanel StudentsPanel;
    private javax.swing.JScrollPane StudentsScrollPane;
    private javax.swing.JPanel StudentsWrapper;
    private javax.swing.JPanel SylMainPanel;
    private com.toedter.calendar.JDateChooser SyllabusEndDateChooser;
    private javax.swing.JLabel SyllabusImage;
    private keeptoo.KGradientPanel SyllabusInnerPanel;
    private javax.swing.JLabel SyllabusLabel;
    private javax.swing.JPanel SyllabusMainPanel;
    private javax.swing.JLabel SyllabusMainTitle;
    private javax.swing.JLabel SyllabusMainTitle1;
    private javax.swing.JLabel SyllabusMainTitle2;
    private javax.swing.JLabel SyllabusMainTitle3;
    private keeptoo.KGradientPanel SyllabusNoCoursePanel;
    private javax.swing.JPanel SyllabusPanel;
    private javax.swing.JScrollPane SyllabusScrollPane;
    private com.toedter.calendar.JDateChooser SyllabusStartDateChooser;
    private javax.swing.JPanel SyllabusTitlePanel;
    private javax.swing.JList<String> SyllabusWeekList;
    private javax.swing.JTextArea SyllabusWeekTopicsArea;
    private javax.swing.JPanel SyllabusWrapper;
    private javax.swing.JLabel Title;
    private javax.swing.JPanel Top;
    private javax.swing.JPanel TopCenter;
    private javax.swing.JPanel TopLeft;
    private javax.swing.JPanel TopRight;
    private javax.swing.JButton UndoButton;
    private javax.swing.JButton ViewAvgAttendance;
    private javax.swing.JButton ViewAvgAttendance7;
    private javax.swing.JButton ViewAvgSuccess7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private keeptoo.KGradientPanel kGradientPanel3;
    private keeptoo.KGradientPanel kGradientPanel4;
    private keeptoo.KGradientPanel kGradientPanel5;
    // End of variables declaration//GEN-END:variables
}
