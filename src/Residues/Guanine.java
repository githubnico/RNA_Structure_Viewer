package Residues;

import Residues.AResidue;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;

import java.util.HashMap;

/**
 * Created by Deviltech on 21.12.2015.
 */
public class Guanine extends AResidue {

    public Guanine() {
        this.myAtoms = new HashMap<>();
    }


    @Override
    public TriangleMesh generateSugarMesh() {
        return generateGeneralMesh(sugarNames, sugarMeshFaces);
    }

    @Override
    public TriangleMesh generateBaseMesh() {
        return generateGeneralMesh(purineBaseNames, purineBaseMeshFaces);

    }


    @Override
    public Shape3D generateLine() {
        return generateLine(myAtoms.get(purineLine[0]), myAtoms.get(purineLine[1]));
    }
}
