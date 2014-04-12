package footballscout;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;

public class GraphingData extends JPanel {
	int[] data = {
			21, 14, 18, 03, 86, 88, 74, 87, 54, 77,
			61, 55, 48, 60, 49, 36, 38, 27, 20, 18
	};

	private int m_Pad = 20;
	private String m_OrdinateLabel;
	private String m_AbscissaLabel;
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int w = getWidth();
		int h = getHeight();
		// Draw ordinate axis
		g2.draw(new Line2D.Double(m_Pad, m_Pad, m_Pad, h-m_Pad));
		// Draw abscissa axis
		g2.draw(new Line2D.Double(m_Pad, h-m_Pad, w-m_Pad, h-m_Pad));
		
		//=============//
		// Draw labels //
		//=============//
		Font font = g2.getFont();
		FontRenderContext frc = g2.getFontRenderContext();
		LineMetrics lm = font.getLineMetrics("0", frc);
		float sh = lm.getAscent() + lm.getDescent();
		// Ordinate label.
		float sy = m_Pad + ((h - 2*m_Pad) - m_OrdinateLabel.length()*sh)/2 + lm.getAscent();
		for(int i = 0; i < m_OrdinateLabel.length(); i++) {
			String letter = String.valueOf(m_OrdinateLabel.charAt(i));
			float sw = (float)font.getStringBounds(letter, frc).getWidth();
			float sx = (m_Pad - sw)/2;
			g2.drawString(letter, sx, sy);
			sy += sh;
		}
		// Abscissa label.
		sy = h - m_Pad + (m_Pad - sh)/2 + lm.getAscent();
		float sw = (float)font.getStringBounds(m_AbscissaLabel, frc).getWidth();
		float sx = (w - sw)/2;
		g2.drawString(m_AbscissaLabel, sx, sy);
		// Draw lines.
		// The space between values along the abcissa.
		double xInc = (double)(w - 2*m_Pad)/(data.length-1);
		// Scale factor for ordinate/data values.
		double scale = (double)(h - 2*m_Pad)/GetMax();
		g2.setPaint(Color.green.darker());
		for(int i = 0; i < data.length-1; i++) {
			double x1 = m_Pad + i*xInc;
			double y1 = h - m_Pad - scale*data[i];
			double x2 = m_Pad + (i+1)*xInc;
			double y2 = h - m_Pad - scale*data[i+1];
			g2.draw(new Line2D.Double(x1, y1, x2, y2));
		}
		// Mark data points.
		g2.setPaint(Color.red);
		for(int i = 0; i < data.length; i++) {
			// For the x location, start with m_Pad to move
			// to the graph origin and count "xInc" intervals
			// to the desired value location:
			double x = m_Pad + i*xInc;
			double y = h - m_Pad - scale*data[i];
			g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
		}
	}

	private int GetMax() {
		int max = -Integer.MAX_VALUE;
		for(int i = 0; i < data.length; i++) {
			if(data[i] > max)
				max = data[i];
		}
		return max;
	}

	public void SetOrdinateLabel(String label)
	{
		m_OrdinateLabel = label;
	}
	
	public void SetAbscissaLabel(String label)
	{
		m_AbscissaLabel = label;
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphingData g = new GraphingData();
		f.add(g);
		g.SetAbscissaLabel("AbscissaLabel");
		g.SetOrdinateLabel("OrdinateLabel");
		f.setSize(400,400);
		f.setLocation(200,200);
		f.setVisible(true);
	}
}