package loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

public class OBJLoader {


   
    private static Vector3d parseVertex(String line) {
        String[] xyz = line.split(" ");
        float x = Float.valueOf(xyz[1]);
        float y = Float.valueOf(xyz[2]);
        float z = Float.valueOf(xyz[3]);
        return new Vector3d(x, y, z);
    }

    private static Vector3d parseNormal(String line) {
        String[] xyz = line.split(" ");
        float x = Float.valueOf(xyz[1]);
        float y = Float.valueOf(xyz[2]);
        float z = Float.valueOf(xyz[3]);
        return new Vector3d(x, y, z);
    }

    private static Model.Face parseFace(boolean hasNormals, String line) {
        String[] faceIndices = line.split(" ");
        int[] vertexIndicesArray = {Integer.parseInt(faceIndices[1].split("/")[0]),
                Integer.parseInt(faceIndices[2].split("/")[0]), Integer.parseInt(faceIndices[3].split("/")[0])};
        if (hasNormals) {
            int[] normalIndicesArray = new int[3];
            normalIndicesArray[0] = Integer.parseInt(faceIndices[1].split("/")[2]);
            normalIndicesArray[1] = Integer.parseInt(faceIndices[2].split("/")[2]);
            normalIndicesArray[2] = Integer.parseInt(faceIndices[3].split("/")[2]);
            return new Model.Face(vertexIndicesArray, normalIndicesArray);
        } else {
            return new Model.Face((vertexIndicesArray));
        }
    }

    public static Model loadModel(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        Model m = new Model();
        String line;
        while ((line = reader.readLine()) != null) {
            String prefix = line.split(" ")[0];
            if (prefix.equals("#")) {
                continue;
            } else if (prefix.equals("v")) {
                m.getVertices().add(parseVertex(line));
            } else if (prefix.equals("vn")) {
                m.getNormals().add(parseNormal(line));
            } else if (prefix.equals("f")) {
                m.getFaces().add(parseFace(m.hasNormals(), line));
            }
        }
        reader.close();
        return m;
    }

   

    public static Model loadTexturedModel(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        Model m = new Model();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#")) {
                continue;            
            } else if (line.startsWith("v ")) {
                String[] xyz = line.split(" ");
                float x = Float.valueOf(xyz[1]);
                float y = Float.valueOf(xyz[2]);
                float z = Float.valueOf(xyz[3]);
                m.getVertices().add(new Vector3d(x, y, z));
            } else if (line.startsWith("vn ")) {
                String[] xyz = line.split(" ");
                float x = Float.valueOf(xyz[1]);
                float y = Float.valueOf(xyz[2]);
                float z = Float.valueOf(xyz[3]);
                m.getNormals().add(new Vector3d(x, y, z));
            } else if (line.startsWith("vt ")) {
                String[] xyz = line.split(" ");
                float s = Float.valueOf(xyz[1]);
                float t = Float.valueOf(xyz[2]);
                m.getTextureCoordinates().add(new Vector2d(s, t));
            } else if (line.startsWith("f ")) {
                String[] faceIndices = line.split(" ");
                int[] vertexIndicesArray = {Integer.parseInt(faceIndices[1].split("/")[0]),
                        Integer.parseInt(faceIndices[2].split("/")[0]), Integer.parseInt(faceIndices[3].split("/")[0])};
                int[] textureCoordinateIndicesArray = {-1, -1, -1};
                if (m.hasTextureCoordinates()) {
                    textureCoordinateIndicesArray[0] = Integer.parseInt(faceIndices[1].split("/")[1]);
                    textureCoordinateIndicesArray[1] = Integer.parseInt(faceIndices[2].split("/")[1]);
                    textureCoordinateIndicesArray[2] = Integer.parseInt(faceIndices[3].split("/")[1]);
                }
                int[] normalIndicesArray = {0, 0, 0};
                if (m.hasNormals()) {
                    normalIndicesArray[0] = Integer.parseInt(faceIndices[1].split("/")[2]);
                    normalIndicesArray[1] = Integer.parseInt(faceIndices[2].split("/")[2]);
                    normalIndicesArray[2] = Integer.parseInt(faceIndices[3].split("/")[2]);
                }
                //                Vector3d vertexIndices = new Vector3d(Float.valueOf(faceIndices[1].split("/")[0]),
                //                        Float.valueOf(faceIndices[2].split("/")[0]),
                // Float.valueOf(faceIndices[3].split("/")[0]));
                //                Vector3d normalIndices = new Vector3d(Float.valueOf(faceIndices[1].split("/")[2]),
                //                        Float.valueOf(faceIndices[2].split("/")[2]),
                // Float.valueOf(faceIndices[3].split("/")[2]));
                m.getFaces().add(new Model.Face(vertexIndicesArray, normalIndicesArray,
                        textureCoordinateIndicesArray));
            }
        }
        reader.close();
        return m;
    }
}