package Residues;

import Viewer.myValues;
import javafx.geometry.Point3D;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.HashMap;

/**
 * Created by Deviltech on 21.12.2015.
 */
public abstract class AResidue {


    //contains all atoms of residue
    HashMap<String, Atom> myAtoms;


    // START abstract

    /**
     * Generate the sugar TriangleMesh according to Residue Type
     *
     * @return mesh
     */
    abstract public MeshView generateSugarMesh();


    /**
     * Generate the base TriangleMesh according to Residue Type
     *
     * @return mesh
     */
    abstract public MeshView generateBaseMesh();


    /**
     * Generate Line between sugar and base
     *
     * @return
     */
    abstract public Shape3D generateLine();

    // END abstract

    /**
     * Add a single Residues.Atom to the Residues.AResidue
     *
     * @param a
     */
    public void addAtom(Atom a) {
        myAtoms.put(a.getAtomName(), a);
    }

    /**
     * Get the Atom according to it's name
     * @param atomName
     * @return
     */
    public Atom getAtom(String atomName) {
            return myAtoms.get(atomName);
    }


    /**
     * Generate phosoporus shape
     *
     * @return shape
     */
    public boolean generatePhosphorusMesh(Shape3D sphere) {
        Atom phosphorus = myAtoms.get("P");
        if (phosphorus != null) {
            sphere.setTranslateX(phosphorus.getCoordX());
            sphere.setTranslateY(phosphorus.getCoordY());
            sphere.setTranslateZ(phosphorus.getCoordZ());
            sphere.setMaterial(myValues.MATERIAL_DARK_GREEN);
            return true;
        } else {
            return false;
        }
    }


    /**
     * general TriangleMesh generation
     *
     * @param atomNames
     * @param faces
     * @return
     */
    public MeshView generateGeneralMesh(String[] atomNames, int faces[]) {
        TriangleMesh mesh = new TriangleMesh();

        for (String currentName : atomNames) {
            Atom currentAtom = myAtoms.get(currentName);
            float points[] = {currentAtom.getCoordX(), currentAtom.getCoordY(), currentAtom.getCoordZ()};
            mesh.getPoints().addAll(points);
        }
        mesh.getTexCoords().addAll(0.0f, 1.0f, 0.5f, 0.5f, 0.5f, 1.0f);
        mesh.getFaces().addAll(faces);

        MeshView myMeshView = new MeshView(mesh);

        Atom firstAtom = myAtoms.get(atomNames[0]);
        Tooltip myToolTip = new Tooltip(firstAtom.getAtomResidue()+Integer.toString(firstAtom.getAtomResidueIndex()));
        Tooltip.install(myMeshView, myToolTip);

        return  myMeshView;
    }

    /**
     * generate a Line (cylinder) between two Atoms
     *
     * @param firstAtom
     * @param secondAtom
     * @param width
     * @return
     */
    public Shape3D generateLine(Atom firstAtom, Atom secondAtom, double width) {
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

        Cylinder line = new Cylinder(width, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }


}
