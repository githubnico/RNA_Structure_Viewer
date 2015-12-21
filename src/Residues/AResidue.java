package Residues;

import javafx.geometry.Point3D;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.HashMap;

/**
 * Created by Deviltech on 21.12.2015.
 */
public abstract class AResidue {


    // names for structures
    String[] pyrimidineLine = {"N1", "C1\'"};
    String[] purineLine = {"N9", "C1\'"};

    String[] purineBaseNames = {"C4", "C5", "C6","N1", "C2", "N3", "N9", "C8", "N7"};
    String[] pyrimidineBaseNames = {"N1", "C2", "N3", "C4", "C5", "C6"};
    String[] sugarNames = {"C1\'", "C2\'", "C3\'", "C4\'", "O4\'"};

    int purineBaseMeshFaces[] = {
            0,0,    1,0,    5,0, // front
            1,0,    2,0,    5,0,
            2,0,    3,0,    5,0,
            3,0,    4,0,    5,0,
            0,0,    6,0,    7,0,
            0,0,    7,0,    8,0,
            0,0,    8,0,    1,0,
            0,0,    5,0,    1,0, // back
            1,0,    5,0,    2,0,
            2,0,    5,0,    3,0,
            3,0,    5,0,    4,0,
            0,0,    7,0,    6,0,
            0,0,    8,0,    7,0,
            0,0,    1,0,    8,0

    };



    int pyrimidineBaseMeshFaces[] = {
            0,0,    1,0,    5,0,// front
            1,0,    2,0,    5,0,
            2,0,    3,0,    5,0,
            3,0,    4,0,    5,0,
            0,1,    5,0,    1,0,//back
            1,0,    5,0,    2,0,
            2,0,    5,0,    3,0,
            3,0,    5,0,    4,0
    };

    int sugarMeshFaces[] = {
            0,0,    1,0,    4,0, //front
            1,0,    2,0,    4,0,
            2,0,    3,0,    4,0,
            0,0,    4,0,    1,0,//back
            1,0,    4,0,    2,0,
            2,0,    4,0,    3,0
    };


    //contains all atoms of residue
    HashMap<String, Atom> myAtoms;


    // START abstract

    /**
     * Generate the sugar TriangleMesh according to Residue Type
     * @return mesh
     */
    abstract public TriangleMesh generateSugarMesh();


    /**
     * Generate the base TriangleMesh according to Residue Type
     * @return mesh
     */
    abstract public TriangleMesh generateBaseMesh();


    /**
     * Generate Line between sugar and base
     * @return
     */
    abstract public Shape3D generateLine();

    // END abstract

    /**
     * Add a single Residues.Atom to the Residues.AResidue
     * @param a
     */
    public void addAtom(Atom a){
        myAtoms.put(a.getAtomName(), a);
    }


    /**
     * Generate phosoporus shape
     * @return shape
     */
    public boolean generatePhosphorusMesh(Shape3D sphere){
        Atom phosphorus = myAtoms.get("P");
        if(phosphorus != null) {
            sphere.setTranslateX(phosphorus.getCoordX());
            sphere.setTranslateY(phosphorus.getCoordY());
            sphere.setTranslateZ(phosphorus.getCoordZ());
            return true;
        } else {
            return false;
        }
    }


    /**
     * general TriangleMesh generation
     * @param atomNames
     * @param faces
     * @return
     */
    public TriangleMesh generateGeneralMesh(String[] atomNames, int faces[]){
        TriangleMesh mesh = new TriangleMesh();

        for(String currentName: atomNames){
            Atom currentAtom = myAtoms.get(currentName);
            float points[] = {currentAtom.getCoordX(), currentAtom.getCoordY(), currentAtom.getCoordZ()};
            mesh.getPoints().addAll(points);
        }
        mesh.getTexCoords().addAll(0.0f, 1.0f, 0.5f, 0.5f, 0.5f, 1.0f);
        mesh.getFaces().addAll(faces);

        return mesh;
    }

    /**
     * generate a Line (cylinder) between two Atoms
     * @param firstAtom
     * @param secondAtom
     * @return
     */
    public Shape3D generateLine(Atom firstAtom, Atom secondAtom){
        Point3D origin = new Point3D(firstAtom.getCoordX(), firstAtom.getCoordY(), firstAtom.getCoordZ());
        Point3D target = new Point3D(secondAtom.getCoordX(), secondAtom.getCoordY(), secondAtom.getCoordZ());
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(0.03, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }



}
