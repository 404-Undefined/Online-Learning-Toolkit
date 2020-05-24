package sample;

public class Student {

    private String name;
    private String nationality;
    private String homeRoom;
    private String gender;
    private String email;


    public Student(String name, String gender, String nationality, String homeRoom, String email){

        this.name = name;
        this.email = email;
        this.nationality = nationality;
        this.homeRoom = homeRoom;
        this.gender = gender;

    }



    public String getName() {
        return name;
    }

    public String getEmail(){
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getHomeRoom() {
        return homeRoom;
    }

    public void setHomeRoom(String homeRoom) {
        this.homeRoom = homeRoom;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", homeRoom='" + homeRoom + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
