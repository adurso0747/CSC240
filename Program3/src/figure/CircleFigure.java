package figure;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Alex Durso <ad901965@wcupa.edu>
 */
public class CircleFigure extends Figure {
	private final double diameter;
	
	public CircleFigure(double diameter){
		this.diameter = diameter;
		this.shape = new Ellipse2D.Double(0, 0, diameter, diameter);
	}

	@Override
	public void draw(Graphics2D g2) {
		if (stroke == null) {
      stroke = new BasicStroke(strokeWidth);
    }
    g2.setStroke(stroke);
	 if (fillColor != null) {
      g2.setColor(fillColor);  // set color
      g2.fill(shape);          // and fill the shape
    }
 
    g2.setColor(lineColor); // set color
    g2.draw(shape);         // draw the shape (the outline)
	}

	@Override
	public Shape getPositionShape() {
		return new Ellipse2D.Double(xLoc, yLoc, diameter * scale, diameter * scale);
	}
}
