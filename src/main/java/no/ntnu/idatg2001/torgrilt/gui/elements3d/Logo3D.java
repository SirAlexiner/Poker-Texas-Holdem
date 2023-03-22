package no.ntnu.idatg2001.torgrilt.gui.elements3d;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import lombok.experimental.UtilityClass;

/**
 * It's a 3D logo.
 */
@UtilityClass
public class Logo3D {

  /**
   * "Create a box with a logo image on it."
   *
   * <p>.
   * The first line creates an Image object from the logo.png file.
   * The second line creates a PhongMaterial object. The
   * third line sets the diffuse map of the PhongMaterial object to the logo image.
   * The fourth line sets the diffuse color
   * of the PhongMaterial object to white.
   * The fifth line sets the specular color of the PhongMaterial object to white.
   * The sixth line sets the specular power of the PhongMaterial object to 30.
   * The seventh line creates a Box object with the
   * dimensions 720, 127, and 2.
   * The eighth line sets the rotation axis of the Box object to the Y axis.
   * The ninth line sets the rotation of the Box object to -11.5 degrees.
   * The tenth line sets the material of the Box object to the
   * PhongMaterial object. The eleventh line returns the Box object
   *
   * @return A Box object with a logo image on it.
   */
  public static Box getLogoBox() {
    Image logoImage = new Image("images/logo.png");
    PhongMaterial logoMaterial = new PhongMaterial();
    logoMaterial.setDiffuseMap(logoImage);
    logoMaterial.setDiffuseColor(Color.rgb(255, 255, 255));
    logoMaterial.setSpecularColor(Color.WHITE);
    logoMaterial.setSpecularPower(30);

    Box logo = new Box(720, 127, 2);
    logo.setRotationAxis(Rotate.Y_AXIS);
    logo.setRotate(-11.5);
    logo.setMaterial(logoMaterial);

    return logo;
  }
}
