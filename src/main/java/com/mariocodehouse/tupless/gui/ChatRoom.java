package com.mariocodehouse.tupless.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class ChatRoom extends Composite {
	private Text txtDigiteSuaMensagem;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ChatRoom(Composite parent, int style) {
		super(parent, style);
		setLayout(new StackLayout());

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new FormLayout());

		txtDigiteSuaMensagem = new Text(composite, SWT.BORDER);
		FormData fd_txtDigiteSuaMensagem = new FormData();
		fd_txtDigiteSuaMensagem.right = new FormAttachment(0, 210);
		fd_txtDigiteSuaMensagem.top = new FormAttachment(0, 269);
		fd_txtDigiteSuaMensagem.left = new FormAttachment(0, 10);
		txtDigiteSuaMensagem.setLayoutData(fd_txtDigiteSuaMensagem);
		txtDigiteSuaMensagem.setText("Digite sua mensagem");

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		FormData fd_composite_1 = new FormData();
		fd_composite_1.bottom = new FormAttachment(0, 263);
		fd_composite_1.right = new FormAttachment(0, 210);
		fd_composite_1.top = new FormAttachment(0, 10);
		fd_composite_1.left = new FormAttachment(0, 10);
		composite_1.setLayoutData(fd_composite_1);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
