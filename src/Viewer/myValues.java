package Viewer;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * Created by Deviltech on 13.12.2015.
 */
public class myValues {

    /**
     * Read in
     */
    // separator indices for pdb standard
    public static int PDB_INDICES[] = new int[]{0, 6, 11, 16, 17, 20, 22, 26, 27, 38, 46, 54, 60, 66, 76, 78};

    /**
     * General Labeling
     */
    // Strings
    public static String FILE_NOT_FOUND = "File not found.";

    public static String NO_FILE_SELECTED = "No file selected";

    /**
     * Draw values
     */

    // Doubles

    public static double LINE_WIDTH_SMALL = 0.03;

    public static double LINE_WIDTH_MEDIUM = 0.35;

    // Materials

    public static PhongMaterial MATERIAL_DARK_GREEN = new PhongMaterial(Color.SEAGREEN);

    public static PhongMaterial MATERIAL_BLACK = new PhongMaterial((Color.DARKGRAY));

    public static PhongMaterial MATERIAL_RED = new PhongMaterial((Color.DARKRED));

    public static PhongMaterial MATERIAL_ORANGE = new PhongMaterial((Color.ORANGE));


    /**
     * Residue mesh values
     */

    // names for structures
    public static String[] RESIDUE_PYRIMIDINE_LINE = {"N1", "C1\'"};

    public static String[] RESIDUE_PURINE_LINE = {"N9", "C1\'"};

    public static String[] RESIDUE_PURINE_BASE_NAMES = {"C4", "C5", "C6", "N1", "C2", "N3", "N9", "C8", "N7"};

    public static String[] RESIDUE_PYRIMIDINE_BASE_NAMES = {"N1", "C2", "N3", "C4", "C5", "C6"};

    public static String[] RESIDUE_SUGAR_NAMES = {"C1\'", "C2\'", "C3\'", "C4\'", "O4\'"};

    public static int RESIDUE_PURINE_BASE_FACES[] = {
            0, 0, 1, 0, 5, 0, // front
            1, 0, 2, 0, 5, 0,
            2, 0, 3, 0, 5, 0,
            3, 0, 4, 0, 5, 0,
            0, 0, 6, 0, 7, 0,
            0, 0, 7, 0, 8, 0,
            0, 0, 8, 0, 1, 0,
            0, 0, 5, 0, 1, 0, // back
            1, 0, 5, 0, 2, 0,
            2, 0, 5, 0, 3, 0,
            3, 0, 5, 0, 4, 0,
            0, 0, 7, 0, 6, 0,
            0, 0, 8, 0, 7, 0,
            0, 0, 1, 0, 8, 0

    };

    public static int RESIDUE_PYRIMIDINE_BASE_FACES[] = {
            0, 0, 1, 0, 5, 0,// front
            1, 0, 2, 0, 5, 0,
            2, 0, 3, 0, 5, 0,
            3, 0, 4, 0, 5, 0,
            0, 1, 5, 0, 1, 0,//back
            1, 0, 5, 0, 2, 0,
            2, 0, 5, 0, 3, 0,
            3, 0, 5, 0, 4, 0
    };

    public static int RESIDUE_SUGAR_FACES[] = {
            0, 0, 1, 0, 4, 0, //front
            1, 0, 2, 0, 4, 0,
            2, 0, 3, 0, 4, 0,
            0, 0, 4, 0, 1, 0,//back
            1, 0, 4, 0, 2, 0,
            2, 0, 4, 0, 3, 0
    };




}
