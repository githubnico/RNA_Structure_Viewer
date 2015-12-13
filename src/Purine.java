import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;

import java.util.HashMap;

/**
 * Created by Deviltech on 13.12.2015.
 */
public class Purine implements Residue{


    HashMap<String, Atom> myAtoms;
    String[] baseNames = {"C4", "C5", "C6","N1", "C2", "N3", "N9", "C8", "N7"};
    String[] sugarNames = {"C1\'", "C2\'", "C3\'", "C4\'", "O4\'"};

    int baseMeshFaces[] = {
            0,0,    1,0,    5,0,
            1,0,    2,0,    5,0,
            2,0,    3,0,    5,0,
            3,0,    4,0,    5,0,
            0,0,    6,0,    7,0,
            0,0,    7,0,    8,0,
            0,0,    8,0,    1,0

    };

    int sugarMeshFaces[] = {
            0,0,    1,0,    4,0,
            1,0,    2,0,    4,0,
            2,0,    3,0,    4,0
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
        System.out.println(myAtoms.keySet().toString());
        Sphere sphere = new Sphere(0.5);
        sphere.setTranslateX(phosphorus.getCoordX());
        sphere.setTranslateY(phosphorus.getCoordY());
        sphere.setTranslateZ(phosphorus.getCoordZ());
        return sphere;
    }
}
