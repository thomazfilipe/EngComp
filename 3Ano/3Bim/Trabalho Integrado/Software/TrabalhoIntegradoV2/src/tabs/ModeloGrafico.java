package tabs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.sql.ResultSet;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.server.Servidor;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import database.Db;

public class ModeloGrafico extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DefaultCategoryDataset ds;
	private ChartPanel painelGrafico;
	private JFreeChart grafico;
	private Db database;
	private String comando;
	@SuppressWarnings("unused")
	private Servidor servidor;

	@SuppressWarnings("deprecation")
	public ModeloGrafico(Db database, Servidor servidor) {
		// pega conexão do banco de dados
		this.database = database;
		this.servidor = servidor;
		servidor.addObserver(this);
		// pega os ultimos 15 registros
		comando = "SELECT quantidadea, quantidadeb, dataleitura "
				+ "FROM (SELECT codfluxo,quantidadea, quantidadeb,dataleitura FROM fluxo ORDER BY codfluxo DESC  LIMIT 15) as top "
				+ "ORDER BY top.codfluxo asc";
		ResultSet rs = database.consultar(comando);
		ds = new DefaultCategoryDataset();
		if (rs != null) {
			try {
				// adiciona os dados encontrados no DataSet
				while (rs.next()) {
					ds.addValue(rs.getInt("quantidadea"),
							"Quantidade de veiculos A",
							rs.getString("dataleitura").replace(".0", ""));
					ds.addValue(rs.getInt("quantidadeb"),
							"Quantidade de veiculos B",
							rs.getString("dataleitura").replace(".0", ""));
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"Ocorreu um erro ao tentar ler os dados do gráfico: "
								+ e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		// cria o grafico
		grafico = ChartFactory.createLineChart("Fluxo de veiculos",
				"Intervalo", "Quantidade", ds, PlotOrientation.VERTICAL, true,
				true, false);
		// desenha o grafico
		CategoryPlot plot = grafico.getCategoryPlot();
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot
				.getRenderer();
		// Colocando pontos na linha do grafico
		renderer.setShapesVisible(true);
		renderer.setUseFillPaint(true);
		renderer.setFillPaint(Color.white);
		// alterando a posição do label
		CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
		xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		// aumenta a largura da linha
		renderer.setSeriesStroke(0, new BasicStroke(4f, BasicStroke.JOIN_ROUND,
				BasicStroke.JOIN_BEVEL));

		painelGrafico = new ChartPanel(grafico, true);
		// adiciona o gráfico no painel
		add(painelGrafico);
	}

	public void update(Observable o, Object arg) {
		// faz uma nova consulta
		comando = "SELECT codfluxo,quantidadea, quantidadeb,dataleitura FROM fluxo ORDER BY codfluxo DESC  LIMIT 15";
		ResultSet rs = database.consultar(comando);
		if (rs != null) {
			try {
				// limpa os dados mais antigos de A e B se for >= 15 o tamanho
				if (ds.getColumnCount() >= 15) {
					ds.removeColumn(0);
				}
				// preenche o dataset com os novos dados consultados
				while (rs.next()) {
					ds.addValue(rs.getInt("quantidadea"),
							"Quantidade de veiculos A",
							rs.getString("dataleitura").replace(".0", ""));
					ds.addValue(rs.getInt("quantidadeb"),
							"Quantidade de veiculos B",
							rs.getString("dataleitura").replace(".0", ""));
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"Ocorreu um erro ao tentar ler os dados do gráfico: "
								+ e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
