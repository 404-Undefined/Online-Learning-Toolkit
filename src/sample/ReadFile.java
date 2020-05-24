package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    private static String filePath;
    //reading csv

    public ReadFile(String fileName) {
        filePath = fileName;
    }
    public ReadFile(){

    }

    /**
     *
     * @param row of the CSV
     * @param column of the row
     * @return the value stored at row, column
     */
    public String read(int row, int column){
        String[] data;//array for each row
        String r;
        String[][] p = new String[readLine() + 1][5];
        int i = 0;
        try(BufferedReader csvReader = new BufferedReader(new FileReader(filePath))){
            while ((r = csvReader.readLine()) != null) { //if the file has next line
                data = r.split(","); //splits the contents with ","
                p[i] = data;
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return(p[row][column]);//printing specific rows in 2d array,
    }

    /**
     *
     * @return  the number of lines in the file
     */
    public int readLine(){
        int num = 0;
        String row;
        try(BufferedReader csvReader = new BufferedReader(new FileReader(filePath))){
            while ((row = csvReader.readLine()) != null) {
                num++;//if hasNextLine, adds to num
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return num;
    }


    public void storeGroups(List savedGrouped, String name){
        try
        {
            File file = new File(name+".txt");
            FileOutputStream fos = new FileOutputStream(name + ".txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(savedGrouped);
            oos.close();
            fos.close();

            try(FileWriter fw = new FileWriter("pastgroups.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);

                PrintWriter out = new PrintWriter(bw)) {
                bw.write("\r");
                bw.write(name);

            }catch(IOException e){

            }

        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

    }

    public List<List<String>> deserialise(String file){
        ArrayList<List<String>> student = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(file + ".txt");
            ObjectInputStream ois = new ObjectInputStream(fis);

            student = (ArrayList) ois.readObject();

            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();

        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();

        }
        return student;
    }
}
