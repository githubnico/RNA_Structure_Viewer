import javafx.geometry.Point3D;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.HashMap;

/**
 * Created by Deviltech on 13.12.2015.
 */
public class Purine implements Residue{


    HashMap<String, Atom> myAtoms;
    String[] baseNames = {"C4", "C5", "C6","N1", "C2", "N3", "N9", "C8", "N7"};
    String[] sugarNames = {"C1\'", "C2\'", "C3\'", "C4\'", "O4\'"};

    int baseMeshFaces[] = {
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

    int sugarMeshFaces[] = {
            0,0,    1,0,    4,0, // front
            1,0,    2,0,    4,0,
            2,0,    3,0,    4,0,
            0,0,    4,0,    1,0, // back
            1,0,    4,0,    2,0,
            2,0,    4,0,    3,0

    };

    public Purine() {
        this.myAtoms = new HashMap<>();
    }

    @Override
    public void addAtom(Atom a) {
        myAtoms.put(a.getAtomName(), a);
    }

    @Override
    public TriangleMesh generateSugarMesh() {
        TriangleMesh mesh = new TriangleMesh();

        for(String currentSugar: sugarNames){
            Atom currentAtom = myAtoms.get(currentSugar);
            float points[] = {currentAtom.getCoordX(), currentAtom.getCoordY(), currentAtom.getCoordZ()};
            mesh.getPoints().addAll(points);
        }
        mesh.getTexCoords().addAll(0.0f, 1.0f, 0.5f, 0.5f, 0.5f, 1.0f);
        mesh.getFaces().addAll(sugarMeshFaces);

        return mesh;
    }

    @Override
    public TriangleMesh generateBaseMesh() {
        TriangleMesh mesh = new TriangleMesh();

        for(String currentBase: baseNames){
            Atom currentAtom = myAtoms.get(currentBase);
            float points[] = {currentAtom.getCoordX(), currentAtom.getCoordY(), currentAtom.getCoordZ()};
            mesh.getPoints().addAll(points);
        }


        mesh.getTexCoords().addAll(0.0f, 1.0f, 0.5f, 0.5f, 0.5f, 1.0f);
        mesh.getFaces().addAll(baseMeshFaces);

        return mesh;
    }

    @Override
    public Shape3D generatePhosphorusMesh() {
        Atom phosphorus = myAtoms.get("P");
        Sphere sphere = new Sphere(0.5);
        sphere.setTranslateX(phosphorus.getCoordX());
        sphere.setTranslateY(phosphorus.getCoordY());
        sphere.setTranslateZ(phosphorus.getCoordZ());
        return sphere;
    }

    @Override
    public Shape3D generateLine(){
        Atom N9 = myAtoms.get("N9");
        Atom C1 = myAtoms.get("C1\'");
        Point3D origin = new Point3D(N9.getCoordX(), N9.getCoordY(), N9.getCoordZ());
        Point3D target = new Point3D(C1.getCoordX(), C1.getCoordY(), C1.getCoordZ());
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(0.1, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;

    }
}
