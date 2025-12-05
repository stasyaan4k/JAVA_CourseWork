package logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExamRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private String subject;
    private String date;
    private List<Student> students;

    public ExamRecord(String subject, String date) {
        this.subject = subject;
        this.date = date;
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(int index) {
        if (index >= 0 && index < students.size()) {
            students.remove(index);
        }
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public int getTotalStudents() {
        return students.size();
    }

    public int getStudentsWithGrade() {
        return (int) students.stream().filter(Student::hasGrade).count();
    }

    public int getPassedCount() {
        return (int) students.stream()
                .filter(Student::hasGrade)
                .filter(Student::isPassed)
                .count();
    }

    public int getFailedCount() {
        return (int) students.stream()
                .filter(Student::hasGrade)
                .filter(s -> !s.isPassed())
                .count();
    }

    public int getStudentsWithoutGrade() {
        return (int) students.stream()
                .filter(s -> !s.hasGrade())
                .count();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}