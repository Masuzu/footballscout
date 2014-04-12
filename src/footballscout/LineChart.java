package footballscout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

public class LineChart extends ApplicationFrame {

	private JFreeChart m_Chart = null;
	private DefaultCategoryDataset m_Dataset = new DefaultCategoryDataset();
	private ChartPanel m_ChartPanel = null;
	private ChartFrame m_Frame = null;
	
	public LineChart(String chartTitle, String domainAxisLabel, String rangeAxisLabel, String windowTitle) {
		super(windowTitle);

		m_Chart = CreateChart(chartTitle, domainAxisLabel, rangeAxisLabel, m_Dataset);
		m_ChartPanel = new ChartPanel(m_Chart);
		m_ChartPanel.setPreferredSize(new Dimension(500, 270));
		m_ChartPanel.setFillZoomRectangle(true);
		m_ChartPanel.setMouseWheelEnabled(true);
		setContentPane(m_ChartPanel);

		CreateDataset(); 
		
		m_Frame = new ChartFrame(windowTitle,m_Chart);
		m_Frame.pack();
		m_Frame.setVisible(true);
	}

	public void SetVisible(boolean b)
	{
		m_Frame.setVisible(b);
	}
	
	public void AddData(double value, Comparable series, Comparable columnKey)
	{
		m_Dataset.setValue(value, series, columnKey);
	}

	public void RemoveSeries(Comparable series)
	{
		m_Dataset.removeRow(series);
	}

	// For testing purposes only
	private void CreateDataset() {

		// row keys...
		final String series1 = "First";
		final String series2 = "Second";
		final String series3 = "Third";

		// column keys...
		final String type1 = "Type 1";
		final String type2 = "Type 2";
		final String type3 = "Type 3";
		final String type4 = "Type 4";
		final String type5 = "Type 5";
		final String type6 = "Type 6";
		final String type7 = "Type 7";
		final String type8 = "Type 8";

		m_Dataset.setValue(1.0, series1, type1);
		m_Dataset.setValue(4.0, series1, type2);
		m_Dataset.setValue(3.0, series1, type3);
		m_Dataset.setValue(5.0, series1, type4);
		m_Dataset.setValue(5.0, series1, type5);
		m_Dataset.setValue(7.0, series1, type6);
		m_Dataset.setValue(7.0, series1, type7);
		m_Dataset.setValue(8.0, series1, type8);

		m_Dataset.setValue(5.0, series2, type1);
		m_Dataset.setValue(7.0, series2, type2);
		m_Dataset.setValue(6.0, series2, type3);
		m_Dataset.setValue(8.0, series2, type4);
		m_Dataset.setValue(4.0, series2, type5);
		m_Dataset.setValue(4.0, series2, type6);
		m_Dataset.setValue(2.0, series2, type7);
		m_Dataset.setValue(1.0, series2, type8);

		m_Dataset.setValue(4.0, series3, type1);
		m_Dataset.setValue(3.0, series3, type2);
		m_Dataset.setValue(2.0, series3, type3);
		m_Dataset.setValue(3.0, series3, type4);
		m_Dataset.setValue(6.0, series3, type5);
		m_Dataset.setValue(3.0, series3, type6);
		m_Dataset.setValue(4.0, series3, type7);
		m_Dataset.setValue(3.0, series3, type8);
	}

	private JFreeChart CreateChart(String chartTitle, String domainAxisLabel, String rangeAxisLabel, CategoryDataset dataset) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createLineChart(
				chartTitle,       // chart title
				domainAxisLabel,                    // domain axis label
				rangeAxisLabel,                   // range axis label
				dataset,                   // data
				PlotOrientation.VERTICAL,  // orientation
				true,                      // include legend
				true,                      // tooltips
				false                      // urls
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		//        final StandardLegend legend = (StandardLegend) chart.getLegend();
		//      legend.setDisplaySeriesShapes(true);
		//    legend.setShapeScaleX(1.5);
		//  legend.setShapeScaleY(1.5);
		//legend.setDisplaySeriesLines(true);

		chart.setBackgroundPaint(Color.white);

		final CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.white);

		// customise the range axis...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setAutoRangeIncludesZero(true);

		// customise the renderer...
		final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		//        renderer.setDrawShapes(true);

		renderer.setSeriesStroke(
				0, new BasicStroke(
						2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
						1.0f, new float[] {10.0f, 6.0f}, 0.0f
						)
				);
		renderer.setSeriesStroke(
				1, new BasicStroke(
						2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
						1.0f, new float[] {6.0f, 6.0f}, 0.0f
						)
				);
		renderer.setSeriesStroke(
				2, new BasicStroke(
						2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
						1.0f, new float[] {2.0f, 6.0f}, 0.0f
						)
				);
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;
	}

	/**
	 * Starting point for the demonstration application.
	 *
	 * @param args  ignored.
	 */
	public static void main(final String[] args) {

		final LineChart linechart = new LineChart("Line Chart Demo", "Type", "Value", "Line Chart Demo");
	}

}

