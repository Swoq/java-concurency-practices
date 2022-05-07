package com.swoqe.third;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectPointsMain {
    public static void main(String[] args) throws InterruptedException {
        // List.of returns immutable list, so thread safe
        List<Group> groups = List.of(
                new Group("ІP-91", 25),
                new Group("ІP-92", 30),
                new Group("ІP-93", 26)
        );

        Journal journal = new Journal(groups);
        int nWeeks = 3;

        Thread t = new Thread(() -> {
            // creating parallel threads
            List<Thread> threads = List.of(
                    (new Thread(new Teacher("Lecturer 1", Arrays.asList("ІP-91", "ІP-92", "ІP-93"), nWeeks, journal))),
                    (new Thread(new Teacher("Assistant 1", Collections.singletonList("ІP-91"), nWeeks, journal))),
                    (new Thread(new Teacher("Assistant 2", Collections.singletonList("ІP-92"), nWeeks, journal))),
                    (new Thread(new Teacher("Assistant 3", Collections.singletonList("ІP-93"), nWeeks, journal)))
            );
            // starting them
            threads.forEach(Thread::start);
            // joining until all of them will be finished
            threads.forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

        t.start();
        t.join();

        journal.show();
    }
}

@Getter
class Group {
    private final String groupName;
    private final List<Student> students = new ArrayList<>();

    public Group(String groupName, int sizeOfGroup) {
        this.groupName = groupName;
        generateGroupList(sizeOfGroup);
    }

    private void generateGroupList(int sizeOfGroup) {
        for (int i = 0; i < sizeOfGroup; i++) {
            this.students.add(new Student(i));
        }
    }

}

@Getter
class Student {
    private final Integer number;
    private final List<String> marks = new ArrayList<>();

    Student(Integer number) {
        this.number = number;
    }

    synchronized void addMark(String mark) {
        this.marks.add(mark);
    }

}

@Getter
class Journal {
    private final Map<String, Group> groups = new HashMap<>();

    public Journal(List<Group> groups) {
        for (Group group : groups) {
            this.groups.put(group.getGroupName(), group);
        }
    }

    public void addMark(String groupName, Integer studentName, String mark) {
        this.groups.get(groupName).getStudents().get(studentName).addMark(mark);
    }

    public void show() {
        for (String groupName : groups.keySet().stream().sorted().toList()) {
            System.out.printf("Group name: %6s\n", groupName);
            for (Student student : groups.get(groupName).getStudents().stream().sorted(Comparator.comparing(Student::getNumber)).toList()) {
                System.out.printf("Student %5s %5s", student.getNumber(), "-");
                for (String mark : student.getMarks()) {
                    System.out.printf("%30s", mark);
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}

class Teacher implements Runnable {
    private final String teacherName;
    private final List<String> groupNames;
    private final Journal journal;
    private final int nWeeks;

    public Teacher(String teacherName, List<String> groupNames, int nWeeks, Journal journal) {
        this.teacherName = teacherName;
        this.groupNames = groupNames;
        this.journal = journal;
        this.nWeeks = nWeeks;
    }

    @Override
    public void run() {
        for (int i = 0; i < nWeeks; i++) {
            for (String groupName : groupNames) {
                for (Student student : journal.getGroups().get(groupName).getStudents()) {
                    Double mark = (double) (Math.round(100 * Math.random() * 100)) / 100;
                    journal.addMark(groupName, student.getNumber(), mark + " (" + this.teacherName + ")");
                }
            }
        }
    }
}
