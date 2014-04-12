package footballscout;

import  org.jfree.chart.ChartFactory;
import  org.jfree.chart.ChartFrame;
import  org.jfree.chart.JFreeChart;
import  org.jfree.data.general.DefaultPieDataset;

public class PieGraph {

	private DefaultPieDataset m_Data = new DefaultPieDataset();
	private JFreeChart m_Chart = null;
	private ChartFrame m_Frame = null;
	
	public PieGraph(String graphTitle, String windowTitle)
	{
		m_Chart=ChartFactory.createPieChart( graphTitle, m_Data, true/*legend?*/,true/*tooltips?*/, false/*URLs?*/);
		
		m_Frame = new ChartFrame(windowTitle,m_Chart);
		m_Frame.pack();
		m_Frame.setVisible(true);
	}
	
	public void SetVisible(boolean b)
	{
		m_Frame.setVisible(b);
	}
	
	public void AddData(Comparable key, Number value)
	{
		m_Data.setValue(key, value);
	}
	
	public void RemoveEntry(Comparable key)
	{
		m_Data.remove(key);
	}
	
	public static void main(String[] args) {
		PieGraph p = new PieGraph("Test pie graph", "");
		p.AddData("Category1",43.2);
		p.AddData("Category2",27.9);
		p.AddData("Category3",79.5);
	}

}
