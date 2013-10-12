package mobiustransformations;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;

import javax.swing.JPanel;

import org.w3c.dom.css.Rect;

public class DisplayPanel extends JPanel {

	private BufferedImage _bufferImg1;
	private BufferedImage _bufferImg2;
	private BufferedImage _showBuffer;
	
	private int _bufferW;
	private int _bufferH;
	
	private volatile boolean _bufferOk;
	
	public DisplayPanel() {
		setDoubleBuffered(false);
		createBackBuffer();
		
		addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {
				
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				createBackBuffer();
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				createBackBuffer();
			}
			
		});
	}
	
	@Override
	public void paint(Graphics gr) {
		if (_showBuffer == null) {
			createBackBuffer();
		}
		gr.drawImage(_showBuffer, 0, 0, null);
	}
	
	public void createBackBuffer() {
		_bufferOk = false;
		
		_bufferW = getWidth();
		_bufferH = getHeight();
		
		_bufferImg1 = new BufferedImage(Math.max(_bufferW, 16), Math.max(_bufferH, 16), BufferedImage.TYPE_INT_RGB);
		_bufferImg2 = new BufferedImage(Math.max(_bufferW, 16), Math.max(_bufferH, 16), BufferedImage.TYPE_INT_RGB);
		
		_showBuffer = _bufferImg1;
		
		_bufferOk = true;
	}
	
	public void setLine(int y, int[] linePix, Rectangle subImage) {
		if (_bufferOk) {
			WritableRaster raster;
			
			if (_showBuffer == _bufferImg1) {
				raster = _bufferImg2.getRaster();
			} else {
				raster = _bufferImg1.getRaster();
			}
			
			DataBuffer buffer = raster.getDataBuffer();
			
			final int offset = subImage.x + (subImage.y + y) * _bufferW;
			
			int[] bufferData = ((DataBufferInt) buffer).getData();
			
			if (bufferData.length > offset + linePix.length) {
				System.arraycopy(linePix, 0, bufferData, offset, linePix.length);
			}
		}
	}
	
	public void switchBuffers() {
		if (_showBuffer == _bufferImg1) {
			_showBuffer = _bufferImg2;
		} else {
			_showBuffer = _bufferImg1;
		}
	}

}
