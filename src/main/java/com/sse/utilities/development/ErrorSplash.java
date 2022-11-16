package com.sse.utilities.development;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Class to produce a splash error window.
 *
 */
public class ErrorSplash extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new error splash.
	 *
	 * @param waitTime the wait time
	 * @param message  the message
	 * @param backgroundColour the desired background colour
	 */
	public ErrorSplash (int waitTime, String message, Color backgroundColour) {
		setSize(900,300);
        setAlwaysOnTop(true);

		JLabel m = new JLabel("<html>" + message + "</html>");

        JPanel overallPane = new JPanel();
        overallPane.setBackground(backgroundColour);
        overallPane.setLayout(new BoxLayout(overallPane, BoxLayout.PAGE_AXIS));
        overallPane.add(m, BorderLayout.CENTER);

		Dimension labelSize = getPreferredSize();

		Dimension screenSize =
		  Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width/2 - (labelSize.width/2),
					screenSize.height/2 - (labelSize.height/2));

        getContentPane().add(overallPane);
        validate();
        repaint();
		addMouseListener(new MouseAdapter()
			{
				@Override
				public void mousePressed(MouseEvent e)
				{
					setVisible(false);
					dispose();
				}
			});
		final int pause = waitTime;
		final Runnable closerRunner = new Runnable()
			{
				@Override
				public void run()
				{
					setVisible(false);
					dispose();
				}
			};
		Runnable waitRunner = new Runnable()
			{
				@Override
				public void run()
				{
					try
						{
							Thread.sleep(pause);
							SwingUtilities.invokeAndWait(closerRunner);
						}
					catch(Exception e)
						{
							e.printStackTrace();
						}
				}
			};
		setVisible(true);
		Thread splashThread = new Thread(waitRunner, "SplashThread");
		splashThread.start();

		try {
			Thread.sleep(waitTime + 200);
			dispose();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}