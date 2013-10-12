package menu;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import models.ConnectionModel;
import models.MobiusModel;
import models.Model;
import models.VisualiserModel;
import relativity.MainLoop;

public class MainMenu extends JFrame {
	
	public MainMenu() {
		initUI();
	}

	public void initUI() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);
		
		class MainLoopWorker extends SwingWorker<Integer, Integer> {
			private Model _model;
			public MainLoopWorker(final Model model) {
				_model = model;
			}
			@Override
			protected Integer doInBackground() throws Exception {
				MainLoop mainLoop = new MainLoop(_model);
				mainLoop.start();
				return 0;
			}
		}
		
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		
		final JPanel gridForButtons = new JPanel(new GridLayout(0, 1, 2, 2));
		JButton visualiserButton = new JButton("Run Visualiser");
		visualiserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new MainLoopWorker(new VisualiserModel()).execute();
			}
		});
		gridForButtons.add(visualiserButton);

		JButton connectionButton = new JButton("Run Connection");
		connectionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new MainLoopWorker(new ConnectionModel()).execute();
			}
		});
		gridForButtons.add(connectionButton);

		JButton mobiusButton = new JButton("Run Mobius");
		mobiusButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new MainLoopWorker(new MobiusModel()).execute();
			}
		});
		gridForButtons.add(mobiusButton);
		
		add(gridForButtons, BorderLayout.NORTH);
		
		setTitle("Simple Menu");
		setSize(300, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(final String[] args) {
		MainMenu mainMenu = new MainMenu();
		mainMenu.setVisible(true);
	}
}
