import java.util.ArrayList; 

import java.io.File; import java.io.IOException; import java.io.FileReader; import java.io.FileWriter; 
import java.io.PrintWriter; import java.io.BufferedReader; import java.io.BufferedWriter;

import com.opencsv.*; import com.opencsv.exceptions.CsvValidationException;

import java.time.LocalDate; 

class studentDatabase { 
    int studentID; String lastName; String firstName; LocalDate birthday; String address; String guardian; double gwa; int[] courseGrade = new int[8];  
    static CSVReader csvReader = null;                         

    studentDatabase() { // constructor with no parameters
    }

    studentDatabase(int id, String ln, String fn, LocalDate bd, String a, String g, double gwa, int[] cg) {    // constructor that sets the attributes for the objects in the ArrayList studentList                                               
        studentID = id;
        lastName = ln;
        firstName = fn;
        birthday = bd;
        address = a;
        guardian = g;
        courseGrade = cg;
    }

    static ArrayList<studentDatabase> getStudentList() { // get method to get studentlist
        ArrayList<studentDatabase> studentList = new ArrayList<studentDatabase>(); // contains the objects (students) which holds a lot of attributes such as studentID, lastName, firstName, etc.
        readDatabase(studentList);
        return studentList;
    }

    static void createNewDatabase() { // class to create new File class and database.csv file
        String file = "database.csv"; File databaseFile = new File(file);


        try { // method to create new file
            databaseFile.createNewFile();
        } catch (IOException ioe) {
            System.out.println("An error occured in creating database.");
        }

        readDatabase(getStudentList());
    }

    static void readDatabase(ArrayList<studentDatabase> studentList) { // reads every line in the database.csv file to parse into studentList arrayList                                                    
        String[] cellRow; // String that will hold the String for each row in the database.csv file

        try {

            try { // csvreader setup
                csvReader = new CSVReaderBuilder(new FileReader(new File("database.csv")))
                .withCSVParser(new CSVParserBuilder()
                    .withSeparator(';')
                    .build())
                .build();
            } catch (Exception e) {
                e.printStackTrace();
            }

            int databaseIndex = 0;
            while ((cellRow = csvReader.readNext()) != null) { // reads each line in the file to parse data fields of a student
                studentList.add(new studentDatabase());                              
                studentList.get(databaseIndex).studentID = Integer.parseInt(cellRow[0]);
                studentList.get(databaseIndex).lastName = cellRow[1];
                studentList.get(databaseIndex).firstName = cellRow[2];
                studentList.get(databaseIndex).birthday = LocalDate.of(Integer.parseInt(cellRow[3]), Integer.parseInt(cellRow[4]),Integer.parseInt(cellRow[5]));
                studentList.get(databaseIndex).address = cellRow[6];
                studentList.get(databaseIndex).guardian = cellRow[7];
                studentList.get(databaseIndex).gwa = Double.parseDouble(cellRow[8]);
                // reads the individual course grade cells in the file to convert to an array of grades
                int j = 9;
                for (int i = 0; i < 8; i++) {
                    studentList.get(databaseIndex).courseGrade[i] = Integer.parseInt(cellRow[j]);
                    j++;
                }

                databaseIndex++;
            }
            csvReader.close();
        } catch (IOException ioe) {
            System.out.print("An error occured. Print stack trace?\n\nInput ['Y' to Print]:");
            if (Scan.caro.next().equalsIgnoreCase("Y")) ioe.printStackTrace();
        } catch (CsvValidationException csv) {
            System.out.print("An error occured. Print stack trace?\n\nInput ['Y' to Print]:");
            if (Scan.caro.next().equalsIgnoreCase("Y")) csv.printStackTrace();
        }   
    }

    static void writeUserToFile(String lineToWrite) { // writes the line of the user input when adding user to the next empty line in the file
        BufferedReader br = null; BufferedWriter bw = null;
        int rowIndex = 0;
        try {
            br = new BufferedReader(new FileReader(new File("database.csv")));
            bw = new BufferedWriter(new FileWriter(new File("database.csv"), true));
            while ((br.readLine()) != null) { // while (bufferedreader does not read a line that doesnt contain nothing) { add number of lines read by the bufferedreader by 1 }
                rowIndex++; 
            }
            
            if (rowIndex > 0) bw.newLine(); // writes a new line after the last row of content so that it doesnt overwrite it
            bw.write(lineToWrite); br.close(); bw.close(); // writes the actual line

        } catch (Exception e) {
            System.out.print("An error occured. Print stack trace?\n\nInput ['Y' to Print]:");
            if (Scan.caro.next().equalsIgnoreCase("Y")) e.printStackTrace();
        }
    }

    static void reWriteFile(ArrayList<studentDatabase> studentList) { // rewrites the entire file whenever a change is made (edit or deleted)
        BufferedReader br = null; BufferedWriter bw = null; PrintWriter pw = null;
        try {      
            pw = new PrintWriter("database.csv");
            for (int i = 0; i < studentList.size(); i++) {
                br = new BufferedReader(new FileReader(new File("database.csv")));
                bw = new BufferedWriter(new FileWriter(new File("database.csv"), true));  
                while (br.readLine() != null) {}
                String lineToWrite = String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s", 
                studentList.get(i).studentID, studentList.get(i).lastName, studentList.get(i).firstName, 
                studentList.get(i).birthday.getYear(), studentList.get(i).birthday.getMonthValue(), studentList.get(i).birthday.getDayOfMonth(), 
                studentList.get(i).address, studentList.get(i).guardian, studentList.get(i).gwa, 
                studentList.get(i).courseGrade[0], studentList.get(i).courseGrade[1], studentList.get(i).courseGrade[2], 
                studentList.get(i).courseGrade[3], studentList.get(i).courseGrade[4], studentList.get(i).courseGrade[5], 
                studentList.get(i).courseGrade[6], studentList.get(i).courseGrade[7]);
                if (i > 0) {
                    bw.newLine();
                }
                else {
                    pw.close();
                }
                bw.write(lineToWrite); br.close(); bw.close();
            }
        } catch (Exception e) {
            System.out.print("An error occured. Print stack trace?\n\nInput ['Y' to Print]:");
            if (Scan.caro.next().equalsIgnoreCase("Y")) e.printStackTrace();
        }
    }
}