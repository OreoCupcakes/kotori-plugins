package com.theplug.kotori.tarnslair;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public class DirectionArrow
{
	public static void drawLine(Graphics2D graphics, Line2D.Double line, Color color, Rectangle clippingRegion)
	{
		graphics.setStroke(new BasicStroke(1));
		graphics.setClip(clippingRegion);
		graphics.setColor(color);
		graphics.draw(line);
		
		drawLineArrowHead(graphics, line);
	}
	
	public static void drawLineArrowHead(Graphics2D g2d, Line2D.Double line) {
		AffineTransform tx = new AffineTransform();
		
		Polygon arrowHead = new Polygon();
		arrowHead.addPoint( 0,0);
		arrowHead.addPoint( -3, -6);
		arrowHead.addPoint( 3,-6);
		
		tx.setToIdentity();
		double angle = Math.atan2(line.y2-line.y1, line.x2-line.x1);
		tx.translate(line.x2, line.y2);
		tx.rotate((angle-Math.PI/2d));
		
		Graphics2D g = (Graphics2D) g2d.create();
		g.setTransform(tx);
		g.fill(arrowHead);
		g.dispose();
	}
}
