package sample;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OneVariableMix implements Serializable {

    private int groupNum;
    private static String filePath;

    public OneVariableMix(String fileName) {
        filePath = fileName;
    }

    public List<List<Student>> sort(String type, int groupNum) {
        this.groupNum = groupNum;

        ReadFile p = new ReadFile(filePath);
        List<Student> people = new ArrayList<>();
        for (int i = 1; i < new ReadFile(filePath).readLine(); i ++) {
            people.add(
                    new Student(
                            p.read(i, 0),
                            p.read(i, 1),
                            p.read(i, 2),
                            p.read(i, 3),
                            p.read(i, 4)
                    )
                    //(name, gender, nationality, homeroom, email)
            );
        }

        Collections.shuffle(people);//randomize the list of people

        Function<Student, String> discriminator;
        //check the type of sorting the user wants
        switch(type) {
            case "homeroom":
                discriminator = Student::getHomeRoom;
                break;
            case "gender":
                discriminator = Student::getGender;
                break;
            case "nationality":
                discriminator = Student::getNationality;
                break;
            case "random":
                discriminator = Student::getName;
                break;
            default:
                discriminator = Student::getName;
                System.out.println("ERROR");
        }

        AtomicInteger index = new AtomicInteger();
        List<List<Student>> groups = new ArrayList<>(people.stream()
                .sorted(Comparator.comparing(discriminator))
                .collect(Collectors.groupingBy(e -> index.getAndIncrement() % groupNum))
                .values());
        return groups;//returns List<List<Student>>
    }

    /**
     *
     * @param type      String : "gender", "nationality", "homeroom",
     * @return          2D ArrayList of emails
     */
    public List<List<String>> toEmail(List<List<Student>> group){
        List<List<String>> emailList = new ArrayList<List<String>>();

        for(int i = 0; i < group.size(); i++) {
            emailList.add(new ArrayList<>()); //add an empty ArrayList
            for(int j = 0; j < group.get(i).size(); j++) {
                emailList.get(i).add(group.get(i).get(j).getEmail());
            }
        }
        return emailList;
    }
}