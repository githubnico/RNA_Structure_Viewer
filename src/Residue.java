import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;

/**
 * Created by Deviltech on 13.12.2015.
 */
public interface Residue {

    /**
     * Add a single Atom to the Residue
     * @param a
     */
    void addAtom(Atom a);


    /**
     * Generate the sugar TriangleMesh according to Residue Type
     * @return mesh
     */
    TriangleMesh generateSugarMesh();


    /**
     * Generate the base TriangleMesh according to Residue Type
     * @return mesh
     */
    TriangleMesh generateBaseMesh();

    /**
     * Generate phosoporus shape
     * @return shape
     */
    Shape3D generatePhosphorusMesh();

    /**
     * Generate Line between sugar and base
     * @return
     */
    Shape3D generateLine();







}
