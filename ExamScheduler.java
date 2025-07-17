import java.util.*;
class Exam {
    String name;
    Set<String> students;
    int duration; 
    Exam(String name, Set<String> students, int duration) {
        this.name = name;
        this.students = students;
        this.duration = duration;
    }
}
class Room {
    String name;
    int capacity;

    Room(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }
}
class TimeSlot {
    String day;
    String startTime;
    String endTime;
    TimeSlot(String day, String startTime, String endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    @Override
    public String toString() {
        return day + " " + startTime + "-" + endTime;
    }
}
class ExamTimetable {
    List<Exam> exams;
    List<Room> rooms;
    List<TimeSlot> timeSlots;
    Map<String, Exam> schedule = new HashMap<>();
    Map<Exam, String> examAssignments = new HashMap<>();
    ExamTimetable(List<Exam> exams, List<Room> rooms, List<TimeSlot> timeSlots) {
        this.exams = exams;
        this.rooms = rooms;
        this.timeSlots = timeSlots;
    }
    boolean scheduleExams(int index) {
        if (index == exams.size()) return true;
        Exam currentExam = exams.get(index);
        for (Room room : rooms) {
            for (TimeSlot slot : timeSlots) {
                String key = room.name + "-" + slot.toString();
                if (!schedule.containsKey(key) && !hasStudentConflict(currentExam, slot)) {
                    schedule.put(key, currentExam);
                    examAssignments.put(currentExam, key);
                    if (scheduleExams(index + 1)) return true;
                    schedule.remove(key);
                    examAssignments.remove(currentExam);
                }
            }
        }

        return false;
    }
    boolean hasStudentConflict(Exam exam, TimeSlot slot) {
        for (Map.Entry<Exam, String> entry : examAssignments.entrySet()) {
            Exam scheduledExam = entry.getKey();
            String key = entry.getValue();
            if (key.endsWith(slot.toString())) {
                Set<String> common = new HashSet<>(exam.students);
                common.retainAll(scheduledExam.students);
                if (!common.isEmpty()) return true;
            }
        }
        return false;
    }
    void printSchedule() {
        System.out.println("\nFinal Exam Timetable:");
        for (Map.Entry<String, Exam> entry : schedule.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue().name);
        }
    }
}
public class ExamScheduler {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of rooms: ");
        int roomCount = Integer.parseInt(scanner.nextLine());
        List<Room> rooms = new ArrayList<>();
        for (int i = 0; i < roomCount; i++) {
            System.out.print("Room " + (i + 1) + " name: ");
            String name = scanner.nextLine();
            System.out.print("Room capacity: ");
            int cap = Integer.parseInt(scanner.nextLine());
            rooms.add(new Room(name, cap));
        }
        System.out.print("\nEnter number of time slots: ");
        int slotCount = Integer.parseInt(scanner.nextLine());
        List<TimeSlot> slots = new ArrayList<>();
        for (int i = 0; i < slotCount; i++) {
            System.out.print("Day for TimeSlot " + (i + 1) + ": ");
            String day = scanner.nextLine();
            System.out.print("Start time (e.g., 09:00): ");
            String start = scanner.nextLine();
            System.out.print("End time (e.g., 11:00): ");
            String end = scanner.nextLine();
            slots.add(new TimeSlot(day, start, end));
        }
        System.out.print("\nEnter number of exams: ");
        int examCount = Integer.parseInt(scanner.nextLine());
        List<Exam> exams = new ArrayList<>();
        for (int i = 0; i < examCount; i++) {
            System.out.print("Exam " + (i + 1) + " name: ");
            String name = scanner.nextLine();
            System.out.print("Duration in minutes: ");
            int duration = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter number of students for " + name + ": ");
            int studentCount = Integer.parseInt(scanner.nextLine());
            Set<String> students = new HashSet<>();
            for (int j = 0; j < studentCount; j++) {
                System.out.print("Student " + (j + 1) + " ID: ");
                students.add(scanner.nextLine());
            }
            exams.add(new Exam(name, students, duration));
        }
        ExamTimetable timetable = new ExamTimetable(exams, rooms, slots);
        System.out.println("\nScheduling exams...");
        if (timetable.scheduleExams(0)) {
            timetable.printSchedule();
        } else {
            System.out.println("No feasible exam timetable found!");
        }
        scanner.close();
    }
}
