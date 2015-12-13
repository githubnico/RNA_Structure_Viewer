import java.io.*;
import java.util.ArrayList;

/**
 * Created by Deviltech on 13.12.2015.
 */
public class PDB_Reader {

    public ArrayList<Atom> readInFile(File filePath) throws FileNotFoundException {

        // Checks if file exists
        if (!filePath.exists()) {
            throw new FileNotFoundException(myLabels.FILE_NOT_FOUND + filePath);
        }

        ArrayList<Atom> myAtoms = new ArrayList<>();

        try {
            BufferedReader input = new BufferedReader(new FileReader(filePath));
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    if(line.startsWith("ATOM")){
                        String[] currentValues = line.split("\\s+");
                        myAtoms.add(new Atom(currentValues));
                    }

                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return myAtoms;

    }


    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("C:/Users/Deviltech/Documents/MASTER/Advanced_Java/AUGC.pdb");

            new PDB_Reader().readInFile(f);

    }

}
