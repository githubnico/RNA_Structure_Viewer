package Residues;

import Residues.AResidue;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;

import java.util.HashMap;

/**
 * Created by Deviltech on 21.12.2015.
 */
public class Uracil extends AResidue {

    public Uracil() {
        this.myAtoms = new HashMap<>();
    }


    @Override
    public TriangleMesh generateSugarMesh() {
        return generateGeneralMesh(sugarNames, sugarMeshFaces);
    }

    @Override
    public TriangleMesh generateBaseMesh() {
        return generateGeneralMesh(pyrimidineBaseNames, pyrimidineBaseMeshFaces);

    }


    @Override
    public Shape3D generateLine() {
        return generateLine(myAtoms.get(pyrimidineLine[0]), myAtoms.get(pyrimidineLine[1]));
    }
}
