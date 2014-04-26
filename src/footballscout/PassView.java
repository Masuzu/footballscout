package footballscout;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

public class PassView 
{
	Graph<Integer, String> m_Graph;
	int m_MaxNumPasses = 0;

	public PassView(String match_id, String teamID)
	{
		//=============
		// Data loading
		m_Graph = new DirectedSparseGraph<Integer, String>();


		BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("data/Player-Pass.csv")));
		String line;
		try {
			boolean bDataFound = false;
			while ((line = br.readLine()) != null)
			{
				// process the line
				String [] l = line.split("\t");

				if(match_id.equals(l[0]))
				{
					bDataFound = true;
					if(teamID.equals(l[1]))
					{
						//===============
						// Graph creation
						int source = Integer.parseInt(l[2]);
						int target = Integer.parseInt(l[3]);
						m_Graph.addVertex(source);
						m_Graph.addVertex(target);
						int numPasses = Integer.parseInt(l[4]);
						if(numPasses > m_MaxNumPasses)
							m_MaxNumPasses = numPasses;

						m_Graph.addEdge(source + ">" + target + " " + numPasses, source, target);
					}
				}
				else
					if(bDataFound)
						break;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//====================
		// Graph visualisation

		// Layout<V, E>, VisualizationComponent<V,E>
		Layout<Integer, String> layout = new CircleLayout(m_Graph);
		layout.setSize(new Dimension(800,800));
		BasicVisualizationServer<Integer,String> vv = new BasicVisualizationServer<Integer,String>(layout);
		vv.setPreferredSize(new Dimension(820,820));       

		// Setup up a new vertex to paint transformer
		Transformer<Integer,Paint> vertexPaint = new Transformer<Integer,Paint>() {
			public Paint transform(Integer i) {
				return Color.GREEN;
			}
		};  

		// Setup up a new vertex labeller
		Transformer<Integer, String> vertexLabeller = new Transformer<Integer, String>() {
			RESTUtil m_RESTUtil = RESTUtil.getInstance();
			public String transform(Integer i) {
				return m_RESTUtil.getStat(Integer.toString(i), "playerName");
			}
		};

		// Set up a new stroke Transformer for the edges
		Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
			public Stroke transform(String s) {
				String [] l = s.split(" ");
				return new BasicStroke(5*Float.parseFloat(l[1])/m_MaxNumPasses);
			}
		};

		// Setup up a new edge labeller
		Transformer<String, String> edgeLabeller = new Transformer<String, String>() {
			RESTUtil m_RESTUtil = RESTUtil.getInstance();
			public String transform(String s) {
				String [] l = s.split(" ");
				//return l[1].toString();
				return "";
			}
		};

		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
		vv.getRenderContext().setVertexLabelTransformer(vertexLabeller);
		vv.getRenderContext().setEdgeLabelTransformer(edgeLabeller);
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.N); 

		JFrame frame = new JFrame("Graph View of the passes");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new PassView("131897", "810");
	}
}
