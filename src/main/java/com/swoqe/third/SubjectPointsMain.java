package com.swoqe.third;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SubjectPointsMain {
    public static void main(String[] args) throws InterruptedException {
        List<Group> groups = List.of(
                new Group("ІP-91", 25),
                new Group("ІP-92", 30),
                new Group("ІP-93", 26)
        );

        Journal journal = new Journal(groups);
        int nWeeks = 3;

        Thread t = new Thread(() -> {
            List<Thread> threads = List.of(
                    (new Thread(new Teacher("Lecturer 1", Arrays.asList("ІP-91", "ІP-92", "ІP-93"), nWeeks, journal))),
                    (new Thread(new Teacher("Assistant 1", Collections.singletonList("ІP-91"), nWeeks, journal))),
                    (new Thread(new Teacher("Assistant 2", Collections.singletonList("ІP-92"), nWeeks, journal))),
                    (new Thread(new Teacher("Assistant 3", Collections.singletonList("ІP-93"), nWeeks, journal)))
            );
            threads.forEach(Thread::start);
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
    private final Map<Integer, List<String>> groupList = new ConcurrentHashMap<>();

    public Group(String groupName, int sizeOfGroup) {
        this.groupName = groupName;
        generateGroupList(sizeOfGroup);
    }

    private void generateGroupList(int sizeOfGroup) {
        for (int i = 0; i < sizeOfGroup; i++) {
            this.groupList.put(i + 1, new ArrayList<>());
        }
    }

}

@Getter
class Journal {
    private final Map<String, Group> groups = new ConcurrentHashMap<>();

    public Journal(List<Group> groups) {
        for (Group group : groups) {
            this.groups.put(group.getGroupName(), group);
        }
    }

    public void addMark(String groupName, Integer studentName, String mark) {
        this.groups.get(groupName).getGroupList().get(studentName).add(mark);
    }

    public void show() {
        for (String groupName : groups.keySet().stream().sorted().toList()) {
            System.out.printf("Group name: %6s\n", groupName);
            for (Integer studentName : groups.get(groupName).getGroupList().keySet().stream().sorted().toList()) {
                System.out.printf("Student %5s %5s", studentName, "-");
                for (String mark : groups.get(groupName).getGroupList().get(studentName)) {
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
                for (Integer studentName : this.journal.getGroups().get(groupName).getGroupList().keySet()) {
                    Double mark = (double) (Math.round(100 * Math.random() * 100)) / 100;
                    journal.addMark(groupName, studentName, mark + " (" + this.teacherName + ")");
                }
            }
        }
    }
}
