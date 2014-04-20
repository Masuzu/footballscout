package footballscout;

import java.awt.Dimension;   

import javax.swing.JPanel;   

import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;   
import org.jfree.chart.JFreeChart;   
import org.jfree.chart.plot.SpiderWebPlot;   
import org.jfree.chart.title.LegendTitle;   
import org.jfree.chart.title.TextTitle;   
import org.jfree.data.category.CategoryDataset;   
import org.jfree.data.category.DefaultCategoryDataset;   
import org.jfree.ui.*;   

public class SpiderWebChart extends ApplicationFrame   
{   
	private ChartFrame m_Frame = null;
	private ChartPanel m_ChartPanel = null;
	private JFreeChart m_Chart = null;
	private DefaultCategoryDataset m_Dataset = new DefaultCategoryDataset();

	public SpiderWebChart(String chartTitle, String windowTitle)   
	{   
		super(windowTitle);   

		CreateDataset();  

		m_Chart = CreateChart(chartTitle, m_Dataset);   
		m_ChartPanel = new ChartPanel(m_Chart);   
		m_ChartPanel.setPreferredSize(new Dimension(800, 600));   
		setContentPane(m_ChartPanel);   

		m_Frame = new ChartFrame(windowTitle, m_Chart);
		m_Frame.pack();
		m_Frame.setVisible(true);
	}   

	public void AddData(double value, Comparable series, Comparable category)
	{
		m_Dataset.setValue(value, series, category);
	}
	
	public void RemoveSeries(Comparable series)
	{
		m_Dataset.removeRow(series);
	}
	
	// For testing purpose only
	private void CreateDataset()   
	{   
		String s = "Player 1";   
		String s1 = "Player 2";   
		String s2 = "Player 3";   
		String s3 = "Attack";   
		String s4 = "Defense";   
		String s5 = "Skill";   
		String s6 = "Physique";   
		String s7 = "Teamwork";   

		m_Dataset.addValue(1.0D, s, s3);   
		m_Dataset.addValue(4D, s, s4);   
		m_Dataset.addValue(3D, s, s5);   
		m_Dataset.addValue(5D, s, s6);   
		m_Dataset.addValue(5D, s, s7);   
		m_Dataset.addValue(5D, s1, s3);   
		m_Dataset.addValue(7D, s1, s4);   
		m_Dataset.addValue(6D, s1, s5);   
		m_Dataset.addValue(8D, s1, s6);   
		m_Dataset.addValue(4D, s1, s7);   
		m_Dataset.addValue(4D, s2, s3);   
		m_Dataset.addValue(3D, s2, s4);   
		m_Dataset.addValue(2D, s2, s5);   
		m_Dataset.addValue(3D, s2, s6);   
		m_Dataset.addValue(6D, s2, s7);   
	}   

	private JFreeChart CreateChart(String chartTitle, CategoryDataset categorydataset)   
	{   
		SpiderWebPlot spiderwebplot = new SpiderWebPlot(categorydataset);   
		JFreeChart jfreechart = new JFreeChart("Spider Chart Demo", TextTitle.DEFAULT_FONT, spiderwebplot, false);   
		LegendTitle legendtitle = new LegendTitle(spiderwebplot);   
		legendtitle.setPosition(RectangleEdge.BOTTOM);   
		jfreechart.addSubtitle(legendtitle);
		return jfreechart;   
	}   

	public static void main(String args[])   
	{   
		SpiderWebChart chart = new SpiderWebChart("Spider Chart Demo", "");   
	}   
}   