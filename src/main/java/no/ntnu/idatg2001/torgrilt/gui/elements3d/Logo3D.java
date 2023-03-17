package no.ntnu.idatg2001.torgrilt.gui.elements3d;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Logo3D {

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
