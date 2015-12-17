import java.io.*;
import java.util.ArrayList;

/**
 * Created by Deviltech on 13.12.2015.
 */
public class PDB_Reader {

    // separator indices for pdb standard
    int indices[] = new int[]{0, 6, 11, 16, 17, 20, 22, 26, 27, 38, 46, 54, 60, 66, 76, 78};

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
                        String[] currentValues = splitByIndices(line, indices);
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


    /**
     * Splits a string according given indices
     * @param myString
     * @param indices
     * @return
     */
    public String[] splitByIndices(String myString, int[] indices){
        String[] result = new String[indices.length-1];
        for(int i= 0; i< indices.length-1; i++){
            result[i] = myString.substring(indices[i], indices[i+1]);
        }
        return result;
    }

}
